package com.example.climalert.ui.admin;

import static java.lang.Thread.sleep;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.climalert.CosasDeTeo.InformacionUsuario;
import com.example.climalert.CosasDeTeo.Notificacion;
import com.example.climalert.CosasDeTeo.UsuarioEstandar;
import com.example.climalert.MainActivity;
import com.example.climalert.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class GestionarUsuariosFragment extends Fragment {
    JSONObject mapa = new JSONObject();
    View view;
    FragmentManager fm;
    ScrollView scrollView;
    GestionPerfilFragment gestionPerfilFragment;
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
        view = inflater.inflate(R.layout.fragment_usuarios, container, false);
        return view;

    }

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }


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
                                boolean banned =  usuarioResponse.getBoolean("banned");
                                int gravedad =  filtro.getInt("gravedad");
                                UsuarioEstandar n = new UsuarioEstandar(email, password,
                                        radioEfecto, gravedad, admin, banned);
                                usuariosEstandar.add(n);
                            }
                            mostrar_usuarios();
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

    public void mostrar_usuarios() {
        int n = usuariosEstandar.size();
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView = (ScrollView) view.findViewById(R.id.scroll_gestionar_usuarios);
        for(int i = 0; i < n; ++i) {
            Button btn = new Button(getContext());
            btn.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            btn.setId(i);

            UsuarioEstandar u = usuariosEstandar.get(i);
            btn.setText(u.email);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gestionar_perfil(u);
                }
            });
            linearLayout.addView(btn);
        }
        int marg = linearLayout.getWidth();
        setMargins(view, 5, 5, marg/3, 5);
        scrollView.addView(linearLayout);
    }


    private void gestionar_perfil(UsuarioEstandar u) {
        gestionPerfilFragment = new GestionPerfilFragment();
        Bundle b = new Bundle();
        b.putString("email", u.email);
        b.putString("password", u.password);
        b.putInt("radio", u.radioEfecto);
        b.putBoolean("ban", u.banned);
        b.putInt("gravedad", u.gravedad);
        gestionPerfilFragment.setArguments(b);
        fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.contenedor, gestionPerfilFragment, "DESTINO_ADMIN")
                .commit();
    }
}
