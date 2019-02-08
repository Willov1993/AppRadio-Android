package com.innovasystem.appradio.Classes.Models;

import java.util.Date;

public class Noticia {
    private String usuario,mensaje,foto;



    public Noticia(String usuario, String mensaje, String foto,Date fecha_creacion) {
        this.usuario = usuario;
        this.mensaje = mensaje;
        this.foto = foto;
        this.fecha_creacion=fecha_creacion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
    public Date getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    private Date fecha_creacion;
    public Noticia() {
    }


}
