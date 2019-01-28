package com.controlla.controlla;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import static com.controlla.controlla.MainActivity.firebaseManager;


public class statusFrag extends Fragment {

    ListView listView;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    View v;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Car Status");
        v=inflater.inflate(R.layout.fragment_status, container, false);

        listView = (ListView) v.findViewById(R.id.obdReadingsList);
        arrayList.add("Engine Load: \n"+firebaseManager.L_ENGINE_LOAD);
        arrayList.add("RPM: \n"+firebaseManager.L_RPM);
        arrayList.add("Speed: \n"+firebaseManager.L_SPEED);
        arrayList.add("Throttle Position: \n"+firebaseManager.L_THROTTLE_POS);
        arrayList.add("Coolant Temperature: \n"+firebaseManager.L_COOLANT_TEMP);
        arrayList.add("Fuel Pressure: \n"+firebaseManager.L_FUEL_PRESSURE);
        arrayList.add("Intake Pressure: \n"+firebaseManager.L_INTAKE_PRESSURE);
        arrayList.add("Timing Advance: \n"+firebaseManager.L_TIMING_ADVANCE);
        arrayList.add("Air Flow Rate: \n"+firebaseManager.L_MAF);
        arrayList.add("Fuel Level: \n"+firebaseManager.L_FUEL_LEVEL);
        arrayList.add("Fuel Type: \n"+firebaseManager.L_FUEL_TYPE);
        arrayList.add("Oil Temperature: \n"+firebaseManager.L_OIL_TEMP);
        arrayList.add("Fuel Inject Timing \n"+firebaseManager.L_FUEL_INJECT_TIMING);
        arrayList.add("Engine Fuel Rate: \n"+firebaseManager.L_FUEL_RATE);
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);


        firebaseManager.L_ReadingsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                arrayList.clear();
                arrayList.add("Engine Load: \n"+firebaseManager.L_ENGINE_LOAD);
                arrayList.add("RPM: \n"+firebaseManager.L_RPM);
                arrayList.add("Speed: \n"+firebaseManager.L_SPEED);
                arrayList.add("Throttle Position: \n"+firebaseManager.L_THROTTLE_POS);
                arrayList.add("Coolant Temperature: \n"+firebaseManager.L_COOLANT_TEMP);
                arrayList.add("Fuel Pressure: \n"+firebaseManager.L_FUEL_PRESSURE);
                arrayList.add("Intake Pressure: \n"+firebaseManager.L_INTAKE_PRESSURE);
                arrayList.add("Timing Advance: \n"+firebaseManager.L_TIMING_ADVANCE);
                arrayList.add("Air Flow Rate: \n"+firebaseManager.L_MAF);
                arrayList.add("Fuel Level: \n"+firebaseManager.L_FUEL_LEVEL);
                arrayList.add("Fuel Type: \n"+firebaseManager.L_FUEL_TYPE);
                arrayList.add("Oil Temperature: \n"+firebaseManager.L_OIL_TEMP);
                arrayList.add("Fuel Inject Timing \n"+firebaseManager.L_FUEL_INJECT_TIMING);
                arrayList.add("Engine Fuel Rate: \n"+firebaseManager.L_FUEL_RATE);
                arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                arrayList.clear();
                arrayList.add("Engine Load: \n"+firebaseManager.L_ENGINE_LOAD);
                arrayList.add("RPM: \n"+firebaseManager.L_RPM);
                arrayList.add("Speed: \n"+firebaseManager.L_SPEED);
                arrayList.add("Throttle Position: \n"+firebaseManager.L_THROTTLE_POS);
                arrayList.add("Coolant Temperature: \n"+firebaseManager.L_COOLANT_TEMP);
                arrayList.add("Fuel Pressure: \n"+firebaseManager.L_FUEL_PRESSURE);
                arrayList.add("Intake Pressure: \n"+firebaseManager.L_INTAKE_PRESSURE);
                arrayList.add("Timing Advance: \n"+firebaseManager.L_TIMING_ADVANCE);
                arrayList.add("Air Flow Rate: \n"+firebaseManager.L_MAF);
                arrayList.add("Fuel Level: \n"+firebaseManager.L_FUEL_LEVEL);
                arrayList.add("Fuel Type: \n"+firebaseManager.L_FUEL_TYPE);
                arrayList.add("Oil Temperature: \n"+firebaseManager.L_OIL_TEMP);
                arrayList.add("Fuel Inject Timing \n"+firebaseManager.L_FUEL_INJECT_TIMING);
                arrayList.add("Engine Fuel Rate: \n"+firebaseManager.L_FUEL_RATE);
                arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                arrayList.add("Engine Load: \n"+firebaseManager.L_ENGINE_LOAD);
                arrayList.add("RPM: \n"+firebaseManager.L_RPM);
                arrayList.add("Speed: \n"+firebaseManager.L_SPEED);
                arrayList.add("Throttle Position: \n"+firebaseManager.L_THROTTLE_POS);
                arrayList.add("Coolant Temperature: \n"+firebaseManager.L_COOLANT_TEMP);
                arrayList.add("Fuel Pressure: \n"+firebaseManager.L_FUEL_PRESSURE);
                arrayList.add("Intake Pressure: \n"+firebaseManager.L_INTAKE_PRESSURE);
                arrayList.add("Timing Advance: \n"+firebaseManager.L_TIMING_ADVANCE);
                arrayList.add("Air Flow Rate: \n"+firebaseManager.L_MAF);
                arrayList.add("Fuel Level: \n"+firebaseManager.L_FUEL_LEVEL);
                arrayList.add("Fuel Type: \n"+firebaseManager.L_FUEL_TYPE);
                arrayList.add("Oil Temperature: \n"+firebaseManager.L_OIL_TEMP);
                arrayList.add("Fuel Inject Timing \n"+firebaseManager.L_FUEL_INJECT_TIMING);
                arrayList.add("Engine Fuel Rate: \n"+firebaseManager.L_FUEL_RATE);
                arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                arrayList.clear();
                arrayList.add("Engine Load: \n"+firebaseManager.L_ENGINE_LOAD);
                arrayList.add("RPM: \n"+firebaseManager.L_RPM);
                arrayList.add("Speed: \n"+firebaseManager.L_SPEED);
                arrayList.add("Throttle Position: \n"+firebaseManager.L_THROTTLE_POS);
                arrayList.add("Coolant Temperature: \n"+firebaseManager.L_COOLANT_TEMP);
                arrayList.add("Fuel Pressure: \n"+firebaseManager.L_FUEL_PRESSURE);
                arrayList.add("Intake Pressure: \n"+firebaseManager.L_INTAKE_PRESSURE);
                arrayList.add("Timing Advance: \n"+firebaseManager.L_TIMING_ADVANCE);
                arrayList.add("Air Flow Rate: \n"+firebaseManager.L_MAF);
                arrayList.add("Fuel Level: \n"+firebaseManager.L_FUEL_LEVEL);
                arrayList.add("Fuel Type: \n"+firebaseManager.L_FUEL_TYPE);
                arrayList.add("Oil Temperature: \n"+firebaseManager.L_OIL_TEMP);
                arrayList.add("Fuel Inject Timing \n"+firebaseManager.L_FUEL_INJECT_TIMING);
                arrayList.add("Engine Fuel Rate: \n"+firebaseManager.L_FUEL_RATE);
                arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }

}
