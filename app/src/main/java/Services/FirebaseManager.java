package Services;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.controlla.controlla.AESCrypt;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.mail.Session;

import Data.APP_USER;
import Data.KEEP_SIGNED_USER;

public class FirebaseManager {

    Session session = null;
    private Context context ;

    private FusedLocationProviderClient fusedLocationClient;


    public FirebaseDatabase firebaseDatabase;// = FirebaseDatabase.getInstance();

    public DatabaseReference databaseReference;// = firebaseDatabase.getReference();

    public DatabaseReference app_usersRef;// = databaseReference.child("APP_USERS");
    public ArrayList<APP_USER> app_usersArrayList ;//= new ArrayList<>();

    public DatabaseReference keepSignedRef;
    public ArrayList<KEEP_SIGNED_USER> keep_signed_usersArrayList = new ArrayList<>();

    public String currentSignedEmail="";
    public String currentSignedUserName="";

    public DatabaseReference L_ReadingsRef;// = databaseReference.child("L_READINGS");
    public DatabaseReference L_DTCSRef;

    public String L_ENGINE_LOAD = "";
    public DatabaseReference L_ENGINE_LOADRef;

    public String L_COOLANT_TEMP = "";
    public DatabaseReference L_COOLANT_TEMPRef;

    public String L_FUEL_PRESSURE = "";
    public DatabaseReference L_FUEL_PRESSURERef;
    public String L_INTAKE_PRESSURE = "";
    public DatabaseReference L_INTAKE_PRESSURERef;

    public String L_RPM = "";
    public DatabaseReference L_RPMRef;

    public String L_SPEED = "";
    public DatabaseReference L_SPEEDRef;

    public String L_TIMING_ADVANCE = "";
    public DatabaseReference L_TIMING_ADVANCERef;

    public String L_INTAKE_TEMP = "";
    public DatabaseReference L_INTAKE_TEMPRef;

    public String L_MAF = "";
    public DatabaseReference L_MAFRef;

    public String L_THROTTLE_POS = "";
    public DatabaseReference L_THROTTLE_POSRef;

    public String L_FUEL_LEVEL = "";
    public DatabaseReference L_FUEL_LEVELRef;

    public String L_FUEL_TYPE = "";
    public DatabaseReference L_FUEL_TYPERef;

    public String L_OIL_TEMP = "";
    public DatabaseReference L_OIL_TEMPRef;

    public String L_FUEL_INJECT_TIMING = "";
    public DatabaseReference L_FUEL_INJECT_TIMINGRef;

    public String L_FUEL_RATE = "";
    public DatabaseReference L_FUEL_RATERef;

    public DatabaseReference HISTORYRef;


    public String H_COOLANT_TEMPERATURE = "";
    public DatabaseReference H_COOLANT_TEMPERATURERef;
    public ArrayList<String> H_COOLANT_TEMPERATURE_LIST;

    public String H_ENGINE_LOAD = "";
    public DatabaseReference H_ENGINE_LOADRef;
    public ArrayList<String> H_ENGINE_LOAD_LIST;

    public String H_INTAKE_PRESSURE = "";
    public DatabaseReference H_INTAKE_PRESSURERef;
    public ArrayList<String> H_INTAKE_PRESSURE_LIST;


    public String H_RPM = "";
    public DatabaseReference H_RPMRef;
    public ArrayList<String> H_RPM_LIST;

    public String H_SPEED = "";
    public DatabaseReference H_SPEEDRef;
    public ArrayList<String> H_SPEED_LIST;

    public String H_THROTTLE_POSITION = "";
    public DatabaseReference H_THROTTLE_POSITIONRef;
    public ArrayList<String> H_THROTTLE_POSITION_LIST;



    public ArrayList<String> L_DTCS_arraylist = new ArrayList<>();


    public DatabaseReference SETTINGSRef;

    public DatabaseReference SOS_EMAILRef;
    public String SOS_Email = "";

    public DatabaseReference C_SEATBELT_Ref;
    public String C_SEATBELT = "";

    public DatabaseReference C_DROWSINESS_REF;
    public String C_DROWSINESS = "";

    public DatabaseReference C_ECO_Ref;
    public String C_ECO = "";

    public DatabaseReference C_BACKGROUND_Ref;
    public String C_BACKGROUND = "";


    private static final  int MY_PERMISSIONS_REQUEST_ACCES_COARSE_LOCATION = 1;


