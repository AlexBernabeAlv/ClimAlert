package com.example.climalert;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.climalert.CosasDeTeo.InformacionUsuario;

public class VentanaIncidencia extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static VentanaIncidencia newInstance(String param1, String param2) {
        VentanaIncidencia fragment = new VentanaIncidencia();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Info_Fragment.
     */
    // TODO: Rename and change types and number of parameters

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
        View view = inflater.inflate(R.layout.ventanaincidencia, container, false);



        TextView Titulo = (TextView) view.findViewById(R.id.titulo);
        TextView ID = (TextView) view.findViewById(R.id.ID);
        TextView Fecha = (TextView) view.findViewById(R.id.fecha);
        TextView Hora = (TextView) view.findViewById(R.id.hora);
        Button Volver = (Button) view.findViewById(R.id.btnVolver);

        int IdInc = Integer.parseInt(InformacionUsuario.getInstance().IDIncidenciaActual);

        for (int i = 0; i < InformacionUsuario.getInstance().actual.size(); ++i) {
            if (IdInc == InformacionUsuario.getInstance().actual.get(i).identificador) {
                String SFecha = "Fecha: " + InformacionUsuario.getInstance().actual.get(i).fecha.substring(0,10);
                String SID = "ID: " + InformacionUsuario.getInstance().actual.get(i).identificador;
                Fecha.setText(SFecha);
                ID.setText(SID);
                Hora.setText(InformacionUsuario.getInstance().actual.get(i).descripcion);
                //Hora.setText("Hora: " + String.valueOf(InformacionUsuario.getInstance().actual.get(i).));
                Titulo.setText(String.valueOf(InformacionUsuario.getInstance().actual.get(i).nombre));
            }
        }

        Volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity main = (MainActivity) getActivity();
                Fragment f = new MapsFragment();
                main.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contenedor, f, "MAPS")
                        .commit();
            }
        });
        ID.setText(InformacionUsuario.getInstance().IDIncidenciaActual);
        return view;
    }

    @Override
    public void onClick(View view) {

    }
}