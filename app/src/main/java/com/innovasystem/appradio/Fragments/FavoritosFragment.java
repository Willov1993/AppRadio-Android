package com.innovasystem.appradio.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.innovasystem.appradio.Classes.Adapters.FavoritosAdapter;
import com.innovasystem.appradio.Classes.Models.Emisora;
import com.innovasystem.appradio.Classes.Models.Segmento;
import com.innovasystem.appradio.Classes.RestServices;
import com.innovasystem.appradio.Classes.SessionConfig;
import com.innovasystem.appradio.R;
import com.innovasystem.appradio.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoritosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    RecyclerView rv_favoritos;
    ProgressBar progressBar;
    TextView tv_info;


    public FavoritosFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FavoritosFragment newInstance(String param1, String param2) {
        FavoritosFragment fragment = new FavoritosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_favoritos, container, false);

        rv_favoritos= root.findViewById(R.id.rv_favoritos);
        progressBar= root.findViewById(R.id.progressBar_favoritos);
        tv_info= root.findViewById(R.id.tv_mensaje_favoritos);

        rv_favoritos.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_favoritos.setHasFixedSize(true);

        new RestFetchFavoritosTask().execute();

        return root;
    }

    private class RestFetchFavoritosTask extends AsyncTask<Void,Void,Void>{
        List<Emisora> emisoras;
        List<Segmento> segmentos_favoritos;

        @Override
        protected Void doInBackground(Void... voids) {
            segmentos_favoritos= RestServices.consultarFavoritos(getContext(), SessionConfig.getSessionConfig(getContext()).usuario);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);

            /* Prueba */
            //emisoras= Utils.generarEmisorasPrueba();
            //segmentos_favoritos= Utils.generarSegmentosPrueba(emisoras.get(0));

            if(segmentos_favoritos == null){
                Toast.makeText(getContext(), "No se puede obtener informacion, intente mas tarde", Toast.LENGTH_SHORT).show();
                tv_info.setVisibility(View.VISIBLE);
                return;
            }

            if(segmentos_favoritos.size() == 0){
                Toast.makeText(getContext(), "No se pudo obtener la informacion o no ha agregado nada a favoritos aun", Toast.LENGTH_SHORT).show();
                tv_info.setVisibility(View.VISIBLE);
                return;
            }

            tv_info.setVisibility(View.GONE);

            emisoras= new ArrayList<>();
            for(Segmento seg:segmentos_favoritos){
                emisoras.add(seg.getEmisora());
            }

            FavoritosAdapter adapter= new FavoritosAdapter(getContext(),emisoras,segmentos_favoritos);
            rv_favoritos.setAdapter(adapter);
            rv_favoritos.getAdapter().notifyDataSetChanged();
        }
    }

}
