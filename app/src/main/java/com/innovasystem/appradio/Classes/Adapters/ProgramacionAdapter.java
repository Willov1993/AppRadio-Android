package com.innovasystem.appradio.Classes.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovasystem.appradio.Classes.Models.Fecha;
import com.innovasystem.appradio.Classes.Models.Horario;
import com.innovasystem.appradio.Classes.Models.Segmento;
import com.innovasystem.appradio.R;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Map;

public class ProgramacionAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<Horario> horarios;
    private ArrayList<Segmento> segmentos;
    private ArrayList<Segmento> favoritos;
    private Fecha horaActual;

    public ProgramacionAdapter(Context c,ArrayList<Horario> horarios, ArrayList<Segmento> segmentos,Fecha horaActual,ArrayList<Segmento> favoritos){
        context= c;
        this.horarios=horarios;
        this.segmentos= segmentos;
        this.horaActual= horaActual;
        this.favoritos= favoritos;
    }

    @Override
    public int getCount() {
        return segmentos.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        final View itemView;

        if(view == null){
            itemView= LayoutInflater.from(context).inflate(R.layout.lv_programacion_item,parent,false);
        }
        else{
            itemView= view;
        }

        Segmento segmento= segmentos.get(i);
        Horario horario= horarios.get(i);

        TextView tv_horario= itemView.findViewById(R.id.tv_programacion_horario);
        TextView tv_segmento= itemView.findViewById(R.id.tv_programacion_segmento);
        TextView tv_emisora= itemView.findViewById(R.id.tv_programacion_emisora);
        TextView tv_transimision= itemView.findViewById(R.id.tv_programacion_transmision);
        LinearLayout linear_container= itemView.findViewById(R.id.proginfo_container);
        ImageView iv_fav= itemView.findViewById(R.id.iv_programacion_fav);


        //linear_container.setBackgroundColor(context.getResources().getColor(R.color.seleccion_nav));
        tv_transimision.setVisibility(View.VISIBLE);
        iv_fav.setVisibility(View.GONE);

        String hora_inicio= horario.getFecha_inicio();
        String hora_fin= horario.getFecha_fin();

        tv_horario.setText(String.format("%s - %s",
                hora_inicio.substring(0,hora_inicio.length() - 3),
                hora_fin.substring(0,hora_fin.length() - 3)));
        tv_segmento.setText(segmento.getNombre());
        tv_emisora.setText(segmento.getEmisora().getNombre());

        Time h_inicio= Time.valueOf(hora_inicio);
        Time h_fin= Time.valueOf(hora_fin);

        if(horaActual.getHora().before(h_inicio) || horaActual.getHora().after(h_fin)){
            System.out.println("HORA EN VIVO!");
            System.out.println(String.format("HORAS: %s - %s - %s\n",h_inicio,h_fin,horaActual.getHora()));
            //linear_container.setBackgroundColor(Color.TRANSPARENT);
            tv_transimision.setVisibility(View.GONE);
        }

        if(favoritos.contains(segmento)){
            iv_fav.setVisibility(View.VISIBLE);
        }

        return itemView;
    }
}
