package com.innovasystem.appradio.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.innovasystem.appradio.R;

public class RegisterOneActivity extends AppCompatActivity {

    Button btn_next;
    EditText editText_names,editText_lastnames,editText_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);

        btn_next = (Button) findViewById(R.id.btn_next);

        editText_names = (EditText) findViewById(R.id.editText_names);
        editText_lastnames = (EditText) findViewById(R.id.editText_lastname);
        editText_email= (EditText) findViewById(R.id.editText_email);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(form_is_valid()){
                    Intent i = new Intent(RegisterOneActivity.this,RegisterTwoActivity.class);
                    i.putExtra("names", editText_names.getText().toString());
                    i.putExtra("lastnames", editText_lastnames.getText().toString());
                    i.putExtra("email", editText_email.getText().toString());
                    startActivity(i);
                }

            }
        });
    }


    public boolean form_is_valid(){
        if(editText_email.getText().toString().equals("") || editText_lastnames.getText().toString().equals("") || editText_names.getText().toString().equals("") ){
            Toast.makeText(this,"Llene todos los campos",Toast.LENGTH_LONG).show();
            return false;
        }


        return true;
    }

}
