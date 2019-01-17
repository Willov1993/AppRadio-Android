package com.innovasystem.appradio.Classes.Models;

public class Multimedia{
    String url;
    String fecha;
    String descripcion;

    public Multimedia(String url, String descripcion, String fecha) {
        this.url = url;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }

    public Multimedia() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
