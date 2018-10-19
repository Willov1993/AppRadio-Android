package com.innovasystem.appradio.Clases.Models;
import  com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Segmento {

    private Long id;
    private String nombre;
    private String slogan;
    private String descripcion;
    private Long idEmisora;
    private String imagen;

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

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getIdEmisora() {
        return idEmisora;
    }

    public void setIdEmisora(Long idEmisora) {
        this.idEmisora = idEmisora;
    }

    @Override
    public String toString() {
        return "Segmento{" +
                "id='" + id + '\'' +
                "nombre='" + nombre + '\'' +
                ", slogan='" + slogan + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", idEmisora=" + idEmisora +
                '}';
    }
}
