package com.example.climalert.ui.admin;

import android.os.Bundle;
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
import com.example.climalert.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ValidarIncidenciasFragment extends Fragment {
    JSONObject mapa = new JSONObject();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_validar, container, false);
        return view;
    }

    public void getNoValidas() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://climalert.herokuapp.com/usuarios/"+ InformacionUsuario.getInstance().email+"/incidenciasNoValidas";
        mapa = new JSONObject();
        try {
            mapa.put("password", InformacionUsuario.getInstance().password);
            if (InformacionUsuario.getInstance().latitudactual != 0) {
                mapa.put("latitud", InformacionUsuario.getInstance().latitudactual);
                mapa.put("longitud", InformacionUsuario.getInstance().longitudactual);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, mapa,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //JSONObject usuario;
                        //Log.d("a", String.valueOf(response));
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
