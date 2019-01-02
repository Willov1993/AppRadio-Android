package com.innovasystem.appradio.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.innovasystem.appradio.R;

public class RegisterOneActivity extends AppCompatActivity {

    Button btn_next;
    EditText editText_names,editText_lastnames,editText_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);

        btn_next = (Button) findViewById(R.id.btn_next);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterOneActivity.this,RegisterTwoActivity.class);
                startActivity(i);
            }
        });
    }
}
