package com.example.peekaboo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button btcriarbanco;
    SQLiteDatabase db;
    Button btcadastrar_user;
    Button btcadastrardados;
    Button btconsultardados;
    Button btlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btcriarbanco = findViewById(R.id.btcriarbanco);
        btcadastrar_user = findViewById(R.id.btcadastrar_user);
        btcadastrardados= findViewById(R.id.btcadastrardados);
        btlogin = findViewById(R.id.btlogin);
        btconsultardados = findViewById(R.id.btconsultardados);

        btcriarbanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    AlertDialog.Builder dialogo = new AlertDialog.Builder(MainActivity.this);
                    dialogo.setTitle("Aviso")
                            .setMessage("Banco de dados criado (ou já existente) com sucesso!")
                            .setNeutralButton("OK", null)
                            .show();

                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialog.Builder erro = new AlertDialog.Builder(MainActivity.this);
                    erro.setTitle("Erro")
                            .setMessage("Falha ao criar o banco: " + e.getMessage())
                            .setNeutralButton("OK", null)
                            .show();
                }
            }
        });


        btcadastrardados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View args0) {
                Intent gravaRegistroActivity = new Intent(MainActivity.this,
                        activity_grava_registros.class);
                startActivity(gravaRegistroActivity);
            }
        });

        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View args0) {
                Intent loginActivity = new Intent(MainActivity.this,
                        Login.class);
                startActivity(loginActivity);
            }
        });

        btcadastrar_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View args0) {
                Intent cadastroA = new Intent(MainActivity.this,
                        Cadastrar_user.class);
                startActivity(cadastroA);
            }
        });
    }
}