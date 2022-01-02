package com.example.climalert;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.climalert.CosasDeTeo.InformacionUsuario;
import com.example.climalert.ui.catastrofes.Avalancha_Fragment;
import com.example.climalert.ui.catastrofes.Calor_Extremo_Fragment;
import com.example.climalert.ui.catastrofes.Erupcion_Volcanica_Fragment;
import com.example.climalert.ui.catastrofes.Gota_Fria_Fragment;
import com.example.climalert.ui.catastrofes.Granizo_Fragment;
import com.example.climalert.ui.catastrofes.Incendio_Forestal_Fragment;
import com.example.climalert.ui.catastrofes.Inundacion_Fragment;
import com.example.climalert.ui.catastrofes.Lluvia_Acida_Fragment;
import com.example.climalert.ui.catastrofes.Terremoto_Fragment;
import com.example.climalert.ui.catastrofes.Tormenta_Electrica_Fragment;
import com.example.climalert.ui.catastrofes.Tormenta_Invernal_Fragment;
import com.example.climalert.ui.catastrofes.Tornado_Fragment;
import com.example.climalert.ui.catastrofes.Tsunami_Fragment;

public class VentanaIncidencia extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String nombreFenomeno;

    private int IdInc;

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
     *
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

        Button Info = (Button) view.findViewById(R.id.btnConsejo);
        Info.setOnClickListener(this);
        Button Foro = (Button) view.findViewById(R.id.btnForo);
        Foro.setOnClickListener(this);

        IdInc = Integer.parseInt(InformacionUsuario.getInstance().IDIncidenciaActual);

        for (int i = 0; i < InformacionUsuario.getInstance().actual.size(); ++i) {
            if (IdInc == InformacionUsuario.getInstance().actual.get(i).identificador) {
                String SFecha;
                String wtf = InformacionUsuario.getInstance().actual.get(i).fecha;
                if(InformacionUsuario.getInstance().actual.get(i).fecha == null || InformacionUsuario.getInstance().actual.get(i).fecha == "null") {
                    SFecha ="Fecha: no hay fecha disponible";
                }
                else {
                    SFecha = "Fecha: " + InformacionUsuario.getInstance().actual.get(i).fecha.substring(0, 10);
                }
                String SID = "ID: " + InformacionUsuario.getInstance().actual.get(i).identificador;
                Fecha.setText(SFecha);
                ID.setText(SID);
                Hora.setText(InformacionUsuario.getInstance().actual.get(i).descripcion);
                //Hora.setText("Hora: " + String.valueOf(InformacionUsuario.getInstance().actual.get(i).));
                nombreFenomeno = InformacionUsuario.getInstance().actual.get(i).nombre;
                Titulo.setText(nombreFenomeno);
            }
        }
        ID.setText(InformacionUsuario.getInstance().IDIncidenciaActual);



        return view;
    }

    @Override
    public void onClick(View view) {
        MainActivity main;
        switch (view.getId()) {
            case R.id.btnConsejo:
                switch (nombreFenomeno) {
                    case "Calor Extremo":
                        main = (MainActivity) getActivity();
                        main.catastrofe_func(new Calor_Extremo_Fragment());
                        break;

                    case "Granizo":
                        main = (MainActivity) getActivity();
                        main.catastrofe_func(new Granizo_Fragment());
                        break;

                    case "Tormenta Invernal":
                        main = (MainActivity) getActivity();
                        main.catastrofe_func(new Tormenta_Invernal_Fragment());
                        break;

                    case "Tornado":
                        main = (MainActivity) getActivity();
                        main.catastrofe_func(new Tornado_Fragment());
                        break;

                    case "Inundacion":
                        main = (MainActivity) getActivity();
                        main.catastrofe_func(new Inundacion_Fragment());
                        break;

                    case "Incendio":
                        main = (MainActivity) getActivity();
                        main.catastrofe_func(new Incendio_Forestal_Fragment());
                        break;

                    case "Tsunami":
                        main = (MainActivity) getActivity();
                        main.catastrofe_func(new Tsunami_Fragment());
                        break;

                    case "Terremoto":
                        main = (MainActivity) getActivity();
                        main.catastrofe_func(new Terremoto_Fragment());
                        break;

                    case "Avalancha":
                        main = (MainActivity) getActivity();
                        main.catastrofe_func(new Avalancha_Fragment());
                        break;

                    case "LLuvia Ácida":
                        main = (MainActivity) getActivity();
                        main.catastrofe_func(new Lluvia_Acida_Fragment());
                        break;

                    case "Erupción Volcánica":
                        main = (MainActivity) getActivity();
                        main.catastrofe_func(new Erupcion_Volcanica_Fragment());
                        break;

                    case "Gota Fria":
                        main = (MainActivity) getActivity();
                        main.catastrofe_func(new Gota_Fria_Fragment());
                        break;

                    case "Tornmenta Eléctrica":
                        main = (MainActivity) getActivity();
                        main.catastrofe_func(new Tormenta_Electrica_Fragment());
                        break;
                }
                break;
            case R.id.btnForo:
                main = (MainActivity) getActivity();
                main.foro_incidencia_boton(IdInc, true);
                break;
        }
    }
}