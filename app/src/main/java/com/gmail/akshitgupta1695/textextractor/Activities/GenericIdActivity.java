package com.gmail.akshitgupta1695.textextractor.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.akshitgupta1695.textextractor.PostProcessing.GenericProcessing;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class GenericIdActivity extends AppCompatActivity {


    private Button ocr;
    private ImageView imageview;
    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    private Bitmap mImageBitmap;
    private TextView mTextView;
    private CardView mCardView;
    private Button reset;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("CurrentPhotoPath",mCurrentPhotoPath);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genericid);
        ocr=(Button)findViewById(R.id.ocr);
        imageview=(ImageView)findViewById(R.id.imageview);
        mTextView=(TextView)findViewById(R.id.textView);
        mCardView=(CardView)findViewById(R.id.container_card_view);
        reset=findViewById(R.id.reset);
        reset.setText("R"+"\n"+"E"+"\n"+"S"+"\n"+"E"+"\n"+"T");
        if(savedInstanceState!=null){
            mCurrentPhotoPath=savedInstanceState.getString("CurrentPhotoPath");
            mImageBitmap=CameraUtils.getBitmap(mCurrentPhotoPath);
            imageview.setImageBitmap(mImageBitmap);
            reset.setVisibility(View.VISIBLE);
        }

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
                HashMap<String,String> dataMap=new GenericProcessing().processText(firebaseVisionText,getApplicationContext());
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
            mTextView.setText(dataMap.get("text"));
        }
    }



}
