package com.example.peekaboo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class CuriosityDetailDialogFragment extends DialogFragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_FACT = "fact";

    public static CuriosityDetailDialogFragment newInstance(String title, String fact) {
        CuriosityDetailDialogFragment fragment = new CuriosityDetailDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_FACT, fact);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString(ARG_TITLE);
        String fact = getArguments().getString(ARG_FACT);

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_curiosity_detail, null);

        TextView tvTitle = view.findViewById(R.id.tv_dialog_title);
        TextView tvFact = view.findViewById(R.id.tv_dialog_fact);

        tvTitle.setText(title);
        tvFact.setText(fact);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setPositiveButton("FECHAR", (dialog, id) -> {

                });

        return builder.create();
    }
}