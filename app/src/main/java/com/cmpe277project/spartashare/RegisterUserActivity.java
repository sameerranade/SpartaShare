package com.cmpe277project.spartashare;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpe277project.spartashare.DAO.DatabaseHandler;
import com.cmpe277project.spartashare.activities.HomeActivity;
import com.raweng.built.Built;
import com.raweng.built.BuiltApplication;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltObject;
import com.raweng.built.BuiltQuery;
import com.raweng.built.BuiltResultCallBack;
import com.raweng.built.BuiltUser;
import com.raweng.built.QueryResult;
import com.raweng.built.QueryResultsCallBack;
import java.util.HashMap;

public class RegisterUserActivity extends ActionBarActivity {

    String email;
    private EditText editText;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        final DatabaseHandler dbHandler = new DatabaseHandler(RegisterUserActivity.this);

        displayUserInfo();

        Button button = (Button) findViewById(R.id.registerbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = editText.getText().toString();
                Log.d("Inside Button Click", "Email is "+email);
                if (email == null || email.equals("")){
                    email = bundle.getString("FacebookEmail");
                    Log.d("Inside IF case", "Email is "+email);
                }

                final BuiltUser user = new BuiltUser();
                final HashMap usrInfo = new HashMap();

                BuiltApplication app = new BuiltApplication();
                BuiltQuery builtQuery = app.getUsersQuery();
                builtQuery.exec(new QueryResultsCallBack() {
                    @Override
                    public void onSuccess(QueryResult queryResult) {
                        BuiltObject uemail = queryResult.getResultObjects().get(1);
                        String temp = uemail.getUid();

                        Log.d("Email user activity : ",temp);
                    }

                    @Override
                    public void onError(BuiltError builtError) {
                        Log.d("Error in Query", builtError.getErrorMessage());
                    }

                    @Override
                    public void onAlways() {

                    }
                });


                usrInfo.put("email", email);
                usrInfo.put("password", "root123");
                usrInfo.put("password_confirmation", "root123");

                user.register(usrInfo, new BuiltResultCallBack() {
                    @Override
                    public void onSuccess() {
                        // object is created successfully
                        Toast.makeText(RegisterUserActivity.this, "Registered successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterUserActivity.this, HomeActivity.class);
                        //intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(BuiltError builtErrorObject) {
                        // there was an error in creating the object
                        // builtErrorObject will contain more details
                        Toast.makeText(RegisterUserActivity.this, builtErrorObject.getErrorMessage(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterUserActivity.this, HomeActivity.class);
                        //intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onAlways() {
                        // write code here that you want to execute
                        // regardless of success or failure of the operation
                        Toast.makeText(RegisterUserActivity.this, "What happened here", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_user, menu);
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

    private void displayUserInfo(){

        bundle = getIntent().getExtras();

        TextView usernameTextView = (TextView) findViewById(R.id.twitterusernametxtview);
        editText = (EditText) findViewById(R.id.emailedittxtview);

        if(bundle.getString("FacebookEmail") != null ) {
            email = bundle.getString("FacebookEmail");
            usernameTextView.setText("Welcome " + bundle.getString("FacebookEmail"));
        }
        else
        {
            editText.setVisibility(View.VISIBLE);
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    editText.setCursorVisible(true);
                }
            });
            usernameTextView.setText("Welcome " + bundle.getString("TwitterUsername"));
        }
//      ImageView profilePictureImageView = (ImageView) findViewById(R.id.profilepictureimgview);
//      aQuery.id(R.id.profilepictureimgview).image(bundle.getString("ProfileImageURL"));
    }
}