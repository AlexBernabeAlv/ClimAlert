package com.example.climalert.CosasDeTeo;

import java.util.Vector;

public class Notificacion {

    public String fecha;
    public String hora;
    public String fuente;
    public int radio;
    public float longitud;
    public float latitud;
    public String nombre;
    public String medida;
    public String descripcion;
    public int identificador;
    public Vector<String> inidicaciones = new Vector<String>();


    public Notificacion(String f,String hor, String fuent, Integer r, Float la, Float lo, String nom, String desc, Integer id, String med){
        fecha = f;
        hora = hor;
        fuente = fuent;
        radio = r;
        latitud = la;
        longitud = lo;
        nombre = nom;
        descripcion = desc;
        identificador = id;
        medida = med;
    }




}
