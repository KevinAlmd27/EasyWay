package com.example.easyway.cadastros;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.easyway.R;
import com.google.firebase.auth.FirebaseAuth;

public class FormLogin extends AppCompatActivity {

    private TextView text_tela_cadastro;
    private EditText edit_email, edit_senha;
    private Button btn_entrar;
    private boolean isPasswordVisible = false;

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
                Intent intent = new Intent(FormLogin.this, FormCadastro.class);
                startActivity(intent);
            }
        });

        btn_entrar.setOnClickListener(view -> validaDados());

        edit_senha.setOnTouchListener((v, event) -> {
            final int DRAWABLE_END = 2; // Posição do ícone de fim (end) do EditText
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (edit_senha.getRight() - edit_senha.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {
                    togglePasswordVisibility();
                    return true;
                }
            }
            return false;
        });
    }
    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Ocultar a senha
            edit_senha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            edit_senha.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye, 0);
        } else {
            // Mostrar a senha
            edit_senha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            edit_senha.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_closed, 0); // Altere o ícone para indicar o estado
        }
        isPasswordVisible = !isPasswordVisible;
        // Move o cursor para o final do texto
        edit_senha.setSelection(edit_senha.getText().length());
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

                Intent intent = new Intent(FormLogin.this, Menu.class);
                startActivity(intent);

            }else {

                Toast.makeText(this,"E-mail ou senha incorretos.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}