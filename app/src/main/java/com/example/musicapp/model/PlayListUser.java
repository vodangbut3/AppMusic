package com.example.musicapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class PlayListUser implements Parcelable {

    String ID;
    ArrayList<String> songs;

    protected PlayListUser(Parcel in) {
        ID = in.readString();
        songs = in.createStringArrayList();
    }

    public static final Creator<PlayListUser> CREATOR = new Creator<PlayListUser>() {
        @Override
        public PlayListUser createFromParcel(Parcel in) {
            return new PlayListUser(in);
        }

        @Override
        public PlayListUser[] newArray(int size) {
            return new PlayListUser[size];
        }
    };

    public ArrayList<String> getSongs() {
        return songs;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setSongs(ArrayList<String> songs) {
        this.songs = songs;
    }
    public PlayListUser(String ID,ArrayList<String> songs)
    {
        this.ID = ID;
        this.songs= songs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeList(songs);
    }
}
