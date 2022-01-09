package com.example.climalert.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.climalert.MainActivity;
import com.example.climalert.R;

public class EliminarRefugioFragment extends Fragment implements View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_eliminar_refugio, container, false);
        String nombre = getArguments().getString("nombre");
        String lat = getArguments().getString("latitud");
        String lon = getArguments().getString("longitud");

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

        //las tres lineas siguientes para volver atras automaticamente

        MainActivity main = (MainActivity) getActivity();
        View vista = main.findViewById(R.id.navigation_settings);
        vista.callOnClick();

    }
}
