package com.codewithsaurabh.facedetector;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btnCaptureImage;

    private final static int CODE_REQUEST_IMAGE_CAPTURE=124;
    public static String resultText="";
    private InputImage inputImage;
    private FaceDetector faceDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCaptureImage=findViewById(R.id.btnCaptureImage);

        FirebaseApp.initializeApp(this);

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCaptureImage=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intentCaptureImage,CODE_REQUEST_IMAGE_CAPTURE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CODE_REQUEST_IMAGE_CAPTURE && resultCode==RESULT_OK)
        {
            Bundle extras=data.getExtras();
            Bitmap bitmapImage=(Bitmap) extras.get("data");
            startDetection(bitmapImage);
        }
    }

    private void startDetection(Bitmap bitmapImage)
    {
        FaceDetectorOptions faceDetectorOptions =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .setMinFaceSize(0.20f)
                        .build();

        try {
            inputImage=InputImage.fromBitmap(bitmapImage,0);

            faceDetector=com.google.mlkit.vision.face.FaceDetection.getClient(faceDetectorOptions);
        }
        catch(Exception e)
        {
            e.getStackTrace();
            Toast.makeText(MainActivity.this, "Something gone wrong, sorry for inconvenience!!", Toast.LENGTH_SHORT).show();
        }

        faceDetector.process(inputImage).addOnSuccessListener(new OnSuccessListener<List<Face>>() {
            @Override
            public void onSuccess(List<Face> faces) {
                resultText="";
                int noOfFaces=0;

                for(Face face:faces)
                {
                    noOfFaces++;
                }

                resultText="Number of faces detected: "+noOfFaces;

                if(noOfFaces==0) Toast.makeText(MainActivity.this, "No faces detected!!", Toast.LENGTH_SHORT).show();
                else
                {
                    com.codewithsaurabh.facedetector.ResultDialog resultDialog=new com.codewithsaurabh.facedetector.ResultDialog();
                    resultDialog.show(getSupportFragmentManager(),"RESULT_DIALOG");
                }
            }
        });
    }
}