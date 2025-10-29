package com.example.peekaboo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.*;

public class Login extends AppCompatActivity {

    Button btlogin;
    EditText edemail, edsenha;
    SQLiteDatabase db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        btlogin = findViewById(R.id.btlogin);
        edemail = findViewById(R.id.edemail);
        edsenha = findViewById(R.id.edsenha);

        db = openOrCreateDatabase("banco_dados", Context.MODE_PRIVATE, null);

        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edemail.getText().toString();
                String senha = edsenha.getText().toString();

                if (email.isEmpty() || senha.isEmpty()) {
                    MostraMensagem("Preencha todos os campos!");
                    return;
                }

                try {
                    Cursor cursor = db.rawQuery(
                            "SELECT * FROM usuarios WHERE email=? AND senha=?",
                            new String[]{email, senha}
                    );

                    if (cursor.moveToFirst()) {

                        Intent i = new Intent(Login.this, MainActivity.class);
                        startActivity(i);
                    } else {
                        MostraMensagem("Usuário não encontrado. Verifique seus dados!");
                    }
                    cursor.close();
                } catch (Exception e) {
                    MostraMensagem("Erro: " + e.toString());
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
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
