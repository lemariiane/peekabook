package com.example.peekaboo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.content.Intent;

// Certifique-se de que o nome da sua classe é 'Activity_grava_registros'
import com.example.peekaboo.activity_grava_registros;
import com.example.peekaboo.databinding.ActivityTelaPrincipalBinding;
import com.example.peekaboo.R;


public class Tela_principal extends AppCompatActivity {

    ActivityTelaPrincipalBinding binding;
    // Variável para armazenar o email do usuário logado
    private String userEmailLogado = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTelaPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // --- 1. RECEBER O EMAIL DA ACTIVITY DE LOGIN ---
        Intent incomingIntent = getIntent();
        if (incomingIntent != null && incomingIntent.hasExtra("user_email")) {
            // A chave "user_email" deve ser a mesma usada no Login.java
            userEmailLogado = incomingIntent.getStringExtra("user_email");
        }
        // ----------------------------------------------

        binding.fab.setOnClickListener(view -> {

            Intent intent = new Intent(Tela_principal.this, activity_grava_registros.class);

            // --- 2. RETRANSMITIR O EMAIL PARA A ACTIVITY DE CADASTRO DE PET ---
            if (userEmailLogado != null) {
                intent.putExtra("user_email", userEmailLogado);
            }
            // -----------------------------------------------------------------

            startActivity(intent);
        });

        replaceFragment(new fragment_home());
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                replaceFragment(new fragment_home());
            } else if (itemId == R.id.curiosity) {
                replaceFragment(new fragment_curiosity());
            } else if (itemId == R.id.pets) {
                replaceFragment(new fragment_pets());
            } else if (itemId == R.id.profile) {
                replaceFragment(new fragment_profile());
            }

            return true;
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}