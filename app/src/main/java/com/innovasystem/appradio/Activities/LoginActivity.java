package com.innovasystem.appradio.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.innovasystem.appradio.R;
import com.innovasystem.appradio.Classes.RestServices;
import com.innovasystem.appradio.Classes.ResultadoLogIn;
import com.innovasystem.appradio.Utils.Constants;
import com.innovasystem.appradio.Utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static com.innovasystem.appradio.Utils.Utils.encrypt;

/**
 * A login screen that offers login via email/password.
 */


public class LoginActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    ImageView imageViewPortada;

    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressItem;
    private View mLoginFormView;
    private LinearLayout mLoadingView;
    private ImageButton fake_btn_fb_login;
    private LoginButton btn_login_fb1;

    private TextView tv_forgot_password;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Read Shared Preferences
        SharedPreferences preferences = getSharedPreferences("account", MODE_PRIVATE);
        String tokenEncrypted = preferences.getString(encrypt("token"), "default");
        String token;
        System.out.println("====> Token encriptado:"+tokenEncrypted);
        if(!tokenEncrypted.equals("default")){
            token = Utils.decrypt(tokenEncrypted);
            Toast.makeText(this,
                    token, Toast.LENGTH_LONG).show();
            entrarHome();
        }else{
            token="";
        }


        //Valida si ya tiene token de fb
        /*
        if(AccessToken.getCurrentAccessToken() != null){
            Intent i = new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(i);
            finish();
        }*/



        //FB SETUP
        printKeyHash();
        callbackManager = CallbackManager.Factory.create();


        btn_login_fb1 = (LoginButton) findViewById(R.id.btn_logfb);

        fake_btn_fb_login = (ImageButton) findViewById(R.id.btn_login_fb2);

        fake_btn_fb_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_login_fb1.performClick();
            }
        });



        btn_login_fb1.setReadPermissions(Arrays.asList("public_profile","email","user_birthday","user_friends"));
        imageViewPortada = (ImageView) findViewById(R.id.imageViewPortada);
        btn_login_fb1.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                String accesToken = loginResult.getAccessToken().getToken();

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("Response de FB:",object.toString());
                        getFacebookData(object);

                    }
                });

                //Request Graph API
                Bundle parameters = new Bundle();
                parameters.putString("fields","id,email,birthday,friends");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
            }
        });

        Button btn_register = (Button) findViewById(R.id.btn_register);


        btn_register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,RegisterOneActivity.class);
                startActivity(i);
            }
        });



        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.username);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressItem = findViewById(R.id.login_progress);
        mLoadingView = (LinearLayout) findViewById(R.id.loading_view);

        tv_forgot_password = (TextView) findViewById(R.id.tv_forgot_password);


        tv_forgot_password.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                /*String url = Constants.serverDomain + Constants.uriPasswordReset;
                try {
                    Intent i = new Intent("android.intent.action.MAIN");
                    i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                    i.addCategory("android.intent.category.LAUNCHER");
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
                catch(ActivityNotFoundException e) {
                    // Chrome is not installed
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                }*/
                Intent i = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(i);


            }
        });


    }


    private void getFacebookData(JSONObject object) {
        try{
            URL profile_picture = new URL("https://graph.facebook.com/"+object.getString("id")+"/picture?width=250&height=250");

            Picasso.with(this).load(profile_picture.toString()).into(imageViewPortada);
            mEmailView.setText(object.getString("email").split("@")[0]);
            mPasswordView.setText("");

            System.out.println("email:"+object.getString("email")+"--birthday:"+object.getString("birthday")+"--friends:"+
            object.getJSONObject("friends").getJSONObject("summary").getString("total_count"));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent i = new Intent(LoginActivity.this,HomeActivity.class);
        startActivity(i);
        finish();
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid.
        if (TextUtils.isEmpty(username)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (username.length() < 6) {
            mEmailView.setError(getString(R.string.error_invalid_username));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 6;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mLoadingView.setVisibility(show ? View.VISIBLE : View.GONE);


            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
                ResultadoLogIn resultado = RestServices.postLoginSpring(LoginActivity.this,mEmail,mPassword);
                System.out.println("Main:"+resultado.toString());

                if(resultado.getStatusCode()==200){

                    boolean seParseo = resultado.loadToken();
                    System.out.println("Se pudo parsear? :"+seParseo);
                    System.out.println("Token:"+resultado.getToken());
                    if(seParseo){
                        SharedPreferences preferences = getSharedPreferences("account", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(Utils.encrypt("token"), Utils.encrypt(resultado.getToken()));
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
            showProgress(false);

            if (success) {
                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_credentials));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }


    public void printKeyHash(){
        try{
            PackageInfo info = getPackageManager().getPackageInfo("com.innovasystem.appradio",PackageManager.GET_SIGNATURES);
            for(Signature signature: info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        }catch(PackageManager.NameNotFoundException e){
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    public void entrarHome(){
        Intent i = new Intent(LoginActivity.this,HomeActivity.class);
        startActivity(i);
        finish();
    }
}

