package com.innovasystem.appradio.Classes.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.innovasystem.appradio.Classes.ItemClickListener;
import com.innovasystem.appradio.Classes.Models.Horario;
import com.innovasystem.appradio.Classes.Models.Segmento;
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
        v.setOnClickListener((View view)->
        {
            listener.OnItemClick(view,vh.getAdapterPosition());
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Horario hor= keysList.get(position);
        Segmento seg= segmentos_dataset.get(hor);
        holder.tv_titulo_segmento.setText(seg.getNombre());
        holder.tv_horario_segmento.setText(String.format("%s - %s",
                hor.getFecha_inicio().substring(0,5),
                hor.getFecha_fin()).substring(0,5));
        Picasso.with(context)
                .load(seg.getImagen())
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

    /**
     * Listener para manejar la seleccion de un item
     */
    private ItemClickListener listener= new ItemClickListener(){

        @Override
        public void OnItemClick(View v, int position) {
            Segmento seg= segmentos_dataset.get(keysList.get(position));
            Toast.makeText(context, "SEGMENTO: "+seg.getNombre()+" POS: "+position, Toast.LENGTH_SHORT).show();
        }
    };
}
