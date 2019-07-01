package Services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
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

public class DrowsinessDetection {






    public static ArrayList<String> detect(Context context, Bitmap bitmap){

        final ArrayList<String> resultList = new ArrayList<>();

//        BitmapDrawable drawable = (BitmapDrawable) context.getDrawable(R.drawable.salah);
//        Bitmap bitmap = drawable.getBitmap();

        // Real-time contour detection of multiple faces
//        FirebaseVisionFaceDetectorOptions realTimeOpts =
//                new FirebaseVisionFaceDetectorOptions.Builder()
//                        .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
//                        .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
//                        .build();

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
                                            Rect bounds = face.getBoundingBox();
                                            float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
                                            float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees

                                            // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                                            // nose available):
//                                            System.out.println(face.getRightEyeOpenProbability());
//                                            System.out.println(face.getSmilingProbability());



                                            // If classification was enabled:
                                            if (face.getRightEyeOpenProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                                                float rightEyeOpenProb = face.getRightEyeOpenProbability();
                                                System.out.println(rightEyeOpenProb);
                                                resultList.add(String.valueOf(rightEyeOpenProb));

                                            }else{
                                                resultList.add("0");

                                            }

                                            if (face.getLeftEyeOpenProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                                                float leftEyeOpenProb = face.getLeftEyeOpenProbability();
                                                System.out.println(leftEyeOpenProb);
                                                resultList.add(String.valueOf(leftEyeOpenProb));

                                            }else{
                                                resultList.add("0");

                                            }


                                        }
                                        if(resultList.size()<2){
                                            resultList.add("0");
                                            resultList.add("0");
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
                                        resultList.add("0");
                                        resultList.add("0");

                                    }
                                });


        while(resultList.size()<2){
            System.out.println("Detecting");
        }
        return resultList;


    }
}
