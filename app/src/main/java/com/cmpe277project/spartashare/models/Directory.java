package com.cmpe277project.spartashare.models;

/**
 * Created by Varun on 4/26/2015.
 */
public class Directory {
    //private variables
    String directoryName;
    String userName;

    public Directory() {

    }

    public Directory(String directoryName, String userName) {
        this.directoryName = directoryName;
        this.userName = userName;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
