package com.innovasystem.appradio.Utils;

import java.util.Date;

public class RegisterUser {
    private String first_name,last_name,username;
    private String password,email,rol;
    private String fecha_nac;

    public RegisterUser(String first_name, String last_name, String username, String password, String email, String rol, String fecha_nac) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.rol = rol;
        this.fecha_nac = fecha_nac;
    }

    public RegisterUser(){}

    public String getfirst_name() {
        return first_name;
    }

    public void setfirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getlast_name() {
        return last_name;
    }

    public void setApellidos(String last_name) {
        this.last_name = last_name;
    }

    public String getusername() {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPasssword(String passsword) {
        this.password = passsword;
    }

    public String getEmail() {
        return email;
    }

    public void setCorreo(String correo) {
        this.email = correo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getFecha_nac() {
        return fecha_nac;
    }

    public void setFecha_nac(String fecha_nac) {
        this.fecha_nac = fecha_nac;
    }

    @Override
    public String toString() {
        return "RegisterUser{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", rol='" + rol + '\'' +
                ", fecha_nac=" + fecha_nac +
                '}';
    }
}
