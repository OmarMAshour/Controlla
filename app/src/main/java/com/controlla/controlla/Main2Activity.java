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

public class Main2Activity extends AppCompatActivity {
    private TextView mTextMessage;
    private Intent intent;
    private BottomNavigationView navigation;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        final Fragment frag1=new chatRoomFrag();
        final Fragment frag2=new statusFrag();

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
                /*case R.id.navigation_notifications:
//                    intent = new Intent(MainActivity.this, CarStatus.class);
//                    startActivity(intent);
//                    return true;
                    fragment=frag3;
                    break;*/
            }
                fragmentManager.beginTransaction().replace(R.id.framelayout, fragment).commit();
                return true;
            }
        });
        navigation.setSelectedItemId(R.id.container);

    }
}
