package com.example.climalert.Foro;

import android.util.Log;

public class Mensaje {
    private String mensaje;
    private String nombre;
    private int id;
    private int idParent;
    private int idInc;
    private boolean esDeIncidencia;
    private boolean esDeLogeado;

    public Mensaje() {
    }

    public Mensaje(String mensaje, String nombre, int id, int idParent, int idInc, boolean esDeIncidencia, boolean esDeLogeado) {
        this.mensaje = mensaje;
        this.nombre = nombre;
        this.id = id;
        this.idParent = idParent;
        this.idInc = idInc;
        this.esDeIncidencia = esDeIncidencia;
        this.esDeLogeado = esDeLogeado;
        Log.d("LOGEADO2", nombre + " " + String.valueOf(esDeLogeado) + " en mensaje.java");
    }

    public int getId() {
        return id;
    }

    public Mensaje(String mensaje, String nombre) {
        this.mensaje = mensaje;
        this.nombre = nombre;
    }

    public int getIdInc() {
        return idInc;
    }

    public void setIdInc(int idInc) {
        this.idInc = idInc;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdParent() {
        return idParent;
    }

    public void setIdParent(int idParent) {
        this.idParent = idParent;
    }

    public boolean isEsDeIncidencia() {
        return esDeIncidencia;
    }

    public void setEsDeIncidencia(boolean esDeIncidencia) {
        this.esDeIncidencia = esDeIncidencia;
    }

    public boolean isEsDeLogeado() {
        return esDeLogeado;
    }

    public void setEsDeLogeado(boolean esDeLogeado) {
        this.esDeLogeado = esDeLogeado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
