package com.example.climalert.CosasDeTeo;

import java.util.Date;
import java.util.Vector;

public class Notificacion {

    public String fecha;
    public int radio;
    public float longitud;
    public float latitud;
    public String nombre;
    public String descripcion;
    public Vector<String> inidicaciones = new Vector<String>();


    public Notificacion(String f, Integer r, Float la, Float lo, String nom, String desc){
        fecha = f;
        radio = r;
        latitud = la;
        longitud = lo;
        nombre = nom;
        descripcion = desc;
    }




}
