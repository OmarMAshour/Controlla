package com.controlla.controlla;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

import static com.controlla.controlla.MainActivity.firebaseManager;

public class historyFrag extends Fragment {

    ListView listView;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    private Spinner spinner;
    private GraphView graphView;
    View v;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("History");


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_history, container, false);
        spinner = v.findViewById(R.id.history_spinner);

        graphView =  v.findViewById(R.id.history_graph);
        graphView.setVisibility(View.VISIBLE);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("Coolant Temperature")){
                    setGraph(firebaseManager.H_COOLANT_TEMPERATURE_LIST);
                }else if(selectedItem.equals("Engine Load")){
                    setGraph(firebaseManager.H_ENGINE_LOAD_LIST);
                }else if(selectedItem.equals("Intake Pressure")){
                    setGraph(firebaseManager.H_INTAKE_PRESSURE_LIST);
                }else if(selectedItem.equals("RPM")){
                    setGraph(firebaseManager.H_RPM_LIST);
                }else if(selectedItem.equals("Speed")){
                    setGraph(firebaseManager.H_SPEED_LIST);
                }else if(selectedItem.equals("Throttle Position")){
                    setGraph(firebaseManager.H_THROTTLE_POSITION_LIST);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setGraph(firebaseManager.H_COOLANT_TEMPERATURE_LIST);


        return v;
    }

    private void setGraph(List<String> list){
        list =  list.subList(1, list.size());

        ArrayList<DataPoint> dataPoints = new ArrayList<>();
        int i;
        int value= 0;
        int max = 0;
        for(i =0; i<list.size(); i++){
            value= (int)Math.round(Double.parseDouble(list.get(i).split(" ")[0]));
            if(value>max){
                max = value;
            }
            dataPoints.add(new DataPoint(i,value));
            System.out.println(value);
        }


        DataPoint[] dataPointsArray = dataPoints.toArray(new DataPoint[dataPoints.size()]);
        LineGraphSeries<DataPoint> series = new LineGraphSeries < > (dataPointsArray);

        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMaxX(i);
        graphView.getViewport().setMinX(0);

        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMaxY(max);
        graphView.getViewport().setMinX(0);

        System.out.println("iiiiiiii ====>"+i);
        System.out.println("max ========>"+max);

        graphView.removeAllSeries();
        graphView.addSeries(series);
    }

}
