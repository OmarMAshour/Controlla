package com.controlla.controlla;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class capturingFrag extends Fragment {

    TextureView textureView;
    CameraDevice cameraDevice;
    private Button takepictureButton;
    String cameraId;
    android.util.Size imageDimensions;
    CaptureRequest.Builder captureRequestBuilder;
    CaptureRequest.Builder captureBuilder;
    CameraCaptureSession cameraSession;
    Handler backgroundHandler;
    HandlerThread handlerThread;
    private android.util.Size previewsize;
    private android.util.Size jpegSizes[] = null;
    private CaptureRequest.Builder previewBuilder;
    private CameraCaptureSession previewSession;
    Button getpicture;
    View view;
    Timer timer;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Capturing Image");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view= inflater.inflate(R.layout.fragment_capturing, container, false);
        textureView = (TextureView) view.findViewById(R.id.texture);
        textureView.setSurfaceTextureListener(surfaceTextureListener);
        getpicture = (Button) view.findViewById(R.id.cambtn);
        getpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPicture();
            }
        });
//        getpicture.setVisibility(0);

        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                getPicture();
//            }
//        }, 0,120000);

        return view;
    }


    public boolean CheckSeatbelt ( String s) throws JSONException {
        JSONObject jsonObj = new JSONObject(s);
        JSONArray jsonarr = jsonObj.getJSONArray("images");
        JSONObject obj = jsonarr.getJSONObject(0);
        JSONArray jsonarr1 = obj.getJSONArray("classifiers");
        JSONObject obj1 = jsonarr1.getJSONObject(0);
        JSONArray jsonarr2 = obj1.getJSONArray("classes");
        for (int i = 0; i < jsonarr2.length(); i++) {
            JSONObject ok = jsonarr2.getJSONObject(i);
            String Class = ok.getString("class");
            boolean seatbelt = false;
            if (seatbelt) {
                String Score = ok.getString("score");
                int score = Integer.parseInt(Score);
                if (score > 0.2) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    void getPicture() {
        if (cameraDevice == null) {
            return;
        }
        CameraManager manager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
            int width = 250, height = 250;
            if (jpegSizes != null && jpegSizes.length > 0) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
            final CaptureRequest.Builder capturebuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            capturebuilder.addTarget(reader.getSurface());
            capturebuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
            Log.d("THE Rotationnnn", "getPicture: "+ rotation);
            capturebuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
            ImageReader.OnImageAvailableListener imageAvailableListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = null;
                    try {
                        image = reader.acquireLatestImage();
//                        ImageView imageView = (ImageView) image;
                        ByteBuffer bufferr = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[bufferr.remaining()];
                        bufferr.get(bytes);
                        Bitmap myImg = BitmapFactory.decodeByteArray(bytes,0,bytes.length,null);
                        Matrix matrix = new Matrix();
                        matrix.postRotate(180);
                        Bitmap rotated = Bitmap.createBitmap(myImg, 0, 0, myImg.getWidth(), myImg.getHeight(),
                                matrix, true);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        rotated.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
//                        image.setImageBitmap(rotated);

                        save(byteArray);

                        IamOptions options = new IamOptions.Builder()
                                .apiKey("6bE1vwWoNgxGn6Y4WNG4w6C6tbMRtince_kohCsH2D5k")
                                .build();
                        VisualRecognition service = new VisualRecognition("2018-03-19", options);
                        try {
                            String FileName = "MyCameraApp/seatbelt.jpg";
                            String path  =Environment.getExternalStorageDirectory()+"/"+FileName;
                            File initialFile = new File(path);
                            InputStream imagesStream = new DataInputStream(new FileInputStream(initialFile));
                            ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
                                    .imagesFile(imagesStream)
                                    .imagesFilename("seatbelt.jpg")
                                    .threshold((float) 0.0)
                                    .classifierIds(Arrays.asList("default"))
                                    .build();

                            ClassifiedImages result = service.classify(classifyOptions).execute();
                            String strResult = result.toString();
                            System.out.println(strResult);
                            boolean seatbelt = CheckSeatbelt(strResult);

                            if(seatbelt){
                                Toast.makeText(getContext(), "Seat Belt Fastened", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getContext(), "Seat Belt Fastened", Toast.LENGTH_LONG).show();
                            }
          /*  for (int i=0;i<c.length();i++){
                JSONObject obj = c.getJSONObject(i);
                System.out.println(obj.);
            }*/
                            System.out.println(result);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } catch (Exception ee) {

                    } finally {
                        if (image != null)
                            image.close();
                    }
                }

                void save(byte[] bytes) {
                    File file12 = getOutputMediaFile();
                    OutputStream outputStream = null;
                    try {
                        outputStream = new FileOutputStream(file12);
                        outputStream.write(bytes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (outputStream != null)
                                outputStream.close();
                        } catch (Exception e) {
                        }
                    }
                }
            };
            HandlerThread handlerThread = new HandlerThread("takepicture");
            handlerThread.start();
            final Handler handler = new Handler(handlerThread.getLooper());
            reader.setOnImageAvailableListener(imageAvailableListener, handler);
            final CameraCaptureSession.CaptureCallback previewSSession = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frameNumber) {
                    super.onCaptureStarted(session, request, timestamp, frameNumber);
                }

                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    startCamera();
                }
            };
            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        session.capture(capturebuilder.build(), previewSSession, handler);
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            }, handler);
        } catch (Exception e) {
        }
    }

    public void openCamera() {
        CameraManager manager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
        try {
            String camerId = manager.getCameraIdList()[1];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(camerId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            previewsize = map.getOutputSizes(SurfaceTexture.class)[0];
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            manager.openCamera(camerId, stateCallback, null);
        }catch (Exception e)
        {
        }
    }

    private TextureView.SurfaceTextureListener surfaceTextureListener=new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera();
        }
        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        }
        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }
        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };
    private CameraDevice.StateCallback stateCallback=new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            cameraDevice=camera;
            startCamera();
        }
        @Override
        public void onDisconnected(CameraDevice camera) {
        }
        @Override
        public void onError(CameraDevice camera, int error) {
        }
    };
    @Override
    public void onPause() {
        super.onPause();
        if(cameraDevice!=null)
        {
            cameraDevice.close();
        }
    }
    void  startCamera()
    {
        if(cameraDevice==null||!textureView.isAvailable()|| previewsize==null)
        {
            return;
        }
        SurfaceTexture texture=textureView.getSurfaceTexture();
        if(texture==null)
        {
            return;
        }
        texture.setDefaultBufferSize(previewsize.getWidth(),previewsize.getHeight());
        Surface surface=new Surface(texture);
        try
        {
            previewBuilder=cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        }catch (Exception e)
        {
        }
        previewBuilder.addTarget(surface);
        try
        {
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    previewSession=session;
                    getChangedPreview();
                }
                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            },null);
        }catch (Exception e)
        {
        }
    }
    void getChangedPreview()
    {
        if(cameraDevice==null)
        {
            return;
        }
        previewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        HandlerThread thread=new HandlerThread("changed Preview");
        thread.start();
        Handler handler=new Handler(thread.getLooper());
        try
        {
            previewSession.setRepeatingRequest(previewBuilder.build(), null, handler);
        }catch (Exception e){}
    }


    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStorageDirectory(),
                "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "seatbelt" +  ".jpg");
        Log.d("khara", "getOutputMediaFile: " +mediaFile);
        return mediaFile;
    }

}
