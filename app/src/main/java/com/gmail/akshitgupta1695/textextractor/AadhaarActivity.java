package com.gmail.akshitgupta1695.textextractor;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;
import java.util.TreeMap;

public class AadhaarActivity extends AppCompatActivity {

    private ImageView frontImageView;
    private ImageView backImageView;
    String mCurrentPhotoPath;
    String mCurrentPhotoPath2;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_TAKE_PHOTO_2 = 2;
    private Bitmap mImageBitmap;
    private Bitmap mImageBitmap2;
    private EditText name;
    private EditText yearOfBirth;
    private EditText gender;
    private EditText aadhaarNumber;
    private EditText pincode;
    private EditText addressLine1;
    private EditText addressLine2;
    private EditText fatherName;
    private Button reset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aadhaar);
        frontImageView =(ImageView)findViewById(R.id.imageview);
        backImageView=findViewById(R.id.imageview2);
        name=findViewById(R.id.name_edit_text);
        yearOfBirth=findViewById(R.id.yob_edit_text);
        gender=findViewById(R.id.gender_edit_text);
        aadhaarNumber=findViewById(R.id.aadhar_no_edit_text);
        pincode=findViewById(R.id.pincode_edit_text);
        addressLine1=findViewById(R.id.address_line1_edit_text);
        addressLine2=findViewById(R.id.address_line2_edit_text);
        fatherName=findViewById(R.id.father_name_edit_text);
        reset=findViewById(R.id.reset);
        reset.setText("R"+"\n"+"E"+"\n"+"S"+"\n"+"E"+"\n"+"T");


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

    public void takeBackPicture(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = CameraUtils.createImageFile(this);
                mCurrentPhotoPath2=photoFile.getAbsolutePath();
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
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO_2);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            mImageBitmap = CameraUtils.getBitmap(mCurrentPhotoPath);
            frontImageView.setImageBitmap(mImageBitmap);
            reset.setVisibility(View.VISIBLE);

        }
        else if (requestCode == REQUEST_TAKE_PHOTO_2 && resultCode == RESULT_OK) {

            mImageBitmap2 = CameraUtils.getBitmap(mCurrentPhotoPath2);
            backImageView.setImageBitmap(mImageBitmap2);
            reset.setVisibility(View.VISIBLE);

        }
    }

    public void reset(View view){
        mImageBitmap=null;
        mImageBitmap2=null;
        backImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.aadhaar_camera_back));
        frontImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.aadhaar_camera_front));
        reset.setVisibility(View.GONE);
    }

    public void extractInfo(View view){
        if(mImageBitmap!=null && mImageBitmap2!=null) {


            FirebaseVisionTextDetector detector = FirebaseVision.getInstance().getVisionTextDetector();
            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(mImageBitmap);
            detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                @Override
                public void onSuccess(FirebaseVisionText firebaseVisionText) {
                    processExtractedTextForFrontPic(firebaseVisionText);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
            FirebaseVisionImage image2 = FirebaseVisionImage.fromBitmap(mImageBitmap2);
            detector.detectInImage(image2).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                @Override
                public void onSuccess(FirebaseVisionText firebaseVisionText) {
                    processExtractedTextForBackPic(firebaseVisionText);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }
        else {
            Toast.makeText(this,"Please Take Both front and Back Pics first",Toast.LENGTH_SHORT).show();
        }


    }

    private void processExtractedTextForFrontPic(FirebaseVisionText text) {

        List<FirebaseVisionText.Block> blocks = text.getBlocks();

        if (blocks.size() == 0) {
            Toast.makeText(AadhaarActivity.this, "No Text :(", Toast.LENGTH_LONG).show();
            return;
        }
        TreeMap<String,String> map=new TreeMap<>();

        for (FirebaseVisionText.Block block : text.getBlocks()) {
            for(FirebaseVisionText.Line line:block.getLines()){
                Rect rect=line.getBoundingBox();
                String y=String.valueOf(rect.exactCenterY());
                String lineTxt=line.getText().toLowerCase();
                map.put(y,lineTxt);
            }

        }

        List<String> orderedData=new ArrayList<>(map.values());

        int i=0;
        String regex="\\d\\d\\d\\d([,\\s])?\\d\\d\\d\\d.*";
        for(i=0;i<orderedData.size();i++){

            if(orderedData.get(i).matches(regex)){
                aadhaarNumber.setText(orderedData.get(i), TextView.BufferType.EDITABLE);
                break;
            }

        }
        //setting gender first
        for(i=0;i<orderedData.size();i++){

            if(orderedData.get(i).contains("female")){
                gender.setText("Female", TextView.BufferType.EDITABLE);
                break;
            }
            else if(orderedData.get(i).contains("male")){
                gender.setText("Male", TextView.BufferType.EDITABLE);
                break;
            }
        }

        if(aadhaarNumber.getText()==null){
            if(i+1<orderedData.size())
            aadhaarNumber.setText(orderedData.get(i+1),TextView.BufferType.EDITABLE);
        }
        //searching for father
        for(i=0;i<orderedData.size();i++){

            if(orderedData.get(i).contains("father")){
                fatherName.setText(orderedData.get(i).replace("father","").replace(":",""), TextView.BufferType.EDITABLE);
                break;
            }

        }

        for(i=0;i<orderedData.size();i++){

            if(orderedData.get(i).contains("birth")){
                yearOfBirth.setText(orderedData.get(i).substring(orderedData.get(i).length()-4), TextView.BufferType.EDITABLE);
                break;
            }

        }

        if(i-1>-1 && !orderedData.get(i-1).contains("father"))
        name.setText(orderedData.get(i-1), TextView.BufferType.EDITABLE);

    }

    private void processExtractedTextForBackPic(FirebaseVisionText text) {

        List<FirebaseVisionText.Block> blocks = text.getBlocks();

        if (blocks.size() == 0) {
            Toast.makeText(AadhaarActivity.this, "No Text :(", Toast.LENGTH_LONG).show();
            return;
        }
        TreeMap<String,String> map=new TreeMap<>();

        for (FirebaseVisionText.Block block : text.getBlocks()) {
            for(FirebaseVisionText.Line line:block.getLines()){
                Rect rect=line.getBoundingBox();
                String y=String.valueOf(rect.exactCenterY());
                String lineTxt=line.getText().toLowerCase();
                map.put(y,lineTxt);
            }

        }

        List<String> orderedData=new ArrayList<>(map.values());


        //for address first line
        int i=0;
        for(i=0;i<orderedData.size();i++){

            if(orderedData.get(i).contains("address")){
                addressLine1.setText(orderedData.get(i).replaceAll(".*address","").replace(":",""), TextView.BufferType.EDITABLE);
                break;
            }

        }

        if(i+1<orderedData.size())
            addressLine2.setText(orderedData.get(i+1),TextView.BufferType.EDITABLE);

        //for pin code
        String regex="\\d\\d\\d\\d\\d\\d";
        for( i=0;i<orderedData.size();i++){

            if(orderedData.get(i).matches(regex) || orderedData.contains(".*"+regex)){
                pincode.setText(orderedData.get(i), TextView.BufferType.EDITABLE);
                break;
            }

        }




    }


}
