package com.example.climalert.CosasDeTeo;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.ActivityCompat;

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
import com.example.climalert.MapsFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Vector;

public class InformacionUsuario {

    public String email;
    public String password;
    public float latitudactual;
    public float longitudactual;
    public float latitud1;
    public float longitud1;
    public float latitud2;
    public float longitud2;
    public int radioEfecto;
    public int gravedad;
    public Notificacion[] res;
    AlertDialog alert = null;

    static private InformacionUsuario usuario;

    public void SetInformacion(String e, String p, float la1, float lo1, float la2, float lo2, int re, int g){
         email = e;
         password = p;
         latitud1 = la1;
         longitud1 = lo1;
         latitud2 =la2;
         longitud2 = lo2;
         radioEfecto = re;
         gravedad = g;
    }
    public void SetLocalizaiones(float la1, float lo1, float la2, float lo2){
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
    public void buclear(Activity a){
        Log.d("ALGO1234", "buclear: ");
        getloc(a);
        coger_incidencias(a);
        refresh(500, a);
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
    }

    public void getLocalizacionesSecundarias(Activity a){

        RequestQueue queue = Volley.newRequestQueue(a);
        String url = "https://climalert.herokuapp.com/usuario/" +InformacionUsuario.getInstance().email+ "/filtro";
        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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
                        }
                        InformacionUsuario.getInstance().SetLocalizaiones(latitud1, longitud1, latitud2, longitud2);

                      //  Log.d("a", String.valueOf(response));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }

                }) {
        };
        queue.add(request);
    }

    public void coger_incidencias(Activity a){
        Log.d("ALGO", "coger incidencias entro");
        RequestQueue queue = Volley.newRequestQueue(a);
        InformacionUsuario us = InformacionUsuario.getInstance();
        String url = "https://climalert.herokuapp.com/usuario/"+ us.email +"/notificaciones";

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
        Log.d("ALGO", mapa.toString());

        // Request a string response from the provided URL.
        myJsonArrayRequest request = new myJsonArrayRequest(Request.Method.POST, url, mapa,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject Notificacion;
                        res = new Notificacion[response.length()];
                        try {
                            for (int i = 0; i < response.length(); ++i) {
                                Notificacion = response.getJSONObject(i);

                                JSONObject incidenciaFenomeno = Notificacion.getJSONObject("incidenciaFenomeno");
                                JSONArray IndicacionIncidencia = Notificacion.getJSONArray("indicacionIncidencia");
                                Log.d("ALGO3", "INCIDENCIAFENOMENO " + incidenciaFenomeno);
                                String fecha =  incidenciaFenomeno.getString("fecha");
                                Vector<String> indicaciones =  new Vector<String>();
                                for(int j= 0; j <  IndicacionIncidencia.length(); ++j) {
                                    indicaciones.add(IndicacionIncidencia.getString(j));
                                }
                                JSONObject incidencia = incidenciaFenomeno.getJSONObject("incidencia");
                                Integer radio = Integer.parseInt(incidencia.getString("radio"));
                                JSONObject localizacion = incidencia.getJSONObject("localizacion");
                                Float latitud = Float.parseFloat(localizacion.getString("latitud"));
                                Float longitud = Float.parseFloat(localizacion.getString("longitud"));
                                JSONObject femomenoMeteo = incidenciaFenomeno.getJSONObject("fenomenoMeteo");
                                String nombre = femomenoMeteo.getString("nombre");
                                String descripcion = femomenoMeteo.getString("descripcion");
                                res[i] = new Notificacion(fecha, radio, latitud, longitud, nombre, descripcion);

                            }
                           // print_incidencias();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ALGO", "ERROR VOLLEY " + error);
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

    private void getloc(Activity a) {
        // Get the location manager
        //he puesto el getactivity por la cara la verdad
        LocationManager locationManager = (LocationManager) a.getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !MapsFragment.alertaSinGPSMostrada) {
             Alert(a);
            MapsFragment.alertaSinGPSMostrada = true;
        }
        else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ){
            MapsFragment.alertaSinGPSMostrada = false;
        }
        Criteria criteria = new Criteria();
        Log.d("per","entro en permisoss1");
        String bestProvider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(a, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(a, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(a, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 99);
            Log.d("per","entro en permisoss");
            return;
        }
        Location location = locationManager.getLastKnownLocation(bestProvider);
        LocationListener loc_listener = new LocationListener() {
            public void onLocationChanged(Location l) {
            }
            public void onProviderEnabled(String p) {
            }
            public void onProviderDisabled(String p) {
            }
            public void onStatusChanged(String p, int status, Bundle extras) {
            }
        };
        Log.d("per","casi entro en permisos");
        if (ActivityCompat.checkSelfPermission(a, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(a, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager
                .requestLocationUpdates(bestProvider, 0, 20.0f, loc_listener);
        location = locationManager.getLastKnownLocation(bestProvider);
        try {
            InformacionUsuario.getInstance().latitudactual = (float) location.getLatitude();
            // Log.d("ALGO1234", "soy tonto" + location.getLatitude());

            InformacionUsuario.getInstance().longitudactual = (float) location.getLongitude();
        } catch (NullPointerException e) {
            InformacionUsuario.getInstance().longitudactual = -1.0f;
            InformacionUsuario.getInstance().latitudactual = -1.0f;
            Log.d("ALGO1234", "NO PILLO LOC");
        }
    }
    private void Alert(Activity a) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(a);
        builder.setMessage("El sistema GPS esta desactivado, ¿Desea activarlo?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        a.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        alert = builder.create();
        alert.show();
    }

}
