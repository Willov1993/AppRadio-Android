package com.innovasystem.appradio.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.innovasystem.appradio.R;
import com.innovasystem.appradio.Services.RadioStreamService;
import com.innovasystem.appradio.Utils.Utils;

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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    /* Variables de referencia de las views de la ventana */
    TextView tv_emisora,tv_segmento;
    Button btn_reproducir,btn_volumen;
    RecyclerView rv_segmentos;
    AdView adView_segmento;

    /*variables de control de reproduccion */
    private boolean playing= false;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SegmentosFragment() {
        // Required empty public constructor
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
        //tv_emisora= root.findViewById(R.id.tv_emisora);
        //tv_segmento= root.findViewById(R.id.tv_segmento);
        btn_reproducir= root.findViewById(R.id.btn_play_segmento);

        btn_reproducir.setOnClickListener(btnReproducirListener);


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
                    startMediaPlayer("http://str.ecuastreaming.com:9958/");
                    playing = true;
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
}
