package com.app.tasteit;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mapa gastronómico");
        }

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.app_name, R.string.app_name);
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
            } else if (id == R.id.nav_mapa) {
                // ya estamos en el mapa
            } else if (id == R.id.nav_logout) {
                LoginActivity.currentUser = null;
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }

            drawerLayout.closeDrawers();
            return true;
        });

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // UI basica
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);

        // Punto central (Obelisco)
        LatLng centro = new LatLng(-34.6037, -58.3816);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centro, 14f));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-34.6030, -58.3830))
                .title("🍝 Pastas del Centro"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-34.6025, -58.3795))
                .title("🍝 La Casa de la Pasta"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-34.6050, -58.3790))
                .title("🥗 VeggieGreen"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-34.6042, -58.3822))
                .title("🥗 Naturalis Veg"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-34.6020, -58.3800))
                .title("🍔 Hamburguesas del Obelisco"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-34.6045, -58.3840))
                .title("🍔 BurgerFast 24hs"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-34.6060, -58.3815))
                .title("🍰 Delicias Dulces"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-34.6038, -58.3785))
                .title("🍰 Cake & Love"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-34.6070, -58.3807))
                .title("🍣 Sushi Master"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-34.6015, -58.3820))
                .title("🍣 Nori & Friends"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-34.6047, -58.3777))
                .title("🍕 Pizzería del Bajo"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-34.6028, -58.3850))
                .title("🍕 Pizza Express"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-34.6055, -58.3835))
                .title("☕ Café Central"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-34.6032, -58.3803))
                .title("☕ Coffee&Chill"));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle != null && toggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }
}