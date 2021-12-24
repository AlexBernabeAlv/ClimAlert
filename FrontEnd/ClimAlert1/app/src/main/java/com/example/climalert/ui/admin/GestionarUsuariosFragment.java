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
import com.example.climalert.MainActivity;
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
    View view;
    public Vector<UsuarioEstandar> usuariosEstandar = new Vector<UsuarioEstandar>();
    public Vector<String> estadisticos = new Vector<String>();
    public Vector<String> estadisticosComent = new Vector<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_usuarios, container, false);
        getUsuariosEstandar();
        return view;

    }

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    //ESTA FUNCION BANEA Y DESBANEA AL USUARIO QUE LE PASAS COMO PARAMETRO
    public void banUsuario(String usuario){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://climalert.herokuapp.com/usuarios/"+usuario+"/ban";
        mapa = new JSONObject();
        try {
            mapa.put("email", InformacionUsuario.getInstance().email);
            mapa.put("password", InformacionUsuario.getInstance().password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Request a string response from the provided URL.
        InformacionUsuario.myJsonArrayRequest request = new InformacionUsuario.myJsonArrayRequest(Request.Method.PUT, url, mapa,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
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

    //esta función obtiene una lista con todos los usuarios que no son admin
    public void getEstadisticosIncidencia() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://climalert.herokuapp.com/usuarios/"+InformacionUsuario.getInstance().email+"/estadisticosIncidencias";
        mapa = new JSONObject();
        try {
            mapa.put("filtro", "dia");
            mapa.put("password", InformacionUsuario.getInstance().password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Request a string response from the provided URL.
        InformacionUsuario.myJsonArrayRequest request = new InformacionUsuario.myJsonArrayRequest(Request.Method.POST, url, mapa,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject estadisticoResponse;
                        estadisticos.clear();
                        try {
                            for (int i = 0; i < response.length(); ++i) {
                                estadisticoResponse = response.getJSONObject(i);
                                String fecha = estadisticoResponse.getString("fecha");
                                estadisticos.add(fecha);
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

    public void getEstadisticosComentarios() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://climalert.herokuapp.com/usuarios/"+InformacionUsuario.getInstance().email+"/estadisticosIncidencias";
        mapa = new JSONObject();
        try {
            mapa.put("filtro", "dia");
            mapa.put("password", InformacionUsuario.getInstance().password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Request a string response from the provided URL.
        InformacionUsuario.myJsonArrayRequest request = new InformacionUsuario.myJsonArrayRequest(Request.Method.POST, url, mapa,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject estadisticoResponse;
                        estadisticosComent.clear();
                        try {
                            for (int i = 0; i < response.length(); ++i) {
                                estadisticoResponse = response.getJSONObject(i);
                                String fecha = estadisticoResponse.getString("fecha");
                                estadisticosComent.add(fecha);
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
                            mostrar_usuarios();
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

    public void mostrar_usuarios() {
        int n = usuariosEstandar.size();
        linearLayout = view.findViewById(R.id.linear_layout_usuarios);
        for(int i = 0; i < n; ++i) {
            Button btn = new Button(getContext());
            btn.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            btn.setId(i);
            setMargins(view, 100, 0, 0, 0);
            btn.setText(usuariosEstandar.get(i).email);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity main = (MainActivity) getActivity();
                    main.admin_func(new GestionPerfilFragment());
                }
            });
            linearLayout.addView(btn);
        }
    }
}
