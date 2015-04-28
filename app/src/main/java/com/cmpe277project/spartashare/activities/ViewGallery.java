package com.cmpe277project.spartashare.activities;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TabHost;
import android.widget.Toast;

import com.cmpe277project.spartashare.R;
import com.cmpe277project.spartashare.adapters.GalleryGridViewAdapter;
import com.cmpe277project.spartashare.message.convertor.MessageConverter;
import com.cmpe277project.spartashare.models.DirectoryInfo;
import com.cmpe277project.spartashare.models.UsersImage;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltObject;
import com.raweng.built.BuiltQuery;
import com.raweng.built.QueryResult;
import com.raweng.built.QueryResultsCallBack;

import java.util.ArrayList;
import java.util.List;

public class ViewGallery extends TabActivity {
    private GridView gridView;
    private GalleryGridViewAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_gallery);

    /*    TabHost tabHost = getTabHost();

        // Gallary Tab
        Intent intentGallery = new Intent().setClass(this, ViewGallery.class);
        TabHost.TabSpec tabSpecGallery = tabHost
                .newTabSpec("ViewGallery")
                .setIndicator("ViewGallery")
                .setContent(intentGallery);

        // Uploads Tab
        Intent intentUpload = new Intent().setClass(this, UploadImage.class);
        TabHost.TabSpec tabSpecUploadImage = tabHost
                .newTabSpec("UploadImage")
                .setIndicator("UploadImage")
                .setContent(intentUpload);

        // Uploads Tab
        Intent intentSharedImages = new Intent().setClass(this, UploadImage.class);
        TabHost.TabSpec tabSpecSharedImages = tabHost
                .newTabSpec("UploadImage")
                .setIndicator("UploadImage")
                .setContent(intentSharedImages);

        tabHost.addTab(tabSpecGallery);
        tabHost.addTab(tabSpecUploadImage);
        tabHost.addTab(tabSpecSharedImages);
        tabHost.setCurrentTab(1);
*/

        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_gallery, menu);
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

    private void getData() {
        final ArrayList<UsersImage> imageItems = new ArrayList<UsersImage>();
        /*TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            imageItems.add(new UsersImage());
        }
        return imageItems;*/
        BuiltQuery query = new BuiltQuery("images");
        query.exec(new QueryResultsCallBack() {
            @Override
            public void onSuccess(QueryResult queryResult) {
                //fetching Directories first
                ArrayList<UsersImage> dirList = getDirList();
                imageItems.addAll(dirList);
                //fetching images from Server
                List<BuiltObject> images = queryResult.getResultObjects();
                for (BuiltObject i : images)
                    imageItems.add(MessageConverter.getInstance().convertToImages(i));
                /*try {
                    //Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                populateGridView(imageItems);
            }

            @Override
            public void onError(BuiltError builtError) {
                Toast.makeText(ViewGallery.this,"No User Found",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAlways() {

            }
        });
        System.out.println("No of images in gallary: " + imageItems.size());
    }

    //Written a stud to generate Directory List. Afer adding DB we need to
    //modify the method to fetch dir from DB.
    private ArrayList<UsersImage> getDirList() {
        ArrayList<UsersImage> localList = new ArrayList<UsersImage>();
        for (int i = 1; i < 4; i++) {
            DirectoryInfo dir = new DirectoryInfo();
            dir.setName("DirectoryNo_" + i);
            dir.setDirNo(String.valueOf(i));
            localList.add(MessageConverter.getInstance().createImages(dir));
        }
        return localList;
    }

    private void populateGridView(ArrayList<UsersImage> ui) {
        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GalleryGridViewAdapter(ViewGallery.this, R.layout.gallary_grid_layout_item, ui);
        gridView.setAdapter(gridAdapter);
    }
}