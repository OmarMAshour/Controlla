package com.controlla.controlla;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import Data.Weather;
import Services.GPSTracker;
import Services.GoogleMaps;
import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.controlla.controlla.MainActivity.currentWeather;
import static com.controlla.controlla.MainActivity.firebaseManager;

public class chatRoomFrag extends Fragment implements AIListener, View.OnClickListener {

    private List<Message> messages = new ArrayList<>();
    private MessageAdapter adapter;
    private FloatingActionButton voiceBTN;
    private TextView tvresult;
    private AIService aiService;
    private TextToSpeech t1;
    private static final int REQUEST_INTERNET = 200;
    private RecyclerView recyclerView;
    private TextView mTextMessage;
    private Timer ResetDTCTimer;
    String toConvert="";
    String myResponse="";
    JSONObject Json ;
    private ProgressBar ecoProgressBar;
    private TextView ecoTextView;
    private Timer ecoTimer;
    //    AIConfiguration config ;
    private ContextWrapper cw;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Chat Room");
        View view = inflater.inflate(R.layout.fragment_chat_room, container, false);
        voiceBTN = view.findViewById(R.id.button);
        mTextMessage = view.findViewById(R.id.message);
        BottomNavigationView navigation = view.findViewById(R.id.navigation);



        validateOS();
//        final AIConfiguration config = new AIConfiguration("8aa3d40d997a4af18221c98de5bd2b90", AIConfiguration.SupportedLanguages.English, AIConfiguration.RecognitionEngine.System);
        final AIConfiguration config = new AIConfiguration("7c6cc24aa2174ae6b78162caca232905  ", AIConfiguration.SupportedLanguages.English, AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this.getActivity(), config);

        voiceBTN.setOnClickListener(this);

        aiService.setListener(this);

