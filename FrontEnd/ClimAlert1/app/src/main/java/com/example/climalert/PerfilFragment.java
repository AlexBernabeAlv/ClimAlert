package com.example.climalert;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.climalert.CosasDeTeo.InformacionUsuario;
import com.google.android.gms.auth.api.Auth;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.climalert.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

public class PerfilFragment extends Fragment implements View.OnClickListener, Slider.OnChangeListener {
    Button logout, g_cambios, admin_button;
    //GoogleSignInClient googleSignInClient;
    //public static int RC_SIGN_IN = 0;
    View view;
    Switch switchF;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Todos estos botones y cosas se pueden hacer en local
        view = inflater.inflate(R.layout.fragment_perfil, container, false);

        logout = (Button) view.findViewById(R.id.sign_out_button);
        logout.setOnClickListener(this);

        g_cambios = (Button) view.findViewById(R.id.guardar_cambios_button);
        g_cambios.setOnClickListener(this);

        int r = InformacionUsuario.getInstance().radioEfecto;
        float x;
        if(r != 0) x = r;
        else x = 50.0f;
        Slider s = (Slider) view.findViewById(R.id.slider_radio);
        s.setValue(x);
        s.setValueFrom(0.0f);
        s.setValueTo(250.0f);
        s.addOnChangeListener(this);

        switchF = (Switch) view.findViewById(R.id.idSwitchFiltro);
        switchF.setChecked(InformacionUsuario.getInstance().gravedad == 1);
        switchF.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_out_button:
                Intent intent = new Intent(getActivity(),  Auth_Activity.class );
                Auth_Activity.mGoogleSignInClient.signOut();
                Auth_Activity.mGoogleSignInClient.revokeAccess();
                startActivity(intent);
                break;

            case R.id.idSwitchFiltro:
                if (switchF.isChecked()) InformacionUsuario.getInstance().gravedad = 1;
                else InformacionUsuario.getInstance().gravedad = 0;
                break;

            case R.id.guardar_cambios_button:
                update_usuario();
                break;


        }
    }

    @Override
    public void onValueChange(@NonNull Slider slider, float v, boolean b) {
        if(slider.getId() == R.id.slider_radio) {
            InformacionUsuario.getInstance().radioEfecto = (int) slider.getValue();
        }
    }

    public void update_usuario() {
        Log.d("retorno", "updt us");
        RequestQueue queue = Volley.newRequestQueue( InformacionUsuario.getInstance().activity);
        String url = "https://climalert.herokuapp.com/usuarios/"+InformacionUsuario.getInstance().email;
        JSONObject mapa = new JSONObject();
        mapa = new JSONObject();
        try {
            mapa.put("password", InformacionUsuario.getInstance().password);
            mapa.put("gravedad", InformacionUsuario.getInstance().gravedad);
            mapa.put("radioEfecto", InformacionUsuario.getInstance().radioEfecto);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("retorno", "catch map");
        }

        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, mapa,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //JSONObject usuario;
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
