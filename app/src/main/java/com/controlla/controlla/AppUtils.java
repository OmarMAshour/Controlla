package com.controlla.controlla;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;

public class AppUtils {

    public static void requestNeededPermissions(Activity activity){
        ArrayList<String> arrPerm = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            arrPerm.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            arrPerm.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            arrPerm.add(Manifest.permission.BLUETOOTH);
        }



        if(!arrPerm.isEmpty()) {
            String[] permissions = new String[arrPerm.size()];
            permissions = arrPerm.toArray(permissions);
            ActivityCompat.requestPermissions(activity, permissions, 1);
        }
    }
}
