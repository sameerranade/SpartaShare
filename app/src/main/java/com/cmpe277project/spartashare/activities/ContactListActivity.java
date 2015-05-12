package com.cmpe277project.spartashare.activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cmpe277project.spartashare.R;
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
import java.util.Map;

/**
 * Created by Sameer Ranade on 4/30/2015.
 */
public class ContactListActivity extends Activity{
        public TextView outputText;
        List<Map<String, String>> contactList = new ArrayList<Map<String,String>>();
        public SimpleAdapter simpleAdpt;
        private String albumName;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_contact_list);
            Bundle bundle = getIntent().getExtras();
            albumName = bundle.getString("albumName");

            fetchContacts();
            ListView lv = (ListView) findViewById(R.id.listView);

            simpleAdpt = new SimpleAdapter(this, contactList, R.layout.list_layout, new String[] {"Name", "Email"}, new int[] {R.id.nameColumn, R.id.emailColumn});

            lv.setAdapter(simpleAdpt);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
                                        long id) {

                    // We know the View is a TextView so we can cast it
                    final TextView clickedView = (TextView) view.findViewById(R.id.nameColumn);
                    final TextView clickedEmail = (TextView) view.findViewById(R.id.emailColumn);


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ContactListActivity.this);

                    // set title
                    alertDialogBuilder.setTitle("Share With");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Do you Want to share with "+ clickedView.getText())
                            .setCancelable(false)
                            .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    getOtherUsersUID(clickedEmail.getText().toString());
                                    sendMail(clickedEmail.getText().toString());
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                }
            });
        }

        public void fetchContacts() {

            //String phoneNumber = null;
            String email = null;

            Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;

            String _ID = ContactsContract.Contacts._ID;

            String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
            String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;



            Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
            String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
            String DATA = ContactsContract.CommonDataKinds.Email.DATA;

            ContentResolver contentResolver = getContentResolver();

            Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);
            if (cursor.getCount() > 0) {

                while (cursor.moveToNext()) {

                    String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                    String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));
                    Map<String, String> contact = new HashMap<String, String>();

                    int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));

                    if (hasPhoneNumber > 0) {
                        Cursor emailCursor = contentResolver.query(EmailCONTENT_URI,	null, EmailCONTACT_ID+ " = ?", new String[] { contact_id }, null);

                        while (emailCursor.moveToNext()) {

                            email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                            contact.put("Name", name);
                            contact.put("Email", email);

                        }
                        if(contact.containsValue(name)){
                            contactList.add(contact);
                        }

                        emailCursor.close();
                    }

                }
            }
        }

        public void sendMail(String emailID){
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{emailID});
            i.putExtra(Intent.EXTRA_SUBJECT, "Share Image");
            i.putExtra(Intent.EXTRA_TEXT   , "An image is shared with you");
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {

            }
        }
    private void getOtherUsersUID(String email){
        BuiltUser user = new BuiltUser();
        user.fetchUserUidForEmail(email, new BuiltUserResultCallback() {
            @Override
            public void onSuccess(String s) {
                Log.d("ShareImage", "Other User Email " + s);
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


}
