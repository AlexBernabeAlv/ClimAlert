package com.example.climalert.ui.admin;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.climalert.CosasDeTeo.InformacionUsuario;
import com.example.climalert.MainActivity;
import com.example.climalert.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ValidaIncidenciaFragment extends Fragment implements View.OnClickListener {
    JSONObject mapa = new JSONObject();
    String fecha,hora,nombre,desc, fuente;
    int radio, id;
    float latitud, longitud;
    CheckBox gravedad;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_valida_una_incidencia, container, false);
        assert getArguments() != null;
        fecha = getArguments().getString("fecha");
        hora = getArguments().getString("hora");
        nombre = getArguments().getString("nombre");
        desc = getArguments().getString("descripcion");
        fuente = getArguments().getString("fuente");
        radio = getArguments().getInt("radio");
        id  = getArguments().getInt("id");
        latitud = getArguments().getFloat("latitud");
        longitud  = getArguments().getFloat("longitud");

        TextView t_nombre = view.findViewById(R.id.texto_nombre_incidencia_a_rellenar);
        t_nombre.setText(nombre);
        TextView t_fecha = view.findViewById(R.id.texto_fecha_incidencia_a_rellenar);
        t_fecha.setText(fecha);
        TextView t_hora = view.findViewById(R.id.texto_hora_incidencia_a_rellenar);
        t_hora.setText(hora);
        TextView t_desc = view.findViewById(R.id.texto_desc_incidencia_a_rellenar);
        t_desc.setText(desc);
        TextView t_fuente = view.findViewById(R.id.texto_fuente_incidencia_a_rellenar);
        t_fuente.setText(fuente);
        String lat, lon, radi;
        lat = String.valueOf(latitud);
        lon = String.valueOf(longitud);
        radi = String.valueOf(radio);
        TextView t_lat = view.findViewById(R.id.texto_latitud_incidencia_a_rellenar);
        t_lat.setText(lat);
        TextView t_long = view.findViewById(R.id.texto_longitud_incidencia_a_rellenar);
        t_long.setText(lon);
        TextView t_rad = view.findViewById(R.id.texto_radio_incidencia_a_rellenar);
        t_rad.setText(radi);

        gravedad = (CheckBox) view.findViewById(R.id.checkbox_gravedad);
        gravedad.setOnClickListener(this);

        Button val = (Button) view.findViewById(R.id.valida_una_incidencia_button);
        val.setOnClickListener(this);

        return view;
    }

    public void validarIncidencia(int i) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://climalert.herokuapp.com/incidencias?id="+ id;
        mapa = new JSONObject();
        try {
            mapa.put("email", InformacionUsuario.getInstance().email);
            mapa.put("password", InformacionUsuario.getInstance().password);
            try {
                //aqui en vez de un 8 habra que pasarle algo
                mapa.put("gravedad", i);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.valida_una_incidencia_button:
                if(gravedad.isChecked()) validarIncidencia(1);
                else validarIncidencia(0);
                MainActivity main = (MainActivity) getActivity();
                View vista = main.findViewById(R.id.navigation_settings);
                vista.callOnClick();
                break;
        }
    }
}
