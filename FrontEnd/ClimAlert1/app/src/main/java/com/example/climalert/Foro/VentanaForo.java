package com.example.climalert.Foro;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class VentanaForo extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView nombreUs;
    private RecyclerView rvMensajes;
    private EditText txtMensaje;
    private Button btnEnviar;

    private AdapterMensajes adapter;

    private int IdInc;

    public VentanaForo(int idInc) {
        IdInc = idInc;
    }

    public VentanaForo() {
    }

    public static VentanaForo newInstance(String param1, String param2) {
        VentanaForo fragment = new VentanaForo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.ventana_foro, container, false);


        nombreUs = (TextView) view.findViewById(R.id.nombre);
        rvMensajes = (RecyclerView) view.findViewById(R.id.rvMensajes);
        txtMensaje = (EditText) view.findViewById(R.id.txtMensaje);
        btnEnviar = (Button) view.findViewById(R.id.btnEnviar);

        adapter = new AdapterMensajes(this.getContext());
        LinearLayoutManager l = new LinearLayoutManager(this.getContext());
        rvMensajes.setLayoutManager(l);
        rvMensajes.setAdapter(adapter);
        String u = InformacionUsuario.getInstance().email;
        int pos = u.indexOf("@");
        u = u.substring(0, pos);
        nombreUs.setText(u);

        obtener_comentarios();

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviar_mensaje();
                adapter.addMensaje(new Mensaje(txtMensaje.getText().toString(), nombreUs.getText().toString()));
                txtMensaje.setText("");
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });

        return view;
    }

    private void setScrollbar() {
        rvMensajes.scrollToPosition(adapter.getItemCount()-1);
    }

        @Override
    public void onClick(View v) {

    }

    public void enviar_mensaje() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://climalert.herokuapp.com/comentarios"; //mirar url correcto
        JSONObject mensaje = new JSONObject();
        try {
            mensaje.put("Email", String.valueOf(InformacionUsuario.getInstance().email));
            mensaje.put("Password", String.valueOf(InformacionUsuario.getInstance().password));
            //Añadir atributo public String CommentResponseID; a InformacionUsuario
            if (InformacionUsuario.getInstance().CommentResponseID != "") {
                mensaje.put("CommentResponseId", String.valueOf(InformacionUsuario.getInstance().CommentResponseID));
            } else mensaje.put("Incfenid", String.valueOf(InformacionUsuario.getInstance().IDIncidenciaActual));
            //poner variable de la clase mensaje del contenido
            mensaje.put("contenido", txtMensaje.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, mensaje,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //JSONObject usuario;
                        Log.d("a", String.valueOf(response));
                        //Log.d("ALGO", "he acabado el bucle");
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

    public void obtener_comentarios() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://climalert.herokuapp.com/incidenciafenomenos/" + InformacionUsuario.getInstance().IDIncidenciaActual + "/comentarios";
        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String nombre;
                        String contenido;
                        try {
                            if (response != null) {
                                nombre = response.getString("email");
                                int pos = nombre.lastIndexOf("@");
                                nombre = nombre.substring(0, pos);
                                contenido = response.getString("contenido");
                                adapter.addMensaje(new Mensaje(contenido, nombre));
                                //aqui añadiriamos el mensaje al vector de mensajes que saldrían por pantalla
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
