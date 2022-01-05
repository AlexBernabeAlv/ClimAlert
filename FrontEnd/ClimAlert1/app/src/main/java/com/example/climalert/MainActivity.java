package com.example.climalert;

import android.content.Context;
import android.content.Intent;
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
    String currentLang;
    private BottomNavigationView bottomNavigationView;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        email_account = InformacionUsuario.getInstance().email;
        InformacionUsuario.getInstance().setActivity(this);
        getUsuario(email_account);
        currentLang = Locale.getDefault().getLanguage();
        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String savedLang = prefs.getString("saved_lang", currentLang);
        if (savedLang != currentLang) {
            setCurrentLocale(savedLang);
        }
        InformacionUsuario.getInstance().getLocalizacionesSecundarias();
        fragment = new MapsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenedor, fragment, "DESTINO_BACKGROUND")
                .commit();
        configureNav();
    }

    public void configureNav() {
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_call, R.id.navigation_settings, R.id.navigation_info)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
    }

    public void changeLang (String newLang) {
        if (!newLang.equals(currentLang)) {
            SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("saved_lang", newLang);
            editor.apply();
            setCurrentLocale(newLang);
            View v = findViewById(R.id.navigation_home);
            v.callOnClick();
        }
    }
    public void perfil_boton() {
        Fragment perfil = new PerfilFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .remove(perfil)
                .replace(R.id.contenedor, perfil, "DESTINO_AJUSTES")
                .commit();
    }

    public void setCurrentLocale(String newLang) {
        currentLang = newLang;
        Locale newLocale = new Locale(newLang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(newLocale);
        res.updateConfiguration(conf, dm);
    }

    public void idioma_boton() {
        Fragment idioma = new IdiomaFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .remove(idioma)
                .replace(R.id.contenedor, idioma, "DESTINO_AJUSTES")
                .commit();
    }


    public void foro_incidencia_boton(int IdInc, boolean esDeIncidencia) {
        Fragment foro = new VentanaForo(IdInc, esDeIncidencia);
        getSupportFragmentManager()
                .beginTransaction()
                .remove(foro)
                .replace(R.id.contenedor, foro, "DESTINO_INCIDENCIA")
                .commit();
    }

    public void foro_comentario_boton(int IdCom, int IdInc, boolean esDeIncidencia) {
        Fragment foro = new VentanaForo(IdCom, IdInc, esDeIncidencia);
        getSupportFragmentManager()
                .beginTransaction()
                .remove(foro)
                .replace(R.id.contenedor, foro, "DESTINO_FORO")
                .commit();
    }


    public void admin_func(Fragment f) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenedor, f, "DESTINO_AJUSTES")
                .commit();
    }

    public void modo_admin() {
        Fragment f = new VentanaAdminFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenedor, f, "DESTINO_AJUSTES")
                .commit();
    }

    public void catastrofe_func(Fragment catastrofe) { //se le podria hacer un rebrand
        getSupportFragmentManager()
                .beginTransaction()
                .remove(catastrofe)
                .replace(R.id.contenedor, catastrofe, "DESTINO_CONSEJOS")
                .commit();
    }

    private void getUsuario(String email) {
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
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment frag = fm.findFragmentByTag("DESTINO_CONSEJOS");
        if (frag != null && frag.isVisible()) {
            View v = findViewById(R.id.navigation_info);
            v.callOnClick();
            return;
        }
        frag = fm.findFragmentByTag("DESTINO_BACKGROUND");
        if (frag != null && frag.isVisible()) {
            moveTaskToBack(true);
            return;
        }
        frag = fm.findFragmentByTag("DESTINO_AJUSTES");
        if (frag != null && frag.isVisible()) {
            View v = findViewById(R.id.navigation_settings);
            v.callOnClick();
            return;
        }
        frag = fm.findFragmentByTag("DESTINO_INCIDENCIA");
        if(frag != null && frag.isVisible()) {
		Fragment f = new VentanaIncidencia();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contenedor, f)
                        .commit();
        }
        frag = fm.findFragmentByTag("DESTINO_MAPA");
        if (frag != null && frag.isVisible()) {
            View v = findViewById(R.id.navigation_home);
            v.callOnClick();
            return;
        }
        frag = fm.findFragmentByTag("DESTINO_FORO");
        if (frag != null && frag.isVisible()) {
            int IdInc = Integer.parseInt(InformacionUsuario.getInstance().IDIncidenciaActual);
            Fragment foro = new VentanaForo(IdInc, true);
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(foro)
                    .replace(R.id.contenedor, foro, "DESTINO_INCIDENCIA")
                    .commit();
            return;
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
                            .replace(R.id.contenedor, fragment, "DESTINO_BACKGROUND")
                            .commit();
                    break;
                case R.id.navigation_call:

                    f = new LlamaditaFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.contenedor, f, "DESTINO_MAPA")
                            .commit();
                    break;
                case R.id.navigation_info:
                    f = new Info_Fragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.contenedor, f, "DESTINO_MAPA")
                            .commit();
                    break;
                case R.id.navigation_settings:
                    f = new Settings_Fragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.contenedor, f, "DESTINO_MAPA")
                            .commit();
                    break;
            }
            return true;
        }
    };
}

