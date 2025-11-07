package com.example.peekaboo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_grava_lembrete extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private int loggedInUserId = -1;

    private Spinner spinnerPetSelecao;
    private Spinner spinnerTipoLembrete;
    private TextInputEditText editTextDescricao;
    private Button buttonSelecionarData;
    private Button buttonSelecionarHora;
    private Button buttonSalvarLembrete;
    private TextView textViewDataHoraSelecionadas;

    // Dados temporários
    private String selectedDate = ""; // Formato "DD/MM/AAAA"
    private String selectedTime = ""; // Formato "HH:MM"

    private Map<String, Integer> petMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grava_lembrete);

        loggedInUserId = getIntent().getIntExtra("user_id", -1);
        if (loggedInUserId == -1) {
            Toast.makeText(this, "Erro: Usuário não identificado. Tente logar novamente.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        dbHelper = new DatabaseHelper(this);

        inicializarComponentes();

        loadPetsToSpinner();

        buttonSelecionarData.setOnClickListener(v -> showDatePicker());
        buttonSelecionarHora.setOnClickListener(v -> showTimePicker());
        buttonSalvarLembrete.setOnClickListener(v -> salvarLembrete());
    }

    private void inicializarComponentes() {
        spinnerPetSelecao = findViewById(R.id.spinner_pet_selecao);
        spinnerTipoLembrete = findViewById(R.id.spinner_tipo_lembrete);
        editTextDescricao = findViewById(R.id.edit_text_descricao);
        buttonSelecionarData = findViewById(R.id.button_selecionar_data);
        buttonSelecionarHora = findViewById(R.id.button_selecionar_hora);
        buttonSalvarLembrete = findViewById(R.id.button_salvar_lembrete);
        textViewDataHoraSelecionadas = findViewById(R.id.text_view_data_hora_selecionadas);
    }

    //  Carrega a lista de pets do usuário (dbHelper.getAllPetsForUser)

    private void loadPetsToSpinner() {

        List<String> petNames = new ArrayList<>();
        petNames.add("Escolha o pet");
        petMap.put("Escolha o pet", -1); // ID -1 representa que nenhum pet foi selecionado

        List<PetModel> pets = dbHelper.getAllPetsForUser(loggedInUserId);

        if (pets != null) {
            for (PetModel pet : pets) {
                String name = pet.getNome();
                petNames.add(name);
                petMap.put(name, pet.getUserId());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                petNames);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPetSelecao.setAdapter(adapter);
    }


    // Mostra o seletor de data
    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // Formato: DD/MM/AAAA
                    selectedDate = String.format("%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year1);
                    updateDateTimeDisplay();
                }, year, month, day);
        datePickerDialog.show();
    }


     // Mostra o seletor de hora

    private void showTimePicker() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minuteOfHour) -> {
                    // Formato HH:MM (24h)
                    selectedTime = String.format("%02d:%02d", hourOfDay, minuteOfHour);
                    updateDateTimeDisplay();
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void updateDateTimeDisplay() {
        textViewDataHoraSelecionadas.setText("Data: " +
                (selectedDate.isEmpty() ? "Não selecionada" : selectedDate) +
                " | Hora: " +
                (selectedTime.isEmpty() ? "Não selecionada" : selectedTime));
    }


    private void salvarLembrete() {
        String descricao = editTextDescricao.getText().toString().trim();
        String tipo = spinnerTipoLembrete.getSelectedItem().toString();

        if (selectedDate.isEmpty() || selectedTime.isEmpty()) {
            Toast.makeText(this, "Selecione a Data e a Hora do lembrete.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (descricao.isEmpty()) {
            Toast.makeText(this, "Preencha a descrição do lembrete.", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedPetName = spinnerPetSelecao.getSelectedItem().toString();
        Integer petId = petMap.get(selectedPetName);
        if (petId == null) {
            Toast.makeText(this, "Erro ao identificar o Pet.", Toast.LENGTH_SHORT).show();
            return;
        }

        // salvar no banco
        boolean isSaved = dbHelper.addLembrete(
                loggedInUserId,
                petId,
                tipo,
                descricao,
                selectedDate,
                selectedTime
        );

        if (isSaved) {
            Toast.makeText(this, "Lembrete agendado com sucesso!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Falha ao salvar o lembrete.", Toast.LENGTH_LONG).show();
        }
    }
}