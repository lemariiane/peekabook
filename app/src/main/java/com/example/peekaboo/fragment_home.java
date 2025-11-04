// Arquivo: fragment_home.java
package com.example.peekaboo;

import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit; // Para calcular diferença de dias

public class fragment_home extends Fragment {

    private static final String ARG_USER_ID = "user_id";
    private int loggedInUserId = -1;
    private DatabaseHelper dbHelper;

    private TextView tvSaudacao;
    private TextView tvProximoEvento;
    private TextView tvPetSummary;

    public fragment_home() {

    }

    public static fragment_home newInstance(int userId) {
        fragment_home fragment = new fragment_home();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            loggedInUserId = getArguments().getInt(ARG_USER_ID, -1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        if (getContext() != null) {
            dbHelper = new DatabaseHelper(getContext());
        }

        tvSaudacao = view.findViewById(R.id.tv_home_saudacao);
        tvProximoEvento = view.findViewById(R.id.tv_proximo_evento);
        tvPetSummary = view.findViewById(R.id.tv_pet_summary);

        loadUserData();
        loadPetSummary();

        return view;
    }

    /**
     * Busca o nome do usuário e atualiza a saudação.
     */
    private void loadUserData() {
        if (dbHelper == null || loggedInUserId == -1) {
            tvSaudacao.setText("Olá!");
            return;
        }

        try (Cursor cursor = dbHelper.getUserDataById(loggedInUserId)) {
            if (cursor != null && cursor.moveToFirst()) {
                String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
                tvSaudacao.setText(String.format("Olá, %s!", nome.split(" ")[0])); // Usa só o primeiro nome
            } else {
                tvSaudacao.setText("Olá, Usuário!");
            }
        }
    }

    /**
     * Busca todos os pets para calcular o próximo aniversário e o total.
     */
    private void loadPetSummary() {
        if (dbHelper == null) return;

        try (Cursor cursor = dbHelper.getNextPetBirthday(loggedInUserId)) {
            int petCount = cursor.getCount();
            tvPetSummary.setText(String.format("Você está cuidando de %d pet(s).", petCount));

            if (petCount > 0) {
                findNextBirthday(cursor);
            } else {
                tvProximoEvento.setText("🎉 Adicione seu primeiro pet para começar!");
            }
        } catch (Exception e) {
            tvProximoEvento.setText("Erro ao carregar eventos.");
        }
    }

    /**
     * Itera sobre os pets e encontra o próximo aniversário.
     */
    private void findNextBirthday(Cursor cursor) {
        String nextPetName = null;
        long smallestDaysDifference = Long.MAX_VALUE;

        SimpleDateFormat dbFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Calendar todayCal = Calendar.getInstance();
        todayCal.set(Calendar.HOUR_OF_DAY, 0);
        todayCal.set(Calendar.MINUTE, 0);
        todayCal.set(Calendar.SECOND, 0);
        todayCal.set(Calendar.MILLISECOND, 0);

        long todayMillis = todayCal.getTimeInMillis(); // Tempo atual em milissegundos

        if (cursor.moveToFirst()) {
            do {
                try {
                    String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
                    String dtNascimentoStr = cursor.getString(cursor.getColumnIndexOrThrow("datanasc"));

                    Date dtNascimento = dbFormatter.parse(dtNascimentoStr);
                    Calendar petBirthdayCal = Calendar.getInstance();
                    petBirthdayCal.setTime(dtNascimento);

                    // Define o aniversário para o ano atual
                    petBirthdayCal.set(Calendar.YEAR, todayCal.get(Calendar.YEAR));

                    // Se o aniversário já passou este ano, define para o próximo ano
                    if (petBirthdayCal.getTimeInMillis() < todayMillis) {
                        petBirthdayCal.add(Calendar.YEAR, 1);
                    }

                    // Calcula a diferença em milissegundos
                    long diffMillis = petBirthdayCal.getTimeInMillis() - todayMillis;
                    // Converte para dias
                    long daysUntilBirthday = TimeUnit.DAYS.convert(diffMillis, TimeUnit.MILLISECONDS);

                    if (daysUntilBirthday < smallestDaysDifference) {
                        smallestDaysDifference = daysUntilBirthday;
                        nextPetName = String.format("🐶 Aniversário de %s em %d dias!", nome, smallestDaysDifference);
                    }

                } catch (ParseException e) {

                } catch (Exception e) {

                }
            } while (cursor.moveToNext());
        }

        if (nextPetName != null && smallestDaysDifference >= 0) { // Garante que a diferença não é negativa
            tvProximoEvento.setText(nextPetName);
        } else {
            tvProximoEvento.setText("Nenhum evento futuro encontrado.");
        }
    }
}