package com.example.climalert.CosasDeTeo;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Vector;

//import com.example.climalert.MapsFragment;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;

public class InformacionUsuario {

    public String email;
    public String password;
    public String IDIncidenciaActual;
    public String CommentResponseID;
    public float latitudactual;
    public float longitudactual;
    public float latitud1;
    public float longitud1;
    public float latitud2;
    public float longitud2;
    public int radioEfecto;
    public int gravedad;
    public boolean admin;
    public Vector<Notificacion> actual = new Vector<Notificacion>();
    public Vector<Notificacion> aPintar = new Vector<Notificacion>();
    public Vector<Integer> aBorrar = new Vector<Integer>();
    AlertDialog alert = null;
    public int actualtam = 0;
    public Activity activity;

    static private InformacionUsuario usuario;

    public void SetInformacion(float la1, float lo1, float la2, float lo2, int re, int g, boolean admin_app){
         IDIncidenciaActual = "";
         CommentResponseID = "";
         latitud1 = la1;
         longitud1 = lo1;
         latitud2 =la2;
         longitud2 = lo2;
         radioEfecto = re;
         gravedad = g;
         admin = admin_app;
    }
    public void SetLocalizaciones(float la1, float lo1, float la2, float lo2){
        latitud1 = la1;
        longitud1 = lo1;
        latitud2 =la2;
        longitud2 = lo2;
    }

    private InformacionUsuario(){}

    static public InformacionUsuario getInstance(){
        if(usuario == null){
            usuario = new InformacionUsuario();
        }
        return usuario;
    }
    /*
    public void buclear(Activity a){
        getloc(a);
        coger_incidencias(a);
        refresh(1000, a);
    }

    private void refresh(int milliseconds, Activity a){
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                buclear(a);
            }
        };
        handler.postDelayed(runnable, milliseconds);
    }*/

    public void getLocalizacionesSecundarias(){

        Log.d("secun", "getlocsecun");
        RequestQueue queue = Volley.newRequestQueue(activity);
        String url = "https://climalert.herokuapp.com/usuarios/" +InformacionUsuario.getInstance().email+ "/filtro";
        JSONObject mapa = new JSONObject();
        try {
            mapa.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, mapa,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("secun", "getlocsecun onresponse");

                        float latitud1 = 0;
                        float longitud1 = 0;
                        float latitud2 = 0;
                        float longitud2 = 0;
                        try {
                            if(response != null)
                            {
                                JSONObject localizacion1 = response.getJSONObject("localizacion1");
                                if(localizacion1 != null) {
                                    latitud1 = Float.parseFloat(localizacion1.getString("latitud"));
                                    longitud1 = Float.parseFloat(localizacion1.getString("longitud"));
                                }
                                JSONObject localizacion2 = response.getJSONObject("localizacion2");
                                if(localizacion2 != null) {
                                    latitud2 = Float.parseFloat(localizacion2.getString("latitud"));
                                    longitud2 = Float.parseFloat(localizacion2.getString("longitud"));
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("secun", "getlocsecun onexception" + e);

                        }
                        InformacionUsuario.getInstance().SetLocalizaciones(latitud1, longitud1, latitud2, longitud2);

                      //  Log.d("a", String.valueOf(response));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.d("secun", "getlocsecun error" + error);
                    }

                }) {
        };
        queue.add(request);
    }

