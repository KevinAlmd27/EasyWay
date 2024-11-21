package com.example.easyway.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.easyway.cadastros.AdicionarServico;
import com.example.easyway.R;
import com.example.easyway.activity.MainActivity;
import com.google.android.material.button.MaterialButton;

public class Menu extends AppCompatActivity {

    private MaterialButton btnMenu1, btnMenu2, btnMenu3, btnMenu4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu); // Altere com o nome do seu layout

        // Inicialize o botão
        btnMenu1 = findViewById(R.id.btn_menu_1);
        btnMenu2 = findViewById(R.id.btn_menu_2);
        btnMenu3 = findViewById(R.id.btn_menu_3);
        btnMenu4 = findViewById(R.id.btn_menu_4);

        // Defina o clique do botão
        btnMenu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnMenu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, Trabalhos.class);
                startActivity(intent);
            }
        });

        btnMenu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, AdicionarServico.class); // Troque para o nome da sua atividade de "Trabalhos"
                startActivity(intent);
            }
        });

        btnMenu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, Config.class);
                startActivity(intent);
            }
        });
    }
}