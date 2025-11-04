package com.example.peekaboo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Toast;

import com.example.peekaboo.activity_grava_registros;
import com.example.peekaboo.databinding.ActivityTelaPrincipalBinding;
import com.example.peekaboo.R;


public class Tela_principal extends AppCompatActivity {

    ActivityTelaPrincipalBinding binding;
    private String userEmailLogado = null;
    private int loggedInUserId = -1;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTelaPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);

        Intent incomingIntent = getIntent();
        if (incomingIntent != null && incomingIntent.hasExtra("user_email")) {
            userEmailLogado = incomingIntent.getStringExtra("user_email");

            loggedInUserId = dbHelper.getUserId(userEmailLogado);
        }

        if (loggedInUserId == -1) {
            Toast.makeText(this, "Erro: Usuário não identificado. Faça login novamente.", Toast.LENGTH_LONG).show();
        }

        binding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(Tela_principal.this, activity_grava_registros.class);

            //RETRANSMITIR O EMAIL (A activity de gravação usa o email para buscar o ID)
            if (userEmailLogado != null) {
                intent.putExtra("user_email", userEmailLogado);
            }
            startActivity(intent);
        });

        // Chama uma nova versão do replaceFragment que sabe passar dados.
        replaceFragment(new fragment_home(), loggedInUserId, userEmailLogado);
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                replaceFragment(new fragment_home(), loggedInUserId, userEmailLogado);
            } else if (itemId == R.id.curiosity) {
                replaceFragment(new fragment_curiosity(), loggedInUserId, userEmailLogado);
            } else if (itemId == R.id.pets) {
                replaceFragment(new fragment_pets(), loggedInUserId, userEmailLogado);
            } else if (itemId == R.id.profile) {
                replaceFragment(new fragment_profile(), loggedInUserId, userEmailLogado);
            }

            return true;
        });
    }

    //SOBRECARREGADO PARA PASSAR DADOS
    private void replaceFragment(Fragment fragment, int userId, String email) {

        Bundle args = new Bundle();
        args.putInt("user_id", userId);
        args.putString("user_email", email);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}