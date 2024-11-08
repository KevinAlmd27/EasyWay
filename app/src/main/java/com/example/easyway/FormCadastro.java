package com.example.easyway;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FormCadastro extends AppCompatActivity {

    private EditText edt_email, edt_senha, edt_nomecompleto, edt_cnh;
    private Button btn_cadastrar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_cadastro);

        mAuth = FirebaseAuth.getInstance();

        IniciaComponentes();

        btn_cadastrar.setOnClickListener(view -> validaDados());

    }
    private void IniciaComponentes(){
        edt_nomecompleto = findViewById(R.id.edt_nomecompleto);
        edt_cnh = findViewById(R.id.edt_cnh);
        edt_email = findViewById(R.id.edt_email);
        edt_senha = findViewById(R.id.edt_senha);
        btn_cadastrar = findViewById(R.id.btn_cadastrar);
    }
    private void validaDados(){
        String email = edt_email.getText().toString().trim();
        String cnh = edt_cnh.getText().toString().trim();
        String nome = edt_nomecompleto.getText().toString().trim();
        String senha = edt_senha.getText().toString().trim();

        if (!email.isEmpty()) {
            if(!senha.isEmpty()){

                criarContaFirebase(email,cnh,nome,senha);

            }else {
                Toast.makeText(this,"Digite uma senha.", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"Digite seu e-mail.", Toast.LENGTH_SHORT).show();
        }
    }

    private void criarContaFirebase(String email, String cnh, String nome, String senha){
        mAuth.createUserWithEmailAndPassword(
                email,senha
        ).addOnCompleteListener(task ->  {
                if(task.isSuccessful()){

                   finish();

                   Intent intent = new Intent(FormCadastro.this,FormLogin.class);
                   startActivity(intent);

                }else {

                    Toast.makeText(this,"Ocorreu um erro.", Toast.LENGTH_SHORT).show();
                }
        });
    }
}