package com.example.peekaboo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ArrayAdapter; // Necessário para o Spinner

// Imports necessários para o DatePickerDialog
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.util.Calendar;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class Activity_edita_pet extends AppCompatActivity {

    private int petIdToEdit = -1;
    private DatabaseHelper dbHelper;
    private EditText editNome;
    private Spinner spinnerEspecie;
    private EditText editDataNasc;
    private EditText editDescricao;
    private Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edita_pet);

        dbHelper = new DatabaseHelper(this);

        // 1. Inicializar Views (Obrigatório antes de usar qualquer View)
        editNome = findViewById(R.id.edit_nome);
        spinnerEspecie = findViewById(R.id.spinner_especie);
        editDataNasc = findViewById(R.id.edit_data_nasc);
        editDescricao = findViewById(R.id.edit_descricao);
        btnSalvar = findViewById(R.id.btn_salvar_edicao);

        // 2. CONFIGURAR O SPINNER (AGORA A VARIÁVEL 'spinnerEspecie' NÃO É NULA)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.pet_species_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEspecie.setAdapter(adapter);


        // 3. RECEBER O ID DO PET E CARREGAR DADOS
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

        // 4. INTEGRAÇÃO DO DATEPICKER
        editDataNasc.setOnClickListener(v -> showDatePickerDialog());
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

                // 1. OBTÉM ESPÉCIE SALVA
                String especieSalva = cursor.getString(cursor.getColumnIndexOrThrow("especie"));

                // 2. SELECIONA A ESPÉCIE NO SPINNER
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerEspecie.getAdapter();
                int spinnerPosition = adapter.getPosition(especieSalva);
                if (spinnerPosition >= 0) {
                    spinnerEspecie.setSelection(spinnerPosition);
                }

                // Carrega Data e Descrição
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

        // Obtém a espécie do Spinner
        String especie = spinnerEspecie.getSelectedItem().toString();

        String dataNasc = editDataNasc.getText().toString();
        String descricao = editDescricao.getText().toString();

        if (nome.isEmpty() || especie.isEmpty() || dataNasc.isEmpty()) {
            Toast.makeText(this, "Preencha pelo menos Nome, Espécie e Data de Nascimento.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.updatePetData(petIdToEdit, nome, especie, descricao, dataNasc)) {
            Toast.makeText(this, "Pet atualizado com sucesso!", Toast.LENGTH_LONG).show();

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
        final Calendar c = Calendar.getInstance();

        try {
            String existingDate = editDataNasc.getText().toString();
            if (!existingDate.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                c.setTime(sdf.parse(existingDate));
            }
        } catch (ParseException e) {
            // Se falhar, usa a data atual
        }

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear,
                                          int selectedMonth, int selectedDay) {

                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(selectedYear, selectedMonth, selectedDay);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                        editDataNasc.setText(sdf.format(selectedDate.getTime()));
                    }
                },
                year, month, day);

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
}