package com.example.easyway.controller;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.easyway.cadastros.DatabaseHelper;
import com.example.easyway.R;

public class Trabalhos extends AppCompatActivity {

    private RadioGroup rdgTrabalho;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trabalhos);

        // Inicializando o RadioGroup
        rdgTrabalho = findViewById(R.id.rdgTrabalho);
        dbHelper = new DatabaseHelper(this);

        // Carregar os serviços ao iniciar a tela
        carregarServicos();
    }

    private void carregarServicos() {
        // Obter os serviços do banco de dados
        Cursor cursor = dbHelper.getAllServicos();

        // Verificar se o cursor não é nulo e se contém dados
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Recuperar os índices das colunas
                int nomeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NOME);
                int origemIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ORIGEM);
                int destinoIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DESTINO);
                int valorIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_VALOR);

                // Verificar se as colunas foram encontradas
                if (nomeIndex != -1 && origemIndex != -1 && destinoIndex != -1 && valorIndex != -1) {
                    // Recuperar os dados
                    String nome = cursor.getString(nomeIndex);
                    String origem = cursor.getString(origemIndex);
                    String destino = cursor.getString(destinoIndex);
                    String valor = cursor.getString(valorIndex);

                    // Criar um novo RadioButton com os dados do serviço
                    RadioButton radioButton = new RadioButton(this);
                    radioButton.setText(
                            nome + "\nOrigem: " + origem + "\nDestino: " + destino + "\nValor: " + valor
                    );

                    // Estilizando o RadioButton
                    estilizarRadioButton(radioButton);

                    // Adicionar o RadioButton ao RadioGroup
                    rdgTrabalho.addView(radioButton);
                } else {
                    // Se alguma coluna não for encontrada
                    Toast.makeText(this, "Erro ao carregar as colunas do banco de dados.", Toast.LENGTH_SHORT).show();
                }
            } while (cursor.moveToNext());

            // Fechar o cursor após a iteração
            cursor.close();
        } else {
            // Se o cursor não tiver dados
            Toast.makeText(this, "Nenhum serviço encontrado.", Toast.LENGTH_SHORT).show();
        }
    }

    private void estilizarRadioButton(RadioButton radioButton) {
        // Definir o tamanho do texto
        radioButton.setTextSize(16f);

        // Definir o tamanho fixo para o RadioButton
        radioButton.setWidth(getResources().getDimensionPixelSize(R.dimen.radio_button_width));
        radioButton.setHeight(getResources().getDimensionPixelSize(R.dimen.radio_button_height));

        // Definir a cor do texto
        radioButton.setTextColor(getResources().getColor(R.color.black));

        // Remover o círculo padrão do RadioButton
        radioButton.setButtonDrawable(null);

        // Adicionar padding interno
        radioButton.setPadding(32, 32, 32, 32);

        // Definir o fundo estilizado
        radioButton.setBackgroundResource(R.drawable.radio_button_bckg);

        // Adicionar um ícone ao lado do texto (se necessário)
        radioButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pallet, 0, 0, 0);
        radioButton.setCompoundDrawablePadding(10);

        // Adicionar margens entre os RadioButtons
        int marginTop = getResources().getDimensionPixelSize(R.dimen.radio_button_margin_top);
        int marginBottom = getResources().getDimensionPixelSize(R.dimen.radio_button_margin_bottom);

        // Definir o LayoutParams para o RadioButton
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.MATCH_PARENT,
                RadioGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, marginTop, 0, marginBottom);

        // Aplicar os LayoutParams com margens
        radioButton.setLayoutParams(layoutParams);
    }
}
