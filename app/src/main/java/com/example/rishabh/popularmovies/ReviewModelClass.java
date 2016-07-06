package com.example.rishabh.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Rishabh on 6/13/16.
 */
public class ReviewModelClass {

    ArrayList<Reviews> results;

    public static final class Reviews implements Parcelable {

        String content;
        String author;

        // getters and setters
        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            dest.writeString(content);
            dest.writeString(author);

        }

        protected Reviews(Parcel in) {
            content = in.readString();
            author = in.readString();
        }


        public static final Parcelable.Creator<Reviews> CREATOR = new Creator<Reviews>() {
            @Override
            public Reviews createFromParcel(Parcel source) {
                return new Reviews(source);
            }

            @Override
            public Reviews[] newArray(int size) {
                return new Reviews[size];
            }
        };

        Reviews() {

        }


    }

}
