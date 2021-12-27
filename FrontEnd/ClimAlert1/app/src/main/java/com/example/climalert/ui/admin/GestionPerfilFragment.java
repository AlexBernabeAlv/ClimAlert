package com.example.climalert.ui.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.climalert.CosasDeTeo.InformacionUsuario;
import com.example.climalert.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class GestionPerfilFragment extends Fragment implements View.OnClickListener {
    JSONObject mapa = new JSONObject();
    Bundle bundle = getArguments();
    String mail, pass;
    int gravedad, radio;
    public Vector<String> estadisticosComent = new Vector<String>();
    public Vector<String> estadisticos = new Vector<String>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gestion_perfil, container, false);
        mail = getArguments().getString("email");
        pass = getArguments().getString("password");
        gravedad = getArguments().getInt("gravedad");
        radio = getArguments().getInt("radio");
        String rad, grav;
        rad = String.valueOf(radio);
        grav = String.valueOf(gravedad);
        TextView t_email = view.findViewById(R.id.texto_email_a_rellenar);
        t_email.setText(mail);
        TextView t_pass = view.findViewById(R.id.texto_password_a_rellenar);
        t_pass.setText(pass);
        TextView t_gravedad = view.findViewById(R.id.texto_gravedad_a_rellenar);
        t_gravedad.setText(grav);
        TextView t_radio = view.findViewById(R.id.texto_radio_a_rellenar);
        t_radio.setText(rad);
        Button ban = (Button) view.findViewById(R.id.ban_button);
        ban.setOnClickListener(this);
        Button stats = (Button) view.findViewById(R.id.stats_button);
        stats.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ban_button:
                banUsuario(mail);
                break;

            case R.id.stats_button:
                break;
        }
    }


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

}


