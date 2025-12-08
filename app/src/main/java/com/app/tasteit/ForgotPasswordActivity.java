package com.app.tasteit;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etIdentifier;
    private Button btnSendRecovery;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etIdentifier = findViewById(R.id.etIdentifier);
        btnSendRecovery = findViewById(R.id.btnSendRecovery);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnSendRecovery.setOnClickListener(v -> startRecovery());
    }

    private void startRecovery() {
        String identifier = etIdentifier.getText().toString().trim();

        if (TextUtils.isEmpty(identifier)) {
            etIdentifier.setError("Ingresá tu usuario o email");
            return;
        }

        btnSendRecovery.setEnabled(false);

        // Si tiene @ lo tratamos como email directo
        if (identifier.contains("@")) {
            sendResetEmail(identifier);
        } else {
            // Lo tratamos como nombre de usuario -> buscamos email en Firestore
            db.collection("usuarios")
                    .whereEqualTo("username", identifier)  // AJUSTÁ si el campo se llama distinto
                    .limit(1)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            String email = null;
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                email = doc.getString("email"); // AJUSTÁ si el campo se llama distinto
                                break;
                            }
                            if (email != null && !email.isEmpty()) {
                                sendResetEmail(email);
                            } else {
                                Toast.makeText(this,
                                        "No se encontró un email asociado a ese usuario",
                                        Toast.LENGTH_LONG).show();
                                btnSendRecovery.setEnabled(true);
                            }
                        } else {
                            Toast.makeText(this,
                                    "Usuario no encontrado",
                                    Toast.LENGTH_LONG).show();
                            btnSendRecovery.setEnabled(true);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this,
                                "Error al buscar el usuario: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                        btnSendRecovery.setEnabled(true);
                    });
        }
    }

    private void sendResetEmail(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    btnSendRecovery.setEnabled(true);
                    if (task.isSuccessful()) {
                        Toast.makeText(this,
                                "Te enviamos un correo para restablecer tu contraseña",
                                Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        String msg = (task.getException() != null)
                                ? task.getException().getMessage()
                                : "Error desconocido";
                        Toast.makeText(this,
                                "No se pudo enviar el mail: " + msg,
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}