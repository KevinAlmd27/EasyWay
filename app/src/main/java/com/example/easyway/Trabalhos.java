package com.example.easyway;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Trabalhos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trabalhos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RadioGroup rdgTrabalhos = findViewById(R.id.rdg1);
        AppCompatButton btnConfirmar = findViewById(R.id.btnTrabalho);

        btnConfirmar.setOnClickListener(v -> {
            int selectRdg = rdgTrabalhos.getCheckedRadioButtonId();

            if(selectRdg != -1){
                RadioButton rb = findViewById(selectRdg);

                String[] coordenadas = rb.getTag().toString().split(",");
                String origem = coordenadas[0] + "," + coordenadas[1];
                String destino = coordenadas[2] + "," + coordenadas[3];

                Intent intent = new Intent(Trabalhos.this, MapsActivity.class);
                intent.putExtra("Origem", origem);
                intent.putExtra("Destino", destino);
                startActivity(intent);
            }
        });
    }

}