    public void coger_incidencias(){
        RequestQueue queue = Volley.newRequestQueue(activity);
        InformacionUsuario us = InformacionUsuario.getInstance();
        String url = "https://climalert.herokuapp.com/usuarios/"+ us.email +"/notificaciones";

        JSONObject mapa = new JSONObject();
        try {
            mapa.put("password", us.password);
            if(InformacionUsuario.getInstance().latitudactual != -1) {
                mapa.put("latitud", InformacionUsuario.getInstance().latitudactual);
                mapa.put("longitud", InformacionUsuario.getInstance().longitudactual);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Request a string response from the provided URL.
        myJsonArrayRequest request = new myJsonArrayRequest(Request.Method.POST, url, mapa,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject Notificacion;
                        try {
                            //aPintar.clear();
                            for (int i = 0; i < response.length(); ++i) {
                                Notificacion = response.getJSONObject(i);

                                JSONObject incidenciaFenomeno = Notificacion.getJSONObject("incidenciaFenomeno");
                                JSONArray IndicacionIncidencia = Notificacion.getJSONArray("indicacionIncidencia");
                                String fecha =  incidenciaFenomeno.getString("fecha");
                                Vector<String> indicaciones =  new Vector<String>();
                                for(int j= 0; j <  IndicacionIncidencia.length(); ++j) {
                                    indicaciones.add(IndicacionIncidencia.getString(j));
                                }
                                JSONObject incidencia = incidenciaFenomeno.getJSONObject("incidencia");
                                Integer id = Integer.parseInt(incidenciaFenomeno.getString("id"));
                                Integer radio = Integer.parseInt(incidencia.getString("radio"));
                                JSONObject localizacion = incidencia.getJSONObject("localizacion");
                                Float latitud = Float.parseFloat(localizacion.getString("latitud"));
                                Float longitud = Float.parseFloat(localizacion.getString("longitud"));
                                JSONObject femomenoMeteo = incidenciaFenomeno.getJSONObject("fenomenoMeteo");
                                String nombre = femomenoMeteo.getString("nombre");
                                String descripcion = femomenoMeteo.getString("descripcion");
                                Notificacion n = new Notificacion(fecha, radio, latitud, longitud, nombre, descripcion, id);
                                aPintar.add(n);
                            }
                            //Log.d("asd", "onResponse: ");
                            Vector<Notificacion>aux =  new Vector<Notificacion>();
                            for(int i = 0; i < actual.size(); ++i)
                            {
                                boolean existe =  false;
                                for(int j = 0; j < aPintar.size() && !existe; ++j)
                                {
                                    if(actual.get(i).identificador == aPintar.get(j).identificador) {
                                        existe = true;
                                    }
                                }
                                if(!existe) {
                                    aBorrar.add(actual.get(i).identificador);
                                    aux.add(actual.get(i));
                                }
                            }

                            for(int i = 0; i < aux.size(); ++i){

                                actual.removeElement(aux.get(i));
                            }

                            aux.clear();
                            for(int i = 0; i < aPintar.size(); ++i)
                            {
                                for(int j = 0; j < actual.size(); ++j)
                                {

                                    int idpintar = aPintar.get(i).identificador;
                                    int idactual = actual.get(j).identificador;
                                    if(idpintar == idactual) {
                                        aux.add(aPintar.get(i));
                                    }/*
                                    if(aPintar.get(i).identificador == actual.get(j).identificador) {
                                        aPintar.remove(i);
                                    }*/
                                }
                            }
                            for(int i = 0; i < aux.size(); ++i){
                            //    aPintar.removeElementAt(aux.get(i));
                                aPintar.removeElement(aux.get(i));
                               // Log.d("asdasdasdasdadsa", String.valueOf(aux.get(i)));
                              //  aPintar.remove()
                            }
                            actual.addAll(aPintar);
                            Log.d("asd", "onResponse: ");
                            /*
                            for(todas){
                                if(! esta en pintar)
                                {
                                    la meto en borrar
                                    la borro de todas
                                }
                            }
                            for(pintar){
                                if(esta en todas)
                                {
                                    la borro de pintar
                                }
                            }
                            a todas le meto pintar, concat

                            */

                            Log.d("bernat", "Actual " + String.valueOf(InformacionUsuario.getInstance().actual.size()));
                            Log.d("bernat", "Pintar " + String.valueOf(InformacionUsuario.getInstance().aPintar.size()));
                            Log.d("bernat", "Borrar " + String.valueOf(InformacionUsuario.getInstance().aBorrar.size()));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        // Add the request to the RequestQueue.
        queue.add(request);
    }

    public class myJsonArrayRequest extends JsonRequest<JSONArray> {
        public myJsonArrayRequest(int method, String url, JSONObject JsonRequest,
                                  Response.Listener<JSONArray> listener, Response.ErrorListener errorListener){
            super(method,url,(JsonRequest == null) ? null : JsonRequest.toString(), listener, errorListener);
        }

        @Override
        protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
            try {
                String JsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                return Response.success(new JSONArray(JsonString), HttpHeaderParser.parseCacheHeaders(response));
            } catch (JSONException e){
                return Response.error(new ParseError(e));
            } catch (UnsupportedEncodingException e){
                return Response.error(new ParseError(e));
            }
        }
    }
    public void setActivity(Activity a){
        activity = a;

    }
}
