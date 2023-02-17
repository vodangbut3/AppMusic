package com.example.musicapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Song implements Parcelable {
    private String Image;
    private String Link;
    private String Singer;
    private String Type;
    private String ID;
    private String Name;
    private Integer Like;
    private ArrayList<String> userPlayList;
    public Song() {
    }


    public Song(String ID,String Image,String Link,String Name,String Singer,String Type ,Integer Like) {

        this.ID = ID;
        this.Image = Image;
        this.Link = Link;
        this.Name = Name;
        this.Singer = Singer;
        this.Type = Type;
        this.Like = Like;
    }
    public Song(String ID,String Image,String Link,String Name,String Singer,String Type ,ArrayList<String> userPlayList) {

        this.ID = ID;
        this.Image = Image;
        this.Link = Link;
        this.Name = Name;
        this.Singer = Singer;
        this.Type = Type;

        this.userPlayList = userPlayList;
    }

    public Song(String ID,String Image,String Link,String Name,String Singer,String Type) {

        this.ID = ID;
        this.Image = Image;
        this.Link = Link;
        this.Name = Name;
        this.Singer = Singer;
        this.Type = Type;
    }
    public Song(String ID,String Image,String Link,String Name,String Type) {

        this.ID = ID;
        this.Image = Image;
        this.Link = Link;
        this.Name = Name;

        this.Type = Type;
    }

    public Song(String ID,String Image,String Link,String Name) {

        this.ID = ID;
        this.Image = Image;
        this.Link = Link;
        this.Name = Name;

       // this.Type = Type;
    }

    protected Song(Parcel in) {
        Image = in.readString();
        Link = in.readString();
        Singer = in.readString();
        Type = in.readString();
        ID = in.readString();
        Name = in.readString();
        if (in.readByte() == 0) {
            Like = null;
        } else {
            Like = in.readInt();
        }
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getSinger() {
        return Singer;
    }

    public ArrayList<String> getUserPlayList() {
        return userPlayList;
    }

    public void setUserPlayList(ArrayList<String> userPlayList) {
        this.userPlayList = userPlayList;
    }

    public void setSinger(String singer) {
        Singer = singer;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getLike() {
        return Like;
   }

    public void setLike(Integer like) {
        Like = like;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Image);
        parcel.writeString(Link);
        parcel.writeString(Singer);
        parcel.writeString(Type);
        parcel.writeString(ID);
        parcel.writeString(Name);
       if (Like == null) {
            parcel.writeByte((byte) 0);
       } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(Like);
        }
     //   parcel.writeList(userPlayList);
    }
}
