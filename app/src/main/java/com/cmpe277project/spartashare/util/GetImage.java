package com.cmpe277project.spartashare.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;

/**
 * Created by Varun on 4/25/2015.
 */
public class GetImage extends AsyncTask<String, Void, Bitmap> {
    private Bitmap image;

    public GetImage(String... urls) {
        this.execute(urls);
    }

    public Bitmap getImage() {
        return this.image;
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
        this.image = result;
    }

}

