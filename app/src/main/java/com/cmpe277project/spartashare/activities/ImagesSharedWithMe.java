package com.cmpe277project.spartashare.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.cmpe277project.spartashare.R;
import com.cmpe277project.spartashare.adapters.GalleryGridViewAdapter;

public class ImagesSharedWithMe extends ActionBarActivity {
    private GridView gridView;
    private GalleryGridViewAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_shared_with_me);
        gridView = (GridView) findViewById(R.id.iswm_gridView);
        fetchImagesSharedWithMe();
    }

    private void fetchImagesSharedWithMe() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_images_shared_with_me, menu);
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
