package com.example.climalert.ui.admin;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.climalert.CosasDeTeo.InformacionUsuario;
import com.example.climalert.CosasDeTeo.Refugio;
import com.example.climalert.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class GestionarRefugiosFragment extends Fragment {
    JSONObject mapa = new JSONObject();
    public Vector<Refugio> refugios = new Vector<Refugio>();
    LinearLayout linearLayout;
    View view;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getRefugios();
        view = inflater.inflate(R.layout.fragment_refugios, container, false);
        return view;
    }

    private void gestiona_refugios() {
        int n = refugios.size();
        linearLayout = view.findViewById(R.id.scroll_gestionar_refugios);
        for (int i = 0; i < n; ++i) {
            Button btn = new Button(getContext());
            btn.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            btn.setId(i);
            int marg = linearLayout.getWidth();
            btn.setBackgroundColor(Color.RED);
            Refugio r = refugios.get(i);
            btn.setText(r.nombre);
            setMargins(view, marg/3, 10, marg/3, 10);
            btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    eliminar_refugio(r.nombre);
                }
            });
            linearLayout.addView(btn);
        }
        Button add = new Button(getContext());
        add.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        int id = 1502;
        add.setId(id);
        add.setBackgroundColor(Color.GREEN);
        add.setText(R.string.text_add);
        int l = linearLayout.getWidth();
        int marg = l/3;
        setMargins(view, marg, 10, marg, 10);
        //add.setGravity(Gravity.CENTER_HORIZONTAL);
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //creamos otra pantalla donde ira toda la creacion del nuevo refugio
                CrearRefugioFragment f = new CrearRefugioFragment();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.contenedor, f, "SETTINGS")
                        .commit();
            }
        });
        linearLayout.addView(add);
    }

    private void eliminar_refugio(String nombre) {
        //aqui podeis eliminar el refugio
    }

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    public void getRefugios() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://climalert.herokuapp.com/allRefugios";
        mapa = new JSONObject();
        try {
            mapa.put("email", InformacionUsuario.getInstance().email);
            mapa.put("password", InformacionUsuario.getInstance().password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        InformacionUsuario.myJsonArrayRequest request = new InformacionUsuario.myJsonArrayRequest(Request.Method.POST, url, mapa,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject refugioResponse;
                        refugios.clear();
                        try {
                            for (int i = 0; i < response.length(); ++i) {
                                refugioResponse = response.getJSONObject(i);

                                String nombre =  refugioResponse.getString("nombre");
                                JSONObject localizacion = refugioResponse.getJSONObject("localizacion");
                                float latitud = Float.parseFloat(localizacion.getString("latitud"));
                                float longitud = Float.parseFloat(localizacion.getString("latitud"));
                                Refugio r = new Refugio(nombre, latitud, longitud);
                                refugios.add(r);
                            }
                            gestiona_refugios();
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
