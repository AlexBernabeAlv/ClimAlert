package com.example.climalert;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class IdiomaFragment extends Fragment implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_idioma, container, false);
        Button catala = (Button) view.findViewById(R.id.catala_button);
        catala.setOnClickListener(this);

        Button castellano = (Button) view.findViewById(R.id.castellano_button);
        castellano.setOnClickListener(this);

        Button english = (Button) view.findViewById(R.id.english_button);
        english.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        MainActivity main;
        switch (v.getId()) {
            case R.id.catala_button:
                main = (MainActivity) getActivity();
                main.cambio_idioma();
                break;

            case R.id.castellano_button:
                main = (MainActivity) getActivity();
                main.cambio_idioma();
                break;

            case R.id.english_button:
                main = (MainActivity) getActivity();
                main.cambio_idioma();
                break;

        }
    }
}
