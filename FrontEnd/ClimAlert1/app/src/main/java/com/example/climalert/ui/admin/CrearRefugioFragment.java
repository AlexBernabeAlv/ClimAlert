package com.example.climalert.ui.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.climalert.CosasDeTeo.InformacionUsuario;
import com.example.climalert.R;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

public class CrearRefugioFragment extends Fragment {
    String nombreRefugio;
    Float longitudRefugio, latitudRefugio;
    AlertDialog alert = null;
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

        EditText nombre = view.findViewById(R.id.edit_text_nombreRef);
        EditText latitud = view. findViewById(R.id.edit_text_latitudRef);
        EditText longitud = view. findViewById(R.id.edit_text_longitudRef);

        Button ref = view.findViewById(R.id.crea_refugio_button);
        ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nombre.getText().toString().isEmpty() && !latitud.getText().toString().isEmpty() && !longitud.getText().toString().isEmpty()) {
                    nombreRefugio = nombre.getText().toString();
                    latitudRefugio = Float.valueOf(latitud.getText().toString());
                    longitudRefugio = Float.parseFloat(longitud.getText().toString());
                    addRefugio();
                }
                else Alert();
            }
        });
        return view;
    }

    private void Alert() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Todos los campos no han sido rellenados")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        }
                    });
            alert = builder.create();
            alert.show();

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
