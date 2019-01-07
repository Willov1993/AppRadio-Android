package com.innovasystem.appradio.Fragments;


import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.innovasystem.appradio.Activities.HomeActivity;
import com.innovasystem.appradio.R;

import java.security.Key;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebFragment extends Fragment {

    WebView webView;
    ProgressBar progressBar;

    public WebFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_web, container, false);

        //String embedFacebook = "<iframe src=\"https://www.facebook.com/plugins/page.php?href=https%3A%2F%2Fwww.facebook.com%2FParticipaEcuador&tabs=timeline&small_header=false&adapt_container_width=true&hide_cover=false&show_facepile=false&appId\" width=\"340\" height=\"500\" style=\"border:none;overflow:hidden\" scrolling=\"no\" frameborder=\"0\" allowTransparency=\"true\"></iframe>";


        webView= root.findViewById(R.id.webview_fragment);
        progressBar= root.findViewById(R.id.webprogress);
        progressBar.setMax(100);
        progressBar.setProgress(0);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new MyWebClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View view, int code, KeyEvent keyEvent) {
                if(code == KeyEvent.KEYCODE_BACK && webView.canGoBack()){
                    webView.goBack();
                    return true;
                }
                return false;
            }
        });

        //webView.loadDataWithBaseURL("https://facebook.com", embedFacebook, "text/html", "UTF-8", null);
        webView.loadUrl(getArguments().getString("url"));
        return root;
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

    private class MyWebChromeClient extends WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressBar.setProgress(newProgress);
        }
    }
}
