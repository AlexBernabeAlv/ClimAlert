package com.example.climalert.ui.admin;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class GestionarUsuariosFragment extends Fragment {
    JSONObject mapa = new JSONObject();
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
        View view = inflater.inflate(R.layout.fragment_usuarios, container, false);
        return view;

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
}
