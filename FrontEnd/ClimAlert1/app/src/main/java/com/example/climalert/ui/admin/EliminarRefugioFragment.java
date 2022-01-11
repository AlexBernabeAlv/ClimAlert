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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.climalert.CosasDeTeo.InformacionUsuario;
import com.example.climalert.CosasDeTeo.Refugio;
import com.example.climalert.MainActivity;
import com.example.climalert.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EliminarRefugioFragment extends Fragment implements View.OnClickListener {

    String nombre;
    String lat;
    String lon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_eliminar_refugio, container, false);
        nombre = getArguments().getString("nombre");
        lat = getArguments().getString("latitud");
        lon = getArguments().getString("longitud");

        TextView t_nombre = view.findViewById(R.id.texto_nombre_refugio_a_rellenar);
        t_nombre.setText(nombre);
        TextView t_lat = view.findViewById(R.id.texto_latitud_refugio_a_rellenar);
        t_lat.setText(lat);
        TextView t_lon = view.findViewById(R.id.texto_longitud_refugio_a_rellenar);
        t_lon.setText(lon);

        Button eliminar = (Button) view.findViewById(R.id.eliminar_ref_button);
        eliminar.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.eliminar_ref_button) {
            elimina();
        }
    }

    private void elimina() {

        //y te cargas el refugio
        borrarRefugio();
        //las tres lineas siguientes para volver atras automaticamente

        MainActivity main = (MainActivity) getActivity();
        View vista = main.findViewById(R.id.navigation_settings);
        vista.callOnClick();

    }

    public void borrarRefugio() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://climalert.herokuapp.com/refugios/"+ nombre+"?email="+InformacionUsuario.getInstance().email+"&password="+InformacionUsuario.getInstance().password;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
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
