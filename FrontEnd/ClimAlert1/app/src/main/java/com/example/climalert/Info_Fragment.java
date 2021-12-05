package com.example.climalert;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.climalert.ui.catastrofes.Avalancha_Fragment;
import com.example.climalert.ui.catastrofes.Gota_Fria_Fragment;
import com.example.climalert.ui.catastrofes.Granizo_Fragment;
import com.example.climalert.ui.catastrofes.Incendio_Fragment;
import com.example.climalert.ui.catastrofes.Insolacion_Fragment;
import com.example.climalert.ui.catastrofes.Inundacion_Fragment;
import com.example.climalert.ui.catastrofes.Lluvia_Acida_Fragment;
import com.example.climalert.ui.catastrofes.Terremoto_Fragment;
import com.example.climalert.ui.catastrofes.Tormenta_Electrica_Fragment;
import com.example.climalert.ui.catastrofes.Tornado_Fragment;
import com.example.climalert.ui.catastrofes.Tsunami_Fragment;
import com.example.climalert.ui.catastrofes.Volcan_Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Info_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Info_Fragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Info_Fragment() {
        // Required empty public constructor
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
    public static Info_Fragment newInstance(String param1, String param2) {
        Info_Fragment fragment = new Info_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_informacion, container, false);

        Button b_insolacion = (Button) view.findViewById(R.id.button_insolacion);
        b_insolacion.setOnClickListener(this);

        Button b_granizo = (Button) view.findViewById(R.id.button_granizo);
        b_granizo.setOnClickListener(this);

        Button b_nevada = (Button) view.findViewById(R.id.button_nevada);
        b_nevada.setOnClickListener(this);

        Button b_tornado = (Button) view.findViewById(R.id.button_tornado);
        b_tornado.setOnClickListener(this);

        Button b_inundacion = (Button) view.findViewById(R.id.button_inundacion);
        b_inundacion.setOnClickListener(this);

        Button b_incendio = (Button) view.findViewById(R.id.button_incendio);
        b_incendio.setOnClickListener(this);

        Button b_terremoto = (Button) view.findViewById(R.id.button_terremoto);
        b_terremoto.setOnClickListener(this);

        Button b_tsunami = (Button) view.findViewById(R.id.button_tsunami);
        b_tsunami.setOnClickListener(this);

        Button b_avalancha = (Button) view.findViewById(R.id.button_avalancha);
        b_avalancha.setOnClickListener(this);

        Button b_lluvia_acida = (Button) view.findViewById(R.id.button_lluvia_acida);
        b_lluvia_acida.setOnClickListener(this);

        Button b_volcan = (Button) view.findViewById(R.id.button_volcan);
        b_volcan.setOnClickListener(this);

        Button b_gota_fria = (Button) view.findViewById(R.id.button_gota_fria);
        b_gota_fria.setOnClickListener(this);

        Button b_tor_elec = (Button) view.findViewById(R.id.button_tormenta_electrica);
        b_tor_elec.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        MainActivity main;
        switch (v.getId()) {
            case R.id.button_insolacion:
                main = (MainActivity) getActivity();
                main.catastrofe_func(new Insolacion_Fragment());
                break;

            case R.id.button_granizo:
                main = (MainActivity) getActivity();
                main.catastrofe_func(new Granizo_Fragment());
                break;

            case R.id.button_tornado:
                main = (MainActivity) getActivity();
                main.catastrofe_func(new Tornado_Fragment());
                break;

            case R.id.button_inundacion:
                main = (MainActivity) getActivity();
                main.catastrofe_func(new Inundacion_Fragment());
                break;

            case R.id.button_incendio:
                main = (MainActivity) getActivity();
                main.catastrofe_func(new Incendio_Fragment());
                break;

            case R.id.button_tsunami:
                main = (MainActivity) getActivity();
                main.catastrofe_func(new Tsunami_Fragment());
                break;

            case R.id.button_terremoto:
                main = (MainActivity) getActivity();
                main.catastrofe_func(new Terremoto_Fragment());
                break;

            case R.id.button_avalancha:
                main = (MainActivity) getActivity();
                main.catastrofe_func(new Avalancha_Fragment());
                break;

            case R.id.button_lluvia_acida:
                main = (MainActivity) getActivity();
                main.catastrofe_func(new Lluvia_Acida_Fragment());
                break;

            case R.id.button_volcan:
                main = (MainActivity) getActivity();
                main.catastrofe_func(new Volcan_Fragment());
                break;

            case R.id.button_gota_fria:
                main = (MainActivity) getActivity();
                main.catastrofe_func(new Gota_Fria_Fragment());
                break;

            case R.id.button_tormenta_electrica:
                main = (MainActivity) getActivity();
                main.catastrofe_func(new Tormenta_Electrica_Fragment());
                break;
        }
    }
}