package com.innovasystem.appradio.Classes.Models;
import android.os.Parcel;
import android.os.Parcelable;

import  com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Segmento implements Parcelable{

    private Long id;
    private String nombre;
    private String slogan;
    private String descripcion;
    private Long idEmisora;
    private String imagen;
    private Emisora emisora;
    private Horario[] horarios;

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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Horario[] getHorarios() {
        return horarios;
    }

    public void setHorarios(Horario[] horarios) {
        this.horarios = horarios;
    }

    public Emisora getEmisora(){
        return this.emisora;
    }

    public void setEmisora(Emisora em) {
        this.emisora= em;
    }

    @Override
    public String toString() {
        return "Segmento{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", slogan='" + slogan + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", idEmisora=" + idEmisora +
                ", imagen='" + imagen + '\'' +
                ", horarios=" + Arrays.toString(horarios) + '\'' +
                ", emisora= " + emisora +
                '}';
    }

    public Segmento(){

    }

    protected Segmento(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        nombre = in.readString();
        slogan = in.readString();
        descripcion = in.readString();
        idEmisora = in.readLong();
        imagen = in.readString();
        emisora = (Emisora) in.readValue(ClassLoader.getSystemClassLoader());
        horarios =(Horario[]) in.readArray(ClassLoader.getSystemClassLoader());
    }

    public static final Creator<Segmento> CREATOR = new Creator<Segmento>() {
        @Override
        public Segmento createFromParcel(Parcel in) {
            return new Segmento(in);
        }

        @Override
        public Segmento[] newArray(int size) {
            return new Segmento[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        parcel.writeString(nombre);
        parcel.writeString(slogan);
        parcel.writeString(descripcion);
        parcel.writeLong(idEmisora);
        parcel.writeString(imagen);
        parcel.writeString(descripcion);
        parcel.writeValue(emisora);
        parcel.writeArray(horarios);
    }
}
