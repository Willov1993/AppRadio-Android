package com.innovasystem.appradio.Classes.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TelefonoEmisora {
    Long id;
    String nro_telefono;
    Long idEmisora;

    public Long getId() {
        return id;
    }

    public String getNro_telefono() {
        return nro_telefono;
    }

    public Long getIdEmisora() {
        return idEmisora;
    }

    @Override
    public String toString() {
        return "TelefonoEmisora{" +
                "id=" + id +
                ", nro_telefono='" + nro_telefono + '\'' +
                ", idEmisora=" + idEmisora +
                '}';
    }
}
