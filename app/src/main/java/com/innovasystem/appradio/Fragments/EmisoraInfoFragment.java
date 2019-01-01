package com.innovasystem.appradio.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.innovasystem.appradio.Classes.Models.Emisora;
import com.innovasystem.appradio.R;

import com.uncopt.android.widget.text.justify.JustifiedTextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmisoraInfoFragment extends Fragment {


    JustifiedTextView tv_descripcion_emisora;
    TextView tv_web_emisora,tv_telef_emisora,tv_ubicacion_emisora;
    Emisora emisora;

    public EmisoraInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_emisora_info, container, false);
        tv_descripcion_emisora= root.findViewById(R.id.tv_descripcion_emisora);
        tv_web_emisora= root.findViewById(R.id.tv_web_emisora);
        tv_telef_emisora= root.findViewById(R.id.tv_telef_emisora);
        tv_ubicacion_emisora= root.findViewById(R.id.tv_ubicacion_emisora);

        emisora= EmisoraContentFragment.emisora;

        tv_descripcion_emisora.setText(emisora.getDescripcion());
        tv_web_emisora.setText(emisora.getSitio_web());
        tv_telef_emisora.setText("1234567890 - 11122233345");
        tv_ubicacion_emisora.setText(emisora.getDireccion());

        return root;
    }

}
