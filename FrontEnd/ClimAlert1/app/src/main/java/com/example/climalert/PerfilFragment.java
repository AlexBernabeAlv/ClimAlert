package com.example.climalert;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.climalert.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class PerfilFragment extends Fragment implements View.OnClickListener{
    Button logout;
    GoogleSignInClient googleSignInClient;
    public static int RC_SIGN_IN = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        logout = (Button) view.findViewById(R.id.sign_out_button);
        logout.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.sign_out_button) {
            Auth_Activity a = (Auth_Activity) getActivity(); //no ir aqui, ir a main y de ahi a auth?
            a.sign_out();
        }
    }
}
