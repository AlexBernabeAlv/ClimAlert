package com.example.climalert;

import android.content.Intent;
import android.os.Bundle;

import com.example.climalert.CosasDeTeo.InformacionUsuario;
import com.google.android.gms.auth.api.Auth;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Settings_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Settings_Fragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Settings_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Settings_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Settings_Fragment newInstance(String param1, String param2) {
        Settings_Fragment fragment = new Settings_Fragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_config, container, false);

        Button perfil = (Button) view.findViewById(R.id.perfil_usuario);
        perfil.setOnClickListener(this);

        Button idioma = (Button) view.findViewById(R.id.idioma);
        idioma.setOnClickListener(this);

        Button admin = (Button) view.findViewById(R.id.admin);
        if(InformacionUsuario.getInstance().admin) admin.setVisibility(View.VISIBLE);
        else admin.setVisibility(View.GONE);
        admin.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        MainActivity main;
        switch (v.getId()) {
            case R.id.perfil_usuario:
                main = (MainActivity) getActivity();
                main.perfil_boton();
                break;

            case R.id.idioma:
                main = (MainActivity) getActivity();
                main.idioma_boton();
                break;

            case R.id.admin:
                main = (MainActivity) getActivity();
                main.modo_admin();
                break;

        }
    }
}