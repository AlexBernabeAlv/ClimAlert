package com.example.climalert;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.climalert.CosasDeTeo.InformacionUsuario;
import com.example.climalert.CosasDeTeo.Notificacion;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Vector;

public class MapsFragment extends Fragment {
    private GoogleMap mMap;
    AlertDialog alert = null;
    double lat;
    double lon;
    LatLng userLatLong;
    LatLng ll1;
    LatLng ll2;
    Marker UBI1;
    Marker UBI2;
    Notificacion[] res;
    LocationManager locationManager;
    LocationListener locationListener;
    private static final String TAG = "MapsFragment";
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Boolean mLocationPermissionsGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;


    public class myJsonArrayRequest extends JsonRequest<JSONArray>{
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


    /* //esta activada? tienes permisos? preguntar permisos?

        if(tienes permisos)
            if(localizacion activada)
                perfe, se la paso a server
            else
                pido que active la localizacion
        else no tengo permisos
            pregunto por permisos
            if(localizacion activada)
                perfe, se la paso a server
            else
                pido que active la localizacion
        -Mostrar ubicacion en mapa y pasar a servidor
        -Pillar ubi de servidor y mostrar en mapa
    * */



    /*

@Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d( "ALGO", "voy a coger incidencias en oncreate");
        coger_incidencias();
        Log.d( "ALGO", "he cogido incidencias ne oncreate");

    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
*/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("ACVE", "oncreateview: ha entrado");
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                Log.d( "ALGO", "onMapReady: ha entrado");
                mMap = googleMap;
                LatLng sydney = new LatLng(-34, 151);
                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                //Log.d( "ALGO", "voy a coger incidencias");
                coger_incidencias();
                //Log.d( "ALGO","he cogido incidencias");
                //  Log.d( "ALGO","res1 :" + res[0][0]);
                // Log.d( "ALGO","res2 : " + res[0].length);
                // Log.d( "ALGO","res : " + res);
                //   Log.d( "ALGO","res4 : " + res.length);



                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(@NonNull LatLng latLng) {
                        if (ll1 == null) {
                            Alert(1, latLng);
                        } else if (ll2 == null) {
                            Alert(2, latLng);
                        } else {
                            Alert(3, latLng);
                        }
                    }
                });
            }
        });
        return view;
    }



    //////////FUNCIONES

    public void print_incidencias(){
        Log.d( "ALGO", "res: " +  res);
        if(res != null) {
            Log.d( "ALGO", "res > 0");
            for (int i = 0; i < res.length; ++i) {
                Log.d("ALGO", "accedo a res en el bucle");
                LatLng ll = new LatLng((res[i].latitud), (res[i].longitud));
                generarMarcadores(ll, (res[i].descripcion), res[i].nombre, (res[i].radio));
            }
        }
    }

    public void coger_incidencias(){
        Log.d("ALGO", "coger incidencias entro");
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        InformacionUsuario us = InformacionUsuario.getInstance();
        String url = "https://climalert.herokuapp.com/usuario/"+ us.email +"/notificaciones";

        JSONObject mapa = new JSONObject();
        try {
            mapa.put("password", us.password);
            mapa.put("latitud", "23");
            mapa.put("longitud", "32");
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
                            print_incidencias();
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

    public void dar_localizacion() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://climalert.herokuapp.com/usuario/"+InformacionUsuario.getInstance().email+"/localizaciones/new";
        JSONObject mapa = new JSONObject();
        try {
            mapa.put("password", InformacionUsuario.getInstance().password);
            if (ll1 != null) {
                mapa.put("latitud1", ll1.latitude);
                mapa.put("longitud1", ll1.longitude);
            }
            if (ll2 != null) {
                mapa.put("latitud2", ll2.latitude);
                mapa.put("longitud2", ll2.longitude);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, mapa,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //JSONObject usuario;
                        Log.d("a", String.valueOf(response));
                        //Log.d("ALGO", "he acabado el bucle");
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

    private void Alert(int i, LatLng latLng) {
        if(i == 0) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("El sistema GPS esta desactivado, ¿Desea activarlo?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
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
        if(i == 1) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("¿Deseas añadir este punto como ubicación secundaria 1?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            ll1 = latLng;
                            UBI1 = mMap.addMarker(new MarkerOptions()
                                    .anchor(0.0f, 1.0f)
                                    .alpha(0.7f)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                                    .position(ll1));
                            dar_localizacion();
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
        if(i == 2) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); //quizas es getcontext
            builder.setMessage("¿Deseas añadir este punto como ubicación secundaria 2?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            ll2 = latLng;
                            UBI2 = mMap.addMarker(new MarkerOptions()
                                    .anchor(0.0f, 1.0f)
                                    .alpha(0.7f)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                                    .position(ll2));
                            dar_localizacion();
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
        if(i == 3) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Ya has añadido dos ubicaciones secundarias, ¿deseas eliminar alguna?")
                    .setCancelable(false)
                    .setPositiveButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    })
                    .setNeutralButton("La primera  ", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            UBI1.remove();
                            ll1 = latLng;
                            UBI1 = mMap.addMarker(new MarkerOptions()
                                    .anchor(0.0f, 1.0f)
                                    .alpha(0.7f)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                                    .position(ll1));
                            dar_localizacion();
                        }
                    })
                    .setNegativeButton("La segunda", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            UBI2.remove();
                            ll2 = latLng;
                            UBI2 = mMap.addMarker(new MarkerOptions()
                                    .anchor(0.0f, 1.0f)
                                    .alpha(0.7f)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                                    .position(ll2));
                            dar_localizacion();
                        }
                    });
            alert = builder.create();
            alert.show();
        }

    }

    //el this por el getactivity
    private BitmapDescriptor bitmapDescriptorFromVector(MapsFragment context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(getActivity(), R.drawable.ic_launcher_background);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(getActivity(), vectorDrawableResourceId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth() + 0, vectorDrawable.getIntrinsicHeight() + 0);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void drawCircle(LatLng point, int rad) {
        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();
        // Specifying the center of the circle
        circleOptions.center(point);
        // Radius of the circle
        circleOptions.radius(rad*10);
        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);
        // Fill color of the circle
        circleOptions.fillColor(Color.argb(150, 235, 165, 171));
        // Border width of the circle
        circleOptions.strokeWidth(2);
        // Adding the circle to the GoogleMap
        mMap.addCircle(circleOptions);
    }


    public void generarMarcadores(LatLng latLng, String info, String tip, int radio) {
        Log.d("ALGO","3");
        Log.d("ALGO5", mMap.toString());
        mMap.addMarker(new MarkerOptions()
                .snippet(info)
                .position(latLng)
                .alpha(0.7f)
                .title(tip));
        drawCircle(latLng, radio * 2000);
    }
}

//CEMENTERIO
/*

                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        userLatLong = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(userLatLong).title("Your location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLong));
                    }
                };
                askLocatonPermission();

    private void askLocationPermission(){}
 */