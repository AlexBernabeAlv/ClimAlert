package com.example.climalert.CosasDeTeo;

public class UsuarioEstandar {
    public String email;
    public String password;
    public int radioEfecto;
    public int gravedad;
    public boolean admin;


    public UsuarioEstandar(String e, String p, int re, int g, boolean a){
        email = e;
        password = p;
        radioEfecto = re;
        gravedad = g;
        admin = a;
    }
}
