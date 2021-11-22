package com.example.climalert;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.climalert.CosasDeTeo.InformacionUsuario;
import com.example.climalert.databinding.ActivityMainBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    String email_account;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        email_account = InformacionUsuario.getInstance().email;
        getUsuario(email_account);
        //Toast.makeText(this, "email es: " + email_account, Toast.LENGTH_SHORT).show();
        Fragment fragment = new MapsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenedor, fragment)
                .commit();

        InformacionUsuario.getInstance().buclear(this);
        InformacionUsuario.getInstance().getLocalizacionesSecundarias(this);

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

    private void getUsuario(String email){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://climalert.herokuapp.com/usuario/" + email;
                // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String password = "";
                        float latitud1 = 0;
                        float longitud1 = 0;
                        float latitud2 = 0;
                        float longitud2 = 0;
                        int gravedad = 0;
                        int radio = 0;

                        try {
                            password = response.getString("password");
                            JSONObject filtro = response.getJSONObject("filtro");
                            if(filtro != null)
                            {
                                JSONObject localizacion1 = filtro.getJSONObject("localizacion1");
                                if(localizacion1 != null) {
                                    latitud1 = Float.parseFloat(localizacion1.getString("latitud"));
                                    longitud1 = Float.parseFloat(localizacion1.getString("longitud"));
                                }
                                JSONObject localizacion2 = filtro.getJSONObject("localizacion2");
                                if(localizacion2 != null) {
                                    latitud2 = Float.parseFloat(localizacion2.getString("latitud"));
                                    longitud2 = Float.parseFloat(localizacion2.getString("longitud"));
                                }
                                radio = Integer.parseInt(filtro.getString("radioEfecto"));
                                gravedad = Integer.parseInt(filtro.getString("gravedad"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        InformacionUsuario.getInstance().SetInformacion(email, password, latitud1, longitud1, latitud2, longitud2, radio, gravedad);

                        Log.d("a", String.valueOf(response));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }

                }) {
        };
        queue.add(request);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new BottomNavigationView.OnNavigationItemSelectedListener(){



        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment f;

            switch (item.getItemId()){

                case R.id.navigation_home:

                    f = new MapsFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .remove(f)
                            .replace(R.id.contenedor, f)
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