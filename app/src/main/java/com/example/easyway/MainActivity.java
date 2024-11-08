package com.example.easyway;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    String[] caminhoes = {"Mercedes Actros - DEO-6700", "Volvo FH - FPX-6736", "Scania R450 - EVK-2099", "Man TGX - CQG-7498"};
    int caminhoesImages[] = {R.drawable.actros, R.drawable.fh, R.drawable.r450, R.drawable.tgx};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (caminhoes == null) {
            caminhoes = new String[0];
        }
        if (caminhoesImages == null) {
            caminhoesImages = new int[0];
        }

        CustomBaseAdapter adapter = new CustomBaseAdapter(this, caminhoes, caminhoesImages);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String caminhaoSelecionado = caminhoes[position];
            Intent intent = new Intent(MainActivity.this, Trabalhos.class);
            intent.putExtra("Você selecionou o caminhão: ", caminhaoSelecionado);
            startActivity(intent);
        });

    }

}