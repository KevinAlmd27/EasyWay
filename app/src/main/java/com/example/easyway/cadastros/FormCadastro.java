package com.example.easyway.cadastros;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.easyway.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FormCadastro extends AppCompatActivity {

    private EditText edt_email, edt_senha, edt_nomecompleto, edt_cnh;
    private Button btn_cadastrar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);

        // Inicializando Firebase Authentication e Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Inicializando componentes da tela
        IniciaComponentes();

        // Configurando o evento do botão de cadastro
        btn_cadastrar.setOnClickListener(view -> validaDados());
    }

    private void IniciaComponentes() {
        edt_nomecompleto = findViewById(R.id.edt_nomecompleto);
        edt_cnh = findViewById(R.id.edt_cnh);
        edt_email = findViewById(R.id.edt_email);
        edt_senha = findViewById(R.id.edt_senha);
        btn_cadastrar = findViewById(R.id.btn_cadastrar);
    }

    private void validaDados() {
        String email = edt_email.getText().toString().trim();
        String cnh = edt_cnh.getText().toString().trim();
        String nome = edt_nomecompleto.getText().toString().trim();
        String senha = edt_senha.getText().toString().trim();

        // Validando se o email e senha foram preenchidos
        if (!email.isEmpty()) {
            if (!senha.isEmpty()) {

                // Criando o usuário no Firebase Authentication
                criarContaFirebase(email, cnh, nome, senha);

            } else {
                Toast.makeText(this, "Digite uma senha.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Digite seu e-mail.", Toast.LENGTH_SHORT).show();
        }
    }

    private void criarContaFirebase(String email, String cnh, String nome, String senha) {
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        // Após o sucesso na criação da conta no Firebase Authentication,
                        // salve os dados no Firestore
                        salvarDadosNoFirestore(nome, cnh, email);

                    } else {
                        Toast.makeText(this, "Ocorreu um erro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void salvarDadosNoFirestore(String nome, String cnh, String email) {
        // Criando um mapa com os dados do usuário
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("nome", nome);
        usuario.put("cnh", cnh);
        usuario.put("email", email);
        usuario.put("status", "Disponível"); // Status pode ser ajustado conforme necessário

        // Salvando os dados no Firestore na coleção "Motoristas"
        db.collection("Motoristas")
                .add(usuario)
                .addOnSuccessListener(documentReference -> {
                    // Redirecionando para a tela de login após salvar os dados
                    Toast.makeText(FormCadastro.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FormCadastro.this, FormLogin.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(FormCadastro.this, "Erro ao salvar dados no Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
