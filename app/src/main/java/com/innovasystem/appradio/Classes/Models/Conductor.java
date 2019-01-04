package com.innovasystem.appradio.Classes.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Conductor extends Persona {
    String hobbies;
    String apodo;
    RedSocialPersona[] redes_sociales;

    public String getHobbies() {
        return hobbies;
    }

    public String getApodo() {
        return apodo;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public void setApodo(String apodo) {
        this.apodo = apodo;
    }

    public RedSocialPersona[] getRedesSociales() {
        return redes_sociales;
    }

    public void setRedesSociales(RedSocialPersona[] redesSociales) {
        this.redes_sociales = redesSociales;
    }

    @Override
    public String toString() {
        return super.toString() +
                " {" +
                "hobbies='" + hobbies + '\'' +
                ", apodo='" + apodo + '\'' +
                ", redesSociales=" + Arrays.toString(redes_sociales) +
                "}";
    }
}
