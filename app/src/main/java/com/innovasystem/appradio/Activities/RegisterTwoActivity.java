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

import com.innovasystem.appradio.Classes.RestServices;
import com.innovasystem.appradio.R;
import com.innovasystem.appradio.Utils.RegisterUser;
import com.innovasystem.appradio.Utils.ResultadoRegister;

import java.util.Date;

public class RegisterTwoActivity extends AppCompatActivity {
    Button btn_register;
    EditText editText_username,editText_password,editText_birthday,editText_phoneNumber;
    private UserRegisterTask mAuthTask = null;

    private LinearLayout mLoadingView;
    private View mProgressItem;

    LinearLayout content_view;

    String first_name= "Luigi";
    String last_name= "Basantes";
    String email= "luibasantes@hotmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);

        btn_register= (Button) findViewById(R.id.btn_register);

        editText_username = (EditText) findViewById(R.id.editText_username);
        editText_password = (EditText) findViewById(R.id.editText_password);
        editText_birthday = (EditText) findViewById(R.id.editText_birthday);
        editText_phoneNumber = (EditText) findViewById(R.id.editText_phonenumber);

        btn_register.setOnClickListener(new onRegisterClick());


        mLoadingView = (LinearLayout) findViewById(R.id.loading_view);
        mProgressItem = (View) findViewById(R.id.login_progress);
        content_view = (LinearLayout) findViewById(R.id.content_view);





    }


    public class onRegisterClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(validateFields()){
                showProgress(true);
                System.out.println("Validado");
                Date fecha = new Date();
                RegisterUser user = new RegisterUser(first_name,last_name,editText_username.getText().toString(),
                        editText_password.getText().toString(),email,"O","1993-10-08");
                mAuthTask = new RegisterTwoActivity.UserRegisterTask(user);
                mAuthTask.execute((Void) null);
            }
        }
    }

    public boolean validateFields(){
        if(editText_username.getText().toString().length() < 5){
            Toast.makeText(this,"Usuario muy corto(Min 5)",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(editText_username.getText().toString().length() > 10){
            Toast.makeText(this,"Usuario muy largo(Max 10)",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(editText_password.getText().toString().length() >16){
            Toast.makeText(this,"Contraseña muy larga(Max 16)",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(editText_password.getText().toString().length() <8){
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
                ResultadoRegister resultado = RestServices.postRegisterSpring(RegisterTwoActivity.this,user);
                System.out.println("Main:"+resultado.toString());

                if(resultado.getStatusCode()==201){

                    //Toast.makeText(RegisterTwoActivity.this,"Registro exitoso!",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(RegisterTwoActivity.this,LoginActivity.class);
                    startActivity(i);
                    finish();
                    return true;
                }else{
                    //Toast.makeText(RegisterTwoActivity.this,"Intente mas tarde!",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(RegisterTwoActivity.this,LoginActivity.class);
                    startActivity(i);
                    finish();
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
                Intent intent = new Intent(RegisterTwoActivity.this,LoginActivity.class);
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


            content_view.setVisibility(show ? View.GONE : View.VISIBLE);
            content_view.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    content_view.setVisibility(show ? View.GONE : View.VISIBLE);
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
            content_view.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
