package com.example.climalert.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.climalert.CosasDeTeo.InformacionUsuario;
import com.example.climalert.R;

import org.json.JSONException;
import org.json.JSONObject;

public class CrearRefugioFragment extends Fragment {
    String nombreRefugio;
    Float longitudRefugio;
    Float latitudRefugio;
    JSONObject mapa = new JSONObject();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // esto no va a fragment_avalancha
        View view = inflater.inflate(R.layout.fragment_crear_refugio, container, false);
        EditText nombre = (EditText) view.findViewById(R.id.edit_text_nombreRef);
        nombreRefugio = nombre.getText().toString();
        EditText latitud = (EditText) view. findViewById(R.id.edit_text_latitudRef);
        String lat = latitud.getText().toString();
        //latitudRefugio = Float.parseFloat(lat);
        EditText longitud = (EditText) view. findViewById(R.id.edit_text_longitudRef);
        String longi = longitud.getText().toString();
        //longitudRefugio = Float.parseFloat(longi);

        //boton añadir

        addRefugio();
        return view;
    }

    public void addRefugio() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://climalert.herokuapp.com/refugios";
        mapa = new JSONObject();
        try {
            mapa.put("email", InformacionUsuario.getInstance().email);
            mapa.put("password", InformacionUsuario.getInstance().password);
            try {
                mapa.put("nombre", nombreRefugio);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                mapa.put("latitud", latitudRefugio);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                mapa.put("longitud", longitudRefugio);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, mapa,
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
