package com.cmpe277project.spartashare.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by Varun on 4/25/2015.
 */
public class LoadImage extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public LoadImage(ImageView bmImage, String... urls) {
        this.bmImage = bmImage;
        this.execute(urls);
    }

    protected Bitmap doInBackground(String... urls) {
        Log.d("In AsyncTask 2", "URL:" + urls.toString());
        String urldisplay = urls[0];
        System.out.println(urls[0]);
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.toString());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }

}
