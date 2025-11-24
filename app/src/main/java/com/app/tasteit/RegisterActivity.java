package com.app.tasteit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etUsername, etEmail, etPassword;
    private Button btnRegister;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etFirstName = findViewById(R.id.etRegFirstName);
        etLastName = findViewById(R.id.etRegLastName);
        etUsername = findViewById(R.id.etRegUsername);
        etEmail = findViewById(R.id.etRegEmail);
        etPassword = findViewById(R.id.etRegPassword);

        btnRegister = findViewById(R.id.btnRegisterUser);

        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() ||
                email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Completá todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email inválido");
            return;
        }

        if (pass.length() < 6) {
            etPassword.setError("Mínimo 6 caracteres");
            return;
        }

        btnRegister.setEnabled(false);

        // 1) Crear usuario en Auth
        auth.createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener(result -> {
                    String uid = result.getUser().getUid();

                    // 2) Guardar perfil en Firestore
                    Map<String, Object> data = new HashMap<>();
                    data.put("firstName", firstName);
                    data.put("lastName", lastName);
                    data.put("username", username);
                    data.put("email", email);
                    data.put("createdAt", System.currentTimeMillis());

                    db.collection("users")
                            .document(uid)
                            .set(data)
                            .addOnSuccessListener(a -> {
                                Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Error guardando datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                btnRegister.setEnabled(true);
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    btnRegister.setEnabled(true);
                });
    }
}