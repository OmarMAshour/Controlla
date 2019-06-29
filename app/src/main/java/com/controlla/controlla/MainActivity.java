package com.controlla.controlla;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;

import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import java.util.List;

import Data.Weather;
import Services.FirebaseManager;
import Services.SendEmail;
import me.everything.providers.android.calendar.Calendar;
import me.everything.providers.android.calendar.CalendarProvider;


public class MainActivity extends AppCompatActivity {
    private Button b1;
    private EditText email, ps;
    private CheckBox cb;
    public static final FirebaseManager firebaseManager = new FirebaseManager();
    public static Weather currentWeather = null;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1 = (Button) findViewById(R.id.login);
        email = (EditText) findViewById(R.id.email);
        ps = (EditText) findViewById(R.id.password);
        cb = (CheckBox) findViewById(R.id.checkBox);

      /*  try {
      /*  try
          //  firebaseManager.getSOS_Email();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }*/
        AppUtils.requestNeededPermissions(MainActivity.this);


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
        if (firebaseManager.isKeepSignedUser(serial)) {
            Intent myIntent = new Intent(MainActivity.this, Main2Activity.class);
            startActivity(myIntent);
            MainActivity.this.finish();
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View arg0) {


//                CalendarProvider provider = new CalendarProvider(MainActivity.this);
//                List<Calendar> calendars = provider.getCalendars().getList();
//                System.out.println(calendars.size());
//                for(Calendar c : calendars){
//                    System.out.println("OOOOOOOO\n"+c.displayName);
//                    System.out.println(c.);
//                    System.out.println(c.allowedReminders);
//                    System.out.println(c.allowedReminders);
//                    System.out.println(c.allowedReminders);
//                    System.out.println(c.allowedReminders);
//                    System.out.println(c.allowedReminders);
//                    System.out.println(c.allowedReminders);
//                    System.out.println(c.allowedReminders);
//                }
                // Start NewActivity.class
//                AppUtils.getCurrentLocationWeather(MainActivity.this);
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
                    firebaseManager.databaseReference.child("L_READINGS").child(firebaseManager.currentSignedUserName).child("L_ENGINE_LOAD").setValue("NA");
                    firebaseManager.databaseReference.child("L_READINGS").child(firebaseManager.currentSignedUserName).child("L_COOLANT_TEMP").setValue("NA");
                    firebaseManager.databaseReference.child("L_READINGS").child(firebaseManager.currentSignedUserName).child("L_FUEL_PRESSURE").setValue("NA");
                    firebaseManager.databaseReference.child("L_READINGS").child(firebaseManager.currentSignedUserName).child("L_INTAKE_PRESSURE").setValue("NA");
                    firebaseManager.databaseReference.child("L_READINGS").child(firebaseManager.currentSignedUserName).child("L_RPM").setValue("NA");
                    firebaseManager.databaseReference.child("L_READINGS").child(firebaseManager.currentSignedUserName).child("L_SPEED").setValue("NA");
                    firebaseManager.databaseReference.child("L_READINGS").child(firebaseManager.currentSignedUserName).child("L_TIMING_ADVANCE").setValue("NA");
                    firebaseManager.databaseReference.child("L_READINGS").child(firebaseManager.currentSignedUserName).child("L_INTAKE_TEMP").setValue("NA");
                    firebaseManager.databaseReference.child("L_READINGS").child(firebaseManager.currentSignedUserName).child("L_MAF").setValue("NA");
                    firebaseManager.databaseReference.child("L_READINGS").child(firebaseManager.currentSignedUserName).child("L_THROTTLE_POS").setValue("NA");
                    firebaseManager.databaseReference.child("L_READINGS").child(firebaseManager.currentSignedUserName).child("L_FUEL_LEVEL").setValue("NA");
                    firebaseManager.databaseReference.child("L_READINGS").child(firebaseManager.currentSignedUserName).child("L_FUEL_TYPE").setValue("NA");
                    firebaseManager.databaseReference.child("L_READINGS").child(firebaseManager.currentSignedUserName).child("L_OIL_TEMP").setValue("NA");
                    firebaseManager.databaseReference.child("L_READINGS").child(firebaseManager.currentSignedUserName).child("L_FUEL_INJECT_TIMING").setValue("NA");
                    firebaseManager.databaseReference.child("L_READINGS").child(firebaseManager.currentSignedUserName).child("L_FUEL_RATE").setValue("NA");
                    Intent myIntent = new Intent(MainActivity.this, Main2Activity.class);
                    startActivity(myIntent);
                    MainActivity.this.finish();
                } else {
                    Toast.makeText(MainActivity.this, "Wrong email or password", Toast.LENGTH_LONG).show();
                }

            }
        });
    }



        // Here, thisActivity is the current activity


    }


