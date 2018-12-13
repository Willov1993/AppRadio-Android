package com.innovasystem.appradio.Fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.innovasystem.appradio.Classes.Adapters.EmisoraHomeAdapter;
import com.innovasystem.appradio.Classes.Adapters.ProgramacionAdapter;
import com.innovasystem.appradio.Classes.Models.Emisora;
import com.innovasystem.appradio.Classes.Models.Horario;
import com.innovasystem.appradio.Classes.Models.Segmento;
import com.innovasystem.appradio.Classes.RestServices;
import com.innovasystem.appradio.R;
import com.innovasystem.appradio.Services.RadioStreamService;
import com.innovasystem.appradio.Utils.NotificationManagement;
import com.innovasystem.appradio.Utils.Utils;
import com.wefika.horizontalpicker.HorizontalPicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
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

    //Variables de control
    private String streamingActual;

    //Layout views
    private RecyclerView rv_home;
    private HorizontalPicker ciudad_picker;
    private ListView listview_programacion;
    private ProgressBar progress_emisoras,progress_programacion;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
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
        View root= inflater.inflate(R.layout.fragment_home, container, false);

        rv_home= root.findViewById(R.id.rv_home);
        ciudad_picker= root.findViewById(R.id.ciudad_picker);
        listview_programacion= root.findViewById(R.id.listv_programas);
        progress_emisoras= root.findViewById(R.id.progressBar_emisoras);
        progress_programacion= root.findViewById(R.id.progressBar_programacion);

        //Inicializacion de la cinta de ciudades
        String[] ciudades= getResources().getStringArray(R.array.ciudades);
        ciudad_picker.setValues(ciudades);

        //Inicializacion de recyclerview para las tarjetas
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
        SnapHelper snapHelper= new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rv_home);

        //Inicializacion de la tabla de programacion de emisoras


        new RestFetchEmisoraHomeTask().execute();
        new RestFetchProgramacionTask().execute();

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
        protected void onPreExecute() {
            progress_emisoras.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            System.out.println("EXTRAYENDO DATOS");
            emisoras= RestServices.consultarEmisoras(getContext());
            segmentos= RestServices.consultarSegmentosDelDia(getContext());
            return null;
        }



        @Override
        protected void onPostExecute(Void aVoid) {
            progress_emisoras.setVisibility(View.GONE);
            System.out.println("INICIALIZANDO DATASET!!");
            if (emisoras == null || segmentos == null) {
                Toast.makeText(getContext(), "Ocurrio un error con el servidor, intente mas tarde", Toast.LENGTH_SHORT).show();
                return;
            }

            System.out.println("DATASETS: " + emisoras + "\n" + segmentos);
            /*codigo para hacer pruebas con emisoras y segmentos de prueba*/

            /*
            emisoras=Utils.generarEmisorasPrueba();
            segmentos= new ArrayList<>();
            for (int i = 0; i < emisoras.size(); i++) {
                segmentos.addAll(Utils.generarSegmentosPrueba(emisoras.get(i).getId()));
            }
            */


            Map<Emisora, Segmento> mapa_emisoras = new TreeMap<>();

            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
            String horaActual = timeFormatter.format(currentTime.getTime());

            for (int i = 0; i < emisoras.size(); i++) {
                Emisora emisora = emisoras.get(i);
                for (int j = 0; j < segmentos.size(); j++) {
                    Segmento segmento = segmentos.get(j);
                    if (segmento.getIdEmisora()!=null && segmento.getIdEmisora().equals(emisora.getId())) {
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

    /**
     * Clase Asincrona para extraer La info de La programacion del dia
     */
    private class RestFetchProgramacionTask extends AsyncTask<Void,Void,List<Segmento>>{

        @Override
        protected void onPreExecute() {
            progress_programacion.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Segmento> doInBackground(Void... voids) {
            return RestServices.consultarSegmentosDelDia(getContext());
        }

        @Override
        protected void onPostExecute(List<Segmento> listaSegmentos) {
            progress_programacion.setVisibility(View.GONE);
            System.out.println("IMPRIMIENDO RESULTADO_______");
            System.out.println(Arrays.toString(listaSegmentos.toArray()));
            if(listaSegmentos == null){
                Toast.makeText(getContext(), "Ocurrio un error con el servidor, intente mas tarde", Toast.LENGTH_SHORT).show();
                return;
            }


            /*
            //CODIGO DE PRUEBA
            List<Emisora> listaEmisoras= Utils.generarEmisorasPrueba();
            listaSegmentos.clear();
            for(Emisora e: listaEmisoras){
                listaSegmentos.addAll(Utils.generarSegmentosPrueba(e));
            }
            Collections.shuffle(listaSegmentos);
            System.out.println("LISta PRUEBA: "+listaSegmentos);
            */

            Map<Horario,Segmento> mapa_segmentos=new TreeMap<>();
            for (int i = 0; i < listaSegmentos.size(); i++) {
                Segmento segmento =  listaSegmentos.get(i);
                for (int j = 0; j < segmento.getHorarios().length; j++) {
                    Horario h = segmento.getHorarios()[j];
                    mapa_segmentos.put(h,segmento);
                }
            }

            ProgramacionAdapter adapter= new ProgramacionAdapter(getContext(),mapa_segmentos);
            listview_programacion.setAdapter(adapter);
            /*
            for (Horario h: mapa_segmentos.keySet()) {
                Segmento seg= mapa_segmentos.get(h);
                TableRow filaPrograma= new TableRow(getContext());
                filaPrograma.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                LinearLayout infoContainer= new LinearLayout(getContext());

                TextView tv_horario=Utils.crearTextViewPersonalizado(getContext(),14,getContext().getResources().getColor(R.color.text_color),
                        String.format("%s - %s",h.getFecha_inicio(),h.getFecha_fin() ));
                TextView tv_programa= Utils.crearTextViewPersonalizado(getContext(),14,getContext().getResources().getColor(R.color.text_color),seg.getNombre());
                TextView tv_emisora= Utils.crearTextViewPersonalizado(getContext(),14,getContext().getResources().getColor(R.color.text_color),seg.getEmisora().getNombre());
                infoContainer.addView(tv_horario);
                infoContainer.addView(tv_programa);
                infoContainer.addView(tv_emisora);
                filaPrograma.addView(infoContainer);
                tabla_programacion.addView(filaPrograma);
            }*/

        }

    }
}
