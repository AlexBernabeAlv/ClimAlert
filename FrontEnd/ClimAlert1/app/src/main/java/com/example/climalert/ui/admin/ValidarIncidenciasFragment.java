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
import com.example.climalert.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class ValidarIncidenciasFragment extends Fragment {
    JSONObject mapa = new JSONObject();
    public Vector<Notificacion> incidenciasNoValidas = new Vector<Notificacion>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getIncidenciasNoValidas();
        View view = inflater.inflate(R.layout.fragment_validar, container, false);
        return view;
    }


    //esta función obtiene una lista con todos las incidencias que no son validas
    //dependiendo del valor de valido devuelve las validadas (true) o las no validadas (false)
    public void getIncidenciasNoValidas() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://climalert.herokuapp.com/usuarios/"+ InformacionUsuario.getInstance().email+"/incidenciasAdmin";
        mapa = new JSONObject();
        try {
            mapa.put("password", InformacionUsuario.getInstance().password);
            mapa.put("valido", false);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Request a string response from the provided URL.
        InformacionUsuario.myJsonArrayRequest request = new InformacionUsuario.myJsonArrayRequest(Request.Method.POST, url, mapa,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject IncidenciaResponse;
                        incidenciasNoValidas.clear();
                        try {
                            for (int i = 0; i < response.length(); ++i) {
                                IncidenciaResponse = response.getJSONObject(i);


                                String fecha =  IncidenciaResponse.getString("fecha");
                                String hora =  IncidenciaResponse.getString("hora");

                                Integer id = Integer.parseInt(IncidenciaResponse.getString("id"));
                                JSONObject incidencia = IncidenciaResponse.getJSONObject("incidencia");
                                Integer radio = Integer.parseInt(incidencia.getString("radio"));
                                JSONObject localizacion = incidencia.getJSONObject("localizacion");
                                Float latitud = Float.parseFloat(localizacion.getString("latitud"));
                                Float longitud = Float.parseFloat(localizacion.getString("longitud"));
                                JSONObject femomenoMeteo = IncidenciaResponse.getJSONObject("fenomenoMeteo");
                                String fuente = IncidenciaResponse.getString("creador");
                                String nombre = femomenoMeteo.getString("nombre");
                                String descripcion = femomenoMeteo.getString("descripcion");

                                Notificacion n = new Notificacion(fecha,hora,fuente ,radio, latitud, longitud, nombre, descripcion, id);
                                incidenciasNoValidas.add(n);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        // Add the request to the RequestQueue.
        queue.add(request);
    }

    public void validarIncidencia() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://climalert.herokuapp.com/incidencias?id="+ "aquiVaLaId";
        mapa = new JSONObject();
        try {
            mapa.put("email", InformacionUsuario.getInstance().email);
            mapa.put("password", InformacionUsuario.getInstance().password);
            try {
                //aqui en vez de un 8 habra que pasarle algo
                mapa.put("gravedad", 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, mapa,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

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




}
