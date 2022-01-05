package com.example.climalert;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import androidx.fragment.app.Fragment;

import com.example.climalert.CosasDeTeo.InformacionUsuario;
import com.example.climalert.ui.admin.GestionarRefugiosFragment;
import com.example.climalert.ui.admin.GestionarUsuariosFragment;
import com.example.climalert.ui.admin.ValidarIncidenciasFragment;
import com.example.climalert.ui.admin.incidenciasActualesFragment;
import com.google.android.material.slider.Slider;

public class VentanaAdminFragment extends Fragment implements View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Todos estos botones y cosas se pueden hacer en local
        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        Button gestionar_refugios = (Button) view.findViewById(R.id.gestionar_refugios_button);
        gestionar_refugios.setOnClickListener(this);
        Button gestionar_usuarios = (Button) view.findViewById(R.id.gestionar_usuarios_button);
        gestionar_usuarios.setOnClickListener(this);
        Button validar_incidencias = (Button) view.findViewById(R.id.validar_incidencias_button);
        validar_incidencias.setOnClickListener(this);
        Button incidencias_actuales = (Button) view.findViewById(R.id.incidencias_actuales_button);
        incidencias_actuales.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        MainActivity main;
        switch (v.getId()) {
            case R.id.gestionar_refugios_button:
                main = (MainActivity) getActivity();
                main.admin_func(new GestionarRefugiosFragment());
                break;

            case R.id.gestionar_usuarios_button:
                main = (MainActivity) getActivity();
                main.admin_func(new GestionarUsuariosFragment());
                break;

            case R.id.validar_incidencias_button:
                main = (MainActivity) getActivity();
                main.admin_func(new ValidarIncidenciasFragment());
                break;
            case R.id.incidencias_actuales_button:
                main = (MainActivity) getActivity();
                main.admin_func(new incidenciasActualesFragment());
                break;
        }

    }
}
