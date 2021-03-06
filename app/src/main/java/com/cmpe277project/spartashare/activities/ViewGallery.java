package com.cmpe277project.spartashare.activities;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.cmpe277project.spartashare.DAO.DatabaseHandler;
import com.cmpe277project.spartashare.R;
import com.cmpe277project.spartashare.RegisterUserActivity;
import com.cmpe277project.spartashare.adapters.GalleryGridViewAdapter;
import com.cmpe277project.spartashare.message.convertor.MessageConverter;
import com.cmpe277project.spartashare.models.Directory;
import com.cmpe277project.spartashare.models.UsersImage;
import com.raweng.built.Built;
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
    private DatabaseHandler db = new DatabaseHandler(ViewGallery.this);
    private Button share;
    private String directoryName;
    private Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_gallery);
        System.out.println("Inside OnCreate of ViewGallery");
        try {
            Built.initializeWithApiKey(ViewGallery.this, "blt96f850201ab76a1e", "kksv");

        } catch (Exception e) {
            e.printStackTrace();
        }
        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
               UsersImage item = (UsersImage) parent.getItemAtPosition(position);
                Log.d("ViewGallery","Image URL "+item.getImageURL());
                //Create intent
                if(!item.isDir()) {
                    Intent intent = new Intent(ViewGallery.this, ViewImage.class);
                    intent.putExtras(MessageConverter.getInstance().putUsersImageInBundle(item));
                    startActivity(intent);
                }
                else if(item.isDir()){
                    share.setVisibility(View.VISIBLE);
                    directoryName = item.getCaption();
                    getDictionaryData(item.getCaption());
                }
            }
        });
        getDictionaryData("Select Album");

        share = (Button) findViewById(R.id.btn_vg_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("albumName", directoryName);
                Intent intent = new Intent(ViewGallery.this, ContactListActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        search = (Button) findViewById(R.id.btn_vg_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(ViewGallery.this);
                View promptsView = li.inflate(R.layout.searchdialogbox, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewGallery.this);
                alertDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.sd_editTextDialogUserInput);
                // set title
                alertDialogBuilder.setTitle("Enter Caption for Search");

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity
                                searchImage(userInput.getText().toString());
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });
    }

    public void searchImage(String captionToSearch){
        Log.d("View Gallery", "Inside Search Image");
        BuiltQuery query = new BuiltQuery("images");
        query.matches("caption",".*"+captionToSearch+".*","i");
        query.exec(new QueryResultsCallBack() {
            @Override
            public void onSuccess(QueryResult queryResult) {
                Log.d("View Gallery", "Inside Search Image onSuccess");
                List<BuiltObject> result = queryResult.getResultObjects();
                Log.d("View Gallery", "Size of searched result " + result.size());
                UsersImage searchedImage = MessageConverter.getInstance().convertToImages(result.get(0));
                Intent intent = new Intent(ViewGallery.this, ViewImage.class);
                intent.putExtras(MessageConverter.getInstance().putUsersImageInBundle(searchedImage));
                startActivity(intent);
            }

            @Override
            public void onError(BuiltError builtError) {

            }

            @Override
            public void onAlways() {

            }
        });
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

    private void getDictionaryData(final String directoryNo) {
        final ArrayList<UsersImage> imageItems = new ArrayList<UsersImage>();
        System.out.println("Inside get data");
        BuiltQuery query = new BuiltQuery("images");

        query.where("directory", directoryNo);
        query.exec(new QueryResultsCallBack() {
            @Override
            public void onSuccess(QueryResult queryResult) {
                //fetching Directories first

                if (directoryNo.equals("Select Album")) {
                    ArrayList<UsersImage> dirList = getDirList();
                    imageItems.addAll(dirList);
                }
                //fetching images from Server
                List<BuiltObject> images = queryResult.getResultObjects();
                for (BuiltObject i : images)
                    imageItems.add(MessageConverter.getInstance().convertToImages(i));
                /*try {
                    //Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                System.out.println("Number of Images in imageItems " + imageItems.size());
                populateGridView(imageItems);
            }

            @Override
            public void onError(BuiltError builtError) {

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
        /*for (int i = 1; i < 4; i++) {
            DirectoryInfo dir = new DirectoryInfo();
            dir.setName("DirectoryNo_" + i);
            dir.setDirNo(String.valueOf(i));
            localList.add(MessageConverter.getInstance().createImages(dir));
        }*/
        List<Directory> dirList = db.getAllDirectories(RegisterUserActivity.email);
        for(Directory dir: dirList)
            localList.add(MessageConverter.getInstance().createImages(dir));
        return localList;
    }

    private void populateGridView(ArrayList<UsersImage> ui) {
       // gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GalleryGridViewAdapter(ViewGallery.this, R.layout.gallary_grid_layout_item, ui);
        gridView.setAdapter(gridAdapter);
       /* gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                UsersImage item = (UsersImage) parent.getItemAtPosition(position);
                //Create intent
                if(!item.isDir()) {
                    Intent intent = new Intent(ViewGallery.this, ViewImage.class);
                    intent.putExtra("usersImage", new Gson().toJson(item));
                    //Start details activity
                    startActivity(intent);
                }
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDictionaryData("Select Album");

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        getDictionaryData("Select Album");
    }
}