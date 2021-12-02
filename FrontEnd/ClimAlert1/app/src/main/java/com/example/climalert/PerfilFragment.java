package com.example.climalert;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.gms.auth.api.Auth;
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
    //GoogleSignInClient googleSignInClient;
    //public static int RC_SIGN_IN = 0;
    View view;
    Auth_Activity auth_activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_perfil, container, false);
        logout = (Button) view.findViewById(R.id.sign_out_button);
        logout.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.sign_out_button) {
            auth_activity.getmGoogleSignInClient().signOut(); //aqui falla
            Intent intent = new Intent(getActivity(), Auth_Activity.class);
            startActivity(intent);
        }
    }
}
