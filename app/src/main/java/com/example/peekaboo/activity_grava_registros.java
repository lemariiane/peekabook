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
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.util.Calendar;
import java.util.Locale;
import java.text.SimpleDateFormat;
import android.widget.Spinner;

public class activity_grava_registros extends AppCompatActivity {

    Button btcadastrar;
    EditText ednome, datanasc, descricao;
    Spinner spinnerEspecie;
    DatabaseHelper dbHelper;
    private int loggedInUserId = -1; // ID do usuário

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

        btcadastrar = findViewById(R.id.btcadastrar);
        ednome = findViewById(R.id.ednome);
        spinnerEspecie = findViewById(R.id.spinner_especie);
        datanasc = findViewById(R.id.datanasc);
        descricao = findViewById(R.id.descricao);

        datanasc.setOnClickListener(v -> showDatePickerDialog());
        // Impede que o usuário digite no campo, forçando o uso do calendário
        datanasc.setFocusable(false);
        datanasc.setCursorVisible(false);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btcadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg) {
                String nome = ednome.getText().toString().trim();
                String petEspecie = spinnerEspecie.getSelectedItem().toString();
                String petDatanasc = datanasc.getText().toString().trim();
                String petDescricao = descricao.getText().toString().trim();

                // Melhoria na validação: verifica se a espécie é a opção padrão "Outros" ou vazia
                if (nome.isEmpty() || petEspecie.isEmpty() || petEspecie.equals("Selecione uma opção padrão se houver")) {
                    MostraMensagem("Por favor, preencha o Nome e selecione a Espécie do Pet.");
                    return;
                }

                try {
                    Boolean checkInsert = dbHelper.insertPetData(nome, petEspecie, petDatanasc, petDescricao, loggedInUserId);

                    if (checkInsert) {
                        MostraMensagem("Pet cadastrado com sucesso!");

                        // Limpar campos
                        ednome.setText("");
                        // Não limpamos o spinnerEspecie, ele volta para a primeira opção.
                        datanasc.setText("");
                        descricao.setText("");

                        // CORREÇÃO 3: Removendo a linha de código desnecessária (já que 'especie' virou Spinner)
                        // especie.setText("");

                    } else {
                        MostraMensagem("Falha ao cadastrar o Pet. Tente novamente.");
                    }

                } catch (Exception e) {
                    MostraMensagem("Erro ao inserir dados: " + e.toString());
                }
            }
        });
    }

    /**
     * Abre o diálogo do calendário para seleção de data de nascimento.
     */
    private void showDatePickerDialog() {
        // Usa a data atual como padrão para inicializar o calendário
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear,
                                          int selectedMonth, int selectedDay) {

                        // O mês retornado é baseado em zero (0=Janeiro)
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(selectedYear, selectedMonth, selectedDay);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                        datanasc.setText(sdf.format(selectedDate.getTime()));
                    }
                },
                year, month, day);

        // Impede a seleção de datas futuras
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();
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