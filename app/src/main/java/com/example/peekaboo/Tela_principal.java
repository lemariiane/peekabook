package com.example.peekaboo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.peekaboo.R;

import android.os.Bundle;

import com.example.peekaboo.databinding.ActivityTelaPrincipalBinding;

public class Tela_principal extends AppCompatActivity {

    ActivityTelaPrincipalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTelaPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new fragment_home());


        binding.bottomNavigationView.setBackground(null);


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new fragment_home());
                    break;

                case R.id.curiosity:
                    replaceFragment(new fragment_curiosity());
                    break;

                case R.id.pets:
                    replaceFragment(new fragment_pets());
                    break;

                case R.id.profile:
                    replaceFragment(new fragment_profile());
                    break;
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
