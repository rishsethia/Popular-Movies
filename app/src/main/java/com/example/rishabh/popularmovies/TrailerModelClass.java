package com.example.rishabh.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Rishabh on 6/8/16.
 */
public class TrailerModelClass {

    List<Trailer> results;

    public static final class Trailer implements Parcelable {
        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        String key;
        String name;

        // Parcelable Implementation

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            dest.writeString(key);
            dest.writeString(name);
        }

        protected Trailer(Parcel in) {
            key = in.readString();
            name = in.readString();
        }

        Trailer(String key, String name) {
            this.key = key;
            this.name = name;
        }

        public static final Parcelable.Creator<Trailer> CREATOR = new Creator<Trailer>() {
            @Override
            public Trailer createFromParcel(Parcel source) {
                return new Trailer(source);
            }

            @Override
            public Trailer[] newArray(int size) {
                return new Trailer[size];
            }
        };
    }
}
