package com.example.climalert;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.climalert.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    Bundle account;
    Fragment fragment;
    String email_account;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        account = getIntent().getExtras();
        email_account = account.getString("email");
        //Toast.makeText(this, "email es: " + email_account, Toast.LENGTH_SHORT).show();
        fragment = new MapsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenedor, fragment)
                .commit();

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_call, R.id.navigation_settings, R.id.navigation_info)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Región para guardar los datos; así el usuario accede automáticamente, sin tener que
        //pasar por [de nuevo] al inicio de sesión de Google
        SharedPreferences prefe = getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE);

        bottomNavigationView = findViewById(R.id.nav_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);


    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new BottomNavigationView.OnNavigationItemSelectedListener(){



        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment f;

            switch (item.getItemId()){

                case R.id.navigation_home:

                    f = fragment;
                    getSupportFragmentManager()
                            .beginTransaction()
                            .remove(f)
                            .replace(R.id.contenedor, fragment)
                            .commit();

                    break;
                case R.id.navigation_call:

                    f = new LlamaditaFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .remove(f)
                            .replace(R.id.contenedor, f)
                            .commit();
                    break;
                case R.id.navigation_info:

                    f = new Info_Fragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .remove(f)
                            .replace(R.id.contenedor, f)
                            .commit();
                    break;
                case R.id.navigation_settings:

                    f = new Settings_Fragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .remove(f)
                            .replace(R.id.contenedor, f)
                            .commit();
                    break;

            }


            return true;
        }
    };



}