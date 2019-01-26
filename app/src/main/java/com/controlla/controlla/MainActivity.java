package com.controlla.controlla;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private Intent intent;
    private Button b1;
    private EditText name,ps;
    private CheckBox cb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1=(Button) findViewById(R.id.login);
        name=(EditText) findViewById(R.id.username);
        ps=(EditText) findViewById(R.id.password);
        cb=(CheckBox) findViewById(R.id.checkBox);

        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(myIntent);
            }
        });
    }


    }

