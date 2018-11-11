package com.innovasystem.appradio.Fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.innovasystem.appradio.Classes.Adapters.EmisoraHomeAdapter;
import com.innovasystem.appradio.Classes.Models.Emisora;
import com.innovasystem.appradio.Classes.Models.Horario;
import com.innovasystem.appradio.Classes.Models.Segmento;
import com.innovasystem.appradio.Classes.RestServices;
import com.innovasystem.appradio.R;
import com.innovasystem.appradio.Services.RadioStreamService;
import com.innovasystem.appradio.Utils.NotificationManagement;
import com.innovasystem.appradio.Utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String streamingActual;

    //Layout views
    private RecyclerView rv_home;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_home, container, false);

        rv_home= root.findViewById(R.id.rv_home);
        //ZoomCenterCardLayoutManager lmanager= new ZoomCenterCardLayoutManager(getContext());
        //lmanager.setOrientation(LinearLayoutManager.HORIZONTAL);
        final CarouselLayoutManager lmanager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL,true);
        lmanager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        rv_home.setLayoutManager(lmanager);
        rv_home.setHasFixedSize(true);
        rv_home.addOnScrollListener(new CenterScrollListener());
        rv_home.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    int itemPos= ((CarouselLayoutManager) recyclerView.getLayoutManager()).getCenterItemPosition();
                    Emisora em=((EmisoraHomeAdapter)recyclerView.getAdapter()).emisoras_keys.get(itemPos);
                    streamingActual= em.getUrl_streaming();
                    if(!streamingActual.equals(RadioStreamService.radioURL)) {
                        Intent intent = new Intent();
                        intent.setAction(RadioStreamService.BROADCAST_TO_SERVICE);
                        intent.putExtra(RadioStreamService.PLAYER_FUNCTION_TYPE, RadioStreamService.CHANGE_PLAYER_TRACK);
                        intent.putExtra(RadioStreamService.PLAYER_TRACK_URL, streamingActual);
                        getActivity().sendBroadcast(intent);
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        new RestFetchEmisoraHomeTask().execute();

        return root;
    }

    /*----Metodos Utilitarios-----*/

    /**
     * Este metodo permite iniciar el reproductor de streaming del servicio RadioStreaming
     * y realizar el proceso de reproduccion
     *
     * @param url La url del servicio de streaming
     * @return void
     */
    public void startMediaPlayer(String url) {
        Intent intent = new Intent();
        intent.setAction(RadioStreamService.BROADCAST_TO_SERVICE);
        intent.putExtra(RadioStreamService.PLAYER_FUNCTION_TYPE, RadioStreamService.PLAY_MEDIA_PLAYER);
        intent.putExtra(RadioStreamService.PLAYER_TRACK_URL, url);
        getActivity().sendBroadcast(intent);
    }


    private class RestFetchEmisoraHomeTask extends AsyncTask<Void,Void,Void> {
        List<Emisora> emisoras;
        List<Segmento> segmentos;


        @Override
        protected Void doInBackground(Void... voids) {
            System.out.println("EXTRAYENDO DATOS");
            emisoras= RestServices.consultarEmisoras(getContext());
            segmentos= RestServices.consultarSegmentosDelDia(getContext());
            return null;
        }



        @Override
        protected void onPostExecute(Void aVoid) {
            System.out.println("INICIALIZANDO DATASET!!");
            if (emisoras == null || segmentos == null) {
                Toast.makeText(getContext(), "Ocurrio un error con el servidor, intente mas tarde", Toast.LENGTH_SHORT).show();
                return;
            }

            /*codigo para hacer pruebas con emisoras y segmentos de prueba*/

            //emisoras=Utils.generarEmisorasPrueba();
            //segmentos= new ArrayList<>();
            //for (int i = 0; i < emisoras.size(); i++) {
            //    segmentos.addAll(Utils.generarSegmentosPrueba(emisoras.get(i).getId()));
            //}
            /**/

            Map<Emisora, Segmento> mapa_emisoras = new TreeMap<>();

            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
            String horaActual = timeFormatter.format(currentTime.getTime());

            for (int i = 0; i < emisoras.size(); i++) {
                Emisora emisora = emisoras.get(i);
                for (int j = 0; j < segmentos.size(); j++) {
                    Segmento segmento = segmentos.get(j);
                    if (segmento.getIdEmisora().equals(emisora.getId())) {
                        Horario h = Utils.compararHorarioActual(horaActual, segmento.getHorarios());
                        if (h != null) {
                            segmento.setHorarios(new Horario[]{h});
                            mapa_emisoras.put(emisora, segmento);
                            break;
                        }
                    }
                }
                if (!mapa_emisoras.containsKey(emisora)) {
                    mapa_emisoras.put(emisora, null);
                }
            }

            Log.i("RV ITEMS: ", "" + mapa_emisoras.size());

            EmisoraHomeAdapter adapter = new EmisoraHomeAdapter(mapa_emisoras, getContext());
            rv_home.setAdapter(adapter);

            if(!RadioStreamService.radioURL.equals("")){
                for (int i = 0; i < adapter.emisoras_keys.size(); i++) {
                    if(adapter.emisoras_keys.get(i).getUrl_streaming().equals(RadioStreamService.radioURL)){
                        rv_home.scrollToPosition(i);
                    }
                }

            }
            else if(!RadioStreamService.radioURL.equals(adapter.emisoras_keys.get(0).getUrl_streaming())){
                streamingActual = adapter.emisoras_keys.get(0).getUrl_streaming();
                new DetectConnectionTask().execute();
            }
        }

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
                if(result) {
                    startMediaPlayer(streamingActual);
                }
                else{
                    NotificationManagement.notificarError("AppRadio - Error de Reproduccion","No se puede conectar al servidor de la radio",getActivity().getApplicationContext());
                    Toast.makeText(getContext(), "No se puede reproducir la emisora debido a que no tiene internet," +
                            "revise su conexion", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
