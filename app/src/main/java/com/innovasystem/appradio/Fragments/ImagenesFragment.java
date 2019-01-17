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
import android.widget.Toast;

import com.innovasystem.appradio.Classes.Adapters.MultimediaAdapter;
import com.innovasystem.appradio.Classes.Models.Multimedia;
import com.innovasystem.appradio.R;
import com.innovasystem.appradio.Utils.Utils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImagenesFragment extends Fragment {

    RecyclerView rv_imagenes;
    ProgressBar progressBar;

    public ImagenesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_imagenes, container, false);

        rv_imagenes = root.findViewById(R.id.rv_imagenes);
        progressBar = root.findViewById(R.id.progress_imagenes);

        rv_imagenes.setHasFixedSize(true);
        rv_imagenes.setLayoutManager(new GridLayoutManager(getContext(),3));

        new RestFetchImagenesTask().execute();


        return root;
    }

    private class RestFetchImagenesTask extends AsyncTask<Void,Void,List<Multimedia>>{

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Multimedia> doInBackground(Void... voids) {
            List lista= Utils.generarListaLinksImagenesPrueba();
            return lista;
        }

        @Override
        protected void onPostExecute(List<Multimedia> lista_imagenes) {
            progressBar.setVisibility(View.GONE);
            if(lista_imagenes== null || lista_imagenes.size() == 0){
                Toast.makeText(getContext(), "No se pudo obtener las imagenes, intente mas tarde", Toast.LENGTH_SHORT).show();
                return;
            }

            MultimediaAdapter adapter= new MultimediaAdapter(getContext(),lista_imagenes,false);
            rv_imagenes.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }
    }
}
