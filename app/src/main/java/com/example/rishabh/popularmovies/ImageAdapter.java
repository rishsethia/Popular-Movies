package com.example.rishabh.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ImageAdapter extends ArrayAdapter<String> {

    private Context myContext;
    private List<String> imagePathList;

    public ImageAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        myContext = context;
        imagePathList = objects;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // If it's not recycled some attributes must be initialised
            imageView = new ImageView(myContext);
            imageView.setPadding(0, 0, 0, 0);

            // this maintains image's aspect ratio so both the dimensions of the image will be equla
            // or larger than corresponding image view
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        } else {
            //Recycled view
            imageView = (ImageView) convertView;
        }

        // Our ImagePathList should not be equal to null (Very Important)
        if (imagePathList != null) {

            // Picasso always does the downloading of image in the background thread so can be safely
            //done on any thread

            // TODO Get the sharedpreference and write the code for the manual loading here

            // Getting the shared preferences and deciding on basis of that
            SharedPreferences sortPreference = PreferenceManager.getDefaultSharedPreferences(myContext);
            String sortByIntValue = sortPreference.getString(myContext.getString(R.string.sortByKey)
                    , myContext.getString(R.string.sortByDefaultValue));
            int sortByValue = Integer.parseInt(sortByIntValue);
            if (sortByValue == 3) {
                // Load for the favourites here
                String filename = imagePathList.get(position).substring(1);
                loadImageFromStorage(filename, imageView);

            } else {
                try {
                    Picasso.with(myContext).load(buildUrlForPoster(imagePathList.get(position)))
                            .into(imageView);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }

        }

        return imageView;
    }

    // This is a utility function and was wrongly placed initially
    // this method takes in relative poster path and converts it to corresponding url
    //Correct result is returned but the size is a bit small
    public static String buildUrlForPoster(String posterPathName) throws MalformedURLException {

        // Substringing "\" from the string

        posterPathName = posterPathName.substring(1);

        // Recommended size for the poster = w185
        //w500 looks the dopest


        Uri uri = new Uri.Builder().scheme("http").authority("image.tmdb.org")
                .appendPath("t").appendPath("p")
                .appendPath("w500")
                .appendPath(posterPathName).build();

        URL url = new URL(uri.toString());
        return url.toString();
    }

    public void loadImageFromStorage(String filename, ImageView imageView) {

        try {
            File f = new File(myContext.getCacheDir(), filename);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imageView.setImageBitmap(b);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}

