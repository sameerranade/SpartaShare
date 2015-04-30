package com.cmpe277project.spartashare.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.cmpe277project.spartashare.R;
import com.cmpe277project.spartashare.util.GetAbsoluteImagePath;

public class UploadImage extends Activity {

    private static final int SELECT_PICTURE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    private String selectedImagePath;
    private Uri selectedImageUri;
    Button browseImage;
    Button captureImage;
    Button uploadImage;
    ImageView displayImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        //setting views
        browseImage = (Button) findViewById(R.id.btn_uploadFromGallery);
        captureImage = (Button) findViewById(R.id.btn_uploadFromCamera);
        uploadImage = (Button) findViewById(R.id.btn_uploadImage);
        displayImage = (ImageView) findViewById(R.id.iv_imageToUpload);
        browseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleUploadFromGalleryClick();
            }
        });
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleUploadImageClick();
            }
        });
        captureImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_upload_image, menu);
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

    private void handleUploadFromGalleryClick() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    private void handleUploadImageClick() {
        if(selectedImagePath!=null){
            Bundle bundle = new Bundle();
            bundle.putString("ImageURI", String.valueOf(selectedImageUri));
            Intent uploadImageWithInfo = new Intent(UploadImage.this, UploadImageWithInfo.class);
            uploadImageWithInfo.putExtras(bundle);
            startActivity(uploadImageWithInfo);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                selectedImagePath = GetAbsoluteImagePath.getInstance().getPath(UploadImage.this,selectedImageUri);
                System.out.println("Image Path : " + selectedImageUri.toString());
                displayImage.setImageURI(selectedImageUri);
                uploadImage.setVisibility(View.VISIBLE);
                System.out.println("Image FileName " + GetAbsoluteImagePath.getInstance().getPath(UploadImage.this, selectedImageUri));
                //uploadImage(GetAbsoluteImagePath.getInstance().getPath(UploadImage.this, selectedImageUri));
                //getObject();
            }
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap bmp = (Bitmap) extras.get("data");
//                String selectedImagePath = getPath(data.getData());
                System.out.println(bmp);
                /*Intent intent = new Intent(this, EditPhotoActivity.class);
                intent.putExtra("BitmapImage", bmp);
                intent.putExtra("ImagePath", selectedImagePath);
                startActivity(intent);*/
            }
        }
    }

    private String getPath(Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null,null);

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }
}
