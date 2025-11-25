package com.app.tasteit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etEmail, etUsername, etPassword;
    private RecyclerView rvMyRecipes;

    private SharedPreferences userPrefs;
    private SharedPreferences communityPrefs;
    private Gson gson = new Gson();

    private List<CommunityRecipe> myCommunityRecipes = new ArrayList<>();
    private CommunityRecipeAdapter myAdapter;

    private static final String USERS_PREFS = "UsersPrefs";
    private static final String COMMUNITY_PREFS = "CommunityPrefs";
    private static final String COMMUNITY_KEY = "community_recipes";

    // Drawer
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    // Firebase
    private FirebaseAuth auth;
    private String currentEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // ---- Firebase: usuario actual ----
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            // Si por alguna razón no hay usuario, lo mando al Login
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        if (user.getEmail() != null) {
            currentEmail = user.getEmail();
        }

        // ---- Toolbar + Drawer ----
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.app_name,
                R.string.app_name
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_inicio) {
                startActivity(new Intent(this, MainActivity.class));
            } else if (id == R.id.nav_recetas) {
                startActivity(new Intent(this, RecipesActivity.class));
            } else if (id == R.id.nav_comunidad) {
                startActivity(new Intent(this, CommunityActivity.class));
            } else if (id == R.id.nav_favoritos) {
                Intent intent = new Intent(this, RecipesActivity.class);
                intent.putExtra("showFavorites", true);
                startActivity(intent);
            } else if (id == R.id.nav_logout) {
                // Usamos el logout centralizado con Firebase
                LoginActivity.logout(this);
            }

            drawerLayout.closeDrawers();
            return true;
        });

        // ---- Menú de cuenta (popup con Mi perfil / Logout) ----
        ImageView ivAccount = findViewById(R.id.ivAccount);
        AccountMenuHelper.setup(this, ivAccount);

        // ---- Lógica de perfil ----
        userPrefs = getSharedPreferences(USERS_PREFS, MODE_PRIVATE);
        communityPrefs = getSharedPreferences(COMMUNITY_PREFS, MODE_PRIVATE);

        etFirstName = findViewById(R.id.etProfileFirstName);
        etLastName = findViewById(R.id.etProfileLastName);
        etEmail = findViewById(R.id.etProfileEmail);
        etUsername = findViewById(R.id.etProfileUsername);
        etPassword = findViewById(R.id.etProfilePassword);

        rvMyRecipes = findViewById(R.id.rvMyCommunityRecipes);
        rvMyRecipes.setLayoutManager(new LinearLayoutManager(this));

        Button btnSave = findViewById(R.id.btnSaveProfile);
        Button btnLogout = findViewById(R.id.btnLogout);

        // Mostrar email como dato base del usuario
        etEmail.setText(currentEmail);

        // Cargar datos guardados de perfil (asociados al email)
        loadProfileData(currentEmail);

        // Cargar recetas propias de la comunidad (autor = email actual)
        loadMyCommunityRecipes(currentEmail);

        myAdapter = new CommunityRecipeAdapter(this, myCommunityRecipes);
        rvMyRecipes.setAdapter(myAdapter);

        btnSave.setOnClickListener(v -> {
            saveProfileData(currentEmail);
            Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
        });

        btnLogout.setOnClickListener(v -> {
            LoginActivity.logout(this);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull android.view.MenuItem item) {
        if (toggle != null && toggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    private void loadProfileData(String emailKey) {
        String firstName = userPrefs.getString("profile_firstName_" + emailKey, "");
        String lastName  = userPrefs.getString("profile_lastName_" + emailKey, "");
        String email     = userPrefs.getString("profile_email_" + emailKey, currentEmail);
        String username  = userPrefs.getString("profile_username_" + emailKey, "");
        String password  = userPrefs.getString("profile_password_" + emailKey, "");

        etFirstName.setText(firstName);
        etLastName.setText(lastName);
        etEmail.setText(email);
        etUsername.setText(username);
        etPassword.setText(password);
    }

    private void saveProfileData(String emailKey) {
        userPrefs.edit()
                .putString("profile_firstName_" + emailKey,
                        etFirstName.getText().toString().trim())
                .putString("profile_lastName_" + emailKey,
                        etLastName.getText().toString().trim())
                .putString("profile_email_" + emailKey,
                        etEmail.getText().toString().trim())
                .putString("profile_username_" + emailKey,
                        etUsername.getText().toString().trim())
                .putString("profile_password_" + emailKey,
                        etPassword.getText().toString().trim())
                .apply();
    }

    private void loadMyCommunityRecipes(String emailKey) {
        String json = communityPrefs.getString(COMMUNITY_KEY, null);
        if (json == null) {
            myCommunityRecipes = new ArrayList<>();
            return;
        }

        Type type = new TypeToken<List<CommunityRecipe>>(){}.getType();
        List<CommunityRecipe> all = gson.fromJson(json, type);
        if (all == null) {
            myCommunityRecipes = new ArrayList<>();
            return;
        }

        myCommunityRecipes = new ArrayList<>();
        for (CommunityRecipe r : all) {
            if (r.getAuthor() != null && r.getAuthor().equals(emailKey)) {
                myCommunityRecipes.add(r);
            }
        }
    }
}