package com.innovasystem.appradio.Clases.Models;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Horario implements Comparable<Horario> {
    private String fecha_inicio;
    private String fecha_fin;
    private String dia;

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(String fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    @Override
    public String toString() {
        return "Horario{" +
                "fecha_inicio='" + fecha_inicio + '\'' +
                ", fecha_fin='" + fecha_fin + '\'' +
                ", dia='" + dia + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull Horario horario) {
        String hora1= this.fecha_inicio.substring(0,2);
        String hora2= horario.fecha_inicio.substring(0,2);
        String min1= this.fecha_inicio.substring(3,5);
        String min2= horario.fecha_inicio.substring(3,5);
        if(hora1.compareTo(hora2)!=0){
            return hora1.compareTo(hora2);
        }
        return min1.compareTo(min2);
    }
}
