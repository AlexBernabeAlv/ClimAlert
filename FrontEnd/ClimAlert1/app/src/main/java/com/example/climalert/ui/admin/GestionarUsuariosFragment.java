package com.example.climalert.ui.admin;

import static java.lang.Thread.sleep;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.climalert.CosasDeTeo.InformacionUsuario;
import com.example.climalert.CosasDeTeo.Notificacion;
import com.example.climalert.CosasDeTeo.UsuarioEstandar;
import com.example.climalert.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class GestionarUsuariosFragment extends Fragment {
    JSONObject mapa = new JSONObject();
    LinearLayout linearLayout;
    public Vector<UsuarioEstandar> usuariosEstandar = new Vector<UsuarioEstandar>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getUsuariosEstandar();
        try {
            sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int n = usuariosEstandar.capacity();
        View view = inflater.inflate(R.layout.fragment_usuarios, container, false);
        linearLayout = view.findViewById(R.id.linear_layout_usuarios);
        for(int i = 1; i <= n; ++i) {
            Button btn = new Button(getContext());
            btn.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            btn.setId(i);
            btn.setText(String.valueOf(i)); //esto ha de ser el email del usuario
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button ban = new Button(getContext());
                    ban.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    ban.setText("BAN");
                    ban.setBackgroundColor(Color.RED);
                    linearLayout.addView(ban);
                    ban.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            banear();
                        }
                    });
                    Button ver = new Button(getContext());
                    ver.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    ver.setText("VER");
                    ver.setBackgroundColor(Color.GREEN);
                    linearLayout.addView(ver);
                    ver.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ver_perfil();
                        }
                    });

                }
            });
            linearLayout.addView(btn);
        }
        return view;

    }

    private void banear() {
    }

    private void ver_perfil(){

    }

    //esta función obtiene una lista con todos los usuarios que no son admin
    public void getUsuariosEstandar() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://climalert.herokuapp.com/usuariosEstandar";
        mapa = new JSONObject();
        try {
            mapa.put("email", InformacionUsuario.getInstance().email);
            mapa.put("password", InformacionUsuario.getInstance().password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Request a string response from the provided URL.
        InformacionUsuario.myJsonArrayRequest request = new InformacionUsuario.myJsonArrayRequest(Request.Method.POST, url, mapa,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject usuarioResponse;
                        usuariosEstandar.clear();
                        try {
                            for (int i = 0; i < response.length(); ++i) {
                                usuarioResponse = response.getJSONObject(i);

                                String email =  usuarioResponse.getString("email");
                                String password =  usuarioResponse.getString("password");
                                boolean admin =  usuarioResponse.getBoolean("admin");
                                JSONObject filtro = usuarioResponse.getJSONObject("filtro");
                                int radioEfecto =  filtro.getInt("radioEfecto");
                                int gravedad =  filtro.getInt("gravedad");
                                UsuarioEstandar n = new UsuarioEstandar(email, password, radioEfecto, gravedad, admin);
                                usuariosEstandar.add(n);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.d("secun", "dar loc fallar " + error);
                    }

                }) {
        };
        queue.add(request);
    }

    private void buclear() {

        refresh(600);
    }

    private void refresh(int milliseconds){
        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                buclear();
            }
        };
        handler.postDelayed(runnable, milliseconds);

    }

}
