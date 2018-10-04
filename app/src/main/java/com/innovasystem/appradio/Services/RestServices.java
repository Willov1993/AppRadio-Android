package com.innovasystem.appradio.Services;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.innovasystem.appradio.Utils.Constants;
import com.innovasystem.appradio.Utils.LogUser;
import com.innovasystem.appradio.Utils.ResultadoLogIn;

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

import java.util.HashMap;
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
}
