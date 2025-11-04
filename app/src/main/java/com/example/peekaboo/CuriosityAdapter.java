package com.example.peekaboo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CuriosityAdapter extends RecyclerView.Adapter<CuriosityAdapter.CuriosityViewHolder> {

    private final List<CuriosityModel> curiosityList;
    private final CuriosityDialogListener listener;

    public interface CuriosityDialogListener {
        void onCuriosityClicked(String title, String fullFact);
    }

    public CuriosityAdapter(List<CuriosityModel> curiosityList, CuriosityDialogListener listener) {
        this.curiosityList = curiosityList;
        this.listener = listener;
    }



    @NonNull
    @Override
    public CuriosityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_curiosity, parent, false);
        return new CuriosityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CuriosityViewHolder holder, int position) {
        CuriosityModel curiosity = curiosityList.get(position);

        holder.title.setText(curiosity.getTitle());

        // Um snippet do fato para exibir no card (máx. 100 caracteres)
        String fullFact = curiosity.getFact();
        String snippet = fullFact.length() > 100 ?
                fullFact.substring(0, 100) + "..." :
                fullFact;

        holder.factSnippet.setText(snippet);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                //Passa a Pergunta (Title) e a Resposta Completa (Fact)
                listener.onCuriosityClicked(curiosity.getTitle(), curiosity.getFact());
            }
        });
    }

    @Override
    public int getItemCount() {
        return curiosityList.size();
    }


    public static class CuriosityViewHolder extends RecyclerView.ViewHolder {
        TextView title; // Para a Pergunta
        TextView factSnippet; // Para a primeira linha da Resposta

        public CuriosityViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_curiosity_title);
            factSnippet = itemView.findViewById(R.id.tv_curiosity_fact_snippet);
        }
    }
}