    public DatabaseReference Reset_DTCRef;
    public String Reset_DTC = "";


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
                for(APP_USER u: app_usersArrayList){
                    if (u.getEmail().equals(user.getEmail())) {

                        currentSignedUserName = u.getName();
                    }
                }
                directUserLReadings(currentSignedUserName);
                directUserLDTCs(currentSignedUserName);
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
                    currentSignedEmail = email;
                    currentSignedUserName = user.getName();
                    directUserLReadings(currentSignedUserName);
                    directUserLDTCs(currentSignedUserName);
                    setUserSettings(currentSignedUserName);
                    setUserResetDTC(currentSignedUserName);
                    directUserHistory(currentSignedUserName);
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void setUserResetDTC(String username){
        Reset_DTCRef = databaseReference.child("RESET_DTC").child(username);
        Reset_DTCRef.keepSynced(true);


        Reset_DTCRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Reset_DTC = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setUserSettings(String username){
        SETTINGSRef = databaseReference.child("SETTINGS").child(username);
        SETTINGSRef.keepSynced(true);

        SOS_EMAILRef = SETTINGSRef.child("SOS_EMAIL");
        SOS_EMAILRef.keepSynced(true);
        SOS_EMAILRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SOS_Email = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        C_BACKGROUND_Ref = SETTINGSRef.child("C_BACKGROUND");
        C_BACKGROUND_Ref.keepSynced(true);
        C_BACKGROUND_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                C_BACKGROUND = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        C_DROWSINESS_REF = SETTINGSRef.child("C_DROWSINESS");
        C_DROWSINESS_REF.keepSynced(true);
        C_DROWSINESS_REF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                C_DROWSINESS = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        C_ECO_Ref = SETTINGSRef.child("C_ECO");
        C_ECO_Ref.keepSynced(true);
        C_ECO_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                C_ECO = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        C_SEATBELT_Ref = SETTINGSRef.child("C_SEATBELT");
        C_SEATBELT_Ref.keepSynced(true);
        C_SEATBELT_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                C_SEATBELT = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public String getSOS_Email() {
        return SOS_Email;
    }

    public void directUserLDTCs(String username){
        L_DTCSRef = databaseReference.child("L_DTCS").child(username);
        L_DTCSRef.keepSynced(true);
        L_DTCSRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<String>> type = new GenericTypeIndicator<ArrayList<String>>() {};
                L_DTCS_arraylist = dataSnapshot.getValue(type);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void directUserHistory(String username){
        HISTORYRef = databaseReference.child("HISTORY").child(username);
        HISTORYRef.keepSynced(true);

        H_COOLANT_TEMPERATURERef = HISTORYRef.child("H_COOLANT_TEMPERATURE");
        H_COOLANT_TEMPERATURERef.keepSynced(true);
        H_COOLANT_TEMPERATURERef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                H_COOLANT_TEMPERATURE = dataSnapshot.getValue(String.class);
                Type type = new TypeToken<ArrayList<String>>(){}.getType();
                H_COOLANT_TEMPERATURE_LIST = new Gson().fromJson(H_COOLANT_TEMPERATURE, type);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        H_ENGINE_LOADRef = HISTORYRef.child("H_ENGINE_LOAD");
        H_ENGINE_LOADRef.keepSynced(true);
        H_ENGINE_LOADRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                H_ENGINE_LOAD = dataSnapshot.getValue(String.class);
                Type type = new TypeToken<ArrayList<String>>(){}.getType();
                H_ENGINE_LOAD_LIST = new Gson().fromJson(H_ENGINE_LOAD, type);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        H_INTAKE_PRESSURERef = HISTORYRef.child("H_INTAKE_PRESSURE");
        H_INTAKE_PRESSURERef.keepSynced(true);
        H_INTAKE_PRESSURERef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                H_INTAKE_PRESSURE = dataSnapshot.getValue(String.class);
                Type type = new TypeToken<ArrayList<String>>(){}.getType();
                H_INTAKE_PRESSURE_LIST = new Gson().fromJson(H_INTAKE_PRESSURE, type);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        H_RPMRef = HISTORYRef.child("H_RPM");
        H_RPMRef.keepSynced(true);
        H_RPMRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                H_RPM = dataSnapshot.getValue(String.class);
                Type type = new TypeToken<ArrayList<String>>(){}.getType();
                H_RPM_LIST = new Gson().fromJson(H_RPM, type);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        H_SPEEDRef = HISTORYRef.child("H_SPEED");
        H_SPEEDRef.keepSynced(true);
        H_SPEEDRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                H_SPEED = dataSnapshot.getValue(String.class);
                Type type = new TypeToken<ArrayList<String>>(){}.getType();
                H_SPEED_LIST = new Gson().fromJson(H_SPEED, type);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        H_THROTTLE_POSITIONRef = HISTORYRef.child("H_THROTTLE_POSITION");
        H_THROTTLE_POSITIONRef.keepSynced(true);
        H_THROTTLE_POSITIONRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                H_THROTTLE_POSITION = dataSnapshot.getValue(String.class);
                Type type = new TypeToken<ArrayList<String>>(){}.getType();
                H_THROTTLE_POSITION_LIST = new Gson().fromJson(H_THROTTLE_POSITION, type);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


        public void directUserLReadings(String username){
        L_ReadingsRef = databaseReference.child("L_READINGS").child(username);
        L_ReadingsRef.keepSynced(true);

        L_COOLANT_TEMPRef = L_ReadingsRef.child("L_COOLANT_TEMP");
        L_COOLANT_TEMPRef.keepSynced(true);
        L_COOLANT_TEMPRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                L_COOLANT_TEMP = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        L_FUEL_PRESSURERef = L_ReadingsRef.child("L_FUEL_PRESSURE");
        L_FUEL_PRESSURERef.keepSynced(true);
        L_FUEL_PRESSURERef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                L_FUEL_PRESSURE = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        L_INTAKE_PRESSURERef = L_ReadingsRef.child("L_INTAKE_PRESSURE");
        L_INTAKE_PRESSURERef.keepSynced(true);
        L_INTAKE_PRESSURERef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                L_INTAKE_PRESSURE = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        L_RPMRef = L_ReadingsRef.child("L_RPM");
        L_RPMRef.keepSynced(true);
        L_RPMRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                L_RPM = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        L_SPEEDRef = L_ReadingsRef.child("L_SPEED");
        L_SPEEDRef.keepSynced(true);
        L_SPEEDRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                L_SPEED = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        L_TIMING_ADVANCERef = L_ReadingsRef.child("L_TIMING_ADVANCE");
        L_TIMING_ADVANCERef.keepSynced(true);
        L_TIMING_ADVANCERef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                L_TIMING_ADVANCE = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        L_INTAKE_TEMPRef = L_ReadingsRef.child("L_INTAKE_TEMP");
        L_INTAKE_TEMPRef.keepSynced(true);
        L_INTAKE_TEMPRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                L_INTAKE_TEMP = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        L_MAFRef = L_ReadingsRef.child("L_MAF");
        L_MAFRef.keepSynced(true);
        L_MAFRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                L_MAF = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        L_ENGINE_LOADRef = L_ReadingsRef.child("L_ENGINE_LOAD");
        L_ENGINE_LOADRef.keepSynced(true);
        L_ENGINE_LOADRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                L_ENGINE_LOAD = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        L_THROTTLE_POSRef = L_ReadingsRef.child("L_THROTTLE_POS");
        L_THROTTLE_POSRef.keepSynced(true);
        L_THROTTLE_POSRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                L_THROTTLE_POS = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        L_FUEL_LEVELRef = L_ReadingsRef.child("L_FUEL_LEVEL");
        L_FUEL_LEVELRef.keepSynced(true);
        L_FUEL_LEVELRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                L_FUEL_LEVEL = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        L_FUEL_TYPERef = L_ReadingsRef.child("L_FUEL_TYPE");
        L_FUEL_TYPERef.keepSynced(true);
        L_FUEL_TYPERef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                L_FUEL_TYPE = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        L_OIL_TEMPRef = L_ReadingsRef.child("L_OIL_TEMP");
        L_OIL_TEMPRef.keepSynced(true);
        L_OIL_TEMPRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                L_OIL_TEMP = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        L_FUEL_INJECT_TIMINGRef = L_ReadingsRef.child("L_FUEL_INJECT_TIMING");
        L_FUEL_INJECT_TIMINGRef.keepSynced(true);
        L_FUEL_INJECT_TIMINGRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                L_FUEL_INJECT_TIMING = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        L_FUEL_RATERef = L_ReadingsRef.child("L_FUEL_RATE");
        L_FUEL_RATERef.keepSynced(true);
        L_FUEL_RATERef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                L_FUEL_RATE = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


  public void onRequestPermissionResult (int RequestCode , String [] permissions, int []grantResults){
           if(RequestCode == MY_PERMISSIONS_REQUEST_ACCES_COARSE_LOCATION){
               if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

               }
               else{

               }
           }
  }

}
