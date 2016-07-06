package com.example.rishabh.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;


// This is used to map the JSON keys to the object by gson
public class Movie implements Parcelable {

    public String original_title;
    public String overview;
    public String poster_path;
    public float vote_average;
    public String release_date;
    public String id;


    // getters and setters
    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // for parcelable

    protected Movie(Parcel in) {
        original_title = in.readString();
        overview = in.readString();
        poster_path = in.readString();
        vote_average = in.readFloat();
        release_date = in.readString();
        id = in.readString();
    }

    public Movie(String original_title, String overview, String imageURL, float vote_average, String release_date, String id) {
        this.original_title = original_title;
        this.overview = overview;
        this.poster_path = imageURL;
        this.vote_average = vote_average;
        this.release_date = release_date;
        this.id = id;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(original_title);
        parcel.writeString(overview);
        parcel.writeString(poster_path);
        parcel.writeFloat(vote_average);
        parcel.writeString(release_date);
        parcel.writeString(id);
    }


    // returns the poster path
    public String getPosterPath() {
        //returns the poster path and allows data hiding of the poster path string for the accidental change
        return this.poster_path;
    }
}
