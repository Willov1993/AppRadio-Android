package com.innovasystem.appradio.Classes;

import org.json.JSONObject;

public class ResultadoForgotPassword {
    private String response;
    private int statusCode;
    private String errorMessage;
    private String detail;

    public ResultadoForgotPassword() {
    }

    public ResultadoForgotPassword(String response, int statusCode) {
        this.response = response;
        this.statusCode = statusCode;
        this.detail ="";
        this.errorMessage="";
    }



    public ResultadoForgotPassword(String response, int statusCode, String errorMessage, String detail) {
        this.response = response;
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.detail = detail;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public boolean loadToken(){
        try{
            JSONObject object = new JSONObject(this.response);
            this.detail=object.getString("detail");
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
                ", detail='" + detail + '\'' +
                '}';
    }
}
