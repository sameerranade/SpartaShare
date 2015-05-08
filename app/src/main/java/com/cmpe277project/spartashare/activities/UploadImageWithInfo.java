package com.cmpe277project.spartashare.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.ExifInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cmpe277project.spartashare.DAO.DatabaseHandler;
import com.cmpe277project.spartashare.R;
import com.cmpe277project.spartashare.RegisterUserActivity;
import com.cmpe277project.spartashare.models.Directory;
import com.cmpe277project.spartashare.models.DirectoryInfo;
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
import java.util.ArrayList;
import java.util.List;


public class UploadImageWithInfo extends Activity {
    Uri imageUri;
    String imagePath;
    EditText caption;
    TextView location;
    EditText tags;
    Spinner spinner;
    ImageView uploadImage;
    Button upload;
    String albumName = "Select Album";
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image_with_info);
        Bundle bundle = getIntent().getExtras();

        caption = (EditText) findViewById(R.id.et_caption);
        location = (TextView) findViewById(R.id.et_location);
        tags = (EditText) findViewById(R.id.et_tags);
        spinner = (Spinner) findViewById(R.id.sr_directory);
        uploadImage = (ImageView) findViewById(R.id.iv_uploadImage);
        imageUri = Uri.parse(bundle.getString("ImageURI"));
        uploadImage.setImageURI(imageUri);
        System.out.println("Inside UploadImageWithInfo onCreate");
        imagePath = GetAbsoluteImagePath.getInstance().getPath(UploadImageWithInfo.this, imageUri);
        db = new DatabaseHandler(this);
        Log.d("UploadImage", "above get all Directories");
        List<Directory> directories = db.getAllDirectories(RegisterUserActivity.email);
        List<String> directoryNames = new ArrayList<String>();
        directoryNames.add("Select Album");
        directoryNames.add("Create New Album");
        Log.d("UploadImage", "Above Directories");
        for (Directory cn : directories) {
            Log.d("UploadImage", "Inside Directories" + cn.getDirectoryName());
            String log = cn.getDirectoryName();
            directoryNames.add(log);
        }
        ArrayAdapter dataAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, directoryNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 1) {
                    albumName = parent.getItemAtPosition(position).toString();

                } else {
                    if (position == 1) {
                        LayoutInflater li = LayoutInflater.from(UploadImageWithInfo.this);
                        View promptsView = li.inflate(R.layout.prompts, null);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UploadImageWithInfo.this);
                        alertDialogBuilder.setView(promptsView);
                        final EditText userInput = (EditText) promptsView
                                .findViewById(R.id.editTextDialogUserInput);
                        // set title
                        alertDialogBuilder.setTitle("Create New Album");

                        // set dialog message
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, close
                                        // current activity
                                        albumName = userInput.getText().toString();
                                        Directory directory = new Directory(albumName, RegisterUserActivity.email);
                                        db.addDirectory(directory);
                                        handleUploadClick(imagePath);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, just close
                                        // the dialog box and do nothing
                                        dialog.cancel();
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
        Log.d("UploadDicrectory","album name " + albumName);
        obj.set("directory", albumName);

       // BuiltACL acl = new BuiltACL();
       // acl.setUserWriteAccess(RegisterUserActivity.uid,true);
        //obj.setACL(new BuiltACL().setUserDeleteAccess("blt3561d7fc3b8d68ed",true));
// "student_name" is the field uid
        obj.save(new BuiltResultCallBack() {
            @Override
            public void onSuccess() {
                // object is created successfully
                Toast.makeText(UploadImageWithInfo.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                finish();
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
