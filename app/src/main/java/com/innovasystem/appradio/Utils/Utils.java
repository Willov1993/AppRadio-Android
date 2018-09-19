package com.innovasystem.appradio.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*Clase que contiene metodos estaticos para realizar actividades especificas */
public class Utils {


    /**
     * Este metodo detecta si existe una conexion activa a traves de WiFi o a traves de conexion
     * de red de datos  (Este metodo SOLO DETECTA LA CONEXION, NO DETECTA EL ACCESO A WIFI).
     *
     * @param context El contexto de la actividad que lo llama
     * @return valor booleano true en caso de que existe al menos una conexion, caso contrario false
     */
    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivity = null;
        boolean isNetworkAvail = false;
        try {
            connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for(int i = 0; i < info.length; i++)
                        if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        {
                            return true;
                        }
                }
            }
            return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally{
            if (connectivity != null) {
                connectivity = null;
            }
        }
        return isNetworkAvail;
    }
}
