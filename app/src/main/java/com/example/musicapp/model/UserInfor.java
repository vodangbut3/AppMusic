package com.example.musicapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class UserInfor implements Parcelable {
    private String ID ;
    private String Username;
    private ArrayList<String> Favorites = null;
    private String Password;
    private String Email;

    private String FaceBook;
    private String userPlaylist;
//    private String Image;

    private String kindID;
    //Biến xác định có phải là playlist hay không
    private Boolean isPlayList;
    //Biến xác định có phải là favorites hay không
    private Boolean isFavorites;

    private String TempID;
    //Biến xác định playlist hiện tại người dùng đang xem
    private String TempPlayListID;
    //Biến Xác định album hiện tại người dùng đang xem
    private ArrayList<String> CurrentAlbum;

    private static UserInfor instance = new UserInfor();
    private UserInfor() { }

    protected UserInfor(Parcel in) {
        ID = in.readString();
        Username = in.readString();
        Favorites = in.createStringArrayList();
        Password = in.readString();
        Email = in.readString();
        FaceBook = in.readString();
        userPlaylist = in.readString();
        kindID = in.readString();
        byte tmpIsPlayList = in.readByte();
        isPlayList = tmpIsPlayList == 0 ? null : tmpIsPlayList == 1;
        byte tmpIsFavorites = in.readByte();
        isFavorites = tmpIsFavorites == 0 ? null : tmpIsFavorites == 1;
        TempID = in.readString();
        TempPlayListID = in.readString();
        CurrentAlbum = in.createStringArrayList();
    }

    public static final Creator<UserInfor> CREATOR = new Creator<UserInfor>() {
        @Override
        public UserInfor createFromParcel(Parcel in) {
            return new UserInfor(in);
        }

        @Override
        public UserInfor[] newArray(int size) {
            return new UserInfor[size];
        }
    };

    public static  UserInfor getInstance(){
        return instance;
    }



    public UserInfor(String ID, String username, ArrayList<String> favorites,  String password, String email, String FaceBook) {
        this.ID = ID;
        this.Username = username;
        this.Favorites = favorites;


        this.Password = password;
        this.Email = email;
        this.FaceBook = FaceBook;


    }
    public UserInfor(String ID, String username, ArrayList<String> favorites,  String password, String email, String FaceBook,ArrayList<String> userPlayList) {
        this.ID = ID;
        this.Username = username;
        this.Favorites = favorites;


        this.Password = password;
        this.Email = email;
        this.FaceBook = FaceBook;

       // this.userPlaylist = userPlayList;
    } public UserInfor(String ID, String username, ArrayList<String> favorites,  String password, String email, String FaceBook,String userPlayList) {
        this.ID = ID;
        this.Username = username;
        this.Favorites = favorites;


        this.Password = password;
        this.Email = email;
        this.FaceBook = FaceBook;

        this.userPlaylist = userPlayList;
    }
    public UserInfor(String ID, String username, ArrayList<String> favorites,  String password, String email, String FaceBook,String userPlayList,String Image) {
        this.ID = ID;
        this.Username = username;
        this.Favorites = favorites;


        this.Password = password;
        this.Email = email;
        this.FaceBook = FaceBook;

        this.userPlaylist = userPlayList;
//        this.Image = Image;
    }



    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public ArrayList<String> getFavorites() {
        return Favorites;
    }

    public void setFavorites(ArrayList<String> favorites) {
        this.Favorites = favorites;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

//    public void setImage(String image) {
//        Image = image;
//    }
//
//    public String getImage() {
//        return Image;
//    }

    public String getKindID() {
        return kindID;
    }

    public void setKindID(String kindID) {
        this.kindID = kindID;
    }

    public String getFaceBook() {
        return FaceBook;
    }

    public void setFaceBook(String faceBook) {
        FaceBook = faceBook;
    }

    //==================================================================================================================
//Danh Sách Mã Bài Hát được truyền từ accoutfragment-favorite hoặc playlist fragment


    public String getUserPlaylist() {
        return userPlaylist;
    }

    public void setUserPlaylist(String userPlaylist) {
        this.userPlaylist = userPlaylist;
    }

    public Boolean getisPlayList() {
        return isPlayList;
    }

    public void setisPlayList(Boolean playList) {
        isPlayList = playList;
    }

    public String getTempID() {
        return TempID;
    }

    public void setTempID(String tempID) {
        TempID = tempID;
    }

    //Biến tạm của ID playlist được get khi thao tác xóa phần tử trong playlist từ playlistsongid_adaoter
    public String getTempPlayListID() {
        return TempPlayListID;
    }
    //Biến tạm của ID playlist được set khi nhấn vào playlist bất kỳ, gọi trong playlist adapter
    public void setTempPlayListID(String tempPlayListID) {
        TempPlayListID = tempPlayListID;
    }

    public ArrayList<String> getCurrentAlbum() {
        return CurrentAlbum;
    }

    public void setCurrentAlbum(ArrayList<String> currentAlbum) {
        CurrentAlbum = currentAlbum;
    }

    public void setisFavorites(Boolean favorites) {
        isFavorites = favorites;
    }

    public Boolean getisFavorites(){
        return isFavorites;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(Username);
        dest.writeList(Favorites);
        dest.writeString(Password);
        dest.writeString(Email);
        dest.writeString(FaceBook);
        dest.writeString(userPlaylist);
//        dest.writeString(Image);
    }
}