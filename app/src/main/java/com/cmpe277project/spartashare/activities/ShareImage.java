package com.cmpe277project.spartashare.activities;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cmpe277project.spartashare.R;
import com.cmpe277project.spartashare.RegisterUserActivity;
import com.cmpe277project.spartashare.message.convertor.MessageConverter;
import com.cmpe277project.spartashare.models.UsersImage;
import com.raweng.built.BuiltACL;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltObject;
import com.raweng.built.BuiltQuery;
import com.raweng.built.BuiltResultCallBack;
import com.raweng.built.BuiltUser;
import com.raweng.built.BuiltUserResultCallback;
import com.raweng.built.QueryResult;
import com.raweng.built.QueryResultsCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShareImage extends Activity {
    private String albumName;
    EditText email;
    Button shareWithUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_image);
        email = (EditText) findViewById(R.id.et_si_email);
        Bundle bundle = getIntent().getExtras();
        albumName = bundle.getString("albumName");
        shareWithUser = (Button) findViewById(R.id.btn_si_share);
        shareWithUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOtherUsersUID();
            }
        });
        /*BuiltUser user = new BuiltUser();
        user.fetchUserUidForEmail(email.getText().toString(), new BuiltUserResultCallback() {
            @Override
            public void onSuccess(String s) {
                Log.d("ShareImage","Other User Email " + s);
                shareAlbumWithUser(s);
            }

            @Override
            public void onError(BuiltError builtError) {

            }

            @Override
            public void onAlways() {

            }
        });*/
    }
    private void getOtherUsersUID(){
        BuiltUser user = new BuiltUser();
        user.fetchUserUidForEmail(email.getText().toString(), new BuiltUserResultCallback() {
            @Override
            public void onSuccess(String s) {
                Log.d("ShareImage","Other User Email " + s);
                shareAlbumWithUser(s);
            }

            @Override
            public void onError(BuiltError builtError) {

            }

            @Override
            public void onAlways() {

            }
        });
    }
    private void shareAlbumWithUser(final String otherUsersUID) {
        final ArrayList<UsersImage> imageItems = new ArrayList<UsersImage>();
        System.out.println("Inside get data of ShareImage");
        BuiltQuery query = new BuiltQuery("images");
        query.where("directory",albumName);
        query.exec(new QueryResultsCallBack() {
            @Override
            public void onSuccess(QueryResult queryResult) {
                //fetching images from Server
                List<BuiltObject> images = queryResult.getResultObjects();
                for (BuiltObject i : images) {
                    //imageItems.add(MessageConverter.getInstance().convertToImages(i));
                    upsertImage(i.getUid(), otherUsersUID);
                }
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

    private void upsertImage(String objectUID, String otherUsersUID) {
        BuiltObject builtObject = new BuiltObject("images");

        HashMap<String, Object> searchCriteria = new HashMap<String, Object>();
        searchCriteria.put("uid", objectUID);

        HashMap<String, Object> replaceCriteria = new HashMap<String, Object>();
        //replaceCriteria.put("caption", "trying to modify caption one");
       // BuiltACL acl = new BuiltACL();
        //acl.setPublicDeleteAccess(true);

        //replaceCriteria.put("acl",acl);


        builtObject.upsert(searchCriteria);
        //builtObject.set(replaceCriteria);
        //builtObject.setACL(new BuiltACL().setUserWriteAccess(otherUsersUID, true));
        builtObject.setACL(new BuiltACL().setUserReadAccess(otherUsersUID, true));

        builtObject.save(new BuiltResultCallBack() {
            @Override
            public void onSuccess() {

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
        getMenuInflater().inflate(R.menu.menu_share_image, menu);
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