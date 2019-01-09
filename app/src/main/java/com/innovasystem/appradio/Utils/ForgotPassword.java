package com.innovasystem.appradio.Utils;

public class ForgotPassword {
    private String email;

    public ForgotPassword(String email ) {

        this.email=email;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }




    @Override
    public String toString() {
        return "ForgotPassword{" +
                " email='" + email + '\'' +
                '}';
    }


}
