package com.cmpe277project.spartashare.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.cmpe277project.spartashare.R;
import com.cmpe277project.spartashare.RegisterUserActivity;

public class HomeActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Bundle bundle = getIntent().getExtras();

        RegisterUserActivity regUser = new RegisterUserActivity();
        String email = regUser.email;
        Log.d("email",email);

        TabHost tabHost = getTabHost();

        // Gallary Tab
        Intent intentGallery = new Intent().setClass(this, ViewGallery.class);
        TabHost.TabSpec tabSpecGallery = tabHost
                .newTabSpec("ViewGallery")
                .setIndicator("ViewGallery")
                .setContent(intentGallery);

        // Uploads Tab
        Intent intentUpload = new Intent().setClass(this, UploadImage.class);
        TabHost.TabSpec tabSpecUploadImage = tabHost
                .newTabSpec("Upload")
                .setIndicator("Upload")
                .setContent(intentUpload);

        // Share Tab
        Intent intentSharedImages = new Intent().setClass(this, ImagesSharedWithMe.class);
        TabHost.TabSpec tabSpecSharedImages = tabHost
                .newTabSpec("Shared")
                .setIndicator("Shared")
                .setContent(intentSharedImages);

        tabHost.addTab(tabSpecGallery);
        tabHost.addTab(tabSpecUploadImage);
        tabHost.addTab(tabSpecSharedImages);
        if(bundle!= null){
            if(bundle.getString("TabNo")!= null){
                tabHost.setCurrentTab(Integer.parseInt(bundle.getString("TabNo")));
            }
            else{
                tabHost.setCurrentTab(0);
            }
        }
        else {
            tabHost.setCurrentTab(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
