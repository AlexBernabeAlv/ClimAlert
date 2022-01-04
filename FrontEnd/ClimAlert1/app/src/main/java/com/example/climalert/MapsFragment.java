package com.example.climalert;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.climalert.CosasDeTeo.InformacionUsuario;
import com.example.climalert.CosasDeTeo.Notificacion;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.Vector;

public class MapsFragment extends Fragment {
    private  GoogleMap mMap;
    AlertDialog alert = null;
    int contador = 0;
    static int num_bucleares = 0;
    LatLng userLatLong;
    Timer timer;
    Marker UBI1;
    Marker UBI2;
    HashMap<Integer, Marker> IncidenciasActuales =  new HashMap<Integer, Marker>();
    HashMap<Integer, Circle> CirculosIncidencias =  new HashMap<Integer, Circle>();;
    Vector<LatLng> items = new Vector<LatLng>();
    LocationManager locationManager;
    LocationListener locationListener;
    private static final String TAG = "MapsFragment";
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Boolean mLocationPermissionsGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    static public boolean alertaSinGPSMostrada = false;
    NotificationCompat.Builder mBuilder;
    JSONObject mapa;
     boolean borrados = true;
     boolean pintados = true;
    boolean localizacionespuestas = false;
    public Marker markerActual;
    Button Buscador;
    boolean buscador_pulsado = false;
    EditText textoObjeto;
    Button Buscar;


