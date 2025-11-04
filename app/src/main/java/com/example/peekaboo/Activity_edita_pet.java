package com.example.peekaboo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.peekaboo.databinding.ActivityEditaPetBinding;


public class Activity_edita_pet extends AppCompatActivity {

    private int petIdToEdit = -1;
    private DatabaseHelper dbHelper;
    private EditText editNome;
    private EditText editEspecie;
    private EditText editDataNasc;
    private EditText editDescricao;
    private Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEditaPetBinding binding = ActivityEditaPetBinding.inflate(getLayoutInflater());
         setContentView(binding.getRoot());
        setContentView(R.layout.activity_edita_pet);

        dbHelper = new DatabaseHelper(this);

        // Inicializar Views
        editNome = findViewById(R.id.edit_nome);
        editEspecie = findViewById(R.id.edit_especie);
        editDataNasc = findViewById(R.id.edit_data_nasc);
        editDescricao = findViewById(R.id.edit_descricao);
        btnSalvar = findViewById(R.id.btn_salvar_edicao);

        // RECEBER O ID DO PET
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("pet_id")) {
            petIdToEdit = intent.getIntExtra("pet_id", -1);
            if (petIdToEdit != -1) {
                loadPetData(); // Carrega os dados atuais do pet
            } else {
                Toast.makeText(this, "Erro: ID do pet inválido.", Toast.LENGTH_LONG).show();
                finish();
            }
        }

        btnSalvar.setOnClickListener(v -> {
            updatePet();
        });
    }

    /** Carrega os dados atuais do pet usando o petIdToEdit. */
    private void loadPetData() {
        Cursor cursor = dbHelper.getPetById(petIdToEdit);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                editNome.setText(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                editEspecie.setText(cursor.getString(cursor.getColumnIndexOrThrow("especie")));
                editDataNasc.setText(cursor.getString(cursor.getColumnIndexOrThrow("datanasc")));
                editDescricao.setText(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
            } catch (IllegalArgumentException e) {
                Toast.makeText(this, "Erro: Colunas do banco incompatíveis. Verifique o DB.", Toast.LENGTH_LONG).show();
            }
            cursor.close();
        } else {
            Toast.makeText(this, "Pet não encontrado no banco de dados.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /** Executa a operação de UPDATE no banco de dados. */
    private void updatePet() {
        String nome = editNome.getText().toString();
        String especie = editEspecie.getText().toString();
        String dataNasc = editDataNasc.getText().toString();
        String descricao = editDescricao.getText().toString();

        if (nome.isEmpty() || especie.isEmpty() || dataNasc.isEmpty()) {
            Toast.makeText(this, "Preencha pelo menos Nome, Espécie e Data de Nascimento.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.updatePetData(petIdToEdit, nome, especie, descricao, dataNasc)) {
            Toast.makeText(this, "Pet atualizado com sucesso!", Toast.LENGTH_LONG).show();

            // Sinaliza para o Fragment que a lista precisa ser recarregada
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Erro ao atualizar o pet.", Toast.LENGTH_LONG).show();
        }
    }
}