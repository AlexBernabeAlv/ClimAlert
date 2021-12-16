package com.example.climalert;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import androidx.fragment.app.Fragment;

import com.example.climalert.CosasDeTeo.InformacionUsuario;
import com.google.android.material.slider.Slider;

public class VentanaAdminFragment extends Fragment {
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

        return view;
    }
}
