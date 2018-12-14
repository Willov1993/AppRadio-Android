package com.innovasystem.appradio.Classes.Models;

import java.sql.Time;
import java.util.Calendar;

public class Fecha {
    private Calendar fecha;
    private Time hora;

    public Calendar getFecha() {
        return fecha;
    }

    public void setFecha(Calendar fecha) {
        this.fecha = fecha;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    @Override
    public String toString() {
        return "Fecha{" +
                "fecha=" + fecha +
                ", hora=" + hora +
                '}';
    }
}
