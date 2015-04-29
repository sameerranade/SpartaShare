package com.cmpe277project.spartashare.models;

import android.graphics.Bitmap;

import com.raweng.built.BuiltACL;
import com.raweng.built.BuiltFile;

/**
 * Created by Varun on 4/25/2015.
 */
public class UsersImage {
    private String uid;
    private String caption;
    private BuiltFile file;
    private String imageURL;
    private String directoryNo;
    private boolean isDir;
    private Bitmap image;
    private BuiltACL acl;
    private String location;
    private String tags;

    public BuiltACL getAcl() {
        return acl;
    }

    public void setAcl(BuiltACL acl) {
        this.acl = acl;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public BuiltFile getFile() {
        return file;
    }

    public void setFile(BuiltFile file) {
        this.file = file;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage() {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDirectoryNo() {
        return directoryNo;
    }

    public void setDirectoryNo(String directoryNo) {
        this.directoryNo = directoryNo;
    }

    public boolean isDir() {
        return isDir;
    }

    public void setDir(boolean isDir) {
        this.isDir = isDir;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
