package com.example.climalert.ui.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.climalert.CosasDeTeo.InformacionUsuario;
import com.example.climalert.MainActivity;
import com.example.climalert.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GestionPerfilFragment extends Fragment implements View.OnClickListener {
    JSONObject mapa = new JSONObject();
    String mail, pass;
    int gravedad, radio;
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
        Button stats_incidencias = (Button) view.findViewById(R.id.stats_incidencias_button);
        stats_incidencias.setOnClickListener(this);
        Button stats_comentarios = (Button) view.findViewById(R.id.stats_comentarios_button);
        stats_comentarios.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Fragment f;
        Bundle b = new Bundle();
        b.putString(mail, "email");
        FragmentManager fm = getFragmentManager();;
        switch (v.getId()) {
            case R.id.ban_button:
                banUsuario(mail);
                MainActivity main = (MainActivity) getActivity();
                View vista = main.findViewById(R.id.navigation_settings);
                vista.callOnClick();
                break;

            case R.id.stats_incidencias_button:
                f = new GestionPerfilStatsIncidenciasFragment();
                f.setArguments(b);
                fm.beginTransaction()
                        .replace(R.id.contenedor, f, "SETTINGS")
                        .commit();
                break;

            case R.id.stats_comentarios_button:
                f = new GestionPerfilStatsComentariosFragment();
                f.setArguments(b);
                fm.beginTransaction()
                        .replace(R.id.contenedor, f, "SETTINGS")
                        .commit();
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
}


