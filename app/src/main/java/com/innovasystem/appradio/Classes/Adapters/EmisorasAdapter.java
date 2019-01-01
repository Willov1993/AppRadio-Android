package com.innovasystem.appradio.Classes.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahamed.multiviewadapter.RecyclerAdapter;
import com.innovasystem.appradio.Activities.HomeActivity;
import com.innovasystem.appradio.Classes.ItemClickListener;
import com.innovasystem.appradio.Classes.Models.Emisora;
import com.innovasystem.appradio.Fragments.EmisoraContentFragment;
import com.innovasystem.appradio.Fragments.EmisorasFragment;
import com.innovasystem.appradio.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EmisorasAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public List<Object> emisoras_dataset;
    Context context;

    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;

    public EmisorasAdapter(List<Object> lista_emisoras, Context c){
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

    public static class TitleViewHolder extends RecyclerView.ViewHolder {
        TextView tv_ciudad;

        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_ciudad = itemView.findViewById(R.id.tv_item_titulo_ciudad);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_VIEW_TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_emisora_item, parent, false);
            EmisorasAdapter.ViewHolder vh = new EmisorasAdapter.ViewHolder(v);
            v.setOnClickListener((View view) ->
            {
                listener.OnItemClick(view, vh.getAdapterPosition());
            });
            return vh;
        }
        else{
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_emisora_titulo_item, parent, false);
            EmisorasAdapter.TitleViewHolder vh= new EmisorasAdapter.TitleViewHolder(v);
            return vh;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        System.out.println("POSITION: "+ position);
        if(!isTitle(emisoras_dataset.get(position))) {
            ViewHolder holder= (ViewHolder) viewHolder;
            Emisora em= (Emisora) emisoras_dataset.get(position);
            holder.tv_titulo_emisora.setText(em.getNombre());
            holder.tv_info_emisora.setText(em.getFrecuencia_dial());
            Picasso.with(context)
                    .load(em.getLogotipo())
                    .placeholder(R.drawable.radio_banner2)
                    .into(holder.iv_emisora);
        }
        else{//if(viewHolder instanceof  TitleViewHolder){
            TitleViewHolder holder= (TitleViewHolder) viewHolder;
            String provincia= (String) emisoras_dataset.get(position);
            holder.tv_ciudad.setText(provincia + "\t");
            holder.tv_ciudad.setPaintFlags(holder.tv_ciudad.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        }
    }

    @Override
    public int getItemCount() {
        return emisoras_dataset.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemViewType(int position) {
        return isTitle(emisoras_dataset.get(position)) ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
    }

    public boolean isTitle(Object o){
        return o instanceof String;
    }

    private ItemClickListener listener= new ItemClickListener(){

        @Override
        public void OnItemClick(View v, int position) {
            Emisora emisoraSeleccionada= (Emisora) emisoras_dataset.get(position);
            EmisoraContentFragment fragment=new EmisoraContentFragment();
            Bundle parameters= new Bundle();
            parameters.putParcelable("emisora",emisoraSeleccionada);
            fragment.setArguments(parameters);
            ((HomeActivity) context).changeFragment(fragment,R.id.frame_container,true);
        }
    };


}
