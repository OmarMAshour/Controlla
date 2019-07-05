package Services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.controlla.controlla.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.common.FirebaseVisionPoint;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark;

import java.util.ArrayList;
import java.util.List;

import static com.controlla.controlla.chatRoomFrag.*;
public class DrowsinessDetection {






    public static ArrayList<String> detect(Context context, Bitmap bitmap, final MediaPlayer player){

        final ArrayList<String> resultList = new ArrayList<>();

        FirebaseVisionFaceDetectorOptions realTimeOpts =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setPerformanceMode(FirebaseVisionFaceDetectorOptions.FAST)
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                        .build();

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance()
                .getVisionFaceDetector(realTimeOpts);

        Task<List<FirebaseVisionFace>> result =
                detector.detectInImage(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<FirebaseVisionFace>>() {
                                    @Override
                                    public void onSuccess(List<FirebaseVisionFace> faces) {
                                        // Task completed successfully
                                        // ...

                                        System.out.println(faces.size());


                                        for (FirebaseVisionFace face : faces) {



                                            boolean leftOpened = true;
                                            boolean rightOpened = true;

                                            boolean detectedLeftEye = false;
                                            boolean detectedRightEye = false;


                                            if (face.getRightEyeOpenProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                                                float rightEyeOpenProb = face.getRightEyeOpenProbability();
                                                System.out.println(rightEyeOpenProb);
                                                resultList.add(String.valueOf(rightEyeOpenProb));
                                                detectedRightEye = true;
                                                if(rightEyeOpenProb>0&&rightEyeOpenProb<=0.2){
                                                    rightOpened = false;
                                                }

                                            }else{
                                                resultList.add("-1");

                                            }

                                            if (face.getLeftEyeOpenProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                                                float leftEyeOpenProb = face.getLeftEyeOpenProbability();
                                                System.out.println(leftEyeOpenProb);
                                                resultList.add(String.valueOf(leftEyeOpenProb));
                                                detectedLeftEye = true;
                                                if(leftEyeOpenProb>0&&leftEyeOpenProb<=0.2){
                                                    leftOpened = false;
                                                }
                                            }else{
                                                resultList.add("-1");

                                            }

                                            if(!rightOpened && !leftOpened){
                                                drowsinessDetectionList.add("T");
                                            }else{
                                                if(drowsinessDetectionList.size()>=5 && detectedLeftEye && detectedRightEye){
                                                    player.pause();

//                                                    player.release();
                                                    drowsinessDetectionList.clear();

                                                }
                                            }

                                            if(drowsinessDetectionList.size()>=5 && !player.isPlaying()){
                                                player.start();
                                            }


                                        }
                                        if(resultList.size()<2){
                                            resultList.add("-1");
                                            resultList.add("-1");


                                        }

                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                        System.out.println("isA 5eeer ...........");
                                        resultList.add("-1");
                                        resultList.add("-1");

                                    }
                                });


        while(resultList.size()<2){
            System.out.println("Detecting");
        }
        return resultList;


    }
}
