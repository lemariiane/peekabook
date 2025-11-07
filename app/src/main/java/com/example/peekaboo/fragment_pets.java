package com.example.peekaboo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.example.peekaboo.Activity_edita_pet;

import java.util.ArrayList;
import java.util.List;

public class fragment_pets extends Fragment implements PetAdapter.PetActionListener {

    private static final String ARG_USER_ID = "user_id";
    private static final String ARG_USER_EMAIL = "user_email";

    private int loggedInUserId = -1;
    // private String userEmail = null;

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerViewPets;
    private PetAdapter petAdapter;
    private List<PetModel> petList;
    private ActivityResultLauncher<Intent> editPetLauncher;

    public fragment_pets() {

    }

    public static fragment_pets newInstance(int userId, String email) {
        fragment_pets fragment = new fragment_pets();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);
        args.putString(ARG_USER_EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            loggedInUserId = getArguments().getInt(ARG_USER_ID, -1);
        }
        editPetLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        Toast.makeText(getContext(), "Lista de pets atualizada.", Toast.LENGTH_SHORT).show();
                        loadPetsData(); // recarrega a lista para mostrar as mudanças
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pets, container, false);

        if (getContext() != null) {
            dbHelper = new DatabaseHelper(getContext());
        }

        petList = new ArrayList<>();
        recyclerViewPets = view.findViewById(R.id.recycler_view_pets);
        recyclerViewPets.setLayoutManager(new LinearLayoutManager(getContext()));

        petAdapter = new PetAdapter(getContext(), petList, this);
        recyclerViewPets.setAdapter(petAdapter);

        loadPetsData();

        return view;
    }

    @Override
    public void onDeletePet(int petId) {
        showDeleteConfirmationDialog(petId);
    }


     //Mostrar antes de deletar o Pet.

    private void showDeleteConfirmationDialog(int petId) {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirmar Exclusão")
                .setMessage("Tem certeza que deseja remover este pet? Esta ação não pode ser desfeita.")
                .setPositiveButton("Excluir", (dialog, which) -> {
                    executeDelete(petId);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onEditPet(int petId) {
        Intent intent = new Intent(getContext(), Activity_edita_pet.class);
        intent.putExtra("pet_id", petId);

        editPetLauncher.launch(intent);
    }
    private void executeDelete(int petId) {
        if (dbHelper == null) return;

        if (dbHelper.deletePet(petId)) {
            Toast.makeText(getContext(), "Pet excluído com sucesso!", Toast.LENGTH_SHORT).show();
            // Após deletar, recarrega a lista para remover o item da tela
            loadPetsData();
        } else {
            Toast.makeText(getContext(), "Erro ao excluir o pet.", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Busca os pets do usuário logado no banco de dados e atualiza a lista.
     */
    private void loadPetsData() {
        if (dbHelper == null || loggedInUserId == -1) {
            if (getContext() != null) {
                Toast.makeText(getContext(), "Erro: ID do usuário inválido.", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        petList.clear();

        try (Cursor cursor = dbHelper.getOwnerPets(loggedInUserId)) {
            if (cursor != null && cursor.moveToFirst()) {

                int idIndex = cursor.getColumnIndexOrThrow("pet_id");
                int nomeIndex = cursor.getColumnIndexOrThrow("nome");
                int especieIndex = cursor.getColumnIndexOrThrow("especie");
                int dataNascIndex = cursor.getColumnIndexOrThrow("datanasc");
                int descricaoIndex = cursor.getColumnIndexOrThrow("descricao");
                int userIdIndex = cursor.getColumnIndexOrThrow("user_id");

                do {
                    PetModel pet = new PetModel(
                            cursor.getInt(idIndex),
                            cursor.getString(nomeIndex),
                            cursor.getString(especieIndex),
                            cursor.getString(dataNascIndex),
                            cursor.getString(descricaoIndex),
                            cursor.getInt(userIdIndex)
                    );
                    petList.add(pet);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            if (getContext() != null) {
                Toast.makeText(getContext(), "Erro ao carregar pets: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        petAdapter.notifyDataSetChanged();
    }
}