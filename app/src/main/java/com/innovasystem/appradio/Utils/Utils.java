package com.innovasystem.appradio.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.innovasystem.appradio.Classes.Models.Emisora;
import com.innovasystem.appradio.Classes.Models.Horario;
import com.innovasystem.appradio.Classes.Models.Segmento;
import com.innovasystem.appradio.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/*Clase que contiene metodos estaticos para realizar actividades especificas */
public class Utils {

    private static Snackbar mSnackBar;

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


    public static boolean isActiveInternet (String uri){
        boolean success = false;
        try {
            URL url = new URL(uri!=null ? uri : "http://www.google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            success = connection.getResponseCode() == 200;
            System.out.println("RESULT OF CONNECTION: "+connection.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return success;
    }


    public static String encrypt(String input) {
        // This is base64 encoding, which is not an encryption
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    public static String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }

    /**
     * Este metodo permite comparar un horario dado con los horarios de un arreglo,
     * de este arreglo se escogera un objeto Horario h tal que:
     *
     * h.horaInicio =< horario < h.horarioFin
     *
     * @param horaActual El horario que se deseaComparar en formato String
     * @param horarios el arreglo de horarios con los que se va a hacer la comparacion
     * @return Un objeto Horario del arreglo que cumpla la condicion
     */
    public static Horario compararHorarioActual(String horaActual,Horario[] horarios){
        for(Horario hor : horarios){
            String horaInicio= hor.getFecha_inicio().substring(0,hor.getFecha_inicio().length() - 3);
            String horaFin= hor.getFecha_fin().substring(0,hor.getFecha_fin().length() - 3);
            if(horaActual.compareTo(horaInicio) >= 0 && horaActual.compareTo(horaFin) < 0){
                return hor;
            }
        }
        return null;
    }

    public static List<Emisora> generarEmisorasPrueba(){
        int muestra= 10;
        List<Emisora> lista_emisora= new ArrayList<>();
        for (int i = 0; i <muestra ; i++) {
            Emisora e= new Emisora();
            e.setId((long) (i+ 1));
            e.setNombre("Emisora de Prueba " + (i+1));
            e.setFrecuencia_dial("9" + i + "." + (i+1));
            e.setUrl_streaming("http://pruebastreaming.com");

            lista_emisora.add(e);
        }
        return lista_emisora;
    }

    public static List<Segmento> generarSegmentosPrueba(Emisora idEmisora){
        int muestra= 18;
        String[] dias= new String[]{"Lunes", "Martes", "Miercoles", "Jueves" , "Viernes" , "Sabado", "Domingo"};
        String[] horariosInicio= new String[]{
                "06:00:00", "07:00:00", "08:00:00", "09:00:00", "10:00:00", "11:00:00", "12:00:00", "13:00:00", "14:00:00", "15:00:00",
                "16:00:00", "17:00:00", "18:00:00", "19:00:00", "20:00:00", "21:00:00", "22:00:00", "23:00:00", "00:00:00",
                "06:30:00", "07:30:00", "08:30:00", "09:30:00", "10:30:00", "11:30:00", "12:30:00", "13:30:00", "14:30:00", "15:30:00",
                "16:30:00", "17:30:00", "18:30:00", "19:30:00", "20:30:00", "21:30:00", "22:30:00", "23:30:00", "00:30:00"};
        String[] horariosFin= new String[]{
                "07:00:00", "08:00:00", "09:00:00", "10:00:00", "11:00:00", "12:00:00", "13:00:00", "14:00:00", "15:00:00", "16:00:00",
                "17:00:00", "18:00:00", "19:00:00", "20:00:00", "21:00:00", "22:00:00", "23:00:00", "00:00:00", "01:00:00",
                "07:30:00", "08:30:00", "09:30:00", "10:30:00", "11:30:00", "12:30:00", "13:30:00", "14:30:00", "15:30:00", "16:30:00",
                "17:30:00", "18:30:00", "19:30:00", "20:30:00", "21:30:00", "22:30:00", "23:30:00", "00:30:00", "01:30:00"};

        List<Segmento> lista_segmentos= new ArrayList<>();
        for (int i = 0; i <muestra ; i++) {
            Segmento seg= new Segmento();
            seg.setNombre("Segmento de Prueba " + (i+1));
            seg.setEmisora(idEmisora);
            Horario[] horariosdePrueba= new Horario[7];

            for (int j = 0; j < dias.length; j++) {
                Horario h = new Horario();
                h.setDia(dias[j]);
                if(i < muestra/2){
                    h.setFecha_inicio(horariosInicio[i]);
                    h.setFecha_fin(horariosFin[i]);
                }
                else{
                    h.setFecha_inicio(horariosInicio[19 + i]);
                    h.setFecha_fin(horariosFin[19 + i]);
                }
                horariosdePrueba[j] = h;
            }

            seg.setHorarios(horariosdePrueba);
            lista_segmentos.add(seg);
        }
        return lista_segmentos;
    }

    public static TextView crearTextViewPersonalizado(Context c,float size,int color,String texto){
       TextView tv= new TextView(c);
       tv.setTextSize(size);
       tv.setTextColor(color);
       tv.setText(texto);

       return tv;
    }

    public static Snackbar mostrarMensajeSnackBar(View v,String mensaje){
        mSnackBar = Snackbar.make(v, mensaje, Snackbar.LENGTH_INDEFINITE);
        View view = mSnackBar.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        TextView mainTextView = (TextView) (view).findViewById(android.support.design.R.id.snackbar_text);
        mSnackBar.setAction("Ignorar",new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mSnackBar.dismiss();
            }
        });
        mSnackBar.setActionTextColor(v.getContext().getResources().getColor(R.color.seleccion_nav));
        mSnackBar.show();
        return mSnackBar;
    }

    public static void ocultarSnackBar(){
        if(mSnackBar != null && mSnackBar.isShown()){
            mSnackBar.dismiss();
        }
    }

}
