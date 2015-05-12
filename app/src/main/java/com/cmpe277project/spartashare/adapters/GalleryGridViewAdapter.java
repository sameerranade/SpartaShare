package com.cmpe277project.spartashare.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmpe277project.spartashare.R;
import com.cmpe277project.spartashare.models.UsersImage;
import com.raweng.built.androidquery.AQuery;

import java.util.ArrayList;

/**
 * Created by Varun on 4/26/2015.
 */
public class GalleryGridViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList data = new ArrayList();

    public GalleryGridViewAdapter(Context context, int layoutResourceId, ArrayList data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        UsersImage item = (UsersImage) data.get(position);
        holder.imageTitle.setText(item.getCaption());
        //holder.image.setImageBitmap(item.getImage());
        //new LoadImage(holder.image, item.getImageURL());
        AQuery androidQuery = new AQuery(holder.image);
//fetch and set the image from internet, cache with file and memory
        androidQuery.id(holder.image).image(item.getImageURL());
        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }
}