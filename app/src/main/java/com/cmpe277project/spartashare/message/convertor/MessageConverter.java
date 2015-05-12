package com.cmpe277project.spartashare.message.convertor;

import android.os.Bundle;

import com.cmpe277project.spartashare.models.Directory;
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
        usersImage.setLocation(buildImages.getString("imglocation"));
        usersImage.setTags(buildImages.getString("imgtags"));
        return usersImage;
    }
    //This method is used to create UserImage object for the Albums of user.
    //It will use default folderimage.png as image attached to it
    //
    public UsersImage createImages(Directory dirInfo){
        UsersImage usersImage = new UsersImage();
        usersImage.setCaption(dirInfo.getDirectoryName());
        usersImage.setImageURL("https://api.built.io/v1/uploads/553d74b11d2d6dc27e6a5584/download");
        usersImage.setDir(true);
        return usersImage;
    }

    public Bundle putUsersImageInBundle(UsersImage usersImage){
        Bundle bundle = new Bundle();
        bundle.putString("caption", usersImage.getCaption());
        bundle.putString("tags", usersImage.getTags());
        bundle.putString("location", usersImage.getLocation());
        bundle.putString("album", usersImage.getDirectoryNo());
        bundle.putString("imageURL", usersImage.getImageURL());
        bundle.putBoolean("isDir", usersImage.isDir());
        return bundle;
    }

    public UsersImage getUsersImageFromBundle(Bundle bundle){
        UsersImage usersImage = new UsersImage();
        usersImage.setCaption(bundle.getString("caption"));
        usersImage.setTags(bundle.getString("tags"));
        usersImage.setLocation(bundle.getString("location"));
        usersImage.setDirectoryNo(bundle.getString("album"));
        usersImage.setImageURL(bundle.getString("imageURL"));
        usersImage.setDir(bundle.getBoolean("isDir"));
        return usersImage;
    }
}
