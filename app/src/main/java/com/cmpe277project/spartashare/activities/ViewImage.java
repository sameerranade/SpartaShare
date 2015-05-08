package com.cmpe277project.spartashare.activities;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpe277project.spartashare.R;
import com.cmpe277project.spartashare.message.convertor.MessageConverter;
import com.cmpe277project.spartashare.models.UsersImage;
import com.google.gson.Gson;
import com.raweng.built.androidquery.AQuery;

public class ViewImage extends Activity {
    TextView caption;
    TextView tags;
    TextView location;
    TextView albumName;
    ImageView displayImage;
    Button share;
    UsersImage usersImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image_with_info);
        caption = (TextView) findViewById(R.id.et_caption);
        caption.setEnabled(false);
        tags = (TextView) findViewById(R.id.et_tags);
        tags.setEnabled(false);
        location = (TextView) findViewById(R.id.et_location);
        location.setEnabled(false);
        albumName = (TextView) findViewById(R.id.tv_vi_directory);
        displayImage = (ImageView) findViewById(R.id.iv_uploadImage);
        share = (Button) findViewById(R.id.btn_upload);
        share.setText("Save");
        fetchImageWithDetails();

    }

    private void fetchImageWithDetails() {
        usersImage = MessageConverter.getInstance().getUsersImageFromBundle(getIntent().getExtras());
        Log.d("ViewImage", "Image URL: " + usersImage.getImageURL());
        Toast.makeText(ViewImage.this,"View Image URL " + usersImage.getImageURL(),Toast.LENGTH_SHORT).show();
        AQuery androidQuery = new AQuery(displayImage);
        androidQuery.id(displayImage).image(usersImage.getImageURL());
        caption.setText(usersImage.getCaption());
        tags.setText(usersImage.getTags());
        location.setText(usersImage.getLocation());
        //albumName.setText(usersImage.getDirectoryNo());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_image, menu);
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
}
