package com.innovasystem.appradio.Clases;

import org.json.JSONObject;

public class ResultadoLogIn {
    private String response;
    private int statusCode;
    private String errorMessage;
    private String token;

    public ResultadoLogIn() {
    }

    public ResultadoLogIn(String response, int statusCode) {
        this.response = response;
        this.statusCode = statusCode;
        this.token ="";
        this.errorMessage="";
    }



    public ResultadoLogIn(String response, int statusCode, String errorMessage, String token) {
        this.response = response;
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.token = token;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean loadToken(){
        try{
            JSONObject object = new JSONObject(this.response);
            this.token="Token "+object.getString("key");
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ResultadoLogIn{" +
                "response='" + response + '\'' +
                ", statusCode=" + statusCode +
                ", errorMessage='" + errorMessage + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
