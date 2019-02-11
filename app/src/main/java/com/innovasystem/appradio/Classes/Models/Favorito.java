package com.innovasystem.appradio.Classes.Models;

public class Favorito {
    private String Usuario;
    private long Segmento;

    public Favorito(String usuario, long segmento) {
        Usuario = usuario;
        Segmento = segmento;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public long getSegmento() {
        return Segmento;
    }

    public void setSegmento(long segmento) {
        Segmento = segmento;
    }
}
