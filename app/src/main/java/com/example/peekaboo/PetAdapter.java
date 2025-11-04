package com.example.peekaboo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {

    private final List<PetModel> petList;
    private final Context context;

    private final PetActionListener actionListener;
    public PetAdapter(Context context, List<PetModel> petList, PetActionListener listener) {
        this.context = context;
        this.petList = petList;
        this.actionListener = listener;
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_pet, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        PetModel pet = petList.get(position);

        holder.textPetName.setText(pet.getNome());
        holder.textPetDetails.setText(pet.getEspecie() + " | Nasc: " + pet.getDataNasc());

        holder.btnDeletePet.setOnClickListener(v -> {
            if (actionListener != null) {
                // Chama o método do Fragment, passando o ID do Pet a ser deletado
                actionListener.onDeletePet(pet.getPetId());
            } else {
                Toast.makeText(context, "Erro: Listener de exclusão não configurado.", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnEditPet.setOnClickListener(v -> {
            if (actionListener != null) {
                // Chama a função de edição do Fragment, passando o ID do Pet
                actionListener.onEditPet(pet.getPetId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    // Classe interna que armazena as views de cada item
    public static class PetViewHolder extends RecyclerView.ViewHolder {
        TextView textPetName;
        TextView textPetDetails;
        Button btnDeletePet;
        Button btnEditPet;

        public PetViewHolder(@NonNull View itemView) {
            super(itemView);
            textPetName = itemView.findViewById(R.id.text_pet_name);
            textPetDetails = itemView.findViewById(R.id.text_pet_details);
            btnDeletePet = itemView.findViewById(R.id.btn_delete_pet);
            btnEditPet = itemView.findViewById(R.id.btn_edit_pet);
        }
    }

    public interface PetActionListener {
        void onDeletePet(int petId);
        void onEditPet(int petId);
    }
}