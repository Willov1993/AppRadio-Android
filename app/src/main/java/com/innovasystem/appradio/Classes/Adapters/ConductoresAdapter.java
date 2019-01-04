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
import com.innovasystem.appradio.Classes.Models.Conductor;
import com.innovasystem.appradio.Fragments.LocutorInfoFragment;
import com.innovasystem.appradio.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ConductoresAdapter extends RecyclerView.Adapter<ConductoresAdapter.ViewHolder> {
    Context context;
    List<Conductor> conductores_dataset;
    String nombreEmisora;

    public ConductoresAdapter(Context context, List<Conductor> conductores_dataset, String nombreEmisora) {
        this.context = context;
        this.conductores_dataset = conductores_dataset;
        this.nombreEmisora= nombreEmisora;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_conductor_item,parent,false);
        ConductoresAdapter.ViewHolder vh= new ConductoresAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Conductor loc= conductores_dataset.get(i);
        viewHolder.tv_nombre_locutor.setText(loc.getFirst_name() + " " +loc.getLast_name());
        if(loc.getImagen() != null) {
            Picasso.with(context)
                    .load(loc.getImagen())
                    .fit()
                    .into(viewHolder.img_locutor);
        }

        viewHolder.btn_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args= new Bundle();
                args.putParcelable("locutor",loc);
                args.putString("emisora",nombreEmisora);
                LocutorInfoFragment fragment= new LocutorInfoFragment();
                fragment.setArguments(args);
                ((HomeActivity) context).changeFragment(fragment,R.id.frame_container,true);

            }
        });

    }

    @Override
    public int getItemCount() {
        return conductores_dataset.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView tv_nombre_locutor;
        ImageView img_locutor;
        Button btn_perfil;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nombre_locutor= itemView.findViewById(R.id.tv_nombre_conductor);
            img_locutor= itemView.findViewById(R.id.img_condutor);
            btn_perfil= itemView.findViewById(R.id.btn_ver_perfil);
        }
    }
}
