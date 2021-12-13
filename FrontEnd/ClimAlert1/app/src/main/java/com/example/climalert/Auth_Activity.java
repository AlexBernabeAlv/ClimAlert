
package com.example.climalert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.climalert.CosasDeTeo.InformacionUsuario;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

public class Auth_Activity extends AppCompatActivity {
    String mail;
    String password;
    public static int RC_SIGN_IN = 0;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleSignInOptions googleConf = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.prefs_file))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleConf);
        firebaseAuth = FirebaseAuth.getInstance();
        if(signedIn()) { //Comprobamos si el usuario ya había iniciado sesión
            Intent main = new Intent(Auth_Activity.this, MainActivity.class);
            startActivity(main);
        }
        else {

            setContentView(R.layout.activity_auth);
            final SignInButton button = findViewById(R.id.sign_in_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.sign_in_button:
                            signIn();
                            break;
                    }
                }
            });
        }

    }

    private boolean signedIn() {
        GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(this);
        if(acc != null) {
            InformacionUsuario.getInstance().email = acc.getEmail();
            InformacionUsuario.getInstance().password = acc.getId();
            return true;
        }
        else return false;
    }

    public void sign_out() {
        mGoogleSignInClient.signOut();
        FirebaseAuth.getInstance().signOut();
        finishActivity(0);
        Auth_Activity a = new Auth_Activity();
        Intent intent = new Intent(a, MainActivity.class);
        startActivity(intent);//seria un new activity auth



    }




    @Override
    protected void onStart() {
        super.onStart();
    }


    private void signIn() {
        Intent singInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(singInIntent, RC_SIGN_IN);
        Intent maini = new Intent(Auth_Activity.this, MainActivity.class);
        startActivity(maini);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                handleSignInResult(task);
            } catch (ApiException e) {
                e.printStackTrace();
            }
            //setContentView(R.layout.activity_main);
        }
    }

    private void handleSignInResult (Task<GoogleSignInAccount> completedTask) throws ApiException {
        GoogleSignInAccount account = completedTask.getResult(ApiException.class);
        mail = account.getEmail();
        password = account.getId();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://climalert.herokuapp.com/usuario/new";
        JSONObject mapa = new JSONObject();
        try {
            mapa.put("email", mail);
            mapa.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, mapa,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //JSONObject usuario;
                        InformacionUsuario.getInstance().email = mail;
                        InformacionUsuario.getInstance().password = password;
                        Intent main = new Intent(Auth_Activity.this, MainActivity.class);
                        startActivity(main);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }

                }){
        };
        // Add the request to the RequestQueue.
        queue.add(request);

    }

    public GoogleSignInClient getmGoogleSignInClient() {
        return mGoogleSignInClient;
    }
}
