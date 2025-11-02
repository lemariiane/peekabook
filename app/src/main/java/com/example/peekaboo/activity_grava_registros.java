package com.example.peekaboo;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.app.AlertDialog;
import android.view.View;
import android.widget.*;

public class activity_grava_registros extends AppCompatActivity {

    Button btcadastrar;
    EditText ednome, especie, datanasc, descricao;
    DatabaseHelper dbHelper;
    private int loggedInUserId = -1; // Variável para armazenar o ID do usuário

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_grava_registros);

        dbHelper = new DatabaseHelper(this);

        String userEmail = getIntent().getStringExtra("user_email");

        if (userEmail != null) {
            loggedInUserId = dbHelper.getUserId(userEmail);
        }

        // Verifica se o ID do usuário foi encontrado
        if (loggedInUserId == -1) {
            MostraMensagem("Erro: ID do usuário logado não encontrado. Não é possível cadastrar o Pet.");
            finish();
            return;
        }


        btcadastrar = (Button) findViewById(R.id.btcadastrar);
        ednome = (EditText) findViewById(R.id.ednome);
        especie = (EditText) findViewById(R.id.especie);
        datanasc = (EditText) findViewById(R.id.datanasc);
        descricao = (EditText) findViewById(R.id.descricao);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btcadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg) {
                String nome = ednome.getText().toString().trim();
                String petEspecie = especie.getText().toString().trim();
                String petDatanasc = datanasc.getText().toString().trim();
                String petDescricao = descricao.getText().toString().trim();

                if (nome.isEmpty() || petEspecie.isEmpty()) {
                    MostraMensagem("Por favor, preencha o Nome e a Espécie do Pet.");
                    return;
                }

                try {
                    Boolean checkInsert = dbHelper.insertPetData(nome, petEspecie, petDatanasc, petDescricao, loggedInUserId);

                    if (checkInsert) {
                        MostraMensagem("Pet cadastrado com sucesso!");

                        // Limpar campos
                        ednome.setText("");
                        especie.setText("");
                        datanasc.setText("");
                        descricao.setText("");

                    } else {
                        MostraMensagem("Falha ao cadastrar o Pet. Tente novamente.");
                    }

                } catch (Exception e) {
                    MostraMensagem("Erro ao inserir dados: " + e.toString());
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