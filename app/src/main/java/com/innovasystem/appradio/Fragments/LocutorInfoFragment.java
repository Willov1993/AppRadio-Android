package com.innovasystem.appradio.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovasystem.appradio.Activities.HomeActivity;
import com.innovasystem.appradio.Classes.Models.Conductor;
import com.innovasystem.appradio.Classes.Models.RedSocialEmisora;
import com.innovasystem.appradio.Classes.Models.RedSocialPersona;
import com.innovasystem.appradio.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocutorInfoFragment extends Fragment {
    TextView tv_nombre_locutor, tv_nombre_emisora, tv_biografia, tv_fecha_nac, tv_hobbies, tv_apodos;
    ImageView img_locutor;
    LinearLayout layoutManager;

    public LocutorInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_locutor_info, container, false);

        tv_nombre_locutor= root.findViewById(R.id.tv_locutor_info_titulo);
        tv_nombre_emisora= root.findViewById(R.id.tv_locutor_emisora);
        tv_biografia= root.findViewById(R.id.tv_locutor_biografia);
        tv_fecha_nac= root.findViewById(R.id.tv_locutor_fecha);
        tv_hobbies= root.findViewById(R.id.tv_locutor_hobbies);
        tv_apodos= root.findViewById(R.id.tv_locutor_apodo);
        img_locutor= root.findViewById(R.id.iv_imagen_locutor);
        layoutManager= root.findViewById(R.id.linlayout_locutor_info);

        Conductor loc= getArguments().getParcelable("locutor");
        tv_nombre_locutor.setText(loc.getFirst_name() + " " + loc.getLast_name());
        tv_nombre_emisora.setText(getArguments().getString("emisora"));
        tv_fecha_nac.setText(loc.getFecha_nac());
        tv_biografia.setText(loc.getBiografia() != null ? loc.getBiografia() : "Ninguna");
        tv_hobbies.setText(loc.getHobbies() != null ? loc.getHobbies() : "Ninguno");
        tv_apodos.setText(loc.getApodo() != null ? loc.getApodo() : "Ninguno");
        if(loc.getImagen() != null) {
            Picasso.with(getContext())
                    .load(loc.getImagen())
                    .fit()
                    .into(img_locutor);
        }
        

        return root;
    }

}
