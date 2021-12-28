package com.example.climalert.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.climalert.R;

public class CrearRefugioFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // esto no va a fragment_avalancha
        View view = inflater.inflate(R.layout.fragment_avalancha, container, false);
        return view;
    }
}
