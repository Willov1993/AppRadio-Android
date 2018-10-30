package com.innovasystem.appradio.Utils;

import org.json.JSONObject;

public class ResultadoRegister {
    private String response;
    private int statusCode;
    private String errorMessage;

    public ResultadoRegister() {
    }

    public ResultadoRegister(String response, int statusCode) {
        this.response = response;
        this.statusCode = statusCode;
        this.errorMessage="";
    }



    public ResultadoRegister(String response, int statusCode, String errorMessage) {
        this.response = response;
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
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


    @Override
    public String toString() {
        return "ResultadoRegister{" +
                "response='" + response + '\'' +
                ", statusCode=" + statusCode +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
