package com.example.climalert.CosasDeTeo;

public class InformacionUsuario {

    public String email;
    public String password;
    public float latitud1;
    public float longitud1;
    public float latitud2;
    public float longitud2;
    public int radioEfecto;
    public int gravedad;
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
    private InformacionUsuario(){}

    static public InformacionUsuario getInstance(){
        if(usuario == null){
            usuario = new InformacionUsuario();
        }
        return usuario;
    }
}
