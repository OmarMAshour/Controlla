package com.controlla.controlla;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;


import static com.controlla.controlla.MainActivity.firebaseManager;

public class Main2Activity extends AppCompatActivity {
    private TextView mTextMessage;
    private Intent intent;
    private BottomNavigationView navigation;
    private Fragment fragment;

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

    }
}
