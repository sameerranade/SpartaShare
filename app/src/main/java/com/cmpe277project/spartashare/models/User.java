package com.cmpe277project.spartashare.models;

/**
 * Created by Varun on 4/28/2015.
 */
public class User {
    private String uid;
    private String builtIOUID;
    private String userName;
    private String email;

    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    public User(String uid, String builtIOUID, String userName, String email) {
        this.uid = uid;
        this.builtIOUID = builtIOUID;
        this.userName = userName;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBuiltIOUID() {
        return builtIOUID;
    }

    public void setBuiltIOUID(String builtIOUID) {
        this.builtIOUID = builtIOUID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
