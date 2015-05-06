package com.cmpe277project.spartashare;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.cmpe277project.spartashare.activities.HomeActivity;
import com.facebook.widget.ProfilePictureView;
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
import java.util.List;

public class RegisterUserActivity extends Activity {

    public static String email;
    public static String uid;
    private EditText editText;
    Bundle bundle;

    private ProfilePictureView user_picture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        user_picture = (ProfilePictureView)findViewById(R.id.userImage);

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

                usrInfo.put("email", email);
                usrInfo.put("password", "root123");
                usrInfo.put("password_confirmation", "root123");

                user.register(usrInfo, new BuiltResultCallBack() {
                    @Override
                    public void onSuccess() {
                        // object is created successfully
                        Toast.makeText(RegisterUserActivity.this, "Registered successfully", Toast.LENGTH_LONG).show();
                        loginUser();
                        Intent intent = new Intent(RegisterUserActivity.this, HomeActivity.class);
                        //intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(BuiltError builtErrorObject) {
                        // there was an error in creating the object
                        // builtErrorObject will contain more details
                        loginUser();
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

    private void loginUser() {
        final BuiltUser builtUserObject = new BuiltUser();
        builtUserObject.login(email, "root123", new BuiltResultCallBack() {
            // here "john@malkovich.com" is a valid email id of your user
            // and "password", the corresponding password
            @Override
            public void onSuccess() {
                // user has logged in successfully
                // builtUserObject.authtoken contains the session authtoken
                Toast.makeText(RegisterUserActivity.this,"Login Successful", Toast.LENGTH_SHORT).show();
                uid = builtUserObject.getUserUid();
                email = builtUserObject.getEmailId();
                Log.d("RegisterUserActivity", "UserID:" + builtUserObject.getUserUid() + " Email" + builtUserObject.getEmailId());
                Toast.makeText(RegisterUserActivity.this,"Login Successful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegisterUserActivity.this, HomeActivity.class);
                //intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onError(BuiltError builtErrorObject) {
                // login failed

                // the message, code and details of the error
                Log.i("error: ", "" + builtErrorObject.getErrorMessage());
                Log.i("error: ", "" + builtErrorObject.getErrorCode());
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

        TextView usernameTextView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.emailedittxtview);

        if(bundle.getString("FacebookEmail") != null ) {
            email = bundle.getString("FacebookEmail");
            user_picture.setProfileId(bundle.getString("FacebookID"));
            usernameTextView.setText(bundle.getString("FacebookEmail"));

        }
        else
        {
            user_picture.setVisibility(View.INVISIBLE);
            editText.setVisibility(View.VISIBLE);
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    editText.setCursorVisible(true);
                }
            });
            usernameTextView.setText(bundle.getString("TwitterUsername"));
        }
    }
}