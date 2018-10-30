package com.innovasystem.appradio.Clases.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Emisora {
    Long id;
    String nombre;
    String frecuencia_dial;
    String url_streaming;
    String sitio_web;
    String direccion;
    String descripcion;
    String ciudad;
    String provincia;
    String logo;

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

    public String getFrecuencia_dial() {
        return frecuencia_dial;
    }

    public void setFrecuencia_dial(String frecuencia_dial) {
        this.frecuencia_dial = frecuencia_dial;
    }

    public String getUrl_streaming() {
        return url_streaming;
    }

    public void setUrl_streaming(String url_streaming) {
        this.url_streaming = url_streaming;
    }

    public String getSitio_web() {
        return sitio_web;
    }

    public void setSitio_web(String sitio_web) {
        this.sitio_web = sitio_web;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "Emisora{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", frecuencia_dial='" + frecuencia_dial + '\'' +
                ", url_streaming='" + url_streaming + '\'' +
                ", sitio_web='" + sitio_web + '\'' +
                ", direccion='" + direccion + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", provincia='" + provincia + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }
}
