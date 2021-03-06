package com.example.climalert.Foro;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
import com.example.climalert.ui.catastrofes.Terremoto_Fragment;

import org.json.JSONException;
import org.json.JSONObject;

public class HolderMensaje extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView nombre;
    private TextView mensaje;
    private Button btnVerComentario;
    private Button btnEliminar;
    private int id;
    private int idParent;
    private int idInc;
    private boolean esDeIncidencia;
    private boolean esDeLogeado;
    private Context c;

    public HolderMensaje(@NonNull View itemView) {
        super(itemView);
        c = itemView.getContext();
        nombre = (TextView) itemView.findViewById(R.id.nombreMensaje);
        mensaje = (TextView) itemView.findViewById(R.id.contenidoMensaje);
        Log.d("prueba", "entra en el holder");
        btnVerComentario = (Button) itemView.findViewById(R.id.btnVerComentario);
        btnVerComentario.setOnClickListener(this);
        btnEliminar = (Button) itemView.findViewById(R.id.btnEliminar);

    }

    public int getIdInc() {
        return idInc;
    }

    public void setIdInc(int idInc) {
        this.idInc = idInc;
    }

    public TextView getNombre() {
        return nombre;
    }

    public void setNombre(TextView nombre) {
        this.nombre = nombre;
    }

    public TextView getMensaje() {
        return mensaje;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMensaje(TextView mensaje) {
        this.mensaje = mensaje;
    }

    public int getId() {
        return id;
    }

    public int getIdParent() {
        return idParent;
    }

    public void setIdParent(int idParent) {
        this.idParent = idParent;
    }

    public boolean isEsDeIncidencia() {
        return esDeIncidencia;
    }

    public void setEsDeIncidencia(boolean esDeIncidencia) {
        this.esDeIncidencia = esDeIncidencia;
    }

    public boolean isEsDeLogeado() {
        return esDeLogeado;
    }

    public void setEsDeLogeado(boolean esDeLogeado) {
        this.esDeLogeado = esDeLogeado;
    }


    public Context getC() {
        return c;
    }

    public void setC(Context c) {
        this.c = c;
    }

    public void mostrarBotonEliminar() {
        Log.d("LOGEADO3", String.valueOf(esDeLogeado));
        if(esDeLogeado) {
            btnEliminar.setVisibility(View.VISIBLE);
            btnEliminar.setOnClickListener(this);
        }
        else btnEliminar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        MainActivity main;
        switch (v.getId()) {
            case R.id.btnVerComentario:
                if (InformacionUsuario.getInstance().getLevelComment() > 0) {
                    Integer prevComm = InformacionUsuario.getInstance().getCommentResponseID();
                    Log.d("pushstack", "prevComm es " + prevComm);
                    InformacionUsuario.getInstance().pushStackCommentsID(prevComm);
                }
                InformacionUsuario.getInstance().setCommentResponseID(id);
                Log.d("prueba", "entra en el boton");
                main = (MainActivity) c;
                main.foro_comentario_boton(id, idInc, false);
                break;
            case R.id.btnEliminar:
                Log.d("prueba", "elimina comentario con id " + id);
                eliminar_mensaje(id);
                main = (MainActivity) c;
                if (esDeIncidencia) main.foro_incidencia_boton(idParent, true);
                else main.foro_comentario_boton(idParent, idInc, false);
                break;
        }
    }

    public void eliminar_mensaje(int idComm) {
        RequestQueue queue = Volley.newRequestQueue(c);
        //a??adir CommentID al url
        String url = "https://climalert.herokuapp.com/comentarios/" + idComm + "/delete";
        JSONObject mapa = new JSONObject();
        try {
            mapa.put("email", String.valueOf(InformacionUsuario.getInstance().email));
            mapa.put("password", String.valueOf(InformacionUsuario.getInstance().password));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, mapa,
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
}


