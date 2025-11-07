package com.example.peekaboo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class fragment_profile extends Fragment {

    private static final String ARG_USER_ID = "user_id";
    private int loggedInUserId = -1;
    private DatabaseHelper dbHelper;


    private TextInputEditText editNome;
    private TextInputEditText editEmail;
    private TextInputEditText editPassword;
    private Button btnSalvar;
    private Button btnLogout;

    public fragment_profile() {

    }

    public static fragment_profile newInstance(int userId) {
        fragment_profile fragment = new fragment_profile();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            loggedInUserId = getArguments().getInt(ARG_USER_ID, -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        if (getContext() != null) {
            dbHelper = new DatabaseHelper(getContext());
        }

        editNome = view.findViewById(R.id.edit_nome_user);
        editEmail = view.findViewById(R.id.edit_email_user);
        editPassword = view.findViewById(R.id.edit_password_user);
        btnSalvar = view.findViewById(R.id.btn_salvar_perfil);
        btnLogout = view.findViewById(R.id.btn_logout);

        loadUserProfile();

        btnSalvar.setOnClickListener(v -> saveProfileChanges());
        btnLogout.setOnClickListener(v -> performLogout());

        return view;
    }


     // Carrega os dados do usuário logado e preenche os campos.

    private void loadUserProfile() {
        if (dbHelper == null || loggedInUserId == -1) {
            Toast.makeText(getContext(), "Erro: ID do usuário inválido.", Toast.LENGTH_SHORT).show();
            return;
        }

        try (Cursor cursor = dbHelper.getUserDataById(loggedInUserId)) {
            if (cursor != null && cursor.moveToFirst()) {
                editNome.setText(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                editEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));

                // Senha não é carregada pela segurança, mas pode ser editada
                editPassword.setText("");
            } else {
                Toast.makeText(getContext(), "Usuário não encontrado.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Erro ao carregar perfil: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


     // Lógica de Salvar Alterações de Perfil

    private void saveProfileChanges() {
        String novoNome = editNome.getText().toString().trim();
        String novaSenha = editPassword.getText().toString();

        if (novoNome.isEmpty()) {
            Toast.makeText(getContext(), "O nome não pode ser vazio.", Toast.LENGTH_SHORT).show();
            return;
        }

        String senhaParaSalvar = null;

        // Se a caixa de senha NÃO estiver vazia, significa que o usuário quer mudar a senha
        if (!novaSenha.isEmpty()) {
            if (novaSenha.length() < 6) {
                Toast.makeText(getContext(), "A nova senha deve ter pelo menos 6 caracteres.", Toast.LENGTH_SHORT).show();
                return;
            }

            senhaParaSalvar = novaSenha;
        }

        // Chama o método de UPDATE
        if (dbHelper.updateUserData(loggedInUserId, novoNome, senhaParaSalvar)) {
            Toast.makeText(getContext(), "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show();

            editPassword.setText("");

            // Recarrega o perfil para confirmar os novos dados
            loadUserProfile();
        } else {
            Toast.makeText(getContext(), "Nenhuma alteração foi salva ou ocorreu um erro.", Toast.LENGTH_SHORT).show();
        }
    }


     // Logout

    private void performLogout() {
        Intent intent = new Intent(getActivity(), Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpa a pilha de Activities
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
        Toast.makeText(getContext(), "Logout realizado.", Toast.LENGTH_SHORT).show();
    }
}