package com.example.easyway.cadastros;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.easyway.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdicionarServico extends AppCompatActivity {

    private EditText etNomeServico, etOrigem, etDestino, etValor;
    private Button btnSalvarServico;
    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_servico);

        // Inicializando as views
        etNomeServico = findViewById(R.id.etNomeServico);
        etOrigem = findViewById(R.id.etOrigem);
        etDestino = findViewById(R.id.etDestino);
        etValor = findViewById(R.id.etValor);
        btnSalvarServico = findViewById(R.id.btnSalvarServico);

        // Inicializando o Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Adicionar TextWatcher para formatar o valor com "R$"
        etValor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Não é necessário implementar
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Garantir que o valor tenha "R$" na frente
                if (charSequence.length() > 0 && !charSequence.toString().startsWith("R$")) {
                    etValor.removeTextChangedListener(this);  // Evitar loop de chamadas
                    String valor = charSequence.toString().replaceAll("[^0-9]", "");  // Remover qualquer coisa que não seja número
                    if (!valor.isEmpty()) {
                        double valorFormatado = Double.parseDouble(valor) / 100.0;  // Converter para formato monetário
                        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                        String valorComFormato = numberFormat.format(valorFormatado);  // Formatar o número como moeda
                        etValor.setText(valorComFormato);  // Definir o texto no EditText
                        etValor.setSelection(etValor.getText().length());  // Manter o cursor no final
                    }
                    etValor.addTextChangedListener(this);  // Re-adicionar o TextWatcher
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Não é necessário implementar
            }
        });

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

        // Buscar as coordenadas dos endereços
        buscarCoordenadas(origem, destino, (origemCoordenadas, destinoCoordenadas) -> {
            if (origemCoordenadas != null && destinoCoordenadas != null) {
                // Criando um objeto para armazenar no Firestore
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

                // Salvar no Firestore
                db.collection("servicos")
                        .add(servico)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(this, "Serviço adicionado com sucesso!", Toast.LENGTH_SHORT).show();
                            limparCampos();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Erro ao adicionar serviço: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
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

    // Método para limpar os campos após salvar
    private void limparCampos() {
        etNomeServico.setText("");
        etOrigem.setText("");
        etDestino.setText("");
        etValor.setText("");  // Limpar o campo de valor
    }

    // Interface de callback para coordenadas
    public interface CoordenadasCallback {
        void onCoordenadasObtidas(LatLng origemCoordenadas, LatLng destinoCoordenadas);
    }

    // Classe LatLng para armazenar coordenadas
    public static class LatLng {
        public double latitude;
        public double longitude;

        public LatLng(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    // Classe Servico para modelar o objeto a ser salvo no Firestore
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

        // Getters e setters (se necessário)
    }
}
