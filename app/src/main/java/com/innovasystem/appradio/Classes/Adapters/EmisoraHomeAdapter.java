package com.innovasystem.appradio.Classes.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.innovasystem.appradio.Activities.EmisoraActivity;
import com.innovasystem.appradio.Activities.HomeActivity;
import com.innovasystem.appradio.Classes.Models.Emisora;
import com.innovasystem.appradio.Classes.Models.Segmento;
import com.innovasystem.appradio.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmisoraHomeAdapter extends  RecyclerView.Adapter<EmisoraHomeAdapter.ViewHolder> {
    public Map<Emisora,Segmento> emisoras_dataset;
    public List<Emisora> emisoras_keys;
    Context context;

    public EmisoraHomeAdapter(Map<Emisora,Segmento> mapa_emisoras,Context c){
        this.emisoras_dataset= mapa_emisoras;
        this.emisoras_keys= new ArrayList<>(this.emisoras_dataset.keySet());
        this.context= c;
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{
        TextView tv_radio, tv_frecuencia,tv_segmento,tv_horario;
        Button btn_verprog;
        ImageView img_segmento;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_radio= itemView.findViewById(R.id.tv_hitem_radio);
            this.tv_frecuencia= itemView.findViewById(R.id.tv_hitem_frecuencia);
            this.tv_segmento= itemView.findViewById(R.id.tv_hitem_prog_actual);
            this.tv_horario= itemView.findViewById(R.id.tv_hitem_horario);
            this.btn_verprog= itemView.findViewById(R.id.btn_hitem_programacion);
            this.img_segmento= itemView.findViewById(R.id.img_hitem_segmento);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_home_emisora_item,parent,false);
        EmisoraHomeAdapter.ViewHolder vh= new EmisoraHomeAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Emisora emisora= this.emisoras_keys.get(i);
        viewHolder.tv_radio.setText(emisora.getNombre());
        viewHolder.tv_frecuencia.setText(emisora.getFrecuencia_dial());

        if(this.emisoras_dataset.get(emisora) != null) {
            viewHolder.tv_segmento.setText(this.emisoras_dataset.get(emisora).getNombre());
            viewHolder.tv_horario.setText(String.format("%s - %s",
                    this.emisoras_dataset.get(emisora).getHorarios()[0].getFecha_inicio(),
                    this.emisoras_dataset.get(emisora).getHorarios()[0].getFecha_fin())
            );
            Picasso.with(this.context)
                    .load(this.emisoras_dataset.get(emisora).getImagen())
                    .placeholder(R.drawable.radio_placeholder2)
                    .fit()
                    .centerInside()
                    //.resize(viewHolder.img_segmento.getMaxWidth(),viewHolder.img_segmento.getMaxHeight())
                    //.centerInside()
                    .into(viewHolder.img_segmento);
        }
        else{
            viewHolder.tv_segmento.setText("No hay información disponible");
            viewHolder.tv_horario.setText("");
            System.out.println("img w: "+viewHolder.img_segmento.getLayoutParams().width);
            System.out.println("img h: "+viewHolder.img_segmento.getLayoutParams().height);
            Picasso.with(this.context)
                    .load(emisora.getLogotipo())
                    .placeholder(R.drawable.radio_placeholder2)
                    .fit()
                    //.centerInside()
                    .into(viewHolder.img_segmento);
        }

        viewHolder.btn_verprog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Viendo Programacion", Toast.LENGTH_SHORT).show();
                Intent i= new Intent((HomeActivity) context, EmisoraActivity.class);
                i.putExtra("emisora",emisora);
                ((HomeActivity) context).startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return emisoras_dataset.size();
    }


}
