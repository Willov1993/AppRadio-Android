package com.innovasystem.appradio.Classes.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Conductor extends Persona implements Parcelable {
    String biografia;
    String hobbies;
    String apodo;
    RedSocialPersona[] redes_sociales;

    public Conductor(){

    }

    protected Conductor(Parcel in) {
        id = in.readLong();
        first_name = in.readString();
        last_name = in.readString();
        fecha_nac = in.readString();
        imagen = in.readString();
        hobbies = in.readString();
        apodo = in.readString();
        redes_sociales= (RedSocialPersona[]) in.readArray(ClassLoader.getSystemClassLoader());
    }


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

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public RedSocialPersona[] getRedesSociales() {
        return redes_sociales;
    }

    public void setRedesSociales(RedSocialPersona[] redesSociales) {
        this.redes_sociales = redesSociales;
    }

    public static final Creator<Conductor> CREATOR = new Creator<Conductor>() {
        @Override
        public Conductor createFromParcel(Parcel in) {
            return new Conductor(in);
        }

        @Override
        public Conductor[] newArray(int size) {
            return new Conductor[size];
        }
    };

    @Override
    public String toString() {
        return super.toString() +
                " {" +
                "hobbies='" + hobbies + '\'' +
                ", apodo='" + apodo + '\'' +
                ", redesSociales=" + Arrays.toString(redes_sociales) +
                "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(first_name);
        parcel.writeString(last_name);
        parcel.writeString(fecha_nac);
        parcel.writeString(imagen);
        parcel.writeString(hobbies);
        parcel.writeString(apodo);
        //parcel.writeArray(redes_sociales);
    }
}
