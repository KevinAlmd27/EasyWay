package com.example.easyway;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class FormLogin extends AppCompatActivity {

    private TextView text_tela_cadastro;
    private EditText edit_email, edit_senha;
    private Button btn_entrar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_login);

        mAuth = FirebaseAuth.getInstance();

        IniciarComponentes();

        text_tela_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FormLogin.this,FormCadastro.class);
                startActivity(intent);
            }
        });

        btn_entrar.setOnClickListener(view -> validaDados());
    }
    private void IniciarComponentes(){
        text_tela_cadastro = findViewById(R.id.text_tela_cadastro);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        btn_entrar = findViewById(R.id.btn_entrar);
    }

    private void validaDados(){
        String email = edit_email.getText().toString().trim();
        String senha = edit_senha.getText().toString().trim();

        if (!email.isEmpty()) {
            if(!senha.isEmpty()){

                loginFirebase(email, senha);

            }else {
                Toast.makeText(this,"Digite sua  senha.", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"Digite seu e-mail.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loginFirebase(String email, String senha){
        mAuth.signInWithEmailAndPassword(
                email,senha
        ).addOnCompleteListener(task ->  {
            if(task.isSuccessful()){

                finish();

                Intent intent = new Intent(FormLogin.this, MainActivity.class);
                startActivity(intent);

            }else {

                Toast.makeText(this,"Ocorreu um erro.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}