package com.gmail.akshitgupta1695.textextractor.PostProcessing;

import android.content.Context;
import android.graphics.Rect;
import android.widget.Toast;

import com.google.firebase.ml.vision.text.FirebaseVisionText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by b0201234 on 6/26/18.
 */

public class AadhaarProcessing {

    public HashMap<String,String> processExtractedTextForFrontPic(FirebaseVisionText text, Context context) {

        List<FirebaseVisionText.Block> blocks = text.getBlocks();

        if (blocks.size() == 0) {
            Toast.makeText(context, "No Text :(", Toast.LENGTH_LONG).show();
            return null;
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
        HashMap<String,String > dataMap=new HashMap<>();
        int i=0;
        String regex="\\d\\d\\d\\d([,\\s])?\\d\\d\\d\\d.*";
        for(i=0;i<orderedData.size();i++){

            if(orderedData.get(i).matches(regex)){
                dataMap.put("aadhaar",orderedData.get(i));
                break;
            }

        }
        //setting gender first
        for(i=0;i<orderedData.size();i++){

            if(orderedData.get(i).contains("female")){
                dataMap.put("gender","Female");
                break;
            }
            else if(orderedData.get(i).contains("male")){
                dataMap.put("gender","Male");
                break;
            }
        }

        if(!dataMap.containsKey("aadhaar")){
            if(i+1<orderedData.size())
                dataMap.put("aadhaar",orderedData.get(i+1));
        }
        //searching for father
        for(i=0;i<orderedData.size();i++){

            if(orderedData.get(i).contains("father")){
                dataMap.put("fatherName",orderedData.get(i).replace("father","").replace(":",""));
                break;
            }

        }
        if(dataMap.containsKey("fatherName")){
            if(i-2>-1){
               dataMap.put("name",orderedData.get(i-2));
            }
        }

        for(i=0;i<orderedData.size();i++){

            if(orderedData.get(i).contains("birth")){
                dataMap.put("yob",orderedData.get(i).substring(orderedData.get(i).length()-4));
                break;
            }

        }

        if(i-1>-1 && !orderedData.get(i-1).contains("father"))
            dataMap.put("name",orderedData.get(i-1));

        return dataMap;

    }

    public HashMap<String,String> processExtractedTextForBackPic(FirebaseVisionText text,Context context) {

        List<FirebaseVisionText.Block> blocks = text.getBlocks();

        if (blocks.size() == 0) {
            Toast.makeText(context, "No Text :(", Toast.LENGTH_LONG).show();
            return null;
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
        HashMap<String,String> dataMap=new HashMap<>();
        int i=0;
        for(i=0;i<orderedData.size();i++){

            if(orderedData.get(i).contains("address")){
                dataMap.put("addressLine1",orderedData.get(i).replaceAll(".*address","").replace(":",""));
                break;
            }

        }

        if(i+1<orderedData.size())
            dataMap.put("addressLine2",orderedData.get(i+1));

        //for pin code
        String regex="\\d\\d\\d\\d\\d\\d";
        for( i=0;i<orderedData.size();i++){

            if(orderedData.get(i).matches(regex) || orderedData.contains(".*"+regex)){
                dataMap.put("pincode",orderedData.get(i));
                break;
            }

        }
        return dataMap;

    }

}
