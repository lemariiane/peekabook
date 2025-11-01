package com.example.peekaboo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.*;

public class Login extends AppCompatActivity {

    Button btlogin;
    EditText edemail, edsenha;
    TextView signupRedirectText;
    DatabaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        btlogin = findViewById(R.id.btlogin);
        edemail = findViewById(R.id.edemail);
        edsenha = findViewById(R.id.edsenha);
        signupRedirectText = findViewById(R.id.signupRedirectText);

        dbHelper = new DatabaseHelper(this);

        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edemail.getText().toString().trim();
                String senha = edsenha.getText().toString().trim();

                if (email.isEmpty() || senha.isEmpty()) {
                    MostraMensagem("Preencha todos os campos!");
                    return;
                }

                Boolean checkUserPass = dbHelper.checkEmailPassword(email, senha);

                if (checkUserPass) {
                    Intent i = new Intent(Login.this, MainActivity.class);
                    startActivity(i);
                } else {
                    MostraMensagem("Usuário não encontrado. Verifique seus dados!");
                }
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Cadastrar_user.class);
                startActivity(intent);
            }
        });
    }

    public void MostraMensagem(String str) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(Login.this);
        dialogo.setTitle("Aviso");
        dialogo.setMessage(str);
        dialogo.setNeutralButton("OK", null);
        dialogo.show();
    }
}
