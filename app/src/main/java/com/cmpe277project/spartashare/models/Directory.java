package com.cmpe277project.spartashare.models;

/**
 * Created by Varun on 4/26/2015.
 */
public class Directory {
    //private variables
    int directoryID;
    String directoryName;
    String directoryURL;

    public Directory(int directoryID, String directoryName, String directoryURL) {
        this.directoryID = directoryID;
        this.directoryName = directoryName;
        this.directoryURL = directoryURL;
    }

    public Directory() {

    }

    public Directory(String directoryName, String directoryURL) {
        this.directoryName = directoryName;
        this.directoryURL = directoryURL;
    }

    public int getDirectoryID() {
        return directoryID;
    }

    public void setDirectoryID(int directoryID) {
        this.directoryID = directoryID;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public String getDirectoryURL() {
        return directoryURL;
    }

    public void setDirectoryURL(String directoryURL) {
        this.directoryURL = directoryURL;
    }
}
