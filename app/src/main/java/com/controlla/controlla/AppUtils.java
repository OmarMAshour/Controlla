package com.controlla.controlla;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;


import static com.controlla.controlla.MainActivity.firebaseManager;

import Data.Weather;
import Services.GPSTracker;
import Services.SendEmail;

public class AppUtils {
    final static int REQUEST_CODE = 1234;
    final static String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";

    final static String API_KEY = "d5d4cb412c8828b9bed71c004f7884d5";

    final static long MIN_TIME = 5000;

    final static float MIN_DISTANCE = 1000;

    public static void sendSOSEmail(ArrayList<String> L_Strings) {
            SendEmail sendEmail = new SendEmail(firebaseManager.getSOS_Email());
            sendEmail.DoConfiguration(L_Strings.get(0), L_Strings.get(1));

    }


    public static void requestNeededPermissions(Activity activity) {
        ArrayList<String> arrPerm = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            arrPerm.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            arrPerm.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            arrPerm.add(Manifest.permission.BLUETOOTH);
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            arrPerm.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            arrPerm.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }


        if (!arrPerm.isEmpty()) {
            String[] permissions = new String[arrPerm.size()];
            permissions = arrPerm.toArray(permissions);
            ActivityCompat.requestPermissions(activity, permissions, 1);
        }
    }


    static String notificationChannelID = "0";

    public static void sendNotification(Context context, String Error, String Message){

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        notificationChannelID = String.valueOf(Integer.parseInt(notificationChannelID)+1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ControllaChannel";//getString(R.string.channel_name);
            String description = "Notification Channel for Controlla App";//getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(notificationChannelID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, notificationChannelID)
                .setSmallIcon(R.drawable.img)
                .setContentTitle(Error)
                .setContentText(Message)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(Integer.valueOf(notificationChannelID), builder.build());

//        textToSpeech.speak(Message, TextToSpeech.QUEUE_FLUSH, null);
    }

    public static void getCurrentLocationWeather(Context context) {



        GPSTracker gpsTracker = new GPSTracker(context);

        RequestParams requestParams = new RequestParams();

        requestParams.put("lat", gpsTracker.getLocation(context).get(0));
        requestParams.put("lon", gpsTracker.getLocation(context).get(1));
        requestParams.put("appid", API_KEY);

       apiCall(requestParams, context);




    }



    private static void apiCall(RequestParams requestParams, final Context context) {

            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

            asyncHttpClient.get(WEATHER_URL, requestParams, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    try {
                        Weather weather = Weather.fromJson(response);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    updateWeatherDetails(weather);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);

                    Toast.makeText(context, "Error occurred while making request!", Toast.LENGTH_LONG).show();
                }
            });

    }



}
