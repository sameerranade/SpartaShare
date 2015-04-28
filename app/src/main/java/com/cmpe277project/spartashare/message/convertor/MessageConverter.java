package com.cmpe277project.spartashare.message.convertor;

import com.cmpe277project.spartashare.models.DirectoryInfo;
import com.cmpe277project.spartashare.models.UsersImage;
import com.raweng.built.BuiltObject;

/**
 * Created by Varun on 4/25/2015.
 */
public class MessageConverter {
    private static MessageConverter ourInstance = new MessageConverter();

    public static MessageConverter getInstance() {
        return ourInstance;
    }

    private MessageConverter() {
    }

    public UsersImage convertToImages (BuiltObject buildImages){
        UsersImage usersImage = new UsersImage();

        usersImage.setCaption(buildImages.getString("caption"));
        usersImage.setFile(buildImages.getBuiltFile("actualimage"));
        usersImage.setImageURL(buildImages.getString("actualimageurl"));
        usersImage.setDirectoryNo(buildImages.getString("dirno"));
        usersImage.setDir(false);
        return usersImage;
    }
    //This method is used to create UserImage object for the Albums of user.
    //It will use default folderimage.png as image attached to it
    //
    public UsersImage createImages(DirectoryInfo dirInfo){
        UsersImage usersImage = new UsersImage();

        usersImage.setCaption(dirInfo.getName());
        usersImage.setDir(true);
        usersImage.setDirectoryNo(dirInfo.getName());
        usersImage.setImageURL(dirInfo.getUrl());

        return usersImage;
    }
}
