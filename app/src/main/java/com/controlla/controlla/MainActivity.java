package com.controlla.controlla;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;

import Services.FirebaseManager;

public class MainActivity extends AppCompatActivity {

    private Intent intent;
    private Button b1;
    private EditText email, ps;
    private CheckBox cb;
    public static FirebaseManager firebaseManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1 = (Button) findViewById(R.id.login);
        email = (EditText) findViewById(R.id.email);
        ps = (EditText) findViewById(R.id.password);
        cb = (CheckBox) findViewById(R.id.checkBox);

        AppUtils.requestNeededPermissions(MainActivity.this);

//        FirebaseApp.initializeApp(this);
        firebaseManager = new FirebaseManager();

        String serial = Build.SERIAL;
        if (serial.equals("unknown")) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: @ASHOUR don't forget to request permission ... Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            serial = Build.getSerial();
        }
        if(firebaseManager.isKeepSignedUser(serial)){
            Intent myIntent = new Intent(MainActivity.this, Main2Activity.class);
            startActivity(myIntent);
            MainActivity.this.finish();
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View arg0) {

                // Start NewActivity.class
                if (firebaseManager.authUser(email.getText().toString().trim(), ps.getText().toString())) {
                    if (cb.isChecked()) {
                        String serial = Build.SERIAL;
                        if (serial.equals("unknown")) {
                            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: @ASHOUR don't forget to request permission ... Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            serial = Build.getSerial();
                        }
                        firebaseManager.addKeepSignedUser(email.getText().toString().trim(), serial);
                    }
                    firebaseManager.currentSignedEmail = email.getText().toString().trim();
                    Intent myIntent = new Intent(MainActivity.this, Main2Activity.class);
                    startActivity(myIntent);
                    MainActivity.this.finish();
                }else{
                    Toast.makeText(MainActivity.this, "Wrong email or password", Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    }

