package com.innovasystem.appradio.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import com.innovasystem.appradio.Classes.Adapters.EmisorasAdapter;
import com.innovasystem.appradio.Classes.Models.Emisora;
import com.innovasystem.appradio.Classes.RestServices;
import com.innovasystem.appradio.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmisorasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmisorasFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    RecyclerView rv_emisoras;
    GridLayoutManager lmanager;
    ProgressBar progressBar;


    public EmisorasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmisorasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmisorasFragment newInstance(String param1, String param2) {
        EmisorasFragment fragment = new EmisorasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root= inflater.inflate(R.layout.fragment_emisoras, container, false);

        rv_emisoras= root.findViewById(R.id.rv_emisoras);
        rv_emisoras.setHasFixedSize(true);
        lmanager= new GridLayoutManager(getContext(),2);
        rv_emisoras.setLayoutManager(lmanager);
        progressBar= root.findViewById(R.id.progressBar_emisoras);

        new RestFetchEmisoraTask().execute();

        return root;
    }


    private class RestFetchEmisoraTask extends AsyncTask<Void,Void,List<Object>> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Object> doInBackground(Void... voids) {
            List<Emisora> lista_emisoras= RestServices.consultarEmisoras(getContext(),null);
            if(lista_emisoras==null){
                return null;
            }

            Map<String,List<Emisora>> mapa_emisoras_provincia=new TreeMap<>();
            for(Emisora em: lista_emisoras){
                if(mapa_emisoras_provincia.containsKey(em.getProvincia())){
                    mapa_emisoras_provincia.get(em.getProvincia()).add(em);
                }
                else{
                    mapa_emisoras_provincia.put(em.getProvincia(),new ArrayList<>());
                    mapa_emisoras_provincia.get(em.getProvincia()).add(em);
                }
            }

            ArrayList lista_emisoras_provincias= new ArrayList();
            for(String prov: mapa_emisoras_provincia.keySet()){
                lista_emisoras_provincias.add(prov);
                for(Emisora em: mapa_emisoras_provincia.get(prov)){
                    lista_emisoras_provincias.add(em);
                }
            }

            return lista_emisoras_provincias;
        }

        @Override
        protected void onPostExecute(List<Object> listaEmisoras){
            progressBar.setVisibility(View.GONE);
            if(listaEmisoras == null){
                Toast.makeText(getContext(), "Ocurrio un error con el servidor, intente mas tarde", Toast.LENGTH_SHORT).show();
                return;
            }

            /* Codigo para datos de prueba*/

            //listaEmisoras= Utils.generarEmisorasPrueba();

            /**/

            for(Object e: listaEmisoras){
                Log.i("Emisora: ",e.toString());
            }



            EmisorasAdapter adapter= new EmisorasAdapter(listaEmisoras,getContext());
            rv_emisoras.setAdapter(adapter);
            rv_emisoras.getAdapter().notifyDataSetChanged();
            lmanager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int i) {
                    return adapter.isTitle(adapter.emisoras_dataset.get(i)) ? lmanager.getSpanCount() : 1;
                }
            });


        }
    }
}