    /*
@Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    private void buclear(){


        if(borrados && pintados) {
            borrados = false;
            pintados = false;
            InformacionUsuario.getInstance().coger_incidencias();
            limpiar_incidencias();
            print_incidencias(InformacionUsuario.getInstance().aPintar);
        }
        Log.d("12345678", String.valueOf(markerActual));

        if(!localizacionespuestas) {

            LatLng ll1 = new LatLng(InformacionUsuario.getInstance().latitud1, InformacionUsuario.getInstance().longitud1);
            LatLng ll2 = new LatLng(InformacionUsuario.getInstance().latitud2, InformacionUsuario.getInstance().longitud2);

            if (ll1.latitude != 0) {
                if(UBI1 != null) UBI1.remove();

                UBI1 = mMap.addMarker(new MarkerOptions()
                        .anchor(0.0f, 1.0f)
                        .alpha(0.7f)
                        .title("  1")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                        .position(ll1));
                localizacionespuestas = true;
            }
            if (ll2.latitude != 0) {
                if(UBI2 != null) UBI2.remove();
                UBI2 = mMap.addMarker(new MarkerOptions()
                        .anchor(0.0f, 1.0f)
                        .alpha(0.7f)
                        .title("  2")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                        .position(ll2));
                localizacionespuestas = true;
            }

        }


        //tratar notificaciones
        if(InformacionUsuario.getInstance().actual.size() > InformacionUsuario.getInstance().actualtam) {
            createNotificationChannel();
            createNotification();
            InformacionUsuario.getInstance().actualtam = InformacionUsuario.getInstance().actual.size();

        }
        else if(InformacionUsuario.getInstance().actualtam > InformacionUsuario.getInstance().actual.size()){
            InformacionUsuario.getInstance().actualtam = InformacionUsuario.getInstance().actual.size();
        }
        refresh(600);
    }

    private void refresh(int milliseconds){
        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                buclear();
            }
        };
        handler.postDelayed(runnable, milliseconds);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        Buscador = (Button) view.findViewById(R.id.botonbuscador);
        Buscador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buscador_pulsado){
                    buscador_pulsado = false;
                    textoObjeto.setText("");
                    textoObjeto.setVisibility(View.INVISIBLE);
                    Buscar.setVisibility(View.INVISIBLE);
                }
                else {
                    buscador_pulsado = true;
                    textoObjeto.setVisibility(View.VISIBLE);
                    Buscar.setVisibility(View.VISIBLE);
                }
            }
        });
        textoObjeto = (EditText) view.findViewById(R.id.textoObj);
        Buscar = (Button) view.findViewById(R.id.buscarObj);
        Buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formeforu();
                Log.d("ENTRA", "HACE FORMEFORU");
                if (items != null){
                    for (int i = 0; i < items.size(); ++i) {
                        Log.d("ENTRA", items.get(i).toString());
                        LatLng ll = items.get(i);
                        Marker m = mMap.addMarker(new MarkerOptions()
                                .snippet(textoObjeto.getText().toString())
                                .position(ll)
                                .alpha(0.9f)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                .title("Objeto"));
                    }
                    items.removeAllElements();
                }

            }
        });


        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mMap = googleMap;
                borrados = true;
                pintados = true;
                //mMap.clear();
                markerActual = null;
                localizacionespuestas = false;
                getloc();
                formeforu();
                print_incidencias(InformacionUsuario.getInstance().actual);

                if(num_bucleares != 1){
                    num_bucleares++;
                    buclear();
                }

                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

                NotificationManager notif=(NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notify=new Notification.Builder
                        (getContext()).setContentTitle("titulo").setContentText("llueve").
                        setSmallIcon(R.drawable.logo_climalert).build();

                notify.flags |= Notification.FLAG_AUTO_CANCEL;
                notif.notify(0, notify);
                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(@NonNull LatLng latLng) {
                        Alert(1, latLng);
                    }
                });
            }
        });


        return view;
    }

    /////////////////////////////CLASES////////////////CLASES////////////////////////////
    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {
        // These are both viewgroups containing an ImageView with id "badge" and two TextViews with id
        // "title" and "snippet".
        private final View mWindow;

        private final View mContents;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
            mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            /*if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_window) {
                Log.d("CustomInfoWindowAdapter", "entra al if");
                // This means that getInfoContents will be called.
                return null;
            }*/
            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
       /* if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_contents) {
            // This means that the default info contents will be used.
            return null;
        }*/
            render(marker, mContents);
            return mContents;
        }

        private void render(Marker marker, View view) {
            int badge;
            // Use the equals() method on a Marker to check for equals.  Do not use ==.
            /*if (marker.equals(UBI1)) {
                badge = R.drawable.fire;
            }
            else if (marker.equals(UBI2)) {
                badge = R.drawable.fire;
            }
            else {
                // Passing 0 to setImageResource will clear the image view.
                badge = 0;
            }*/
            badge = 0;
            ((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);

            String title = marker.getTitle();

            TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                // Spannable string allows us to edit the formatting of the text.
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
                titleUi.setText(titleText);
            } else {
                titleUi.setText("");
            }
            if(!marker.getTitle().equals("  1")  && !marker.getTitle().equals("  2") &&
                    !marker.getTitle().equals("ACTUAL") && !marker.getTitle().equals("refugio")) {
                Log.d("123456", "ENTRO AQUI CUANDO NO DEBERIA");
                Log.d("123456", "marker: " + marker.getTitle() );
                Log.d("123456", "boolean es: " + !marker.getTitle().equals("   1") );
                String snippet = marker.getSnippet();
                int pos = snippet.lastIndexOf(" ");
                snippet = snippet.substring(0, pos);
                TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
                if (snippet != null) {
                    SpannableString snippetText = new SpannableString(snippet);
                    snippetText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, snippet.length(), 0);
                    snippetUi.setText(snippetText);
                } else {
                    snippetUi.setText("");
                }
            }
            else{
                TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
                SpannableString snippetText = new SpannableString("Ubicación del usuario");
                snippetText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 21, 0);
                snippetUi.setText(snippetText);
            }
            mMap.setOnInfoWindowClickListener(this);
        }

        @Override
        public void onInfoWindowClick(Marker marker) {
            if (!marker.getTitle().equals("  1")  && !marker.getTitle().equals("  2") &&
                    !marker.getTitle().equals("ACTUAL") && !marker.getTitle().equals("refugio") && !marker.getTitle().equals("Objeto")) {
                String snippet = marker.getSnippet();
                String lastWord = snippet.substring(snippet.lastIndexOf(" ") + 1);
                InformacionUsuario.getInstance().IDIncidenciaActual = lastWord;
                Fragment f = new VentanaIncidencia();
                MainActivity main = (MainActivity) getActivity();
                main.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contenedor, f)
                        .commit();
            }
            if(marker.getTitle().equals("ACTUAL")){
                pintarRefugios(getActivity());

            }
        }
    }


    ///////////////////FUNCIONES//////////////FUNCIONES////////////FUNCIONES/////////////////////

    public void getloc() {
        // Get the location manager
        //he puesto el getactivity por la cara la verdad
        LocationManager locationManager = (LocationManager) InformacionUsuario.getInstance().activity.getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(InformacionUsuario.getInstance().activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(InformacionUsuario.getInstance().activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(InformacionUsuario.getInstance().activity, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 99);
            return;
        }
      //  Location location = locationManager.getLastKnownLocation(bestProvider);
        LocationListener loc_listener = new LocationListener() {
            public void onLocationChanged(Location l) {
                Log.d("1234567", "onloc");
                LatLng llact = new LatLng(l.getLatitude(), l.getLongitude());
                InformacionUsuario.getInstance().latitudactual = (float) l.getLatitude();
                InformacionUsuario.getInstance().longitudactual = (float) l.getLongitude();
                if(InformacionUsuario.getInstance().latitudactual != 0 && markerActual == null){
                    LatLng actual = new LatLng(InformacionUsuario.getInstance().latitudactual, InformacionUsuario.getInstance().longitudactual);
                    markerActual = mMap.addMarker(new MarkerOptions().position(actual).title("ACTUAL"));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(actual));
                }
                Log.d("berni", "onChanged " +  InformacionUsuario.getInstance().latitudactual);
                if(markerActual != null) markerActual.setPosition(llact);
            }
            public void onProviderEnabled(String p) {
                Log.d("1234567", "onprov");
                if (markerActual != null) {
                    markerActual.setVisible(true);
                    markerActual.setAlpha(1);

                }
                if(MapsFragment.alertaSinGPSMostrada){
                    MapsFragment.alertaSinGPSMostrada = false;
                }
            }
            public void onProviderDisabled(String p) {
                Log.d("1234567", "onprovdis");
                if(markerActual != null) {
                    markerActual.setVisible(false);
                    markerActual.setAlpha(0);
                    Log.d("berni", "onDisabled");
                }
                if (!MapsFragment.alertaSinGPSMostrada) {
                    Alert(0, null);
                    MapsFragment.alertaSinGPSMostrada = true;
                }

            }
            public void onStatusChanged(String p, int status, Bundle extras) {
            }
        };
        if (ActivityCompat.checkSelfPermission(InformacionUsuario.getInstance().activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(InformacionUsuario.getInstance().activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager
                .requestLocationUpdates(bestProvider, 0, 20.0f, loc_listener);
        //location = locationManager.getLastKnownLocation(bestProvider);
    }


    private void createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(InformacionUsuario.getInstance().activity, "hola");
        Intent intent = new Intent(InformacionUsuario.getInstance().activity, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(InformacionUsuario.getInstance().activity, 0, intent, 0);
       // builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.logo_climalert);
        builder.setContentTitle("Aviso de incendio cerca");
        builder.setContentText("Consejos de Incendio");
        builder.setColor(Color.RED);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.MAGENTA, 1000, 1000);
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(InformacionUsuario.getInstance().activity);
        notificationManagerCompat.notify(22, builder.build());
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notificacion";
            NotificationChannel notificationChannel = new NotificationChannel("hola", name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) InformacionUsuario.getInstance().activity.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public void pintarRefugios(Activity a){
        RequestQueue queue = Volley.newRequestQueue(a);
        Log.d("refug", String.valueOf(InformacionUsuario.getInstance().latitudactual));

        String url = "https://climalert.herokuapp.com/refugios?latitud="+InformacionUsuario.getInstance().latitudactual+"&longitud="+InformacionUsuario.getInstance().longitudactual;
        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String nombre;
                        float latitud;
                        float longitud;
                        try {
                            if(response != null)
                            {
                                nombre = response.getString("nombre");
                                latitud = Float.parseFloat(response.getString("latitud"));
                                longitud = Float.parseFloat(response.getString("longitud"));
                                LatLng lr = new LatLng(latitud, longitud);
                                mMap.addMarker(new MarkerOptions()
                                        .anchor(0.0f, 1.0f)
                                        .alpha(0.7f)
                                        .title("refugio")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                                        .position(lr));
                                trazarRutaEntreOrigenDestino(InformacionUsuario.getInstance().latitudactual,InformacionUsuario.getInstance().longitudactual, latitud, longitud);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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


    private void trazarRuta(JSONObject response){
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;

        try {

            jRoutes = response.getJSONArray("routes");
            for (int i = 0; i < jRoutes.length(); i++) {
                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");

                for (int j = 0; j < jLegs.length(); j++) {
                    jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                    for (int k = 0; k < jSteps.length(); k++) {
                        String polyline = "";
                        polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);
                        mMap.addPolyline(new PolylineOptions().addAll(list).color(Color.BLUE).width(5));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

    private void trazarRutaEntreOrigenDestino(float latitud1, float longitud1, float latitud2, float longitud2){
        String l1 = String.valueOf(latitud1);
        String l2 = String.valueOf(longitud1);
        String l3 = String.valueOf(latitud2);
        String l4 = String.valueOf(longitud2);
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+l1+","+l2+"&destination="+l3+","+l4+"&key=AIzaSyCGOeM2aM5SkecHOc4s_Tkf_B_KV77kWEo";
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsor = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        trazarRuta(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }

                });
        queue.add(jsor);
    }

    public void dar_localizacion() {
        Log.d("secun", "dar loc entrar ");
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://climalert.herokuapp.com/usuarios/"+InformacionUsuario.getInstance().email+"/localizaciones";
       // JSONObject mapa = new JSONObject();
        mapa = new JSONObject();
        try {
            Log.d("secun", InformacionUsuario.getInstance().password);
            mapa.put("password", InformacionUsuario.getInstance().password);
            if (InformacionUsuario.getInstance().latitud1 != 0) {
                mapa.put("latitud1", InformacionUsuario.getInstance().latitud1);
                mapa.put("longitud1", InformacionUsuario.getInstance().longitud1);
            }
            if (InformacionUsuario.getInstance().latitud2 != 0) {
                mapa.put("latitud2", InformacionUsuario.getInstance().latitud2);
                mapa.put("longitud2", InformacionUsuario.getInstance().longitud2);
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
                        //Log.d("a", String.valueOf(response));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.d("secun", "dar loc fallar " + error);
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
            builder.setMessage("Editar ubicaciones")
                    .setCancelable(false)
                    .setPositiveButton("Ubicacion1", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            if(UBI1 != null) UBI1.remove();
                            InformacionUsuario.getInstance().latitud1 = (float) latLng.latitude;
                            InformacionUsuario.getInstance().longitud1 = (float) latLng.longitude;
                            Log.d("secun", "ubi 1 asignar ");
                            dar_localizacion();

                            UBI1 = mMap.addMarker(new MarkerOptions()
                                    .anchor(0.0f, 1.0f)
                                    .alpha(0.7f)
                                    .title("  1")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                                    .position(latLng));
                        }
                    })
                    .setNeutralButton("Borrar Ubicaciones", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            if(UBI1 != null) UBI1.remove();
                            if(UBI2 != null) UBI2.remove();
                            InformacionUsuario.getInstance().latitud1 = 0;
                            InformacionUsuario.getInstance().longitud1 = 0;
                            InformacionUsuario.getInstance().latitud2 = 0;
                            InformacionUsuario.getInstance().longitud2 = 0;

                            dar_localizacion();
                        }
                    })
                    .setNegativeButton("Ubicacion2", new DialogInterface.OnClickListener() {
                public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                    if(UBI2 != null) UBI2.remove();
                    InformacionUsuario.getInstance().latitud2 = (float) latLng.latitude;
                    InformacionUsuario.getInstance().longitud2 = (float) latLng.longitude;
                    dar_localizacion();

                    UBI2 = mMap.addMarker(new MarkerOptions()
                            .anchor(0.0f, 1.0f)
                            .alpha(0.7f)
                            .title("  2")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                            .position(latLng));
                    }
                 });

            alert = builder.create();
            alert.show();

        }
    }

    public void formeforu() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://climalert.herokuapp.com/4me4u/products";
        JSONObject mapa = new JSONObject();
        try {
            //DAVID, AQUI CAMBIAS EL HARDCORED POR ELDEL ITEMTEXT
            mapa.put("productName", textoObjeto.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Request a string response from the provided URL.
        InformacionUsuario.myJsonArrayRequest request = new InformacionUsuario.myJsonArrayRequest(Request.Method.POST, url, mapa,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject item;
                        try {
                            for (int i = 0; i < response.length(); ++i) {
                                item = response.getJSONObject(i);
                                JSONObject userId = item.getJSONObject("userId");
                                Float latitud = Float.parseFloat(userId.getString("latitude"));
                                Float longitud = Float.parseFloat(userId.getString("longitude"));
                                LatLng ll = new LatLng(latitud, longitud);
                                items.add(ll);
                            }
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                        }
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

    private void drawCircle(LatLng point, int rad, int id) {
        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();
        // Specifying the center of the circle
        circleOptions.center(point);
        // Radius of the circle
        circleOptions.radius(rad*10);
        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);
        // Fill color of the circle
        circleOptions.fillColor(Color.argb(100, 235, 165, 171));
        // Border width of the circle
        circleOptions.strokeWidth(2);
        // Adding the circle to the GoogleMap
        Circle C = mMap.addCircle(circleOptions);
        CirculosIncidencias.put(id, C);
    }

    private void limpiar_incidencias(){
       // Vector<Notificacion> copia = (Vector<Notificacion>) InformacionUsuario.getInstance().aBorrar.clone();
       // InformacionUsuario.getInstance().aBorrar.clear();
        Vector < Integer > aux = new Vector<Integer>();
        for(int i = 0; i < InformacionUsuario.getInstance().aBorrar.size(); ++i){
            int id = InformacionUsuario.getInstance().aBorrar.get(i);
            if(CirculosIncidencias.containsKey(id)) {
                CirculosIncidencias.get(id).remove();
                CirculosIncidencias.remove(id);
            }
            Iterator entries = IncidenciasActuales.entrySet().iterator();
            boolean exit = true;
            while (entries.hasNext() && exit) {
                Map.Entry entry = (Map.Entry) entries.next();
                Integer keyId = (Integer) entry.getKey();
                Marker marcador = (Marker) entry.getValue();
                if(keyId == id) {
                    marcador.remove();
                    IncidenciasActuales.remove(keyId);
                    exit = false;

                    aux.add(InformacionUsuario.getInstance().aBorrar.get(i));
                }
            }
        }

        for(int i = 0; i < aux.size(); ++i){

            InformacionUsuario.getInstance().aBorrar.removeElement(aux.get(i));
        }

        Log.d("bernat", String.format("Circulos %d %d", CirculosIncidencias.size(), contador));
        Log.d("bernat", String.format("Marcadores %d %d", IncidenciasActuales.size(), contador));

        contador++;

        borrados = true;
    }
    /*
    private void limpiar_incidencias(Vector<Marker> aux){
        for(int i = 0; i < aux.size(); ++i){
            if(!marcadoresIncidencias.contains(marcadoresIncidencias.get(i)))
            {
                marcadoresIncidencias.get(i).remove();
                CirculosMarcadoresIncidencias.get(i).remove();
            }
        }

        marcadoresIncidencias.removeAllElements();
        CirculosMarcadoresIncidencias.removeAllElements();
    }*/

    public void print_incidencias(Vector<Notificacion> print){
        if(print != null) {
            for (int i = 0; i < print.size(); ++i) {
                LatLng ll = new LatLng((print.get(i).latitud), (print.get(i).longitud));
                generarMarcadores(ll, (print.get(i).descripcion), print.get(i).nombre, (print.get(i).radio),(print.get(i).identificador));
            }
            if(print ==  InformacionUsuario.getInstance().aPintar){

                InformacionUsuario.getInstance().aPintar.clear();
            }

            pintados = true;
        }
    }
    public void generarMarcadores(LatLng latLng, String info, String tip, int radio, int id) {
            String ID = String.valueOf(id);
            Marker m = mMap.addMarker(new MarkerOptions()
                    .snippet(info + " " + ID)
                    .position(latLng)
                    .alpha(0.9f)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    .title(tip));
            drawCircle(latLng, radio * 2000, id);
            IncidenciasActuales.put(id, m);

    }
}