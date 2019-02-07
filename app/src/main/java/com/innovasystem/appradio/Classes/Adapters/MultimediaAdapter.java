package com.innovasystem.appradio.Classes.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.innovasystem.appradio.Activities.HomeActivity;
import com.innovasystem.appradio.Classes.Models.Multimedia;
import com.innovasystem.appradio.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

public class MultimediaAdapter extends RecyclerView.Adapter<MultimediaAdapter.ViewHolder> {

    Context context;
    List<Multimedia> multimedia_dataset;
    public Boolean isVideoDataset;

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
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_video_item, parent, false);
        }
        ViewHolder vh= new ViewHolder(v,isVideoDataset);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if(!isVideoDataset) {
            Picasso.with(context)
                    .load(multimedia_dataset.get(i).getUrl())
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.no_image)
                    .fit()
                    .into(viewHolder.iv_preview);

            viewHolder.iv_preview.setClickable(true);
            viewHolder.iv_preview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Drawable imagen = ((ImageView) view).getDrawable();

                    Dialog modalImageDialog = new Dialog(context);
                    modalImageDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    modalImageDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    modalImageDialog.setCancelable(true);
                    modalImageDialog.setCanceledOnTouchOutside(true);
                    View modalView = ((HomeActivity) context).getLayoutInflater().inflate(R.layout.modal_image, null);
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
        else{

            new ThumbnailFetchTask(viewHolder,i).execute();

            viewHolder.iv_preview.setClickable(true);
            viewHolder.iv_preview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog modalImageDialog = new Dialog(context);
                    modalImageDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    modalImageDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    modalImageDialog.setCancelable(true);
                    modalImageDialog.setCanceledOnTouchOutside(true);

                    //Carga de datos en el modal
                    View modalView = ((HomeActivity) context).getLayoutInflater().inflate(R.layout.modal_video, null);
                    ((TextView) modalView.findViewById(R.id.tv_modal_descripcion_video)).setText(multimedia_dataset.get(i).getDescripcion());
                    ((TextView) modalView.findViewById(R.id.tv_modal_fecha_video)).setText(multimedia_dataset.get(i).getFecha());

                    PlayerView playerView=modalView.findViewById(R.id.vplayer_modal_video);
                    ((SurfaceView) playerView.getVideoSurfaceView()).setZOrderOnTop(true);
                    ExoPlayer player= ExoPlayerFactory.newSimpleInstance(context,
                            new DefaultRenderersFactory(context),
                            new DefaultTrackSelector(),
                            new DefaultLoadControl());
                    playerView.setPlayer(player);
                    player.setPlayWhenReady(true);
                    ((PlayerControlView) modalView.findViewById(R.id.vplayer_controls)).setPlayer(player);
                    Uri uri = Uri.parse(multimedia_dataset.get(i).getUrl());
                    MediaSource mediaSource =  buildMediaSource(uri);
                    player.prepare(mediaSource, true, false);
                    /*VideoView vplayer= ((VideoView) modalView.findViewById(R.id.vplayer_modal_video));
                    MediaController mediaController= new MediaController(context);
                    mediaController.setVisibility(View.GONE);
                    vplayer.setMediaController(mediaController);
                    vplayer.setZOrderOnTop(true);
                    vplayer.setVideoURI(Uri.parse(multimedia_dataset.get(i).getUrl()));
                    vplayer.start();*/
                    ((Button) modalView.findViewById(R.id.bnt_close_modal_video)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modalImageDialog.dismiss();
                            player.release();
                        }
                    });
                    modalImageDialog.setContentView(modalView);
                    modalImageDialog.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return multimedia_dataset.size();
    }


    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }


    public static class ViewHolder extends  RecyclerView.ViewHolder{
        public ImageView iv_preview;

        public ViewHolder(@NonNull View itemView,Boolean isVideo) {
            super(itemView);
            if(!isVideo)
                iv_preview= itemView.findViewById(R.id.iv_imagen_galeria);
            else
                iv_preview= itemView.findViewById(R.id.iv_video_galeria);
        }
    }

    private class ThumbnailFetchTask extends AsyncTask<Void,Void,Uri>{
        private ViewHolder viewHolder;
        private int position;

        public ThumbnailFetchTask(ViewHolder vholder, int position){
            this.viewHolder= vholder;
            this.position= position;
        }

        @Override
        protected Uri doInBackground(Void... voids) {
            Uri imagen= null;
            try {
                Bitmap thumbnail = retriveVideoFrameFromVideo(multimedia_dataset.get(position).getUrl());
                imagen= getImageUri(context,thumbnail);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return imagen;
        }

        @Override
        protected void onPostExecute(Uri uri) {
            if(uri!=null) {
                Picasso.with(context)
                        .load(uri)
                        .placeholder(R.drawable.video_placeholder)
                        .error(R.drawable.video_placeholder)
                        .fit()
                        .into(viewHolder.iv_preview);
            }
        }

        public Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable
        {
            Bitmap bitmap = null;
            MediaMetadataRetriever mediaMetadataRetriever = null;
            try
            {
                mediaMetadataRetriever = new MediaMetadataRetriever();
                if (Build.VERSION.SDK_INT >= 14)
                    mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
                else
                    mediaMetadataRetriever.setDataSource(videoPath);
                //   mediaMetadataRetriever.setDataSource(videoPath);
                bitmap = mediaMetadataRetriever.getFrameAtTime();
            } catch (Exception e) {
                e.printStackTrace();
                throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

            } finally {
                if (mediaMetadataRetriever != null) {
                    mediaMetadataRetriever.release();
                }
            }
            return bitmap;
        }

        public Uri getImageUri(Context inContext, Bitmap inImage) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
            return Uri.parse(path);
        }
    }
}
