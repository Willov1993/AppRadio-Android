package com.innovasystem.appradio.Clases.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.innovasystem.appradio.Activities.EmisoraActivity;
import com.innovasystem.appradio.Activities.HomeActivity;
import com.innovasystem.appradio.Clases.ItemClickListener;
import com.innovasystem.appradio.Clases.Models.Emisora;
import com.innovasystem.appradio.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EmisorasAdapter extends  RecyclerView.Adapter<EmisorasAdapter.ViewHolder>{

    List<Emisora> emisoras_dataset;
    Context context;

    public EmisorasAdapter(List<Emisora> lista_emisoras, Context c){
        this.emisoras_dataset= lista_emisoras;
        this.context= c;
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView iv_emisora;
        TextView tv_titulo_emisora;
        TextView tv_info_emisora;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_emisora= itemView.findViewById(R.id.iv_emisora_item);
            this.tv_titulo_emisora= itemView.findViewById(R.id.tv_titulo_emisora_item);
            this.tv_info_emisora= itemView.findViewById(R.id.tv_info_emisora_item);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_emisora_item,parent,false);
        EmisorasAdapter.ViewHolder vh= new EmisorasAdapter.ViewHolder(v);
        v.setOnClickListener((View view)->
        {
            listener.OnItemClick(view,vh.getAdapterPosition());
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.tv_titulo_emisora.setText(emisoras_dataset.get(position).getNombre());
        viewHolder.tv_info_emisora.setText(String.format("%s - %s",
                emisoras_dataset.get(position).getCiudad(),
                emisoras_dataset.get(position).getFrecuencia_dial())
        );
        Picasso.with(context)
                .load(emisoras_dataset.get(position).getLogotipo())
                .placeholder(R.drawable.radio_icon_48)
                .resize(80,80).centerInside()
                .into(viewHolder.iv_emisora);
    }

    @Override
    public int getItemCount() {
        return emisoras_dataset.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    private ItemClickListener listener= new ItemClickListener(){

        @Override
        public void OnItemClick(View v, int position) {
            Emisora emisoraSeleccionada= emisoras_dataset.get(position);
            Intent i= new Intent((HomeActivity) context,EmisoraActivity.class);
            i.putExtra("emisora",emisoraSeleccionada);
            ((HomeActivity) context).startActivity(i);
        }
    };
}
