package com.innovasystem.appradio.Clases;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.innovasystem.appradio.Clases.Models.Emisora;
import com.innovasystem.appradio.Clases.Models.Segmento;
import com.innovasystem.appradio.Utils.Constants;
import com.innovasystem.appradio.Utils.LogUser;
import com.innovasystem.appradio.Clases.ResultadoLogIn;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpAuthentication;
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

public class RestServices {

    //CON VOLLEY TA JODIDO
    public void postLogin(final Context context, String username, String password){
        //Genero el string al servicio
        Uri.Builder builder = Uri.parse(""+Constants.serverDomain + Constants.uriLogIn).buildUpon();
        System.out.println("URI:"+ builder.toString());
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JSONArray jsonArray = new JSONArray();
        JSONObject usuario = new JSONObject();

        Map<String, String > params = new HashMap();
        params.put("username",username);
        params.put("email", "");
        params.put("password",password);

        JSONObject object2Send = new JSONObject(params);
        System.out.println("json:"+object2Send.toString());


        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, builder.toString(),object2Send,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Respuesta:"+response.toString());
                        try{

                        }catch(Exception e){

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR:"+error.toString());
                        return;
                    }
                }
        ){
            //here I want to post data to sever
        };
        requestQueue.add(jsonobj);

    }





    public static ResultadoLogIn postLoginSpring(final Context context, String username, String password){
        ResultadoLogIn resultadoLogIn;
        try{
            LogUser user = new LogUser(username,"",password);

            // Set the Content-Type header
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application","json"));
            HttpEntity<LogUser> requestEntity = new HttpEntity<LogUser>(user,requestHeaders);
            //GsonBuilder builder = new GsonBuilder();
            //Gson gson = builder.create();
            //System.out.println(gson.toJson(posthistdisp));


            //Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            // Add the Jackson and String message converters
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            //restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            System.out.println("POST arraydf: "+ user.toString());
            // Make the HTTP POST request, marshaling the request to JSON, and the response to a String
            ResponseEntity<String> responseEntity = restTemplate.exchange(Constants.serverDomain+Constants.uriLogIn, HttpMethod.POST, requestEntity, String.class);
            System.out.println("Cuerpo de RespuestaLogin:"+responseEntity.getBody());

            resultadoLogIn = new ResultadoLogIn(responseEntity.getBody(),responseEntity.getStatusCode().value());
            HttpStatus status = responseEntity.getStatusCode();
            System.out.println("status Post Login "+status);

        } catch (HttpStatusCodeException exception) {
            int statusCode = exception.getStatusCode().value();
            resultadoLogIn = new ResultadoLogIn("",statusCode);
            resultadoLogIn.setErrorMessage(exception.getResponseBodyAsString());


        }catch(Exception e){
            resultadoLogIn = new ResultadoLogIn("",500);
        }

        return  resultadoLogIn;
    }

    public static List<Emisora> consultarEmisoras(){

        HttpHeaders reqHeaders= new HttpHeaders();
        reqHeaders.setAccept(Collections.singletonList(new MediaType("application","json")));
        reqHeaders.set("Authorization","token ");
        HttpEntity<?> requestEntity = new HttpEntity<Object>(reqHeaders);
        Emisora[] emisoras = null;
        RestTemplate restTemplate= new RestTemplate();
        String url= Constants.serverDomain + Constants.uriEmisoras;

        try{
            ResponseEntity<Emisora[]> responseEntity= restTemplate.exchange(url,HttpMethod.GET,requestEntity,Emisora[].class);
            if(responseEntity.getStatusCode() == HttpStatus.OK) {
                emisoras = responseEntity.getBody();
            }
        }catch (Exception e){
            Log.e("RestGetError", e.getMessage());
        }

        return new ArrayList<>(Arrays.asList(emisoras));
    }

    public static List<Segmento> consultarSegmentos(){
        HttpHeaders reqHeaders= new HttpHeaders();
        reqHeaders.setAccept(Collections.singletonList(new MediaType("application","json")));
        reqHeaders.set("Authorization","token ");
        HttpEntity<?> requestEntity = new HttpEntity<Object>(reqHeaders);
        Segmento[] segmentos= null;
        RestTemplate restTemplate= new RestTemplate();
        String url= Constants.serverDomain + Constants.uriSegmentos;
        try{
            ResponseEntity<Segmento[]> responseEntity= restTemplate.exchange(url,HttpMethod.GET,requestEntity,Segmento[].class);
            if(responseEntity.getStatusCode() == HttpStatus.OK) {
                segmentos= responseEntity.getBody();
            }
        }catch (Exception e){
            Log.e("RestGetError", e.getMessage());
        }

        return new ArrayList<>(Arrays.asList(segmentos));
    }
}
