package com.example.climalert.Foro;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climalert.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterMensajes extends RecyclerView.Adapter<HolderMensaje> {

    List<Mensaje> listMensaje = new ArrayList<>();
    private Context c;

    public AdapterMensajes(Context c) {
        this.c = c;
    }

    public void addMensaje(Mensaje m) {
        listMensaje.add(m);
        notifyItemInserted(listMensaje.size());
    }

    @NonNull
    @Override
    public HolderMensaje onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.mensaje_foro, parent, false);
        return new HolderMensaje(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMensaje holder, int position) {
        holder.setEsDeLogeado(false);
        holder.getNombre().setText(listMensaje.get(position).getNombre());
        holder.getMensaje().setText(listMensaje.get(position).getMensaje());
        holder.setId(listMensaje.get(position).getId());
        holder.setIdParent(listMensaje.get(position).getIdParent());
        holder.setIdInc(listMensaje.get(position).getIdInc());
        holder.setEsDeIncidencia(listMensaje.get(position).isEsDeIncidencia());
        holder.setEsDeLogeado(listMensaje.get(position).isEsDeLogeado());

        holder.mostrarBotonEliminar();

        Log.d("LOGEADO2", listMensaje.get(position).getNombre() + " " + String.valueOf(listMensaje.get(position).isEsDeLogeado()) + " en adaptermensaje.java");

    }

    @Override
    public int getItemCount() {
        return listMensaje.size();
    }
}
