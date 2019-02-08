package com.innovasystem.appradio.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.innovasystem.appradio.Classes.Models.Noticia;
import com.innovasystem.appradio.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class NoticiaArrayAdapter extends ArrayAdapter<Noticia> {
    private final Context context;
    private final ArrayList<Noticia> noticias;
    private final int layoutLoad;


    public NoticiaArrayAdapter(Context context,int layoutLoad, ArrayList<Noticia> values) {
        super(context, layoutLoad, values);
        this.context = context;
        this.noticias = values;
        this.layoutLoad = layoutLoad;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(this.layoutLoad, parent, false);
        TextView firstline = (TextView) rowView.findViewById(R.id.firstLine);
        TextView secondline = (TextView) rowView.findViewById(R.id.secondLine);
        TextView thirdline = (TextView) rowView.findViewById(R.id.thirdLine);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        try{
            Picasso.with(getContext()).load(noticias.get(position).getFoto()).into(imageView);
        }catch(Exception e){
            Picasso.with(getContext()).load("https://moonvillageassociation.org/wp-content/uploads/2018/06/default-profile-picture1-744x744.jpg").into(imageView);
        }


        firstline.setText(noticias.get(position).getUsuario());
        secondline.setText(noticias.get(position).getMensaje());


        String pattern = "dd MMMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);



        thirdline.setText(simpleDateFormat.format(noticias.get(position).getFecha_creacion()));
        return rowView;
    }

    public Noticia getMap(int position){
        return noticias.get(position);
    }
}
