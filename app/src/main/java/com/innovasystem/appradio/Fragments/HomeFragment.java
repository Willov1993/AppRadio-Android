package com.innovasystem.appradio.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.innovasystem.appradio.Classes.Adapters.EmisoraHomeAdapter;
import com.innovasystem.appradio.Classes.Adapters.ProgramacionAdapter;
import com.innovasystem.appradio.Classes.Models.Emisora;
import com.innovasystem.appradio.Classes.Models.Fecha;
import com.innovasystem.appradio.Classes.Models.Horario;
import com.innovasystem.appradio.Classes.Models.Segmento;
import com.innovasystem.appradio.Classes.RestServices;
import com.innovasystem.appradio.Classes.SessionConfig;
import com.innovasystem.appradio.R;
import com.innovasystem.appradio.Services.RadioStreamService;
import com.innovasystem.appradio.Utils.NotificationManagement;
import com.innovasystem.appradio.Utils.Utils;
import com.wefika.horizontalpicker.HorizontalPicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    //Variables de control
    private String streamingActual;
    private static boolean radion_on=true;
    private static boolean muted= false;
    String[] ciudades;
    String provinciaActual="";
    Snackbar snackMessage;

    //Layout views
    private RecyclerView rv_home;
    private HorizontalPicker ciudad_picker;
    private ListView listview_programacion;
    private ProgressBar progress_emisoras, progress_programacion;
    private ImageButton btn_apagar, btn_silenciar;

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
        btn_apagar= root.findViewById(R.id.btn_apagar);
        btn_silenciar= root.findViewById(R.id.btn_mute);

        //Inicializacion de la cinta de ciudades
        ciudades= getResources().getStringArray(R.array.ciudades);
        ciudad_picker.setValues(ciudades);
        ciudad_picker.setOnItemSelectedListener(new HorizontalPicker.OnItemSelected(){
            @Override
            public void onItemSelected(int index) {
                Toast.makeText(getContext(), ciudad_picker.getValues()[index], Toast.LENGTH_SHORT).show();
                SessionConfig.getSessionConfig(getContext()).provincia= ciudades[index];
                SharedPreferences preferences= getContext().getSharedPreferences("session", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("provincia",ciudades[index]);
                editor.apply();
                editor.commit();
                new RestFetchEmisoraHomeTask().execute();
                new RestFetchProgramacionTask().execute();
            }
        });
        int indiceProvincia= Arrays.binarySearch(ciudades, SessionConfig.getSessionConfig(getContext()).provincia);
        if(indiceProvincia>=0) {
            ciudad_picker.setSelectedItem(indiceProvincia);
            provinciaActual= ciudades[indiceProvincia];
        }
        else{
            ciudad_picker.setSelectedItem(0);
            SessionConfig.getSessionConfig(getContext()).provincia= ciudades[0];
            provinciaActual= ciudades[0];
        }

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
                    if(!streamingActual.equals(RadioStreamService.radioURL) && radion_on && !muted) {
                        Intent intent = new Intent();
                        intent.setAction(RadioStreamService.BROADCAST_TO_SERVICE);
                        intent.putExtra(RadioStreamService.PLAYER_FUNCTION_TYPE, RadioStreamService.CHANGE_PLAYER_TRACK);
                        intent.putExtra(RadioStreamService.PLAYER_TRACK_URL, streamingActual);
                        getActivity().sendBroadcast(intent);
                        Utils.mostrarMensajeSnackBar(getView(),"Conectando al servidor de la emisora....");
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

        //Inicializacion de listeners de botones
        btn_apagar.setOnClickListener(btn_apagar_listener);
        btn_silenciar.setOnClickListener(btn_mute_listener);

        if(radion_on){
            btn_apagar.setBackground(getContext().getDrawable(R.drawable.round_button_enabled));
        }

        if(muted){
            btn_silenciar.setBackground(getContext().getDrawable(R.drawable.round_button_enabled));
        }

        //Extraccion de datos de emisoras y programacion
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



    /*---------- Listeners ---------*/

    /**
     * Listener para el boton de Apagar, Detiene por completo el streaming de la emisora
     */
    private final View.OnClickListener btn_apagar_listener= new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            if(!radion_on){
                System.out.println("Start Playing!!!!!");
                if(Utils.isNetworkAvailable(getContext())) {
                    btn_apagar.setBackground(getContext().getDrawable(R.drawable.round_button_enabled));
                    radion_on = true;
                    new StartStreamingTask().execute();
                    if(muted){
                        btn_silenciar.setBackground(getContext().getDrawable(R.drawable.round_button));
                        muted= false;
                    }
                }
                else{
                    Toast.makeText(getContext(), "No se puede reproducir la emisora debido a que no tiene internet," +
                            "revise su conexion", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                btn_apagar.setBackground(getContext().getDrawable(R.drawable.round_button));
                Intent intent = new Intent();
                intent.setAction(RadioStreamService.BROADCAST_TO_SERVICE);
                intent.putExtra(RadioStreamService.PLAYER_FUNCTION_TYPE, RadioStreamService.STOP_MEDIA_PLAYER);
                getActivity().sendBroadcast(intent);
                radion_on =false;
            }
        }
    };

    private final View.OnClickListener btn_mute_listener= new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            if(!muted){
                btn_silenciar.setBackground(getContext().getDrawable(R.drawable.round_button_enabled));
                Intent intent = new Intent();
                intent.setAction(RadioStreamService.BROADCAST_TO_SERVICE);
                intent.putExtra(RadioStreamService.PLAYER_FUNCTION_TYPE, RadioStreamService.PAUSE_MEDIA_PLAYER);
                getActivity().sendBroadcast(intent);
                muted=true;
            }
            else{
                btn_silenciar.setBackground(getContext().getDrawable(R.drawable.round_button));
                Intent intent = new Intent();

                if(RadioStreamService.radioURL.equals(streamingActual)) {
                    intent.setAction(RadioStreamService.BROADCAST_TO_SERVICE);
                    intent.putExtra(RadioStreamService.PLAYER_FUNCTION_TYPE, RadioStreamService.RESUME_MEDIA_PLAYER);
                }
                else{
                    intent.setAction(RadioStreamService.BROADCAST_TO_SERVICE);
                    intent.putExtra(RadioStreamService.PLAYER_FUNCTION_TYPE, RadioStreamService.CHANGE_PLAYER_TRACK);
                    intent.putExtra(RadioStreamService.PLAYER_TRACK_URL, streamingActual);
                    Utils.mostrarMensajeSnackBar(getView(),"Conectando al servidor de la emisora....");
                }
                getActivity().sendBroadcast(intent);
                muted=false;
            }
        }
    };


    /*---------- Tasks -------------*/

    /**
     * Esta clase asincrona obtiene los datos de emisoras para presentarlos en la pantalla principal
     * como tarjetas
     */
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
            emisoras= RestServices.consultarEmisoras(getContext(),SessionConfig.getSessionConfig(getContext()).provincia);
            segmentos= RestServices.consultarSegmentosDelDia(getContext(),SessionConfig.getSessionConfig(getContext()).provincia);
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
            else if(emisoras.size() == 0){
                Toast.makeText(getContext(), "No hay informacion para presentar o ocurrio un error de conexion!", Toast.LENGTH_SHORT).show();
            }

            System.out.println("DATASETS: " + emisoras + "\n" + segmentos);


            /*
            //codigo para hacer pruebas con emisoras y segmentos de prueba
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


            EmisoraHomeAdapter adapter = new EmisoraHomeAdapter(mapa_emisoras, getContext());
            rv_home.setAdapter(adapter);

            Log.i("RV ITEMS: ", "" + mapa_emisoras.size());

            /*Se busca la posicion de la url en el adapter para hacer que el recyclerview se dirija
            * a la posicion de la emisora en reproduccion actualmente
            */
            if(adapter.getItemCount() != 0) {
                if (!RadioStreamService.radioURL.equals("")) {
                    for (int i = 0; i < adapter.emisoras_keys.size(); i++) {
                        if (adapter.emisoras_keys.get(i).getUrl_streaming().equals(RadioStreamService.radioURL)) {
                            rv_home.scrollToPosition(i);
                            streamingActual = adapter.emisoras_keys.get(i).getUrl_streaming();
                        }
                    }

                } else if (!RadioStreamService.radioURL.equals(adapter.emisoras_keys.get(0).getUrl_streaming())) {
                    streamingActual = adapter.emisoras_keys.get(0).getUrl_streaming();
                    if (radion_on && !muted) {
                        new StartStreamingTask().execute();
                    }
                }

                //Verificar si se ha cambiado de provincia
                if (!provinciaActual.equals(SessionConfig.getSessionConfig(getContext()).provincia)) {
                    System.out.println("****** LA PROVINCIA HA CAMBIADO *******!!!!");
                    streamingActual = adapter.emisoras_keys.get(0).getUrl_streaming();
                    if (radion_on && !muted) {
                        Intent intent = new Intent();
                        intent.setAction(RadioStreamService.BROADCAST_TO_SERVICE);
                        intent.putExtra(RadioStreamService.PLAYER_FUNCTION_TYPE, RadioStreamService.CHANGE_PLAYER_TRACK);
                        intent.putExtra(RadioStreamService.PLAYER_TRACK_URL, streamingActual);
                        getActivity().sendBroadcast(intent);
                       Utils.mostrarMensajeSnackBar(getView(),"Conectando al servidor de la emisora....");
                    }
                }
            }

            provinciaActual= SessionConfig.getSessionConfig(getContext()).provincia;

        }
    }

    /**
     * Esta clase interna realiza el trabajo de detectar la conexion a internet
     * del telefono, si hay una conexion activa, entonces inicia el reproductor
     */
    private class StartStreamingTask extends AsyncTask<Object,Object,Boolean>{

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
                Utils.mostrarMensajeSnackBar(getView(),"Conectando al servidor de la emisora....");
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
     * Clase Asincrona para extraer La info de La programacion del dia y presentarla en una ListView
     */
    private class RestFetchProgramacionTask extends AsyncTask<Void,Void,List<Segmento>>{
        Fecha horaActual;

        @Override
        protected void onPreExecute() {
            progress_programacion.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Segmento> doInBackground(Void... voids) {
            horaActual= RestServices.consultarHoraActual(getContext());
            return RestServices.consultarSegmentosDelDia(getContext(),SessionConfig.getSessionConfig(getContext()).provincia);
        }

        @Override
        protected void onPostExecute(List<Segmento> listaSegmentos) {
            progress_programacion.setVisibility(View.GONE);
            System.out.println("IMPRIMIENDO RESULTADO_______");
            System.out.println(Arrays.toString(listaSegmentos.toArray()));
            System.out.println("FECHA ACTUAL: " + horaActual);
            if(listaSegmentos == null){
                if(getContext() != null)
                    Toast.makeText(getContext(), "Ocurrio un error con el servidor, intente mas tarde", Toast.LENGTH_SHORT).show();
                return;
            }
            else if(listaSegmentos.size()==0){
                if(getContext() != null)
                    Toast.makeText(getContext(), "No hay programacion para presentar o ocurrio algun error!", Toast.LENGTH_SHORT).show();
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

            Map<Horario,Set<Segmento>> mapa_segmentos=new TreeMap<>();
            for (int i = 0; i < listaSegmentos.size(); i++) {
                Segmento segmento =  listaSegmentos.get(i);
                for (int j = 0; j < segmento.getHorarios().length; j++) {
                    Horario h = segmento.getHorarios()[j];
                    if(mapa_segmentos.containsKey(h)){
                        mapa_segmentos.get(h).add(segmento);
                    }
                    else {
                        mapa_segmentos.put(h, new HashSet<>());
                        mapa_segmentos.get(h).add(segmento);
                    }
                }
            }

            ArrayList<Horario> horarios= new ArrayList<>();
            ArrayList<Segmento> segmentos= new ArrayList<>();

            for(Horario h: mapa_segmentos.keySet()){
                for(Segmento seg: mapa_segmentos.get(h)){
                    horarios.add(h);
                    segmentos.add(seg);
                }
            }

            ProgramacionAdapter adapter= new ProgramacionAdapter(getContext(),horarios,segmentos,horaActual);
            listview_programacion.setAdapter(adapter);

        }

    }
}
