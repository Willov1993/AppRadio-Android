package com.innovasystem.appradio.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.innovasystem.appradio.Classes.Adapters.MultimediaAdapter;
import com.innovasystem.appradio.Classes.Models.Multimedia;
import com.innovasystem.appradio.Classes.RestServices;
import com.innovasystem.appradio.R;
import com.innovasystem.appradio.Utils.Utils;

import org.w3c.dom.Text;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideosFragment extends Fragment {

    RecyclerView rv_videos;
    ProgressBar progressBar;
    TextView tv_mensaje;

    public VideosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_videos, container, false);

        rv_videos= root.findViewById(R.id.rv_videos);
        progressBar= root.findViewById(R.id.progress_videos);
        tv_mensaje= root.findViewById(R.id.tv_mensaje_video);

        rv_videos.setHasFixedSize(true);
        rv_videos.setLayoutManager(new GridLayoutManager(getContext(),3));

        new RestFetchVideosTask().execute();

        return root;
    }

    private class RestFetchVideosTask extends AsyncTask<Void,Void,List<Multimedia>> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Multimedia> doInBackground(Void... voids) {
            List lista= RestServices.consultarMultimediaSegmento(getContext(),GaleriaFragment.segmento.getId().intValue(),true);//Utils.generarListaLinksVideosPrueba();
            return lista;
        }

        @Override
        protected void onPostExecute(List<Multimedia> lista_imagenes) {
            progressBar.setVisibility(View.GONE);
            if(lista_imagenes== null || lista_imagenes.size() == 0){
                Toast.makeText(getContext(), "No se pudo obtener los videos, intente mas tarde", Toast.LENGTH_SHORT).show();
                tv_mensaje.setVisibility(View.VISIBLE);
                return;
            }

            MultimediaAdapter adapter= new MultimediaAdapter(getContext(),lista_imagenes,true);
            rv_videos.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }
    }

}
