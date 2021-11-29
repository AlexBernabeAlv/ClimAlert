package com.example.climalert;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LlamaditaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LlamaditaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Spinner mSpinner;
    EditText descripcion;
    Button aceptar;

    public LlamaditaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LlamaditaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LlamaditaFragment newInstance(String param1, String param2) {
        LlamaditaFragment fragment = new LlamaditaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.formulario, container, false);

        mSpinner = (Spinner) view.findViewById(R.id.mSpinner);
        ArrayList<String> incidencias = new ArrayList<String>();
        incidencias.add("Insolación");
        incidencias.add("Granizo");
        incidencias.add("Nevada");
        incidencias.add("Tornado");
        incidencias.add("Inundación");
        incidencias.add("Incendio");
        incidencias.add("Terremoto");
        incidencias.add("Tsunami");
        incidencias.add("Avalancha");
        incidencias.add("Lluvia Ácida");
        incidencias.add("Volcan");
        incidencias.add("Gota Fría");
        incidencias.add("Tormenta Eléctrica");


        ArrayAdapter adp;
        adp = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                incidencias);
        mSpinner.setAdapter(adp);
        descripcion = (EditText) view.findViewById(R.id.editDescripcion);
        aceptar = (Button) view.findViewById(R.id.btnAceptar);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }
}