package com.example.peekaboo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// Imports necessários para o DatePickerDialog
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.util.Calendar;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.text.ParseException; // Necessário para parsear a data existente

// Se você não está usando o binding em outras partes, pode remover este import
// import com.example.peekaboo.databinding.ActivityEditaPetBinding;


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

        // CORREÇÃO: Removendo o binding se você não estiver usando,
        // e mantendo apenas uma chamada de setContentView.
        // ActivityEditaPetBinding binding = ActivityEditaPetBinding.inflate(getLayoutInflater());
        // setContentView(binding.getRoot());
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

        // ==========================================================
        // INTEGRAÇÃO DO DATEPICKER NO CAMPO DE DATA
        // ==========================================================
        editDataNasc.setOnClickListener(v -> showDatePickerDialog());
        // Impede a digitação manual, forçando o uso do calendário
        editDataNasc.setFocusable(false);
        editDataNasc.setCursorVisible(false);

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
                // Garante que a data salva (ex: dd/MM/yyyy) é exibida
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
        // A data agora é garantida pelo DatePicker no formato dd/MM/yyyy
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

    /**
     * Abre o diálogo do calendário, pré-selecionando a data existente.
     */
    private void showDatePickerDialog() {
        // Usa a data atual como padrão para inicializar o calendário
        final Calendar c = Calendar.getInstance();

        // Tentativa de parsear a data existente (para o calendário abrir na data correta)
        try {
            // CORREÇÃO: Usando a variável de classe editDataNasc
            String existingDate = editDataNasc.getText().toString();
            if (!existingDate.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                c.setTime(sdf.parse(existingDate));
            }
        } catch (ParseException e) {
            // Se a data for inválida, usa a data atual (padrão)
        }

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Cria o DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear,
                                          int selectedMonth, int selectedDay) {

                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(selectedYear, selectedMonth, selectedDay);

                        // Formato: dd/MM/yyyy
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                        // CORREÇÃO: Usando a variável de classe editDataNasc
                        editDataNasc.setText(sdf.format(selectedDate.getTime()));
                    }
                },
                year, month, day);

        // Impede a seleção de datas futuras
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();
    }
}