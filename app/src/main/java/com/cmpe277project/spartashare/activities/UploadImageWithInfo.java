package com.cmpe277project.spartashare.activities;

import android.media.ExifInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cmpe277project.spartashare.R;
import com.cmpe277project.spartashare.RegisterUserActivity;
import com.cmpe277project.spartashare.util.GetAbsoluteImagePath;
import com.facebook.TestSession;
import com.raweng.built.BuiltACL;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltFile;
import com.raweng.built.BuiltFileUploadCallback;
import com.raweng.built.BuiltObject;
import com.raweng.built.BuiltResultCallBack;

import android.net.Uri;
import android.widget.Toast;

import java.io.IOException;


public class UploadImageWithInfo extends ActionBarActivity {
    Uri imageUri;
    String imagePath;
    EditText caption;
    TextView location;
    EditText tags;
    Spinner directory;
    ImageView uploadImage;
    Button upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image_with_info);
        Bundle bundle = getIntent().getExtras();

        caption = (EditText) findViewById(R.id.et_caption);
        location = (TextView) findViewById(R.id.et_location);
        tags = (EditText) findViewById(R.id.et_tags);
        directory = (Spinner) findViewById(R.id.sr_directory);
        uploadImage = (ImageView) findViewById(R.id.iv_uploadImage);
        imageUri = Uri.parse(bundle.getString("ImageURI"));
        uploadImage.setImageURI(imageUri);
        System.out.println("Inside UploadImageWithInfo onCreate");
        imagePath = GetAbsoluteImagePath.getInstance().getPath(UploadImageWithInfo.this, imageUri);
        upload = (Button) findViewById(R.id.btn_upload);
        getImageMetadata();
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Inside onclick");
                handleUploadClick(imagePath);
            }
        });
    }

    private void handleUploadClick(String path) {
        System.out.println("Inside handleUploadClick");
        final BuiltFile builtFileObject = new BuiltFile();

// you may obtain the file path using the picker object
// that is bundled with the built.io UI elements package
// or you may use any third party picker like OI File Manager

        builtFileObject.setFile(path);
        builtFileObject.save(new BuiltFileUploadCallback() {
            @Override
            public void onSuccess() {
                System.out.println("Uploaded Image Uid " + builtFileObject.getUploadUid() + " URL " + builtFileObject.getUploadUrl());
                sendObject(builtFileObject.getUploadUid(), builtFileObject.getUploadUrl());
            }

            @Override
            public void onProgress(int i) {

            }

            @Override
            public void onError(BuiltError builtErrorObject) {
                // there was an error in creating the object
                // builtErrorObject will contain more details
                // will return the error message
                Log.i("error: ", "" + builtErrorObject.getErrorMessage());
                // will return the error code
                Log.i("error: ", "" + builtErrorObject.getErrorCode());
                // will return a detailed error report
                Log.i("error: ", "" + builtErrorObject.getErrors());
            }

            @Override
            public void onAlways() {

            }
        });
    }

    private void sendObject(String imageUID, String imageURL) {
        BuiltObject obj = new BuiltObject("images");
        obj.set("caption", caption.getText().toString());
        obj.set("imglocation", location.getText().toString());
        obj.set("imgtags", tags.getText().toString());
        obj.set("actualimage", imageUID);
        obj.set("actualimageurl", imageURL);
        obj.set("dirno","0");

       // BuiltACL acl = new BuiltACL();
       // acl.setUserWriteAccess(RegisterUserActivity.uid,true);
        //obj.setACL(new BuiltACL().setUserDeleteAccess("blt3561d7fc3b8d68ed",true));
// "student_name" is the field uid
        obj.save(new BuiltResultCallBack() {
            @Override
            public void onSuccess() {
                // object is created successfully
                Toast.makeText(UploadImageWithInfo.this, "Object Sent to Server", Toast.LENGTH_SHORT).show();
                //getObject();
            }

            @Override
            public void onError(BuiltError builtErrorObject) {
                // there was an error in creating the object
                // builtErrorObject will contain more details
                // will return the error message
                Log.i("error: ", "" + builtErrorObject.getErrorMessage());
                // will return the error code
                Log.i("error: ", "" + builtErrorObject.getErrorCode());
                // will return a detailed error report
                Log.i("error: ", "" + builtErrorObject.getErrors());
            }

            @Override
            public void onAlways() {
                // write code here that you want to execute
                // regardless of success or failure of the operation
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_upload_image_with_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getImageMetadata(){
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //ShowExif(exif);
        String myAttribute="Exif information ---\n";
        String locationInfo = "\n";
        myAttribute += getTagString(ExifInterface.TAG_DATETIME, exif);

        myAttribute += getTagString(ExifInterface.TAG_FLASH, exif);

        myAttribute += getTagString(ExifInterface.TAG_GPS_LATITUDE, exif);
        myAttribute += getTagString(ExifInterface.TAG_GPS_LATITUDE_REF, exif);
        myAttribute += getTagString(ExifInterface.TAG_GPS_LONGITUDE, exif);
        myAttribute += getTagString(ExifInterface.TAG_GPS_LONGITUDE_REF, exif);
        myAttribute += getTagString(ExifInterface.TAG_IMAGE_LENGTH, exif);
        myAttribute += getTagString(ExifInterface.TAG_IMAGE_WIDTH, exif);
        myAttribute += getTagString(ExifInterface.TAG_MAKE, exif);
        myAttribute += getTagString(ExifInterface.TAG_MODEL, exif);
        myAttribute += getTagString(ExifInterface.TAG_ORIENTATION, exif);
        myAttribute += getTagString(ExifInterface.TAG_WHITE_BALANCE, exif);
        System.out.println(myAttribute);

        locationInfo += getTagString(ExifInterface.TAG_GPS_LATITUDE,exif);
        locationInfo += getTagString(ExifInterface.TAG_GPS_LATITUDE_REF,exif);
        locationInfo += getTagString(ExifInterface.TAG_GPS_LONGITUDE,exif);
        locationInfo += getTagString(ExifInterface.TAG_GPS_LONGITUDE_REF,exif);
        setLocationTextView(locationInfo);
        //myTextView.setText(myAttribute);

    }

    private void setLocationTextView(String locInfo){
        location.setText(locInfo);
    }

    private String getTagString(String tag, ExifInterface exif){
        //System.out.println(tag + " : " + exif.getAttribute(tag) + "\n");
        return(tag + " : " + exif.getAttribute(tag) + "\n");
    }
}
