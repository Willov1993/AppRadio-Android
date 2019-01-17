package com.innovasystem.appradio.Classes.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Multimedia{
    String url;
    String fecha_creacion;
    String descripcion;
    String segmento;

    public Multimedia(String url, String descripcion, String fecha,String segmento) {
        this.url = url;
        this.fecha_creacion = fecha;
        this.descripcion = descripcion;
        this.segmento= segmento;
    }

    public Multimedia(String url, String descripcion, String fecha) {
        this.url = url;
        this.fecha_creacion = fecha;
        this.descripcion = descripcion;
        this.segmento= segmento;
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
        return fecha_creacion;
    }

    public void setFecha(String fecha) {
        this.fecha_creacion = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getSegmento() {
        return segmento;
    }

    public void setSegmento(String segmento) {
        this.segmento = segmento;
    }
}
