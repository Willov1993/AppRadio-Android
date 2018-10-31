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
import com.innovasystem.appradio.Clases.Adapters.SegmentosAdapter;
import com.innovasystem.appradio.Clases.Models.Emisora;
import com.innovasystem.appradio.Clases.Models.Horario;
import com.innovasystem.appradio.Clases.Models.Segmento;
import com.innovasystem.appradio.Clases.RestServices;
import com.innovasystem.appradio.R;
import com.innovasystem.appradio.Services.RadioStreamService;
import com.innovasystem.appradio.Utils.NotificationManagement;
import com.innovasystem.appradio.Utils.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SegmentosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SegmentosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

/*Fragmento que representa la ventana de seleccion de segmentos de la emisora */
public class SegmentosFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    /* Variables de referencia de las views de la ventana */
    TextView tv_emisora,tv_segmento;
    Button btn_reproducir,btn_volumen;
    RecyclerView rv_segmentos;
    AdView adView_segmento;
    Emisora emisora;


    /*variables de control de reproduccion */
    private boolean playing= false;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SegmentosFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SegmentosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SegmentosFragment newInstance(String param1, String param2) {
        SegmentosFragment fragment = new SegmentosFragment();
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
        View root= inflater.inflate(R.layout.fragment_segmentos, container, false);
        tv_emisora= root.findViewById(R.id.tv_emisora);
        tv_segmento= root.findViewById(R.id.tv_segmento);
        btn_reproducir= root.findViewById(R.id.btn_play_segmento);
        rv_segmentos= root.findViewById(R.id.rv_segmentos);
        emisora= getActivity().getIntent().getParcelableExtra("emisora");

        btn_reproducir.setOnClickListener(btnReproducirListener);

        rv_segmentos.setHasFixedSize(true);
        RecyclerView.LayoutManager lmanager= new LinearLayoutManager(getContext());
        rv_segmentos.setLayoutManager(lmanager);

        tv_emisora.setText(emisora.getNombre());


        new RestFetchSegmentoTask().execute();

        return root;


    }


    /* ====== Listeners de botones =======*/

    /**
     * Listener que maneja el evento del boton de reproduccion de emisora en la parte de seleccion
     * de segmentos
     */
    private final View.OnClickListener btnReproducirListener= new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            if(!playing){
                System.out.println("Start Playing!!!!!");
                if(Utils.isNetworkAvailable(getContext())) {
                    btn_reproducir.setBackgroundResource(R.drawable.stop_button);
                    playing = true;
                    new DetectConnectionTask().execute();
                }
                else{
                    Toast.makeText(getContext(), "No se puede reproducir la emisora debido a que no tiene internet," +
                            "revise su conexion", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                btn_reproducir.setBackgroundResource(R.drawable.play_button);
                Intent intent = new Intent();
                intent.setAction(RadioStreamService.BROADCAST_TO_SERVICE);
                intent.putExtra(RadioStreamService.PLAYER_FUNCTION_TYPE, RadioStreamService.STOP_MEDIA_PLAYER);
                getActivity().sendBroadcast(intent);
                playing=false;
            }
        }
    };


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


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    /**
     * Esta clase interna realiza el trabajo de detectar la conexion a internet
     * del telefono, si hay una conexion activa, entonces inicia el reproductor
     */
    private class DetectConnectionTask extends AsyncTask<Object,Object,Boolean>{

        @Override
        protected void onPreExecute() {
            tv_segmento.setText("Espere un momento...");
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Object... objects) {
            return Utils.isActiveInternet(null);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            tv_emisora.setText("Nombre Emisora");
            tv_segmento.setText("Nombre del Segmento");
            if(result == true && playing)
                startMediaPlayer(emisora.getUrl_streaming());
            else{
                NotificationManagement.notificarError("AppRadio - Error de Reproduccion","No se puede conectar al servidor de la radio",getActivity().getApplicationContext());
                btn_reproducir.setBackgroundResource(R.drawable.play_button);
                playing = false;
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
