package com.innovasystem.appradio.Fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.innovasystem.appradio.Activities.HomeActivity;
import com.innovasystem.appradio.Classes.Adapters.NoticiaArrayAdapter;
import com.innovasystem.appradio.Classes.Authenticated;
import com.innovasystem.appradio.Classes.Models.Emisora;
import com.innovasystem.appradio.Classes.Models.Noticia;
import com.innovasystem.appradio.Classes.Models.RedSocialEmisora;
import com.innovasystem.appradio.Classes.Models.TelefonoEmisora;
import com.innovasystem.appradio.Classes.RestServices;
import com.innovasystem.appradio.R;
import com.squareup.picasso.Picasso;

import com.google.gson.Gson;






import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import static com.facebook.FacebookSdk.getApplicationContext;


public class NoticiasFragment extends Fragment {
    final static String ScreenName = "luibasantes";
    ArrayList<String> al_text;
    Emisora emisora;
    //WebView webview;
    ProgressBar progressBar;
    ListView listViewTweets;
    ArrayList<Noticia> values;
    List<RedSocialEmisora> redesTwitter;


    public NoticiasFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_noticias, container, false);

        emisora = EmisoraContentFragment.emisora;

        listViewTweets = (ListView) root.findViewById(R.id.listViewTweets);


        progressBar= root.findViewById(R.id.progressBarNoticias);
        progressBar.setMax(100);
        progressBar.setProgress(0);
        /*al_text = new ArrayList<>();

        webview = (WebView) root.findViewById(R.id.webView_twitter);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.setWebViewClient(new MyWebClient());
        webview.setWebChromeClient(new MyWebChromeClient());
        webview.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View view, int code, KeyEvent keyEvent) {
                if(code == KeyEvent.KEYCODE_BACK && webview.canGoBack()){
                    webview.goBack();
                    return true;
                }
                return false;
            }
        });*/

        //webView.loadDataWithBaseURL("https://facebook.com", embedFacebook, "text/html", "UTF-8", null);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new RestFetchEmisoraRedesTask().execute();
        getTweets();
    }

    private class RestFetchEmisoraRedesTask extends AsyncTask<Void,Void,Void> {
        List<RedSocialEmisora> redesG;
        //List<RedSocialEmisora> redesTwitter;

        @Override
        protected Void doInBackground(Void... voids) {
            redesG= RestServices.consultarRedesEmisora(getContext(),emisora.getId().intValue());
            redesTwitter = new ArrayList<>();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(redesG!= null){
                LinearLayout container;
                Button button;
                ImageView imgView;

                for(RedSocialEmisora red: redesG) {

                    if (red.getNombre().equalsIgnoreCase("Twitter")) {
                        redesTwitter.add(red);
                        imgView = new ImageView(getContext());
                        Picasso.with(getContext()).load(R.drawable.twitter_icon).resize(90, 100).into(imgView);
                    }
                }
                for(RedSocialEmisora red: redesTwitter) {

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

                    container= new LinearLayout(getContext());
                    //container.addView(imgView);
                    container.addView(button);


                }
                if(redesTwitter.size()>0){
                    //webview.loadUrl(redesTwitter.get(0).getLink());
                    getTweets();
                    progressBar.setVisibility(View.GONE);
                }
            }else{
                Toast.makeText(getContext(),"No tiene vinculada pagina de noticias",Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class MyWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            progressBar.setProgress(100);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
            super.onPageStarted(view, url, favicon);
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressBar.setProgress(newProgress);
        }
    }


    public void getTweets(){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("Uf8c1losbkgo8kGEVJTYRsNEN")
                .setOAuthConsumerSecret("u85MCqWwFf79GvXAWkVlSXRv1A0n5LY3R58kAQ48c111iZdMxP")
                .setOAuthAccessToken("141818178-OHqJV0c8pURsBUHSj7QO4mgUtmVoQPHWQNE0i3WR")
                .setOAuthAccessTokenSecret("f3dF90pLFxsax29yPwsarV7cGLFMCsnVs3tsWru5LnFJH");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();



        try {
            List<Status> statuses;


            values = new ArrayList<>();
            String user = "";
            RedSocialEmisora red = redesTwitter.get(0);
            if(red.getLink().matches("(.*)\\.com/(\\w|\\s|\\d)+")){
                int index= red.getLink().indexOf("com/");
                user= red.getLink().substring(index+4,red.getLink().length());
            }

            statuses = twitter.getUserTimeline(user);
            System.out.println("Showing @" + user + "'s user timeline.");
            for (Status status : statuses) {
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
                values.add(new Noticia("@" + status.getUser().getScreenName(),status.getText(),status.getUser().getBiggerProfileImageURLHttps(),status.getCreatedAt()));
            }


        } catch (TwitterException te) {
            values.add(new Noticia("No se pudo cargar las noticias","","",new Date()));
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());

        }
        catch (Exception e){
            values.add(new Noticia("No se pudo cargar las noticias","","",new Date()));
            System.out.println(e.toString());
        }
        NoticiaArrayAdapter adapter = new NoticiaArrayAdapter (getContext(),R.layout.list_view_item,values);
        listViewTweets.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
    }
}
