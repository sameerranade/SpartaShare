package com.cmpe277project.spartashare.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.cmpe277project.spartashare.R;
import com.cmpe277project.spartashare.RegisterUserActivity;
import com.cmpe277project.spartashare.adapters.GalleryGridViewAdapter;
import com.cmpe277project.spartashare.message.convertor.MessageConverter;
import com.cmpe277project.spartashare.models.UsersImage;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltObject;
import com.raweng.built.BuiltQuery;
import com.raweng.built.QueryResult;
import com.raweng.built.QueryResultsCallBack;

import java.util.ArrayList;
import java.util.List;

public class ImagesSharedWithMe extends Activity {
    private GridView gridView;
    private GalleryGridViewAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_shared_with_me);
        gridView = (GridView) findViewById(R.id.iswm_gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                UsersImage item = (UsersImage) parent.getItemAtPosition(position);
                Log.d("ViewGallery","Image URL "+item.getImageURL());
                //Create intent
                if(!item.isDir()) {
                    Intent intent = new Intent(ImagesSharedWithMe.this, ViewImage.class);
                    intent.putExtras(MessageConverter.getInstance().putUsersImageInBundle(item));
                    startActivity(intent);
                }
                //Toast.makeText(ViewGallery.this,"Clicked on Item " + position + "Item " + item.isDir(), Toast.LENGTH_SHORT ).show();
            }
        });
        fetchImagesSharedWithMe();
    }

    private void fetchImagesSharedWithMe() {
        final ArrayList<UsersImage> imageItems = new ArrayList<UsersImage>();
        System.out.println("Inside get data");
        BuiltQuery query = new BuiltQuery("images");
        query.includeOwner().exec(new QueryResultsCallBack() {
            @Override
            public void onSuccess(QueryResult queryResult) {
                //fetching Directories first
                //fetching images from Server
                List<BuiltObject> images = queryResult.getResultObjects();
                for (BuiltObject i : images) {
                    //System.out.println("Onwer hashmap \n" + i.getOwner().toString() );
                    if(i.getOwner()!= null){
                        System.out.println("Onwer hashmap \n" + i.getOwner().toString() );
                        Log.d("ImagesSharedWithMe", "Caption " + i.getOwner().get("uid") + " Owner " + i.getOwner().get("uid"));
                        if(!(i.getOwner().get("uid").equals(RegisterUserActivity.uid)))
                            imageItems.add(MessageConverter.getInstance().convertToImages(i));
                    }
                    //if(!(i.getOwner().get("uid").equals(RegisterUserActivity.uid) || i.getUid().equals("anonymous")))
                        //imageItems.add(MessageConverter.getInstance().convertToImages(i));
                }
                System.out.println("Number of Images in imageItems " + imageItems.size());
                populateGridView(imageItems);
            }

            @Override
            public void onError(BuiltError builtError) {
                Toast.makeText(ImagesSharedWithMe.this, "No User Found", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAlways() {

            }
        });
        System.out.println("No of images in gallary: " + imageItems.size());
    }

    private void populateGridView(ArrayList<UsersImage> ui) {
        // gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GalleryGridViewAdapter(ImagesSharedWithMe.this, R.layout.gallary_grid_layout_item, ui);
        gridView.setAdapter(gridAdapter);
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
