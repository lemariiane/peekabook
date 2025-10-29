package com.example.peekaboo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Cadastrar_user extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_user);

        EditText signupNome = findViewById(R.id.signup_nome);
        EditText signupEmail = findViewById(R.id.signup_email);
        EditText signupPassword = findViewById(R.id.signup_password);
        Button signupButton = findViewById(R.id.btcadastrar_user);
        TextView loginRedirectText = findViewById(R.id.loginRedirectText);

        databaseHelper = new DatabaseHelper(this);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = signupNome.getText().toString();
                String email = signupEmail.getText().toString();
                String password = signupPassword.getText().toString();

                if (nome.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(activity_cadastrar_user.this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
                } else {
                    if (!databaseHelper.checkEmail(email)) {
                        boolean insert = databaseHelper.insertData(nome, email, password);
                        if (insert) {
                            Toast.makeText(SignupActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Login.class));
                        } else {
                            Toast.makeText(SignupActivity.this, "Falha ao cadastrar!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignupActivity.this, "Usuário já existe! Faça login.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, Login.class));
            }
        });
    }
}
