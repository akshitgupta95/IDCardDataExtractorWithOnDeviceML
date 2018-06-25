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

public class PanProcessing {

    public HashMap<String,String> processText(FirebaseVisionText text, Context context) {

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
                String lineTxt=line.getText();
                map.put(y,lineTxt);

            }

        }
        HashMap<String,String> dataMap=new HashMap<>();
        List<String> orderedData=new ArrayList<>(map.values());
        int i=0;
        for(i=0;i<orderedData.size();i++){

            if(orderedData.get(i).contains("/")){
                dataMap.put("dob",orderedData.get(i));
                break;
            }

        }
        if(i-1>-1)
            dataMap.put("fatherName",orderedData.get(i-1));
        if(i-2>-1)
            dataMap.put("name",orderedData.get(i-2));
        String regex="\\w\\w\\w\\w\\w\\d\\d\\d\\d.*";
        for(i=0;i<orderedData.size();i++){

            if(orderedData.get(i).matches(regex)){
                dataMap.put("pan",orderedData.get(i));
                break;
            }

        }
        return dataMap;

    }

}
