package com.example.ja010.project;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ja010 on 17-06-01.
 */

public class dataclass implements Parcelable {
    String URL="";
    String MUSIC_NAME="";
    public dataclass(String URL,String MUSIC_NAME){
        this.URL = URL;
        this.MUSIC_NAME =MUSIC_NAME;
    }
    public String getURL(){
        return  URL;
    }
    public String getMUSIC_NAME(){
        return  MUSIC_NAME;
    }


    protected dataclass(Parcel in) {
        URL = in.readString();
        MUSIC_NAME = in.readString();
    }

    public static final Creator<dataclass> CREATOR = new Creator<dataclass>() {
        @Override
        public dataclass createFromParcel(Parcel in) {
            return new dataclass(in);
        }

        @Override
        public dataclass[] newArray(int size) {
            return new dataclass[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(URL);
        parcel.writeString(MUSIC_NAME);
    }
}
