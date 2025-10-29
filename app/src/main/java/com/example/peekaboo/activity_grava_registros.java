package com.example.peekaboo;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.app.AlertDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.*;

public class activity_grava_registros extends AppCompatActivity {

    Button btcadastrar;
    EditText ednome, edtelefone, edemail, edsenha;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_grava_registros);

        btcadastrar = (Button) findViewById(R.id.btcadastrar);
        ednome = (EditText) findViewById(R.id.ednome);
        edtelefone = (EditText) findViewById(R.id.edtelefone);
        edemail = (EditText) findViewById(R.id.edemail);
        edsenha = (EditText) findViewById(R.id.edsenha);

        try {
            db = openOrCreateDatabase("banco_dados",
                    Context.MODE_PRIVATE, null);
        } catch (Exception e) {
            MostraMensagem("Erro: +" + e.toString());
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btcadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg) {
                String nome = ednome.getText().toString();
                String telefone = edtelefone.getText().toString();
                String email = edemail.getText().toString();
                String senha = edsenha.getText().toString();

                try {
                    db.execSQL("INSERT INTO usuarios (nome, telefone, email, senha) VALUES('"
                            + nome + "','" + telefone + "','" + email + "','" + senha + "')");
                    MostraMensagem("Dados cadastrados com sucesso");
                } catch (Exception e) {
                    MostraMensagem("Erro: " + e.toString());
                }
            }
        });
    }
            public void MostraMensagem (String str){
            AlertDialog.Builder dialogo = new
                    AlertDialog.Builder (activity_grava_registros.this);
            dialogo.setTitle ("Aviso") ;
            dialogo.setMessage (str) ;
            dialogo.setNeutralButton ("OK", null);
            dialogo.show ();
    }
}