package com.cmpe277project.spartashare;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.raweng.built.Built;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;

public class LoginActivity extends ActionBarActivity {

    //Twitter
    private TwitterLoginButton loginButton;

    //Facebook
    private UiLifecycleHelper uiHelper;
    private View otherView;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TwitterAuthConfig authConfig =
                new TwitterAuthConfig("ZbXxnF9McAcdEpf3SFkMVlqcm",
                        "korGqXI6OAVZRyJlNC54imDektcDmvLFNUwn3LxwaFNUgk6ddt");
        Fabric.with(this, new TwitterCore(authConfig));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            Built.initializeWithApiKey(LoginActivity.this, "blt96f850201ab76a1e", "kksv");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Twitter Authorization using oAuth
        authorizeUser();

        //Facebook Authorization
        facebookAuthorization(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Twitter onActivityResult
        loginButton.onActivityResult(requestCode, resultCode, data);

        //Facebook onActivityResult
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    //Twitter Authorization
    private void authorizeUser(){

        loginButton = (TwitterLoginButton)
                findViewById(R.id.login_button);

        loginButton.setCallback(new Callback<TwitterSession>() {

            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a
                // TwitterSession for making API calls
                callRegisterIntentOnSuccess(result);
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }
        });
    }

    //Twitter Authorization
    private void callRegisterIntentOnSuccess(Result<TwitterSession> result){

        Bundle bundle = new Bundle();
        bundle.putString("TwitterUsername",result.data.getUserName());

        Intent intent = new Intent(LoginActivity.this, RegisterUserActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //Facebook authorization
    private void facebookAuthorization(Bundle savedInstanceState){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.cmpe277project.spartashare",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private void onSessionStateChange(Session session, SessionState state,
                                      Exception exception) {
        // When Session is successfully opened (User logged-in)
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
            // make request to the /me API to get Graph user
            Request.newMeRequest(session, new Request.GraphUserCallback() {

                // callback after Graph API response with user
                // object
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        // Set view visibility to true

                        Bundle bundle = new Bundle();
                        bundle.putString("FacebookEmail",user.getProperty("email").toString());
                        bundle.putString("FacebookID", user.getId() );
                        Intent intent = new Intent(LoginActivity.this, RegisterUserActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }).executeAsync();
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
            //otherView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }
}