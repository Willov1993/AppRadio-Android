package com.innovasystem.appradio.Classes.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.innovasystem.appradio.Activities.HomeActivity;
import com.innovasystem.appradio.Classes.Models.Multimedia;
import com.innovasystem.appradio.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MultimediaAdapter extends RecyclerView.Adapter<MultimediaAdapter.ViewHolder> {

    Context context;
    List<Multimedia> multimedia_dataset;
    Boolean isVideoDataset;

    public MultimediaAdapter(Context c,List<Multimedia> listaMultimedia, Boolean isVideo){
        this.context= c;
        this.multimedia_dataset= listaMultimedia;
        this.isVideoDataset= isVideo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v= null;
        if(!isVideoDataset) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_imagen_item, parent, false);
        }
        else{

        }
        ViewHolder vh= new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Picasso.with(context)
                .load(multimedia_dataset.get(i).getUrl())
                .fit()
                .into(viewHolder.iv_preview);

        viewHolder.iv_preview.setClickable(true);
        viewHolder.iv_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable imagen=((ImageView) view).getDrawable();
                Toast.makeText(context, "Elemento Seleccionado", Toast.LENGTH_SHORT).show();

                Dialog modalImageDialog= new Dialog(context);
                modalImageDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                modalImageDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                modalImageDialog.setCancelable(true);
                modalImageDialog.setCanceledOnTouchOutside(true);
                View modalView = ((HomeActivity) context).getLayoutInflater().inflate(R.layout.modal_image,null);
                ((TextView) modalView.findViewById(R.id.tv_modal_descripcion)).setText(multimedia_dataset.get(i).getDescripcion());
                ((TextView) modalView.findViewById(R.id.tv_modal_fecha)).setText(multimedia_dataset.get(i).getFecha());
                ((ImageView) modalView.findViewById(R.id.iv_modal)).setImageDrawable(imagen);
                ((Button) modalView.findViewById(R.id.bnt_close_modal_image)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modalImageDialog.dismiss();
                    }
                });
                modalImageDialog.setContentView(modalView);
                modalImageDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return multimedia_dataset.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{
        public ImageView iv_preview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_preview= itemView.findViewById(R.id.iv_imagen_galeria);
        }
    }
}
