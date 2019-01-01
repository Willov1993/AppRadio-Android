package com.innovasystem.appradio.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.innovasystem.appradio.Classes.Models.Emisora;
import com.innovasystem.appradio.R;

/**
 *
 */
public class EmisoraContentFragment extends Fragment {

    TextView tv_nombre_emisora,tv_ciudad_frecuencia;

    Emisora emisora;

    public EmisoraContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_emisora_content, container, false);

        tv_nombre_emisora= root.findViewById(R.id.tv_econtent_nombre);
        tv_ciudad_frecuencia= root.findViewById(R.id.tv_econtent_frecuencia);

        emisora= getArguments().getParcelable("emisora");

        tv_nombre_emisora.setText(emisora.getNombre());
        tv_ciudad_frecuencia.setText(emisora.getCiudad() + " • " + emisora.getFrecuencia_dial());


        return root;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
