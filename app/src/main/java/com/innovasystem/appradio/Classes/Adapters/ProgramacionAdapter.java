package com.innovasystem.appradio.Classes.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.innovasystem.appradio.Classes.Models.Horario;
import com.innovasystem.appradio.Classes.Models.Segmento;
import com.innovasystem.appradio.R;

import java.util.ArrayList;
import java.util.Map;

public class ProgramacionAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<Map.Entry<Horario,Segmento>> segmentosDataset;

    public ProgramacionAdapter(Context c,Map<Horario,Segmento> dataset){
        context= c;
        segmentosDataset= new ArrayList<>();
        segmentosDataset.addAll(dataset.entrySet());
    }

    @Override
    public int getCount() {
        return segmentosDataset.size();
    }

    @Override
    public Map.Entry<Horario,Segmento> getItem(int i) {
        return segmentosDataset.get(i);
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

        Map.Entry<Horario,Segmento> item= getItem(i);

        TextView tv_horario= itemView.findViewById(R.id.tv_programacion_horario);
        TextView tv_segmento= itemView.findViewById(R.id.tv_programacion_segmento);
        TextView tv_emisora= itemView.findViewById(R.id.tv_programacion_emisora);

        tv_horario.setText(String.format("%s - %s",
                item.getKey().getFecha_inicio().substring(0,item.getKey().getFecha_inicio().length() - 3),
                item.getKey().getFecha_fin().substring(0,item.getKey().getFecha_fin().length() - 3)));
        tv_segmento.setText(item.getValue().getNombre());
        tv_emisora.setText(item.getValue().getEmisora().getNombre());

        return itemView;
    }
}
