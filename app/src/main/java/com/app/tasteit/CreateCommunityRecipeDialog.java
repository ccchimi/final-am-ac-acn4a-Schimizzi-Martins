package com.app.tasteit;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateCommunityRecipeDialog {

    public interface OnCreateRecipeListener {
        void onCreate(String title, String description, String imageUrl, String time);
    }

    private final Context context;
    private final OnCreateRecipeListener listener;

    public CreateCommunityRecipeDialog(Context context, OnCreateRecipeListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void show() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_community_recipe, null);

        EditText etTitle = view.findViewById(R.id.etTitle);
        EditText etDescription = view.findViewById(R.id.etDescription);
        EditText etImageUrl = view.findViewById(R.id.etImageUrl);
        EditText etTime = view.findViewById(R.id.etTime);

        new AlertDialog.Builder(context)
                .setTitle("Nueva receta de la comunidad")
                .setView(view)
                .setPositiveButton("Publicar", (dialog, which) -> {
                    String title = etTitle.getText().toString().trim();
                    String desc = etDescription.getText().toString().trim();
                    String url = etImageUrl.getText().toString().trim();
                    String time = etTime.getText().toString().trim();

                    if (title.isEmpty() || desc.isEmpty()) {
                        Toast.makeText(context, "Título y descripción son obligatorios", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (time.isEmpty()) time = "Sin especificar";

                    listener.onCreate(title, desc, url, time);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}