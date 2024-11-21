package com.example.easyway.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.easyway.cadastros.FormLogin;
import com.example.easyway.R;
import com.example.easyway.activity.Perfil;
import com.google.firebase.auth.FirebaseAuth;

import com.google.android.material.button.MaterialButton;

public class Config extends AppCompatActivity {

    private FirebaseAuth LogOutAuth;
    private MaterialButton btnConfig1, btnConfig2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_config);

        LogOutAuth = FirebaseAuth.getInstance();
        btnConfig1 = findViewById(R.id.btn_config_1);
        btnConfig2 = findViewById(R.id.btn_config_2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnConfig1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Config.this, Perfil.class);
                startActivity(intent);
            }
        });

        btnConfig2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Realizar log-out do Firebase
                LogOutAuth.signOut();

                // Exibir uma mensagem de sucesso
                Toast.makeText(Config.this, "Desconectado com sucesso", Toast.LENGTH_SHORT).show();

                // Redirecionar para a tela de login
                Intent loginIntent = new Intent(Config.this, FormLogin.class);
                startActivity(loginIntent);
                finish();
            }
        });

    }
}