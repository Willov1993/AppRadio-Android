package com.innovasystem.appradio.Clases.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Emisora implements Parcelable {
    Long id;
    String nombre;
    String frecuencia_dial;
    String url_streaming;
    String sitio_web;
    String direccion;
    String descripcion;
    String ciudad;
    String provincia;
    String logotipo;


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

    public String getLogotipo() {
        return logotipo;
    }

    public void setLogotipo(String logotipo) {
        this.logotipo = logotipo;
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
                ", logotipo='" + logotipo + '\'' +
                '}';
    }

    public Emisora(){

    }

    protected Emisora(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        nombre = in.readString();
        frecuencia_dial = in.readString();
        url_streaming = in.readString();
        sitio_web = in.readString();
        direccion = in.readString();
        descripcion = in.readString();
        ciudad = in.readString();
        provincia = in.readString();
        logotipo = in.readString();
    }

    public static final Creator<Emisora> CREATOR = new Creator<Emisora>() {
        @Override
        public Emisora createFromParcel(Parcel in) {
            return new Emisora(in);
        }

        @Override
        public Emisora[] newArray(int size) {
            return new Emisora[size];
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
        parcel.writeString(frecuencia_dial);
        parcel.writeString(url_streaming);
        parcel.writeString(sitio_web);
        parcel.writeString(direccion);
        parcel.writeString(descripcion);
        parcel.writeString(ciudad);
        parcel.writeString(provincia);
        parcel.writeString(logotipo);
    }
}
