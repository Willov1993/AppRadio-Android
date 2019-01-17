package com.innovasystem.appradio.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.innovasystem.appradio.Classes.Adapters.SegmentosAdapter;
import com.innovasystem.appradio.Classes.Models.Emisora;
import com.innovasystem.appradio.Classes.Models.Horario;
import com.innovasystem.appradio.Classes.Models.Segmento;
import com.innovasystem.appradio.Classes.RestServices;
import com.innovasystem.appradio.R;
import com.innovasystem.appradio.Services.RadioStreamService;
import com.innovasystem.appradio.Utils.NotificationManagement;
import com.innovasystem.appradio.Utils.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/*Fragmento que representa la ventana de seleccion de segmentos de la emisora */
public class SegmentosFragment extends Fragment {

    /* Variables de referencia de las views de la ventana */
    RecyclerView rv_segmentos;
    Emisora emisora;
    TextView tv_mensaje;



    public SegmentosFragment() {

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
        View root= inflater.inflate(R.layout.fragment_segmentos, container, false);
        rv_segmentos= root.findViewById(R.id.rv_segmentos);
        tv_mensaje= root.findViewById(R.id.tv_mensaje_segmentos);

        rv_segmentos.setHasFixedSize(true);
        RecyclerView.LayoutManager lmanager= new LinearLayoutManager(getContext());
        rv_segmentos.setLayoutManager(lmanager);

        emisora= EmisoraContentFragment.emisora;

        new RestFetchSegmentoTask().execute();

        return root;


    }


    /* ====== Listeners de botones =======*/

    /**
     * Listener que maneja el evento del boton de reproduccion de emisora en la parte de seleccion
     * de segmentos
     */


    /**
     * Esta clase interna realiza el trabajo de detectar la conexion a internet
     * del telefono, si hay una conexion activa, entonces inicia el reproductor
     */
    private class DetectConnectionTask extends AsyncTask<Object,Object,Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Object... objects) {
            return Utils.isActiveInternet(null);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result == true );
                //startMediaPlayer(emisora.getUrl_streaming());
            else{
                //NotificationManagement.notificarError("AppRadio - Error de Reproduccion","No se puede conectar al servidor de la radio",getActivity().getApplicationContext());
                Toast.makeText(getContext(), "No se puede reproducir la emisora debido a que no tiene internet," +
                        "revise su conexion", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Esta clase interna realiza el trabajo de extraer los segmentos de la emisora seleccionada en
     * el dia actual
     */
    private class RestFetchSegmentoTask extends AsyncTask<Void,Void,List<Segmento>>{

        @Override
        protected List<Segmento> doInBackground(Void... voids) {
            return RestServices.consultarSegmentosDelDia(getActivity().getApplicationContext(),emisora.getId().intValue());
        }

        @Override
        protected void onPostExecute(List<Segmento> listaSegmentos){
            System.out.println("IMPRIMIENDO RESULTADO_______");
            System.out.println(Arrays.toString(listaSegmentos.toArray()));
            if(listaSegmentos == null){
                Toast.makeText(getContext(), "Ocurrio un error con el servidor, intente mas tarde", Toast.LENGTH_SHORT).show();
                tv_mensaje.setVisibility(View.VISIBLE);
                return;
            }

            if(listaSegmentos.size() == 0){
                tv_mensaje.setVisibility(View.VISIBLE);
                return;
            }



            Map<Horario,Segmento> mapa_segmentos=new TreeMap<>();
            for (int i = 0; i < listaSegmentos.size(); i++) {
                Segmento segmento =  listaSegmentos.get(i);
                for (int j = 0; j < segmento.getHorarios().length; j++) {
                     Horario h = segmento.getHorarios()[j];
                     mapa_segmentos.put(h,segmento);
                }
            }

            for(Horario hor: mapa_segmentos.keySet()){
                System.out.println("-->horario: " + hor.getFecha_inicio() + " - " + hor.getFecha_fin());
            }

            SegmentosAdapter segmentoAdapter=new SegmentosAdapter(mapa_segmentos,getContext());
            rv_segmentos.setAdapter(segmentoAdapter);
            rv_segmentos.getAdapter().notifyDataSetChanged();

        }
    }
}
