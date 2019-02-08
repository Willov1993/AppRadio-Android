package com.innovasystem.appradio.Classes;

import android.content.Context;
import android.content.SharedPreferences;

import com.innovasystem.appradio.Utils.Utils;

import static android.content.Context.MODE_PRIVATE;
import static com.innovasystem.appradio.Utils.Utils.encrypt;

public class SessionConfig implements Config {

    public String userToken;
    public String provincia="";
    public String usuario;
    private static SessionConfig sessionConfig;

    private SessionConfig(Context c){
        iniciarConfig(c);
    }

    @Override
    public void iniciarConfig(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("account", MODE_PRIVATE);
        String tokenEncrypted = preferences.getString(encrypt("token"), "default");
        this.userToken= Utils.decrypt(tokenEncrypted);
        this.usuario= preferences.getString("username","default");
    }

    @Override
    public void actualizarConfig() {

    }


    public static SessionConfig getSessionConfig(Context c) {
        if(sessionConfig == null){
            sessionConfig= new SessionConfig(c);
        }
        return sessionConfig;
    }
}
