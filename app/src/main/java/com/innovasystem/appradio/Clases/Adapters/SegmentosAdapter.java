package com.innovasystem.appradio.Clases.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.innovasystem.appradio.Clases.Models.Segmento;
import com.innovasystem.appradio.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Clase Adaptador para el recyclerView de la Vista 'Seleccionar Segmentos'
 */
public class SegmentosAdapter extends RecyclerView.Adapter<SegmentosAdapter.ViewHolder>{
    private List<Segmento> segmentos_dataset;
    private Context context;

    public SegmentosAdapter(List<Segmento> listaSegmentos,Context c){
        this.segmentos_dataset= listaSegmentos;
        this.context= c;
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{

        ImageView iv_segmento;
        TextView tv_titulo_segmento;
        TextView tv_horario_segmento;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_segmento= itemView.findViewById(R.id.iv_segmento_item);
            this.tv_titulo_segmento= itemView.findViewById(R.id.tv_titulo_segmento_item);
            this.tv_horario_segmento= itemView.findViewById(R.id.tv_horario_segmento_item);
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
        holder.tv_titulo_segmento.setText(segmentos_dataset.get(position).getNombre());
        Picasso.with(context)
                .load(segmentos_dataset.get(position).getImagen())
                .placeholder(R.drawable.radio_icon_48)
                .resize(80,80).centerCrop()
                .into(holder.iv_segmento);
    }

    @Override
    public int getItemCount() {
        return segmentos_dataset.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
