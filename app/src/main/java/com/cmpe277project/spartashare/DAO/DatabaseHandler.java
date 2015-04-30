package com.cmpe277project.spartashare.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cmpe277project.spartashare.models.Directory;
import com.cmpe277project.spartashare.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Varun on 4/26/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "directoryManager";

    // Directories table name
    private static final String TABLE_DIRECTORIES = "directories";
    private static final String TABLE_USER = "users";

    // Directory Table Columns names
    private static final String KEY_DIR_ID = "id";
    private static final String KEY_DIR_NAME = "name";
    private static final String KEY_DIR_URL = "phone_number";

    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_USERNAME = "username";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_BUILTIOUID = "builtIOUID";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DIRECTORIES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_DIRECTORIES + "("
                + KEY_DIR_NAME + " TEXT, "
                + KEY_DIR_URL + " TEXT" + ")";
        db.execSQL(CREATE_DIRECTORIES_TABLE);

        /*String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + "("
                + KEY_USER_ID + " INTEGER AUTOINCREMENT," + KEY_USER_USERNAME + " TEXT,"
                + KEY_USER_EMAIL + " TEXT" + KEY_USER_BUILTIOUID + "TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIRECTORIES);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new directory
    public void addDirectory(Directory contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DIR_NAME, contact.getDirectoryName()); // Directory Name
        values.put(KEY_DIR_URL, contact.getUserName()); // Directory Phone

        // Inserting Row
        db.insert(TABLE_DIRECTORIES, null, values);
        db.close(); // Closing database connection
    }

    // Getting single directory
/*    Directory getDirectory(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DIRECTORIES, new String[] {KEY_DIR_ID,
                        KEY_DIR_NAME, KEY_DIR_URL}, KEY_DIR_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Directory directory = new Directory(cursor.getString(0),
                cursor.getString(1));
        // return Directory
        return directory;
    }*/

    // Getting All Directories
    public List<Directory> getAllDirectories( String userName) {
        List<Directory> directoryList = new ArrayList<Directory>();
        // Select All Query
        String selectQuery = "SELECT " + KEY_DIR_NAME + ", " + KEY_DIR_URL + " FROM " + TABLE_DIRECTORIES
                + " WHERE " + KEY_DIR_URL + " = " + "\"" + userName + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Directory directory = new Directory();
                directory.setDirectoryName(cursor.getString(0));
                directory.setUserName(cursor.getString(1));
                // Adding directory to list
                directoryList.add(directory);
            } while (cursor.moveToNext());
        }

        // return directory list
        return directoryList;
    }

/*    // Updating single directory
    public int updateDirectory(Directory contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DIR_NAME, contact.getDirectoryName());
        values.put(KEY_DIR_URL, contact.getUserName());

        // updating row
        return db.update(TABLE_DIRECTORIES, values, KEY_DIR_ID + " = ?",
                new String[] { String.valueOf(contact.getDirectoryID()) });
    }*/

    // Deleting single contact
  /*  public void deleteDirectory(Directory directory) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DIRECTORIES, KEY_DIR_ID + " = ?",
                new String[] { String.valueOf(directory.getDirectoryID()) });
        db.close();
    }*/


    // Getting contacts Count
    public int getDirectoriesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_DIRECTORIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

/*
    void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_USERNAME, user.getUserName()); // userName
        values.put(KEY_USER_EMAIL, user.getEmail()); // userEmail
        values.put(KEY_USER_BUILTIOUID, user.getBuiltIOUID()); // user_BuiltIO_UID

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection
    }

    User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, new String[] {KEY_USER_ID,
                        KEY_USER_USERNAME, KEY_USER_EMAIL, KEY_USER_BUILTIOUID}, KEY_USER_EMAIL + "=?",
                new String[] { String.valueOf(email) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        User user = new User(cursor.getString(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));
        // return user
        return user;
    }

*/

}
