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
        //Creacion de botones de redes sociales
        if(loc.getRedesSociales()!= null){
            LinearLayout lcontainer;
            Button button;
            ImageView imgView;

            for(RedSocialPersona red: loc.getRedesSociales()){
                if(red.getNombre().equalsIgnoreCase("Facebook")){
                    imgView= new ImageView(getContext());
                    Picasso.with(getContext()).load(R.drawable.facebook).resize(90,100).into(imgView);

                }
                else if(red.getNombre().equalsIgnoreCase("Twitter")){
                    imgView= new ImageView(getContext());
                    Picasso.with(getContext()).load(R.drawable.twitter_icon).resize(90,100).into(imgView);
                }
                else if(red.getNombre().equalsIgnoreCase("Youtube")){
                    imgView= new ImageView(getContext());
                    Picasso.with(getContext()).load(R.drawable.youtube_icon).resize(90,100).into(imgView);
                }
                else{
                    imgView= new ImageView(getContext());
                    Picasso.with(getContext()).load(R.drawable.social_media_icon).centerInside().into(imgView);
                }
                LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,0.05f);
                layoutParams.setMarginStart(30);
                layoutParams.setMarginEnd(60);
                layoutParams.gravity= Gravity.CENTER;
                imgView.setLayoutParams(layoutParams);

                SpannableString username;
                if(red.getLink().matches("(.*)\\.com/(\\w|\\s|\\d)+")){
                    int index= red.getLink().indexOf("com/");
                    username= new SpannableString(red.getLink().substring(index+4,red.getLink().length() ) );
                }
                else{
                    username= new SpannableString(red.getLink());
                }

                username.setSpan(new UnderlineSpan(), 0,username.length(),0);
                button=new Button(getContext());
                button.setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
                button.setText(username);
                button.setTextSize(16);
                button.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                button.setAllCaps(false);
                button.setTextColor(getContext().getResources().getColor(R.color.text_color));
                button.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,0.95f));
                button.setOnClickListener((View v)->{
                    WebFragment fragment= new WebFragment();
                    Bundle args= new Bundle();
                    args.putString("url",red.getLink());
                    fragment.setArguments(args);
                    ((HomeActivity) getContext()).changeFragment(fragment,R.id.frame_container,true);
                });

                lcontainer= new LinearLayout(getContext());
                lcontainer.addView(imgView);
                lcontainer.addView(button);

                layoutManager.addView(lcontainer);


            }
        }

        return root;
    }

}
