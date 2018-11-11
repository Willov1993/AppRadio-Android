package com.innovasystem.appradio.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.innovasystem.appradio.R;

import java.util.Timer;
import java.util.TimerTask;

import static com.innovasystem.appradio.Utils.Utils.encrypt;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler= new Handler();

        TimerTask task= new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences preferences = getSharedPreferences("account", MODE_PRIVATE);
                        String tokenEncrypted = preferences.getString(encrypt("token"), "default");

                        Intent i =new Intent(SplashActivity.this,tokenEncrypted.equals("default") ? LoginActivity.class : HomeActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
            }
        };

        Timer timer= new Timer();
        timer.schedule(task,3000);
    }
}
