package com.innovasystem.appradio.Classes;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.innovasystem.appradio.Classes.Models.Conductor;
import com.innovasystem.appradio.Classes.Models.Emisora;
import com.innovasystem.appradio.Classes.Models.Favorito;
import com.innovasystem.appradio.Classes.Models.Fecha;
import com.innovasystem.appradio.Classes.Models.Multimedia;
import com.innovasystem.appradio.Classes.Models.RedSocialEmisora;
import com.innovasystem.appradio.Classes.Models.Segmento;
import com.innovasystem.appradio.Classes.Models.TelefonoEmisora;
import com.innovasystem.appradio.Utils.Constants;
import com.innovasystem.appradio.Utils.ForgotPassword;
import com.innovasystem.appradio.Utils.LogUser;
import com.innovasystem.appradio.Utils.RegisterUser;
import com.innovasystem.appradio.Utils.ResultadoRegister;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RestServices {

    //CON VOLLEY TA JODIDO
    public void postLogin(final Context context, String username, String password) {
        //Genero el string al servicio
        Uri.Builder builder = Uri.parse("" + Constants.serverDomain + Constants.uriLogIn).buildUpon();
        System.out.println("URI:" + builder.toString());
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JSONArray jsonArray = new JSONArray();
        JSONObject usuario = new JSONObject();

        Map<String, String> params = new HashMap();
        params.put("username", username);
        params.put("email", "");
        params.put("password", password);

        JSONObject object2Send = new JSONObject(params);
        System.out.println("json:" + object2Send.toString());


        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, builder.toString(), object2Send,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Respuesta:" + response.toString());
                        try {

                        } catch (Exception e) {

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR:" + error.toString());
                        return;
                    }
                }
        ) {
            //here I want to post data to sever
        };
        requestQueue.add(jsonobj);

    }


    public static ResultadoLogIn postLoginSpring(final Context context, String username, String password) {
        ResultadoLogIn resultadoLogIn;
        try {
            LogUser user = new LogUser(username, "", password);

            // Set the Content-Type header
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application", "json"));
            HttpEntity<LogUser> requestEntity = new HttpEntity<LogUser>(user, requestHeaders);
            //Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            // Add the Jackson and String message converters
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            //restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            System.out.println("POST arraydf: " + user.toString());
            // Make the HTTP POST request, marshaling the request to JSON, and the response to a String
            ResponseEntity<String> responseEntity = restTemplate.exchange(Constants.serverDomain + Constants.uriLogIn, HttpMethod.POST, requestEntity, String.class);
            System.out.println("Cuerpo de RespuestaLogin:" + responseEntity.getBody());

            resultadoLogIn = new ResultadoLogIn(responseEntity.getBody(), responseEntity.getStatusCode().value());
            HttpStatus status = responseEntity.getStatusCode();
            System.out.println("status Post Login " + status);

        } catch (HttpStatusCodeException exception) {
            int statusCode = exception.getStatusCode().value();
            resultadoLogIn = new ResultadoLogIn("", statusCode);
            resultadoLogIn.setErrorMessage(exception.getResponseBodyAsString());


        } catch (Exception e) {
            resultadoLogIn = new ResultadoLogIn("", 500);
        }

        return resultadoLogIn;
    }

    public static ResultadoRegister postRegisterSpring(final Context context, RegisterUser usuario) {
        ResultadoRegister resultadoLogIn;
        try {
            // Set the Content-Type header
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application", "json"));
            HttpEntity<RegisterUser> requestEntity = new HttpEntity<>(usuario, requestHeaders);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            System.out.println("POST arraydf: " + usuario.toString());
            ResponseEntity<String> responseEntity = restTemplate.exchange(Constants.serverDomain + Constants.uriRegister, HttpMethod.POST, requestEntity, String.class);
            System.out.println("Cuerpo de RespuestaRegister:" + responseEntity.getBody());

            resultadoLogIn = new ResultadoRegister(responseEntity.getBody(), responseEntity.getStatusCode().value());
            HttpStatus status = responseEntity.getStatusCode();
            System.out.println("status Post Register " + status);

        } catch (HttpStatusCodeException exception) {
            int statusCode = exception.getStatusCode().value();
            resultadoLogIn = new ResultadoRegister("", statusCode);
            resultadoLogIn.setErrorMessage(exception.getResponseBodyAsString());


        } catch (Exception e) {
            resultadoLogIn = new ResultadoRegister("", 500);
        }

        return resultadoLogIn;
    }

    public static Fecha consultarHoraActual(Context c){
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        reqHeaders.set("Authorization", SessionConfig.getSessionConfig(c).userToken);
        HttpEntity<?> requestEntity = new HttpEntity<Object>(reqHeaders);
        RestTemplate restTemplate = new RestTemplate();
        String url = Constants.serverDomain + Constants.uriHora;
        try {
            ResponseEntity<Fecha> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Fecha.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity.getBody();
            }
        } catch (Exception e) {
            Log.e("RestGetError", e.getMessage());
            return null;
        }

        return null;
    }


    /**
     *
     * @param c Contexto de la actividad que llama a este metodo
     * @param provincia OPCIONAL: especifica de que provincia se desea consultar
     * @return una Lista con Emisoras
     */
    public static List<Emisora> consultarEmisoras(Context c,String provincia) {
        System.out.println("USER TOKEN: " + SessionConfig.getSessionConfig(c).userToken);
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        reqHeaders.set("Authorization", SessionConfig.getSessionConfig(c).userToken);
        HttpEntity<?> requestEntity = new HttpEntity<Object>(reqHeaders);
        Emisora[] emisoras = null;
        RestTemplate restTemplate = new RestTemplate();
        String url = Constants.serverDomain + Constants.uriEmisoras + (provincia != null ? String.format("?provincia=%s",provincia) : "" );

        try {
            ResponseEntity<Emisora[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Emisora[].class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                emisoras = responseEntity.getBody();
            }
        } catch (Exception e) {
            Log.e("RestGetError", e.getMessage());
            return null;
        }
        return new ArrayList<>(Arrays.asList(emisoras));
    }

    public static List<Segmento> consultarSegmentos(Context c) {
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        reqHeaders.set("Authorization", SessionConfig.getSessionConfig(c).userToken);
        HttpEntity<?> requestEntity = new HttpEntity<Object>(reqHeaders);
        Segmento[] segmentos = null;
        RestTemplate restTemplate = new RestTemplate();
        String url = Constants.serverDomain + Constants.uriSegmentos;
        try {
            ResponseEntity<Segmento[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Segmento[].class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                segmentos = responseEntity.getBody();
            }
        } catch (Exception e) {
            Log.e("RestGetError", e.getMessage());
            return null;
        }

        return new ArrayList<>(Arrays.asList(segmentos));
    }

    public static List<Segmento> consultarSegmentosEmisora(Context c, int idEmisora) {
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        reqHeaders.set("Authorization", SessionConfig.getSessionConfig(c).userToken);
        HttpEntity<?> requestEntity = new HttpEntity<Object>(reqHeaders);
        Segmento[] segmentos = null;
        RestTemplate restTemplate = new RestTemplate();
        String url = Constants.serverDomain + String.format(Constants.uriSegmentosEmisora, idEmisora);

        try {
            ResponseEntity<Segmento[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Segmento[].class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                segmentos = responseEntity.getBody();
            }
        } catch (Exception e) {
            Log.e("RestGetError", e.getMessage());
            return null;
        }

        return new ArrayList<>(Arrays.asList(segmentos));
    }

    /**
     *
     * @param c El contexto de la actividad que llama a este metodo
     * @param provincia OPCIONAL: la provincia en la cual se desea hacer la consulta
     * @return una lista con los Segmentos del dia actual.
     */
    public static List<Segmento> consultarSegmentosDelDia(Context c,String provincia) {
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        reqHeaders.set("Authorization", SessionConfig.getSessionConfig(c).userToken);
        HttpEntity<?> requestEntity = new HttpEntity<Object>(reqHeaders);
        Segmento[] segmentos = null;
        RestTemplate restTemplate = new RestTemplate();
        String url = Constants.serverDomain + Constants.uriSegmentosDelDia + (provincia != null ? String.format("?provincia=%s",provincia) : "" );

        try {
            ResponseEntity<Segmento[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Segmento[].class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                segmentos = responseEntity.getBody();
            }
        } catch (Exception e) {
            Log.e("RestGetError", e.getMessage());
            return null;
        }

        return new ArrayList<>(Arrays.asList(segmentos));
    }

    public static List<Segmento> consultarSegmentosDelDia(Context c, int idEmisora) {
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        reqHeaders.set("Authorization", SessionConfig.getSessionConfig(c).userToken);
        HttpEntity<?> requestEntity = new HttpEntity<Object>(reqHeaders);
        Segmento[] segmentos = null;
        RestTemplate restTemplate = new RestTemplate();
        String url = Constants.serverDomain + String.format(Constants.uriSegmentosDelDiaEmisora, idEmisora);

        try {
            ResponseEntity<Segmento[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Segmento[].class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                segmentos = responseEntity.getBody();
            }
        } catch (Exception e) {
            Log.e("RestGetError", e.getMessage());
            return null;
        }

        return new ArrayList<>(Arrays.asList(segmentos));
    }

    /**
     * Obtiene los telefonos de una emisora dada
     * @param c
     * @param idEmisora
     * @return
     */
    public static List<TelefonoEmisora> consultarTelefonosEmisora(Context c,int idEmisora){
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        reqHeaders.set("Authorization", SessionConfig.getSessionConfig(c).userToken);
        HttpEntity<?> requestEntity = new HttpEntity<Object>(reqHeaders);
        TelefonoEmisora[] telefonos= null;
        RestTemplate restTemplate= new RestTemplate();
        String url = Constants.serverDomain + String.format(Constants.uriTelefonosEmisora, idEmisora);
        try {
            ResponseEntity<TelefonoEmisora[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, TelefonoEmisora[].class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                telefonos = responseEntity.getBody();
            }
        } catch (Exception e) {
            Log.e("RestGetError", e.getMessage());
            return null;
        }
        return new ArrayList<>(Arrays.asList(telefonos));
    }

    public static List<RedSocialEmisora> consultarRedesEmisora(Context c, int idEmisora){
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        reqHeaders.set("Authorization", SessionConfig.getSessionConfig(c).userToken);
        HttpEntity<?> requestEntity = new HttpEntity<Object>(reqHeaders);
        RedSocialEmisora[] redes= null;
        RestTemplate restTemplate= new RestTemplate();
        String url = Constants.serverDomain + String.format(Constants.uriRedesEmisora, idEmisora);
        try {
            ResponseEntity<RedSocialEmisora[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, RedSocialEmisora[].class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                redes = responseEntity.getBody();
            }
        } catch (Exception e) {
            Log.e("RestGetError", e.getMessage());
            return null;
        }
        return new ArrayList<>(Arrays.asList(redes));
    }

    public static List<Conductor> consultarLocutores(Context c,int idSegmento){
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        reqHeaders.set("Authorization", SessionConfig.getSessionConfig(c).userToken);
        HttpEntity<?> requestEntity = new HttpEntity<Object>(reqHeaders);
        Conductor[] conductores= null;
        RestTemplate restTemplate= new RestTemplate();
        String url = Constants.serverDomain + String.format(Constants.uriLocutoresSegmento, idSegmento);
        try {
            ResponseEntity<Conductor[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Conductor[].class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                conductores = responseEntity.getBody();
            }
        } catch (Exception e) {
            Log.e("RestGetError", e.getMessage());
            return null;
        }
        return new ArrayList<>(Arrays.asList(conductores));
    }

    public static List<Multimedia> consultarMultimediaSegmento(Context c, int idSegmento,boolean consultarVideos){
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        reqHeaders.set("Authorization", SessionConfig.getSessionConfig(c).userToken);
        HttpEntity<?> requestEntity = new HttpEntity<Object>(reqHeaders);
        Multimedia[] recursos_multimedia= null;
        RestTemplate restTemplate= new RestTemplate();
        String url = Constants.serverDomain + String.format(consultarVideos ? Constants.uriVideosSegmento :Constants.uriImagenesSegmento, idSegmento);
        try {
            ResponseEntity<Multimedia[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Multimedia[].class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                recursos_multimedia = responseEntity.getBody();
            }
        } catch (Exception e) {
            Log.e("RestGetError", e.getMessage());
            return null;
        }
        return new ArrayList<>(Arrays.asList(recursos_multimedia));
    }

    public static ResultadoForgotPassword postForgotPasswordSpring(final Context context, String email) {
        ResultadoForgotPassword resultadoLogIn;
        try {
            ForgotPassword user = new ForgotPassword(email);

            // Set the Content-Type header
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application", "json"));
            HttpEntity<ForgotPassword> requestEntity = new HttpEntity<ForgotPassword>(user, requestHeaders);
            //Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            // Add the Jackson and String message converters
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            //restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            System.out.println("POST arraydf: " + user.toString());
            // Make the HTTP POST request, marshaling the request to JSON, and the response to a String
            ResponseEntity<String> responseEntity = restTemplate.exchange(Constants.serverDomain + Constants.uriResetPass, HttpMethod.POST, requestEntity, String.class);
            System.out.println("Cuerpo de ForgotPassword:" + responseEntity.getBody());

            resultadoLogIn = new ResultadoForgotPassword(responseEntity.getBody(), responseEntity.getStatusCode().value());
            HttpStatus status = responseEntity.getStatusCode();
            System.out.println("status Post Resetpassword " + status);

        } catch (HttpStatusCodeException exception) {
            int statusCode = exception.getStatusCode().value();
            resultadoLogIn = new ResultadoForgotPassword("", statusCode);
            resultadoLogIn.setErrorMessage(exception.getResponseBodyAsString());


        } catch (Exception e) {
            resultadoLogIn = new ResultadoForgotPassword("", 500);
        }

        return resultadoLogIn;
    }


    public static List<Segmento> consultarFavoritos(Context c,String username){
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        reqHeaders.set("Authorization", SessionConfig.getSessionConfig(c).userToken);
        HttpEntity<?> requestEntity = new HttpEntity<Object>(reqHeaders);
        Segmento[] segmentos= null;
        RestTemplate restTemplate= new RestTemplate();
        String url = Constants.serverDomain + String.format(Constants.uriFavoritos, username);
        try {
            ResponseEntity<Segmento[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Segmento[].class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                segmentos = responseEntity.getBody();
            }
        } catch (Exception e) {
            Log.e("RestGetError", e.getMessage());
            return null;
        }
        return new ArrayList<>(Arrays.asList(segmentos));
    }

    public static boolean agregarFavorito(Context c,String username,long idSegmento){
        /*ResultadoRegister resultadoLogIn;
        try {
            // Set the Content-Type header
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application", "json"));
            requestHeaders.set("Authorization", SessionConfig.getSessionConfig(c).userToken);
            Favorito fav= new Favorito(username,idSegmento);
            HttpEntity<Favorito> requestEntity = new HttpEntity<>(fav, requestHeaders);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<String> responseEntity = restTemplate.exchange(Constants.serverDomain + Constants.uriAgregarFavoritos, HttpMethod.POST, requestEntity, String.class);
            System.out.println("Cuerpo de RespuestaRegister:" + responseEntity.getBody());

            resultadoLogIn = new ResultadoRegister(responseEntity.getBody(), responseEntity.getStatusCode().value());
            HttpStatus status = responseEntity.getStatusCode();
            System.out.println("status Post Register " + status);

            return true;

        } catch (HttpStatusCodeException exception) {
            int statusCode = exception.getStatusCode().value();
            resultadoLogIn = new ResultadoRegister("", statusCode);
            resultadoLogIn.setErrorMessage(exception.getResponseBodyAsString());
            return false;

        } catch (Exception e) {
            resultadoLogIn = new ResultadoRegister("", 500);
            return false;
        }*/

        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        reqHeaders.set("Authorization", SessionConfig.getSessionConfig(c).userToken);
        HttpEntity<?> requestEntity = new HttpEntity<Object>(reqHeaders);
        RestTemplate restTemplate= new RestTemplate();
        String url = Constants.serverDomain + String.format(Constants.uriAgregarFavoritosFalse,idSegmento,username);
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return true;
            }
        } catch (Exception e) {
            Log.e("RestGetError", e.getMessage());
            return false;
        }
        return false;
    }

}