package com.example.climalert;

import static android.content.Context.LOCATION_SERVICE;

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
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import com.google.android.gms.maps.model.Circle;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class MapsFragment extends Fragment {
    private GoogleMap mMap;
    AlertDialog alert = null;
    LatLng userLatLong;
    Timer timer;
    Marker UBI1;
    Marker UBI2;
    Vector<Marker> marcadoresIncidencias =  new Vector<Marker>();
    Vector<Circle> CirculosMarcadoresIncidencias =  new Vector<Circle>();
    LocationManager locationManager;
    LocationListener locationListener;
    private static final String TAG = "MapsFragment";
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Boolean mLocationPermissionsGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    static public boolean alertaSinGPSMostrada = false;
    private RadioGroup mOptions;
    private Marker mIncendio;
    private Marker mUBI1;

    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        // These are both viewgroups containing an ImageView with id "badge" and two TextViews with id
        // "title" and "snippet".
        private final View mWindow;

        private final View mContents;

        CustomInfoWindowAdapter() {
            Log.d("CustomInfoWindowAdapter", "entra a CustomInfoWindowAdapter");
            Log.d("CustomInfoWindowAdapter", "entra a mWindow");
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
            Log.d("CustomInfoWindowAdapter", "sale de mWindow");
            Log.d("CustomInfoWindowAdapter", "entra a mContents");
            mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
            Log.d("CustomInfoWindowAdapter", "sale de mContents");
        }

        @Override
        public View getInfoWindow(Marker marker) {
            Log.d("CustomInfoWindowAdapter", "entra a getInfoWindow");
            /*if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_window) {
                Log.d("CustomInfoWindowAdapter", "entra al if");
                // This means that getInfoContents will be called.
                return null;
            }*/
            Log.d("CustomInfoWindowAdapter", "sale del if");
            render(marker, mWindow);
            Log.d("CustomInfoWindowAdapter", "sale de getInfoWindow");
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            Log.d("CustomInfoWindowAdapter", "entra a getInfoContents");
            if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_contents) {
                // This means that the default info contents will be used.
                return null;
            }
            render(marker, mContents);
            Log.d("CustomInfoWindowAdapter", "sale de getInfoContents");
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

            String snippet = marker.getSnippet();
            TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
            if (snippet != null ) {
                SpannableString snippetText = new SpannableString(snippet);
                snippetText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, snippet.length(), 0);
                snippetUi.setText(snippetText);
            } else {
                snippetUi.setText("");
            }
        }
    }
    //CLASE PORQUE JSONARRAY REQUEST ORIGINAL TE PIDE DE ENTRADA UNA ARRAY Y QUEREMOS QUE SEA OBJECT





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
        Log.d("ALGO1234", "buclear: ");
        limpiar_incidencias();
        if(InformacionUsuario.getInstance().latitudactual != -1){
            Log.d("ALGO1234", "buclear: tengo loc" + InformacionUsuario.getInstance().latitudactual);
            LatLng actual = new LatLng(InformacionUsuario.getInstance().latitudactual, InformacionUsuario.getInstance().longitudactual);
            mMap.addMarker(new MarkerOptions().position(actual).title("USTED ESTA AQUÍ"));
        }
        print_incidencias(InformacionUsuario.getInstance().res);
        refresh(3000);
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
        Log.d("ACVE", "oncreateview: ha entrado");
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                Log.d( "ALGO", "onMapReady: ha entrado");
                mMap = googleMap;
                //Log.d( "ALGO", "voy a coger incidencias");
              //  coger_incidencias();
               // getloc();
                //Log.d( "ALGO","he cogido incidencias");
                //  Log.d( "ALGO","res1 :" + res[0][0]);
                // Log.d( "ALGO","res2 : " + res[0].length);
                // Log.d( "ALGO","res : " + res);
                //   Log.d( "ALGO","res4 : " + res.length);
                if(InformacionUsuario.getInstance().latitudactual != -1){
                    LatLng actual = new LatLng(InformacionUsuario.getInstance().latitudactual, InformacionUsuario.getInstance().longitudactual);
                    mMap.addMarker(new MarkerOptions().position(actual).title("USTED ESTA AQUÍ"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(actual));
                }
                LatLng ll1 = new LatLng(InformacionUsuario.getInstance().latitud1, InformacionUsuario.getInstance().longitud1);
                LatLng ll2 = new LatLng(InformacionUsuario.getInstance().latitud2, InformacionUsuario.getInstance().longitud2);
                if(ll1.latitude != 0){
                    UBI1 = mMap.addMarker(new MarkerOptions()
                            .anchor(0.0f, 1.0f)
                            .alpha(0.7f)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                            .position(ll1)
                            .title("UBI1")
                            .snippet("Esta es la ubicación 1"));
                }
                if(ll2.latitude != 0) {
                    UBI2 = mMap.addMarker(new MarkerOptions()
                            .anchor(0.0f, 1.0f)
                            .alpha(0.7f)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                            .position(ll2)
                            .title("UBI2")
                            .snippet("Esta es la ubicación 2"));
                }
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
                buclear();


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



    ///////////////////FUNCIONES//////////////FUNCIONES////////////FUNCIONES/////////////////////


    public void print_incidencias(Notificacion[] res){
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

    public void dar_localizacion() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://climalert.herokuapp.com/usuario/"+InformacionUsuario.getInstance().email+"/localizaciones/new";
        JSONObject mapa = new JSONObject();
        String a="";
        String b="";
        try {
            mapa.put("password", InformacionUsuario.getInstance().password);
            if (InformacionUsuario.getInstance().latitud1 != 0) {
                a = "soy feo";
                mapa.put("latitud1", InformacionUsuario.getInstance().latitud1);
                mapa.put("longitud1", InformacionUsuario.getInstance().longitud1);
            }
            if (InformacionUsuario.getInstance().latitud2 != 0) {
                b = "soy MUY feo";
                mapa.put("latitud2", InformacionUsuario.getInstance().latitud2);
                mapa.put("longitud2", InformacionUsuario.getInstance().longitud2);
            }
            Log.d("XDDDD", a+b);
            a="";
            b="";
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
            builder.setMessage("Editar ubicaciones")
                    .setCancelable(false)
                    .setPositiveButton("Ubicacion1", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            if(UBI1 != null) UBI1.remove();
                            InformacionUsuario.getInstance().latitud1 = (float) latLng.latitude;
                            InformacionUsuario.getInstance().longitud1 = (float) latLng.longitude;
                            dar_localizacion();
                            UBI1 = mMap.addMarker(new MarkerOptions()
                                    .anchor(0.0f, 1.0f)
                                    .alpha(0.7f)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                                    .position(latLng)
                                    .title("UBI1")
                                    .snippet("Esta es la ubicación 1"));
                            Log.d("creacion UBI1", "entra a setInfoWindowAdapter");
                            //mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
                            Log.d("creacion UBI1", "sale de setInfoWindowAdapter");
                            //UBI1.showInfoWindow();
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
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                            .position(latLng)
                            .title("UBI2")
                            .snippet("Esta es la ubicación 2"));
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
        circleOptions.fillColor(Color.argb(100, 235, 165, 171));
        // Border width of the circle
        circleOptions.strokeWidth(2);
        // Adding the circle to the GoogleMap
        Circle C = mMap.addCircle(circleOptions);
        CirculosMarcadoresIncidencias.add(C);
    }

    private void limpiar_incidencias(){
        for(int i = 0; i < marcadoresIncidencias.size(); ++i){
            marcadoresIncidencias.get(i).remove();
            CirculosMarcadoresIncidencias.get(i).remove();

        }
        marcadoresIncidencias.removeAllElements();
        CirculosMarcadoresIncidencias.removeAllElements();
    }

    public void generarMarcadores(LatLng latLng, String info, String tip, int radio) {
        Log.d("ALGO","3");
        Log.d("ALGO5", mMap.toString());
        Marker m  = mMap.addMarker(new MarkerOptions()
                .snippet(info)
                .position(latLng)
                .alpha(0.9f)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .title(tip));
        drawCircle(latLng, radio * 2000);
        marcadoresIncidencias.add(m);
    }
}

//CEMENTERIO
/*


------------------------------------------------------------------------------------------------------------------
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
 */