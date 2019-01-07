package com.innovasystem.appradio.Classes.Models;

public class RedSocialEmisora {
    Long id;
    String nombre;
    String link;
    Long idEmisora;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Long getIdEmisora() {
        return idEmisora;
    }

    @Override
    public String toString() {
        return "RedSocialEmisora{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", link='" + link + '\'' +
                ", idEmisora=" + idEmisora +
                '}';
    }
}
