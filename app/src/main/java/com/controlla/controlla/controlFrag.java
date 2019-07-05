package com.controlla.controlla;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import static com.controlla.controlla.MainActivity.firebaseManager;

public class controlFrag extends Fragment{

    EditText sosEditText;
    Button sosSaveBtn;
    CheckBox seatbeltCB;
    CheckBox ecoCB;
    CheckBox drowsinessCB;
    CheckBox backgroundCB;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Features Control");

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_control, container, false);


        sosEditText = view.findViewById(R.id.SOSEditText);
        sosSaveBtn = view.findViewById(R.id.sosSaveBtn);
        seatbeltCB = view.findViewById(R.id.seatbeltCB);
        ecoCB = view.findViewById(R.id.ecoCB);
        drowsinessCB = view.findViewById(R.id.drowsinessCB);
        backgroundCB = view.findViewById(R.id.backgroundCB);

        sosEditText.setText(firebaseManager.getSOS_Email());
        sosSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = sosEditText.getText().toString();
                firebaseManager.SOS_Email=value;
                firebaseManager.SOS_EMAILRef.setValue(value);
                Toast.makeText(getContext(), "SOS Emails Edited successfully", Toast.LENGTH_SHORT).show();
            }
        });

        if (firebaseManager.C_SEATBELT.equals("T")){
            seatbeltCB.setChecked(true);
        }

        if (firebaseManager.C_ECO.equals("T")){
            ecoCB.setChecked(true);
        }

        if (firebaseManager.C_DROWSINESS.equals("T")){
            drowsinessCB.setChecked(true);
        }

        if (firebaseManager.C_BACKGROUND.equals("T")){
            backgroundCB.setChecked(true);
        }

        seatbeltCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    firebaseManager.C_SEATBELT="T";
                    firebaseManager.C_SEATBELT_Ref.setValue("T");
                    Toast.makeText(getContext(), "Seatbelt Detection Enabled", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseManager.C_SEATBELT="F";
                    firebaseManager.C_SEATBELT_Ref.setValue("F");
                    Toast.makeText(getContext(), "Seatbelt Detection Disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });


        ecoCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    firebaseManager.C_ECO="T";
                    firebaseManager.C_ECO_Ref.setValue("T");
                    Toast.makeText(getContext(), "Eco Detection Enabled", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseManager.C_ECO="F";
                    firebaseManager.C_ECO_Ref.setValue("F");
                    Toast.makeText(getContext(), "Eco Detection Disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });


        backgroundCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    firebaseManager.C_BACKGROUND="T";
                    firebaseManager.C_BACKGROUND_Ref.setValue("T");
                    Toast.makeText(getContext(), "Background Car Check Enabled", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseManager.C_BACKGROUND="F";
                    firebaseManager.C_BACKGROUND_Ref.setValue("F");
                    Toast.makeText(getContext(), "Background Car Check Disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });


        drowsinessCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    firebaseManager.C_DROWSINESS="T";
                    firebaseManager.C_DROWSINESS_REF.setValue("T");
                    Toast.makeText(getContext(), "Drowsiness Detection Enabled", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseManager.C_DROWSINESS="F";
                    firebaseManager.C_DROWSINESS_REF.setValue("F");
                    Toast.makeText(getContext(), "Drowsiness Detection Disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });






        return view;
    }
}
