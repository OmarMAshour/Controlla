package Services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import static com.controlla.controlla.MainActivity.*;

import com.controlla.controlla.AppUtils;
import com.controlla.controlla.R;

public class BackgroundOBDCheck extends Service {
    private static final String TAG = null;
    MediaPlayer player;
    Thread backgroundThread;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
//        player = MediaPlayer.create(this, R.raw.organfinale);
//        player.setLooping(true); // Set looping
//        player.setVolume(100,100);


    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start();

        backgroundThread = new Thread() {
            public void run() {

                while(true){
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

                    if(!L_COOLANT_TEMP.equals("NA")){

                        String[] values = L_COOLANT_TEMP.split(" ");

                        double value = Double.parseDouble(values[0]);

                        if (value>104.4 || value<90.5){


                            AppUtils.sendNotification(BackgroundOBDCheck.this, "Coolant Temperature Problem",
                                    "Your coolant temperature is "+value+" which is not with in the normal boundaries ... go and check it ASAP!");

                        }

                    }

                    if(!L_ENGINE_LOAD.equals("NA")){
                        String[] values = L_ENGINE_LOAD.split(" ");

                        double value = Double.parseDouble(values[0]);

                        if (value>60){
                            AppUtils.sendNotification(BackgroundOBDCheck.this, "Engine Load High",
                                    "Be careful by this way you are harming your Engine.!");
                        }
                    }

                    if(!L_FUEL_INJECT_TIMING.equals("NA")){

                    }

                    if(!L_FUEL_PRESSURE.equals("NA")){
                        String[] values = L_FUEL_PRESSURE.split(" ");

                        double value = Double.parseDouble(values[0]);

                        if (value>448.159){
                            AppUtils.sendNotification(BackgroundOBDCheck.this, "Fuel Pressure High",
                                    "Be careful your fuel pressure is at a high rate !");
                        }
                    }

                    if(!L_FUEL_LEVEL.equals("NA")){
                        String[] values = L_FUEL_LEVEL.split(" ");

                        double value = Double.parseDouble(values[0]);

                        if (value>10){
                            AppUtils.sendNotification(BackgroundOBDCheck.this, "Fuel Pressure High",
                                    "Be careful you are getting out of fuel!!!");
                        }
                    }

                    if(!L_FUEL_RATE.equals("NA")){

                    }

                    if(!L_FUEL_TYPE.equals("NA")){

                    }

                    if(!L_INTAKE_PRESSURE.equals("NA")){
                        String[] values = L_INTAKE_PRESSURE.split(" ");

                        double value = Double.parseDouble(values[0]);

                        if (value>137.895){
                            AppUtils.sendNotification(BackgroundOBDCheck.this, "Intake Pressure High",
                                    "Warning!!! Intake pressure is higher than normal!");
                        }
                    }

                    if(!L_MAF.equals("NA")){
                        String[] values = L_MAF.split(" ");

                        double value = Double.parseDouble(values[0]);

                        if (value<1 || value>3.5){
                            AppUtils.sendNotification(BackgroundOBDCheck.this, "Mass Air Flow Rate Warning",
                                    "Your air flow rate is "+value+" which is not a normal flow and may impact your engine performance");
                        }
                    }

                    if(!L_OIL_TEMP.equals("NA")){
                        String[] values = L_OIL_TEMP.split(" ");

                        double value = Double.parseDouble(values[0]);

                        if (value>275){
                            AppUtils.sendNotification(BackgroundOBDCheck.this, "Oil Temperature is very high",
                                    "Your oil temperature is "+value+" degree celsius ... take care this is danger!");
                        }
                    }

                    if(!L_RPM.equals("NA")){

                    }

                    if(!L_SPEED.equals("NA")){

                    }

                    if(!L_THROTTLE_POS.equals("NA")){

                    }

                    if(!L_TIMING_ADVANCE.equals("NA")){

                    }









                }


            }
        };

        backgroundThread.start();


        return START_NOT_STICKY;
    }


    public IBinder onUnBind(Intent arg0) {
        // TO DO Auto-generated method
        return null;
    }

    public void onStop() {
        player.stop();
        player.release();
    }
    public void onPause() {

    }
    @Override
    public void onDestroy() {
        player.stop();
        player.release();
    }

    @Override
    public void onLowMemory() {

    }
}
