package com.controlla.controlla;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;


import java.util.Timer;
import java.util.TimerTask;

import static com.controlla.controlla.MainActivity.firebaseManager;

public class Main2Activity extends AppCompatActivity {
    private TextView mTextMessage;
    private Intent intent;
    private BottomNavigationView navigation;
    private Fragment fragment;
    private Timer BackgroundOBDCheckTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setTitle("Main Page");

        final FragmentManager fragmentManager = getSupportFragmentManager();
        final Fragment frag1=new chatRoomFrag();
        final Fragment frag2=new statusFrag();
        final Fragment frag3=new DTCfrag();
        final Fragment frag4=new capturingFrag();

        fragment = frag1;
        fragmentManager.beginTransaction() .replace(R.id.framelayout, fragment).commit();

        mTextMessage = (TextView) findViewById(R.id.message);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {switch (item.getItemId()) {
                case R.id.navigation_chat:
                    fragment= frag1;
                    break;
                case R.id.navigation_status:
                    fragment=frag2;
                    break;
                case R.id.navigation_DTC:
                    fragment=frag3;
                    break;
                case R.id.navigation_Img_capture:
                    fragment=frag4;
                    break;
                case R.id.navigation_Logout:
                    firebaseManager.currentSignedEmail="";
                    firebaseManager.currentSignedUserName="";
                    Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                    startActivity(intent);
                    Main2Activity.this.finish();

                    break;
            }

                fragmentManager.beginTransaction() .replace(R.id.framelayout, fragment).commit();
                return true;
            }
        });
        navigation.setSelectedItemId(R.id.container);



        BackgroundOBDCheckTimer = new Timer();

        BackgroundOBDCheckTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                BackgroundOBDCheck();
            }
        }, 0, 1000);


    }

    private void BackgroundOBDCheck(){

//        new Thread() {
//            public void run() {
//
//                AppUtils.sendNotification(Main2Activity.this, "Coolant Temperature Problem",
//                        "Your coolant temperature is 150 degree celsius which is not with in the normal boundaries ... go and check it ASAP!");
//                while(true){
                    String L_COOLANT_TEMP = firebaseManager.L_COOLANT_TEMP;
                    String L_ENGINE_LOAD = firebaseManager.L_ENGINE_LOAD;
                    String L_FUEL_INJECT_TIMING = firebaseManager.L_FUEL_INJECT_TIMING;
                    String L_FUEL_LEVEL = firebaseManager.L_FUEL_LEVEL;
                    String L_FUEL_PRESSURE = firebaseManager.L_FUEL_PRESSURE;
                    String L_FUEL_RATE = firebaseManager.L_FUEL_RATE;
                    String L_FUEL_TYPE = firebaseManager.L_FUEL_TYPE;
                    String L_MAF = firebaseManager.L_MAF;
                    String L_OIL_TEMP = firebaseManager.L_OIL_TEMP;
                    String L_RPM = firebaseManager.L_RPM;
                    String L_SPEED = firebaseManager.L_SPEED;
                    String L_THROTTLE_POS = firebaseManager.L_THROTTLE_POS;
                    String L_TIMING_ADVANCE = firebaseManager.L_TIMING_ADVANCE;
                    String L_INTAKE_PRESSURE = firebaseManager.L_INTAKE_PRESSURE;

                    if(!L_COOLANT_TEMP.equals("NA") && !L_COOLANT_TEMP.equals("")){

                        String[] values = L_COOLANT_TEMP.split(" ");

                        double value = Double.parseDouble(values[0]);

                        if (value>104.4 || value<90.5){


                            AppUtils.sendNotification(Main2Activity.this, "Coolant Temperature Problem",
                                    "Your coolant temperature is "+value+" which is not with in the normal boundaries ... go and check it ASAP!");

                        }

                    }

                    if(!L_ENGINE_LOAD.equals("NA") && !L_ENGINE_LOAD.equals("")){
                        String[] values = L_ENGINE_LOAD.split(" ");

                        double value = Double.parseDouble(values[0]);

                        if (value>60){
                            AppUtils.sendNotification(Main2Activity.this, "Engine Load High",
                                    "Be careful by this way you are harming your Engine.!");
                        }
                    }

                    if(!L_FUEL_INJECT_TIMING.equals("NA")&& !L_FUEL_INJECT_TIMING.equals("")){

                    }

                    if(!L_FUEL_PRESSURE.equals("NA") && !L_FUEL_PRESSURE.equals("")){
                        String[] values = L_FUEL_PRESSURE.split(" ");

                        double value = Double.parseDouble(values[0]);

                        if (value>448.159){
                            AppUtils.sendNotification(Main2Activity.this, "Fuel Pressure High",
                                    "Be careful your fuel pressure is at a high rate !");
                        }
                    }

                    if(!L_FUEL_LEVEL.equals("NA") && !L_FUEL_LEVEL.equals("")){
                        String[] values = L_FUEL_LEVEL.split(" ");

                        double value = Double.parseDouble(values[0]);

                        if (value>10){
                            AppUtils.sendNotification(Main2Activity.this, "Fuel Pressure High",
                                    "Be careful you are getting out of fuel!!!");
                        }
                    }

                    if(!L_FUEL_RATE.equals("NA") && !L_FUEL_RATE.equals("")){

                    }

                    if(!L_FUEL_TYPE.equals("NA") && !L_FUEL_TYPE.equals("")){

                    }

                    if(!L_INTAKE_PRESSURE.equals("NA") && !L_INTAKE_PRESSURE.equals("")){
                        String[] values = L_INTAKE_PRESSURE.split(" ");

                        double value = Double.parseDouble(values[0]);

                        if (value>137.895){
                            AppUtils.sendNotification(Main2Activity.this, "Intake Pressure High",
                                    "Warning!!! Intake pressure is higher than normal!");
                        }
                    }

                    if(!L_MAF.equals("NA") && !L_MAF.equals("")){
                        String[] values = L_MAF.split(" ");

                        double value = Double.parseDouble(values[0]);

                        if (value<1 || value>3.5){
                            AppUtils.sendNotification(Main2Activity.this, "Mass Air Flow Rate Warning",
                                    "Your air flow rate is "+value+" which is not a normal flow and may impact your engine performance");
                        }
                    }

                    if(!L_OIL_TEMP.equals("NA") && !L_OIL_TEMP.equals("")){
                        String[] values = L_OIL_TEMP.split(" ");

                        double value = Double.parseDouble(values[0]);

                        if (value>275){
                            AppUtils.sendNotification(Main2Activity.this, "Oil Temperature is very high",
                                    "Your oil temperature is "+value+" degree celsius ... take care this is danger!");
                        }
                    }

                    if(!L_RPM.equals("NA") && !L_RPM.equals("")){
                        String[] values = L_RPM.split(" ");

                        double value = Double.parseDouble(values[0]);

                        if (value>6000){
                            AppUtils.sendNotification(Main2Activity.this, "RPM High",
                                    "Kindly notice that your RPM is high");
                        }
                    }

                    if(!L_SPEED.equals("NA") && !L_SPEED.equals("")){
                        String[] values = L_SPEED.split(" ");

                        double value = Double.parseDouble(values[0]);

                        if (value>100){
                            AppUtils.sendNotification(Main2Activity.this, "Speed Over Normal",
                                    "Your are moving on speed greater than 100 kph which may not be permitted in your region");
                        }
                    }

                    if(!L_THROTTLE_POS.equals("NA") && !L_THROTTLE_POS.equals("")){

                    }

                    if(!L_TIMING_ADVANCE.equals("NA") && !L_TIMING_ADVANCE.equals("")){

                    }
                }


//            }
//        }.start();
    }



//    public void startServices(){
//        Intent backgroundCheckService = new Intent(Main2Activity.this, BackgroundOBDCheck.class);
//        startService(backgroundCheckService);
//    }
//
//    public void stopServices(){
//        Intent backgroundCheckService = new Intent(Main2Activity.this, BackgroundOBDCheck.class);
//        stopService(backgroundCheckService);
//    }

