package com.example.easyway.cadastros;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.easyway.R;
import com.example.easyway.activity.MainActivity;
import com.example.easyway.controller.AutoCompleteAdapter;
import com.example.easyway.controller.SimpleTextWatcher;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdicionarServico extends AppCompatActivity {

    private EditText etNomeServico, etValor;
    private AutoCompleteTextView etOrigem, etDestino;
    private Button btnSalvarServico;
    private FirebaseFirestore db;

    private PlacesClient placesClient;
    private AutocompleteSessionToken sessionToken;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_servico);

        placesClient = Places.createClient(this);
        sessionToken = AutocompleteSessionToken.newInstance();

        // Inicializando as views
        etNomeServico = findViewById(R.id.etNomeServico);
        etOrigem = findViewById(R.id.etOrigem);
        etDestino = findViewById(R.id.etDestino);
        etValor = findViewById(R.id.etValor);
        btnSalvarServico = findViewById(R.id.btnSalvarServico);

        // Inicializando o Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Adicionar TextWatcher para formatar o valor como moeda
        etValor.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Não é necessário implementar
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (isUpdating) return;

                isUpdating = true;
                String valor = charSequence.toString().replaceAll("[^0-9]", "");
                if (!valor.isEmpty()) {
                    double valorFormatado = Double.parseDouble(valor) / 100.0;
                    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                    etValor.setText(numberFormat.format(valorFormatado));
                    etValor.setSelection(etValor.getText().length());
                }
                isUpdating = false;
            }

        });

        setupAutocomplete(etOrigem);
        setupAutocomplete(etDestino);

        // Botão para salvar o serviço
        btnSalvarServico.setOnClickListener(v -> salvarServico());
    }

    private void salvarServico() {
        String nomeServico = etNomeServico.getText().toString();
        String origem = etOrigem.getText().toString();
        String destino = etDestino.getText().toString();
        String valor = etValor.getText().toString();

        if (TextUtils.isEmpty(nomeServico) || TextUtils.isEmpty(origem) || TextUtils.isEmpty(destino) || TextUtils.isEmpty(valor)) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        buscarCoordenadas(origem, destino, (origemCoordenadas, destinoCoordenadas) -> {
            if (origemCoordenadas != null && destinoCoordenadas != null) {
                Servico servico = new Servico(
                        nomeServico,
                        origem,
                        destino,
                        origemCoordenadas.latitude,
                        origemCoordenadas.longitude,
                        destinoCoordenadas.latitude,
                        destinoCoordenadas.longitude,
                        valor
                );

                db.collection("servicos")
                        .add(servico)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(this, "Serviço adicionado com sucesso!", Toast.LENGTH_SHORT).show();
                            limparCampos();
                        })
                        .addOnFailureListener(e -> Toast.makeText(this, "Erro ao adicionar serviço: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(this, "Erro ao obter coordenadas. Verifique os endereços.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buscarCoordenadas(String partidaEndereco, String chegadaEndereco, CoordenadasCallback callback) {
        new Thread(() -> {
            try {
                LatLng partidaCoordenadas = geocodeEndereco(partidaEndereco);
                LatLng chegadaCoordenadas = geocodeEndereco(chegadaEndereco);

                runOnUiThread(() -> callback.onCoordenadasObtidas(partidaCoordenadas, chegadaCoordenadas));
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(AdicionarServico.this, "Erro ao obter coordenadas: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
    private void setupAutocomplete(AutoCompleteTextView autoCompleteTextView) {
        autoCompleteTextView.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString();
                if (!input.isEmpty()) {
                    FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                            .setSessionToken(sessionToken)
                            .setQuery(input)
                            .build();

                    placesClient.findAutocompletePredictions(request)
                            .addOnSuccessListener(response -> {
                                ArrayList<String> suggestions = new ArrayList<>();
                                for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                                    suggestions.add(prediction.getFullText(null).toString());
                                }

                                AutoCompleteAdapter adapter = new AutoCompleteAdapter(AdicionarServico.this, suggestions);
                                autoCompleteTextView.setAdapter(adapter);
                            })
                            .addOnFailureListener(e -> Toast.makeText(AdicionarServico.this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private LatLng geocodeEndereco(String endereco) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(endereco, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return new LatLng(address.getLatitude(), address.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void limparCampos() {
        etNomeServico.setText("");
        etOrigem.setText("");
        etDestino.setText("");
        etValor.setText("");
    }

    public interface CoordenadasCallback {
        void onCoordenadasObtidas(LatLng origemCoordenadas, LatLng destinoCoordenadas);
    }

    public static class LatLng {
        public double latitude;
        public double longitude;

        public LatLng(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    public static class Servico {
        private String nome;
        private String origem;
        private String destino;
        private double origemLat;
        private double origemLng;
        private double destinoLat;
        private double destinoLng;
        private String valor;

        public Servico(String nome, String origem, String destino, double origemLat, double origemLng, double destinoLat, double destinoLng, String valor) {
            this.nome = nome;
            this.origem = origem;
            this.destino = destino;
            this.origemLat = origemLat;
            this.origemLng = origemLng;
            this.destinoLat = destinoLat;
            this.destinoLng = destinoLng;
            this.valor = valor;
        }
    }
}
