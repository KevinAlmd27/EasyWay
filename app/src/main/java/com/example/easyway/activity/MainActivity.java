package com.example.easyway.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyway.controller.CustomBaseAdapter;
import com.example.easyway.R;
import com.example.easyway.controller.Trabalhos;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CustomBaseAdapter caminhaoAdapter; // Usando CustomBaseAdapter

    String[] caminhoes = {"Mercedes Actros", "Volvo FH", "Scania R450", "Man TGX"};
    String[] placas = {"DEO-6700", "FPX-6736", "EVK-2099", "CQG-7498"};
    int[] caminhoesImages = {R.drawable.actros, R.drawable.fh, R.drawable.r450, R.drawable.tgx};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView); // Referência ao RecyclerView

        // Configuração do RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Layout vertical

        // Criar o adaptador e passar os dados
        caminhaoAdapter = new CustomBaseAdapter(this, caminhoes, placas, caminhoesImages, position -> {
            // Ação ao clicar em um item
            String caminhaoSelecionado = caminhoes[position];
            String placaSelecionada = placas[position];

            // Exibindo um Toast com o nome do caminhão
            Toast.makeText(MainActivity.this, "Caminhão selecionado: " + caminhaoSelecionado, Toast.LENGTH_SHORT).show();

            // Criando o Intent para ir para a página "Trabalhos"
            Intent intent = new Intent(MainActivity.this, Trabalhos.class);
            intent.putExtra("caminhao_nome", caminhaoSelecionado);  // Passando o nome do caminhão
            intent.putExtra("caminhao_placa", placaSelecionada);    // Passando a placa do caminhão

            // Iniciando a atividade "Trabalhos"
            startActivity(intent);
        });

        // Definir o adaptador para o RecyclerView
        recyclerView.setAdapter(caminhaoAdapter);
    }
}
