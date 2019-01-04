package com.innovasystem.appradio.Utils;

public class Constants {

    public static final String serverDomain = "http://appradio.pythonanywhere.com";


    //Uri para servicios de logueo
    public static final String uriLogIn = "/api/rest-auth/login/";
    public static final String uriRegister = "/api/rest-auth/register/";
    public static final String uriLogOut = "/api/rest-auth/logout/";
    public static final String uriResetPass = "/api/rest-auth/password/reset/";
    public static final String uriHora= "/api/hora_actual/";
    public static final String uriEmisoras= "/api/emisoras/";
    public static final String uriSegmentos= "/api/segmentos";
    public static final String uriSegmentosEmisora= "/api/%d/segmentos";
    public static final String uriSegmentosDelDia= "/api/segmentos/today";
    public static final String uriSegmentosDelDiaEmisora= "/api/emisoras/%d/segmentos/today";
    public static final String uriTelefonosEmisora= "/api/emisoras/%d/telefonos";
    public static final String uriRedesEmisora= "/api/emisoras/%d/redes_sociales";
    public static final String USER_AGENT = "Mozilla/5.0";
}
