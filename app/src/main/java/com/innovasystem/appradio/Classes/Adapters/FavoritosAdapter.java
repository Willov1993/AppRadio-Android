package com.innovasystem.appradio.Classes.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.innovasystem.appradio.Classes.Models.Emisora;
import com.innovasystem.appradio.Classes.Models.Segmento;
import com.innovasystem.appradio.R;

import java.util.List;

public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.ViewHolder> {
    List<Emisora> emisoras_dataset;
    List<Segmento> favoritos_dataset;
    Context context;

    public FavoritosAdapter(Context c,List<Emisora> lista_emisoras,List<Segmento> lista_segmentos_favoritos){
        this.context= c;
        this.emisoras_dataset= lista_emisoras;
        this.favoritos_dataset= lista_segmentos_favoritos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_favoritos_item, parent, false);
        FavoritosAdapter.ViewHolder vh = new FavoritosAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder vholder, int i) {
        Emisora em= emisoras_dataset.get(i);
        Segmento seg= favoritos_dataset.get(i);

        vholder.tv_emisora.setText(em.getNombre());
        vholder.tv_frecuencia.setText(em.getFrecuencia_dial());
        vholder.tv_segmento.setText(seg.getNombre());
        vholder.tv_ciudad.setText(em.getCiudad());
    }

    @Override
    public int getItemCount() {
        return favoritos_dataset.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{
        TextView tv_emisora,tv_frecuencia,tv_segmento,tv_ciudad;
        ImageButton btn_favorito;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tv_emisora= itemView.findViewById(R.id.tv_favitem_emisora);
            tv_frecuencia= itemView.findViewById(R.id.tv_favitem_frecuencia);
            tv_segmento= itemView.findViewById(R.id.tv_favitem_segmento);
            tv_ciudad= itemView.findViewById(R.id.tv_favitem_ciudad);
            btn_favorito= itemView.findViewById(R.id.btn_favitem_favorito);

        }
    }
}
