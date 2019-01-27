package Services;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import com.controlla.controlla.AESCrypt;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Data.APP_USER;
import Data.KEEP_SIGNED_USER;

public class FirebaseManager {



    public FirebaseDatabase firebaseDatabase;// = FirebaseDatabase.getInstance();

    public DatabaseReference databaseReference;// = firebaseDatabase.getReference();

    public DatabaseReference app_usersRef;// = databaseReference.child("APP_USERS");
    public ArrayList<APP_USER> app_usersArrayList = new ArrayList<>();

    public DatabaseReference keepSignedRef;
    public ArrayList<KEEP_SIGNED_USER> keep_signed_usersArrayList = new ArrayList<>();

    public String currentSignedEmail="";
    public FirebaseManager(){

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);


        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);

        app_usersRef = databaseReference.child("APP_USERS");
        app_usersRef.keepSynced(true);
        app_usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<APP_USER>> type = new GenericTypeIndicator<ArrayList<APP_USER>>() {};
                app_usersArrayList = dataSnapshot.getValue(type);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        keepSignedRef = databaseReference.child("KEEP_SIGNED_USERS");
        keepSignedRef.keepSynced(true);
        keepSignedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<KEEP_SIGNED_USER>> type = new GenericTypeIndicator<ArrayList<KEEP_SIGNED_USER>>() {};
                keep_signed_usersArrayList = dataSnapshot.getValue(type);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public boolean isKeepSignedUser(String serial){

        for(KEEP_SIGNED_USER user: keep_signed_usersArrayList){
            if(user.getDeviceSerial().equals(serial) && user.isKeepSigned()){
                currentSignedEmail = user.getEmail();
                return true;
            }
        }
        return false;
    }

    public void addKeepSignedUser(String email, String deviceSerial){
        for(KEEP_SIGNED_USER user:keep_signed_usersArrayList){
            if(user.getEmail().equals(email) && user.getDeviceSerial().equals(deviceSerial)){
                user.setKeepSigned(true);
                keepSignedRef.setValue(keep_signed_usersArrayList);
                return;
            }
        }
        keep_signed_usersArrayList.add(new KEEP_SIGNED_USER(email, deviceSerial, true));
        keepSignedRef.setValue(keep_signed_usersArrayList);
    }

    public boolean authUser(String email, String password){
        for(APP_USER user: app_usersArrayList){
            try {
                if(user.getEmail().equals(email) && user.getPassword().equals(AESCrypt.encrypt(password))){
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
