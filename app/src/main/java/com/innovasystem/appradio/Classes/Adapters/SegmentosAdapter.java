package com.innovasystem.appradio.Classes.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.innovasystem.appradio.Activities.HomeActivity;
import com.innovasystem.appradio.Classes.ItemClickListener;
import com.innovasystem.appradio.Classes.Models.Horario;
import com.innovasystem.appradio.Classes.Models.Segmento;
import com.innovasystem.appradio.Fragments.SegmentoInfoFragment;
import com.innovasystem.appradio.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Clase Adaptador para el recyclerView de la Vista 'Seleccionar Segmentos'
 */
public class SegmentosAdapter extends RecyclerView.Adapter<SegmentosAdapter.ViewHolder>{
    private Map<Horario,Segmento> segmentos_dataset;
    private List<Horario> keysList;
    private Context context;

    public SegmentosAdapter(Map<Horario,Segmento> mapa_segmentos,Context c){
        this.segmentos_dataset= mapa_segmentos;
        this.keysList= new ArrayList<>(this.segmentos_dataset.keySet());
        this.context= c;
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{

        Button btn_titulo_segmento;
        Button btn_horario_segmento;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.btn_titulo_segmento= itemView.findViewById(R.id.btn_segmento_titulo);
            this.btn_horario_segmento= itemView.findViewById(R.id.btn_segmento_horario);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_seleccion_segmento,parent,false);
        ViewHolder vh= new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Horario hor= keysList.get(position);
        Segmento seg= segmentos_dataset.get(hor);
        holder.btn_titulo_segmento.setText(seg.getNombre());
        holder.btn_horario_segmento.setText( hor.getFecha_inicio().substring(0,5) + " - " + hor.getFecha_fin().substring(0,5));

        holder.btn_titulo_segmento.setOnClickListener((View v)->{
            listener.OnItemClick(v,position);
        });

    }

    @Override
    public int getItemCount() {
        return segmentos_dataset.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * Listener para manejar la seleccion de un item
     */
    private ItemClickListener listener= new ItemClickListener(){

        @Override
        public void OnItemClick(View v, int position) {
            Segmento seg= segmentos_dataset.get(keysList.get(position));
            Bundle args= new Bundle();
            args.putParcelable("segmento",seg);
            SegmentoInfoFragment fragment= new SegmentoInfoFragment();
            fragment.setArguments(args);
            ((HomeActivity) context).changeFragment(fragment,R.id.frame_container,true);
        }
    };
}
