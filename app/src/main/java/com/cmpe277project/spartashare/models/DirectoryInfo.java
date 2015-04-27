package com.cmpe277project.spartashare.models;

/**
 * Created by Varun on 4/26/2015.
 */
public class DirectoryInfo {
    private String name;
    private String dirNo;
    private String url = "https://api.built.io/v1/uploads/553d74b11d2d6dc27e6a5584/download";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirNo() {
        return dirNo;
    }

    public void setDirNo(String dirNo) {
        this.dirNo = dirNo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
