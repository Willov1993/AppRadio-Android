package com.innovasystem.appradio.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.innovasystem.appradio.Classes.Adapters.ConductoresAdapter;
import com.innovasystem.appradio.Classes.Models.Conductor;
import com.innovasystem.appradio.Classes.Models.Horario;
import com.innovasystem.appradio.Classes.Models.Segmento;
import com.innovasystem.appradio.Classes.RestServices;
import com.innovasystem.appradio.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SegmentoInfoFragment extends Fragment {
    TextView tv_nombre, tv_descripcion, tv_horario, tv_info_conductores;
    ImageView imgView_poster;
    Button btn_galeria;
    RecyclerView rv_conductores;
    GridLayoutManager lmanager;

    Segmento segInfo;

    public SegmentoInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_segmento_info, container, false);

        tv_nombre= root.findViewById(R.id.tv_segmento_info_titulo);
        tv_descripcion= root.findViewById(R.id.tv_segmento_descripcion);
        tv_horario= root.findViewById(R.id.tv_segmento_horarios);
        tv_info_conductores= root.findViewById(R.id.tv_info_conductores);
        imgView_poster= root.findViewById(R.id.iv_poster_segmento);
        btn_galeria= root.findViewById(R.id.btn_galeria);
        rv_conductores= root.findViewById(R.id.rv_conductores);

        segInfo= getArguments().getParcelable("segmento");

        tv_nombre.setText(segInfo.getNombre());
        tv_descripcion.setText(segInfo.getDescripcion());

        Picasso.with(getContext())
                .load(segInfo.getImagen())
                .fit()
                //.resize(200,50)
                .centerInside()
                .into(imgView_poster);

        Horario[] horarios= segInfo.getHorarios();
        String horarioString="";
        for(Horario h: horarios){
            horarioString+= String.format("%s de %s a %s \n",
                    h.getDia(),
                    h.getFecha_inicio().substring(0,h.getFecha_inicio().length() - 3),
                    h.getFecha_fin().substring(0,h.getFecha_fin().length() -3)
            );
        }

        horarioString = horarioString.substring(0,horarioString.length() - 1);

        tv_horario.setText(horarioString);

        lmanager= new GridLayoutManager(getContext(),2);
        rv_conductores.setLayoutManager(lmanager);

        new RestFetchConductoresTask().execute();

        return root;
    }

    private class RestFetchConductoresTask extends AsyncTask<Void,Void,List<Conductor>>{

        @Override
        protected List<Conductor> doInBackground(Void... voids) {
            return RestServices.consultarLocutores(getContext(),segInfo.getId().intValue());
        }

        @Override
        protected void onPostExecute(List<Conductor> lista_conductores) {
            if(lista_conductores == null || lista_conductores.size() == 0){
                tv_info_conductores.setText("No hay informacion disponible por el momento");
                tv_info_conductores.setVisibility(View.VISIBLE);
                return;
            }

            ConductoresAdapter adapter= new ConductoresAdapter(getContext(),lista_conductores,segInfo.getNombre());
            rv_conductores.setAdapter(adapter);
            rv_conductores.getAdapter().notifyDataSetChanged();
            lmanager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int i) {
                    if(rv_conductores.getAdapter().getItemCount() % 2 != 0 && i == rv_conductores.getAdapter().getItemCount() - 1){
                        return lmanager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

}
