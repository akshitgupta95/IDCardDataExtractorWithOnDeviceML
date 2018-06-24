package com.gmail.akshitgupta1695.textextractor.Utilities;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by b0201234 on 6/24/18.
 */

public class CameraUtils {

    public static File createImageFile( Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents

        return image;
    }

    public static Bitmap getBitmap(String path) {
        try {

            File f= new File(path);
            Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }}
}
