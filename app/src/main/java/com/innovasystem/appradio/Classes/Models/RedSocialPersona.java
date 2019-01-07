package com.innovasystem.appradio.Classes.Models;

public class RedSocialPersona {
    private long id;
    private long idUsuario_id;
    private String nombre;
    private String link;

    public long getId() {
        return id;
    }

    public long getIdUsuario_id() {
        return idUsuario_id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "RedSocialPersona{" +
                "id=" + id +
                ", idUsuario_id=" + idUsuario_id +
                ", nombre='" + nombre + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