        t1 = new TextToSpeech(this.getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });

        recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new MessageAdapter(messages, this);
        recyclerView.setAdapter(adapter);

        ResetDTCTimer = new Timer();
        ResetDTCTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                resetDTCDone();
            }
        }, 0, 1000);


        ecoProgressBar = view.findViewById(R.id.eco_progress_bar);
        ecoTextView = view.findViewById(R.id.eco_value_txt);

        ecoTimer = new Timer();

        ecoTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try{
                    setEcoData();
                }catch (Exception ex){
                    System.out.println(ex.getStackTrace());
                }
            }
        }, 0, 1000);
        return view;
    }

    private void setEcoData(){
        if(!firebaseManager.L_RPM.equals("NA") && !firebaseManager.L_RPM.equals("")){
            String rpmStr = firebaseManager.L_RPM.split(" ")[0];
            double rpm = Double.parseDouble(rpmStr);
            rpm -=2000;
            if(rpm<=0){
                ecoProgressBar.setProgress(100);
                ecoTextView.setText("Eco Percentage: 100%");
            }else if (rpm>=5000){
                ecoProgressBar.setProgress(0);
                ecoTextView.setText("Eco Percentage: 0%");

            }else{
                double value = 100 - ((rpm/5000)*100);
                int progressValue = (int) value;
                ecoProgressBar.setProgress(progressValue);
                ecoTextView.setText("Eco Percentage: "+progressValue+"%");
            }
        }
    }

    private void resetDTCDone(){
        if(firebaseManager.Reset_DTC.equals("DONE")){
            t1.speak("Reseting fault code has been done successfully", TextToSpeech.QUEUE_FLUSH, null, null);
            messages.add(new Message("Reseting fault code has been done successfully",true));
            adapter.notifyItemInserted(messages.size() - 1);
            recyclerView.smoothScrollToPosition(messages.size() -1);
            firebaseManager.Reset_DTC = " ";
            firebaseManager.Reset_DTCRef.setValue(firebaseManager.Reset_DTC);
        }
    }
    public void onMessageClick(final int position) {
//        messages.remove(position);
//        adapter.notifyItemRemoved(position);

    }

    public void validateOS() {
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_INTERNET);
        }
    }

    @Override
    public void onResult(AIResponse response) {

        Result result = response.getResult();
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + "," + entry.getValue();

            }
        }
        String message = result.getResolvedQuery() + ".";
        boolean isLeftMessage = false;
        messages.add(new Message(message, isLeftMessage));

        adapter.notifyItemInserted(messages.size() - 1);
        /*tvresult.setText("Query:"+result.getResolvedQuery()+
                "\nAction:  " + result.getAction()+
                "\nParameters:  "  + parameterString +
                "\nResult:  "+ result.getFulfillment().getSpeech());*/

        //JUST A COMMENT
        String respond = result.getFulfillment().getSpeech(); //+ " .";
        if(!respond.contains("Searching")){
            t1.speak(respond, TextToSpeech.QUEUE_FLUSH, null, null);
            messages.add(new Message(respond, true));
            adapter.notifyItemInserted(messages.size() - 1);
            recyclerView.smoothScrollToPosition(messages.size() - 1);
        }else{
            String searchWord="";
            if(respond.contains("Searching for")){
                String preMsg = messages.get(messages.size()-1).getMessage();
                String[] words = preMsg.split(" ");
                ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(words));
                ArrayList<String> searchWordsList = new ArrayList<>();
                searchWordsList.addAll(arrayList.subList(2, arrayList.size()));
                for(String str: searchWordsList){
                    searchWord+=str+" ";
                }
            }else if(respond.contains("Searching")){
                String preMsg = messages.get(messages.size()-1).getMessage();
                String[] words = preMsg.split(" ");
                ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(words));
                ArrayList<String> searchWordsList = new ArrayList<>();
                searchWordsList.addAll(arrayList.subList(1, arrayList.size()));
                for(String str: searchWordsList){
                    searchWord+=str+" ";
                }
            }



            OkHttpClient client = new OkHttpClient();
            String Url = "https://api.duckduckgo.com/?q="+searchWord+"&format=json";
            final Request request =  new Request.Builder().url(Url).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("failed");
                    myResponse = "No Result FOund";
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    System.out.println(response.isSuccessful());
                    if(response.isSuccessful()){
                        myResponse = response.body().string();
                        try {
                             Json = new JSONObject(myResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String searchResult= null;
                        String AbstractText = "";

                        try {
                            AbstractText =String.valueOf(Json.get("AbstractText"));
                            if(AbstractText.length()>3) {
                                searchResult = String.valueOf(Json.get("AbstractText"));
                            }
                            else{
                                boolean notnull=false;
                                JSONArray JsonArray = Json.getJSONArray("RelatedTopics");
                                System.out.println(JsonArray.length());
                                for (int i=0;i<JsonArray.length();i++){
                                    JSONObject JsonObject = JsonArray.getJSONObject(i);
                                    if(JsonObject !=null){
                                        searchResult = JsonObject.getString("Text");
                                        notnull=true;
                                        break;
                                    }
                                }
                                if(!notnull){
                                    searchResult="Not Found";
                                }



                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        t1.speak(searchResult, TextToSpeech.QUEUE_FLUSH, null, null);
                        messages.add(new Message(searchResult, true));
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyItemInserted(messages.size() - 1);
                                recyclerView.smoothScrollToPosition(messages.size() - 1);
                            }
                        });

                    }
                }
            });




        }


        if (respond.contains("Getting you")) {
            String value = getCorrespondingOBDValue(respond);
            t1.speak(value, TextToSpeech.QUEUE_FLUSH, null, null);
            messages.add(new Message(value, true));
            adapter.notifyItemInserted(messages.size() - 1);
            recyclerView.smoothScrollToPosition(messages.size() - 1);
        }


        if (respond.contains("SOS")) {

           // onGPS();
            GPSTracker gps = new GPSTracker(getContext());
             AppUtils.sendSOSEmail(gps.getLocation(getContext()));
        }

        if(respond.contains("weather")){
            Weather weather = currentWeather;
            String msg = weather.getDescription() +" with temperature of "+weather.getTemperature();
            t1.speak(msg, TextToSpeech.QUEUE_FLUSH, null, null);
            messages.add(new Message(msg, true));
            adapter.notifyItemInserted(messages.size() - 1);
            recyclerView.smoothScrollToPosition(messages.size() - 1);
        }




        if(respond.contains("Sending reset")){
            firebaseManager.Reset_DTC = "RESET";
            firebaseManager.Reset_DTCRef.setValue(firebaseManager.Reset_DTC);
        }

    }

    public String getCorrespondingOBDValue(String line) {
        String value = "";
        if (line.contains("Engine load")) {
            return firebaseManager.L_ENGINE_LOAD;
        }
        if (line.contains("Coolant Temperature")) {
            return firebaseManager.L_COOLANT_TEMP;
        }
        if (line.contains("Fuel Pressure")) {
            return firebaseManager.L_FUEL_PRESSURE;
        }
        if (line.contains("Intake Pressure")) {
            return firebaseManager.L_INTAKE_PRESSURE;
        }
        if (line.contains("Round Per Minute")) {
            return firebaseManager.L_RPM;
        }
        if (line.contains("Speed")) {
            return firebaseManager.L_SPEED;
        }
        if (line.contains("Timing Advance")) {
            return firebaseManager.L_TIMING_ADVANCE;
        }
        if (line.contains("Intake Temperature")) {
            return firebaseManager.L_INTAKE_TEMP;
        }
        if (line.contains("Airflow Rate")) {
            return firebaseManager.L_MAF;
        }
        if (line.contains("MAF")) {
            return firebaseManager.L_MAF;
        }
        if (line.contains("Throttle Position")) {
            return firebaseManager.L_THROTTLE_POS;
        }
        if (line.contains("Fuel Level")) {
            return firebaseManager.L_FUEL_LEVEL;
        }
        if (line.contains("Fuel Type")) {
            return firebaseManager.L_FUEL_TYPE;
        }
        if (line.contains("Oil Temperature")) {
            return firebaseManager.L_OIL_TEMP;
        }
        if (line.contains("Fuel Inject Timing")) {
            return firebaseManager.L_FUEL_INJECT_TIMING;
        }
        if (line.contains("Fuel Rate")) {
            return firebaseManager.L_FUEL_RATE;
        }
        if (line.contains("Intake Temperature.")) {
            return firebaseManager.L_INTAKE_TEMP;
        }
        if (line.contains("Round Per Minute.")) {
            return firebaseManager.L_RPM;
        }
        return value;
    }

    @Override
    public void onError(AIError error) {
        messages.add(new Message("Please try again.", true));
        t1.speak("Please try again", TextToSpeech.QUEUE_FLUSH, null, null);
        adapter.notifyItemInserted(messages.size() - 1);

        recyclerView.smoothScrollToPosition(messages.size() - 1);
    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                aiService.startListening();
                break;
        }
    }

    public void onPause() {
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
        super.onDestroy();
    }
    public void redirectToGoogleMaps(String Dest) {
        GoogleMaps GM = new GoogleMaps(getContext());
        String Origin = GM.GetCurrentAddress();
        System.out.println(Origin);
        Origin = Origin.replaceAll("\\s+", "+");
        Dest = Dest.replaceAll("\\s+" , "+");
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.google.com/maps/dir/?api=1&origin="+Origin+"&destination="+Dest+"&travelmode=driving"));
         startActivity(intent);

    }


}
