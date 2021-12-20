package com.example.climalert;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import com.example.climalert.Foro.VentanaForo;
import com.example.climalert.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    String email_account;
    String currentLocale;
    private BottomNavigationView bottomNavigationView;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        email_account = InformacionUsuario.getInstance().email;
        InformacionUsuario.getInstance().setActivity(this);
        getUsuario(email_account);
        currentLocale = Locale.getDefault().getLanguage();
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("currentLocale")) {
            currentLocale = getIntent().getStringExtra("currentLocale");
        }
        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String newLocale = prefs.getString("saved_locale", currentLocale);
        cambio_idioma(newLocale);
        //Toast.makeText(this, "email es: " + email_account, Toast.LENGTH_SHORT).show();
        InformacionUsuario.getInstance().getLocalizacionesSecundarias();
        fragment = new MapsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenedor, fragment, "MAPS")
                .commit();

        //InformacionUsuario.getInstance().buclear(this);
        ActivityMainBinding binding;
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //BottomNavigationView navView = findViewById(R.id.nav_view);
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
        //SharedPreferences prefe = getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE);

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);

    }


    public void perfil_boton() {
        Fragment perfil = new PerfilFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .remove(perfil)
                .replace(R.id.contenedor, perfil, "SETTINGS")
                .commit();
    }

   public void cambio_idioma(String newLocale) {
       if (!newLocale.equals(currentLocale)) {
            currentLocale = newLocale;
            SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("saved_locale", newLocale);
            editor.apply();
            Locale myLocale = new Locale(newLocale);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            //onBackPressed();
            View v = findViewById(R.id.navigation_settings);
            v.callOnClick();
        }
    }

    public void idioma_boton() {
        Fragment idioma = new IdiomaFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .remove(idioma)
                .replace(R.id.contenedor, idioma, "SETTINGS")
                .commit();
    }

    public void foro_incidencia_boton(int IdInc) {
        Fragment foro = new VentanaForo(IdInc);
        getSupportFragmentManager()
                .beginTransaction()
                .remove(foro)
                .replace(R.id.contenedor, foro, "FORO")
                .commit();
    }

    public void catastrofe_func(Fragment catastrofe) {

        getSupportFragmentManager()
                .beginTransaction()
                .remove(catastrofe)
                .replace(R.id.contenedor, catastrofe, "CATASTROFE")
                .commit();
    }

    private void getUsuario(String email){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://climalert.herokuapp.com/usuarios/" + email;
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
                        boolean admin = false;

                        try {
                            JSONObject filtro = response.getJSONObject("filtro");
                            if(filtro != null)
                            {
                                try {
                                    JSONObject localizacion1 = filtro.getJSONObject("localizacion1");
                                    if (localizacion1 != null) {
                                        latitud1 = Float.parseFloat(localizacion1.getString("latitud"));
                                        longitud1 = Float.parseFloat(localizacion1.getString("longitud"));
                                    }
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    JSONObject localizacion2 = filtro.getJSONObject("localizacion2");
                                    if (localizacion2 != null) {
                                        latitud2 = Float.parseFloat(localizacion2.getString("latitud"));
                                        longitud2 = Float.parseFloat(localizacion2.getString("longitud"));
                                    }
                                }
                                catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                try {
                                    radio = Integer.parseInt(filtro.getString("radioEfecto"));
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    gravedad = Integer.parseInt(filtro.getString("gravedad"));
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            admin = Boolean.parseBoolean(response.getString("admin"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        InformacionUsuario.getInstance().SetInformacion(latitud1, longitud1, latitud2, longitud2, radio, gravedad, admin);

                        Log.d("a", String.valueOf(response));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }

                })
        ;
        queue.add(request);
    }

    @Override
    public void onBackPressed()
    {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragCatastrofe = fm.findFragmentByTag("CATASTROFE");
        if (fragCatastrofe != null && fragCatastrofe.isVisible()) {
            View v = findViewById(R.id.navigation_info);
            v.callOnClick();
        } else {
            Fragment fragMaps = fm.findFragmentByTag("MAPS");
            if (fragMaps != null && fragMaps.isVisible()) {
                moveTaskToBack(true);
            } else {
                Fragment fragPerfil = fm.findFragmentByTag("SETTINGS");
                if (fragPerfil != null && fragPerfil.isVisible()) {
                    View v = findViewById(R.id.navigation_settings);
                    v.callOnClick();
                } else {
                    View v = findViewById(R.id.navigation_home);
                    v.callOnClick();
                }
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new BottomNavigationView.OnNavigationItemSelectedListener(){


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment f;

            switch (item.getItemId()){

                case R.id.navigation_home:

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.contenedor, fragment, "MAPS")
                            .commit();

                    break;
                case R.id.navigation_call:

                    f = new LlamaditaFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.contenedor, f)
                            .commit();
                    break;
                case R.id.navigation_info:

                    f = new Info_Fragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.contenedor, f)
                            .commit();
                    break;
                case R.id.navigation_settings:

                    f = new Settings_Fragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.contenedor, f)
                            .commit();
                    break;

            }
            return true;
        }
    };

    public void modo_admin() {
        Fragment f = new VentanaAdminFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenedor, f)
                .commit();
    }
}