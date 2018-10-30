package com.innovasystem.appradio.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.innovasystem.appradio.Clases.RestServices;
import com.innovasystem.appradio.R;
import com.innovasystem.appradio.Utils.RegisterUser;
import com.innovasystem.appradio.Utils.ResultadoRegister;

import java.util.Date;

public class RegisterActivity extends AppCompatActivity {


    private UserRegisterTask mAuthTask = null;

    Button btn_register;
    EditText first_name, last_name,username,email,password;
    int day,month,year;
    private LinearLayout registerView;



    private LinearLayout mLoadingView;
    private View mProgressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        first_name = findViewById(R.id.editText_name);
        last_name = findViewById(R.id.editText_lastName);
        username = findViewById(R.id.editText_username);
        email = findViewById(R.id.editText_mail);
        password = findViewById(R.id.editText_password);
        btn_register = findViewById(R.id.btn_registrarse);
        btn_register.setOnClickListener(new onRegisterClick());
        day=0;
        month=0;
        year=0;

        registerView = findViewById(R.id.registerView);
        mProgressItem = findViewById(R.id.login_progress);
        mLoadingView = (LinearLayout) findViewById(R.id.loading_view);


    }


    public class onRegisterClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(validateFields()){
                showProgress(true);
                System.out.println("Validado");
                Date fecha = new Date();
                RegisterUser user = new RegisterUser(first_name.getText().toString(),last_name.getText().toString(),username.getText().toString(),
                        password.getText().toString(),email.getText().toString(),"O","1993-10-08");
                mAuthTask = new UserRegisterTask(user);
                mAuthTask.execute((Void) null);
            }
        }
    }
    public boolean validateFields(){
        if(first_name.length() < 3){
            Toast.makeText(this,"Nombres muy cortos(Min 3)",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(first_name.length() > 15){
            Toast.makeText(this,"Nombres muy largos(Max 15)",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(last_name.length() < 3){
            Toast.makeText(this,"Apellidos muy cortos(Min 3)",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(last_name.length() > 20){
            Toast.makeText(this,"Apellidos muy largos(Max 20)",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(username.length() < 5){
            Toast.makeText(this,"Usuario muy corto(Min 5)",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(username.length() > 10){
            Toast.makeText(this,"Usuario muy largo(Max 10)",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.length() >16){
            Toast.makeText(this,"Contraseña muy larga(Max 16)",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(password.length() <8){
            Toast.makeText(this,"Contraseña muy corta(Min 8)",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final RegisterUser user;
        UserRegisterTask(RegisterUser u) {
            user = u ;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
                ResultadoRegister resultado = RestServices.postRegisterSpring(RegisterActivity.this,user);
                System.out.println("Main:"+resultado.toString());

                if(resultado.getStatusCode()==201){

                    //Toast.makeText(RegisterActivity.this,"Registro exitoso!",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(i);
                    finish();
                    return true;
                }
                return false;

            } catch (InterruptedException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if ( success /*success*/) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mLoadingView.setVisibility(show ? View.VISIBLE : View.GONE);


            registerView.setVisibility(show ? View.GONE : View.VISIBLE);
            registerView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    registerView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mLoadingView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressItem.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressItem.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressItem.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoadingView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressItem.setVisibility(show ? View.VISIBLE : View.GONE);
            registerView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
