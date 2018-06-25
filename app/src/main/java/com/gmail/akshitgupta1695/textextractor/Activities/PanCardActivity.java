package com.gmail.akshitgupta1695.textextractor.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.akshitgupta1695.textextractor.PostProcessing.PanProcessing;
import com.gmail.akshitgupta1695.textextractor.R;
import com.gmail.akshitgupta1695.textextractor.Utilities.CameraUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class PanCardActivity extends AppCompatActivity {

    private Button ocr;
    private ImageView imageview;
    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    private Bitmap mImageBitmap;
    private Button reset;
    private EditText name;
    private EditText dateOfBirth;
    private EditText fatherName;
    private EditText panNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pan_card);
        ocr=(Button)findViewById(R.id.ocr);
        imageview=findViewById(R.id.imageview);
        reset=findViewById(R.id.reset);
        reset.setText("R"+"\n"+"E"+"\n"+"S"+"\n"+"E"+"\n"+"T");
        name=findViewById(R.id.name_edit_text);
        dateOfBirth=findViewById(R.id.dob_edit_text);
        fatherName=findViewById(R.id.father_name_edit_text);
        panNumber=findViewById(R.id.pan_edit_text);
    }

    public void takePicture(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = CameraUtils.createImageFile(this);
                mCurrentPhotoPath=photoFile.getAbsolutePath();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this,"Error creating file",Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            mImageBitmap = CameraUtils.getBitmap(mCurrentPhotoPath);
            imageview.setImageBitmap(mImageBitmap);
            reset.setVisibility(View.VISIBLE);

        }
    }

    public void reset(View view) {
        mImageBitmap=null;
        imageview.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.camera));
        reset.setVisibility(View.GONE);
    }

    public void detectText(View view) {

        if(mImageBitmap!=null){

            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(mImageBitmap);

            FirebaseVisionTextDetector detector = FirebaseVision.getInstance().getVisionTextDetector();

            detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                @Override
                public void onSuccess(FirebaseVisionText firebaseVisionText) {
                    HashMap<String,String> dataMap=new PanProcessing().processText(firebaseVisionText,getApplicationContext());
                    presentOutput(dataMap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
        else {
            Toast.makeText(this,"Please Take a Picture first",Toast.LENGTH_SHORT).show();
        }

    }

    private void presentOutput(HashMap<String ,String > dataMap){

        if(dataMap!=null){
            name.setText(dataMap.get("name"), TextView.BufferType.EDITABLE);
            fatherName.setText(dataMap.get("fatherName"), TextView.BufferType.EDITABLE);
            panNumber.setText(dataMap.get("pan"), TextView.BufferType.EDITABLE);
            dateOfBirth.setText(dataMap.get("dob"), TextView.BufferType.EDITABLE);
        }
    }


}
