package com.innovasystem.appradio.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.innovasystem.appradio.Classes.RestServices;
import com.innovasystem.appradio.Classes.ResultadoForgotPassword;
import com.innovasystem.appradio.Classes.ResultadoLogIn;
import com.innovasystem.appradio.R;
import com.innovasystem.appradio.Utils.ForgotPassword;
import com.innovasystem.appradio.Utils.Utils;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText editText_email;
    private Button btn_send;
    private UserLoginTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        editText_email = (EditText) findViewById(R.id.editText_email_forgot);

        btn_send = (Button) findViewById(R.id.btn_send);




        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText_email.getText().toString().length()<6 ){
                    Toast.makeText(ForgotPasswordActivity.this,"Email invalido",Toast.LENGTH_SHORT).show();
                }else{
                    mAuthTask = new UserLoginTask(editText_email.getText().toString());
                    mAuthTask.execute((Void) null);
                }
            }
        });


    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;


        UserLoginTask(String email) {
            mEmail = email;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
                ResultadoForgotPassword resultado = RestServices.postForgotPasswordSpring(ForgotPasswordActivity.this,mEmail);
                System.out.println("Main:"+resultado.toString());

                if(resultado.getStatusCode()==200){

                    boolean seParseo = resultado.loadToken();
                    System.out.println("Se pudo parsear? :"+seParseo);
                    System.out.println("Detail:"+resultado.getDetail());
                    if(seParseo){
                        SharedPreferences preferences = getSharedPreferences("account", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(Utils.encrypt("token"), Utils.encrypt(resultado.getDetail()));
                        editor.apply(); // Or commit if targeting old devices
                        editor.commit();
                        return true;
                    }else{
                        return false;
                    }

                }else{
                    return false;
                }

            } catch (InterruptedException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {

                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(ForgotPasswordActivity.this,"Algo fallo, intente luego.",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
