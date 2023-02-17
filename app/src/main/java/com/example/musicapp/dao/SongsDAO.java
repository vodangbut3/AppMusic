package com.example.musicapp.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.musicapp.Services.CallBack.SongCallBack;
import com.example.musicapp.model.PlayListUser;
import com.example.musicapp.model.Song;
import com.example.musicapp.model.SongIDList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;

public class SongsDAO {
    Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Song song = new Song();
    ArrayList<Song> suggestList = new ArrayList<>(); //đề xuất danh sách
    ArrayList<Song> ranktList = new ArrayList<>();//Danh sách xếp hạng
    ArrayList<Song> songIdList = new ArrayList<>();//danh sách bài hát
    ArrayList<Song> songNewList = new ArrayList<>();// Gửi danh sách mới
    ArrayList<Song> songList = new ArrayList<>();
    ArrayList<PlayListUser> playListUsers = new ArrayList<>();
    public SongsDAO(Context context) {
        this.context = context;
        db.collection("Songs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                songList.add(new Song(document.get("ID").toString(),
                                        document.get("Image").toString(),
                                        document.get("Link").toString(),
                                        document.get("Name").toString(),
                                        document.get("Singer").toString(),
                                        document.get("Type").toString(),
                                        (ArrayList<String>) document.get("userPlayList")));


                            }
                        }
                    }
                });
        db.collection("PlayList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                playListUsers.add(new PlayListUser(document.get("IDPL").toString(),
                                        (ArrayList<String>)document.get("Songs")));


                            }
                        }
                    }
                });

    }




    public ArrayList<Song> getSuggest(SongCallBack songCallBack) {
        // Truy cập phiên bản Cloud Firestore từ Hoạt động của bạn


        db.collection("Songs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
//                                Log.d("suggesst", snapshot.getId() + " => " + snapshot.getData());

                        suggestList.add(new Song(snapshot.getId().toString(),
                                snapshot.get("Image").toString(),
                                snapshot.get("Link").toString(),
                                snapshot.get("Name").toString(),
                                snapshot.get("Singer").toString(),
                               snapshot.get("Type").toString(),
                                (ArrayList<String>) snapshot.get("userPlayList")));

                    } // handle random
                    int songListSize = suggestList.size();

                    for (int i = 0; i < songListSize; i++) {
                        Song randomsong = suggestList.get(new Random().nextInt(songListSize));

                        if (!suggestList.contains(randomsong)) {suggestList.add(randomsong);
                            if (suggestList.size() == 5) {

                                break;
                            }
                        }
                    }



                } else {
                    Log.w("AAA", "Error getting documents.", task.getException());
                    ToastAnnotation("Có Lỗi Xảy Ra");
                }
            }
        });
       return suggestList;
    }
    public ArrayList<Song> getSongList(String userPlay)
    {
        ArrayList<Song> song2 = new ArrayList<>();
        PlayListUser playListUser1;
        ArrayList<String> songuser = new ArrayList<>();
        for (int i = 0;i<playListUsers.size();i++ )
        {
            if(userPlay.equals(playListUsers.get(i).getID()))
            {
                    songuser=playListUsers.get(i).getSongs();
            }
        }

        for(int i= 0;i<songList.size();i++)
        {
            for(int z = 0; z<songuser.size();z++) {
                if (songList.get(i).getID().toString().equals(songuser.get(z))) {
                    song2.add(songList.get(i));
                }
            }


        }
        return song2;
    };


    public void getRankSongs(final SongCallBack songCallBack) {
        // Truy cập phiên bản Cloud Firestore từ Hoạt động của bạn
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Song").orderBy("Like", Query.Direction.DESCENDING).limit(5).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                Log.d("AAA", snapshot.getId() + " => " + snapshot.getData());
                                song = snapshot.toObject(Song.class);
                                ranktList.add(song);
                                songCallBack.getCallBack(ranktList);
                            }
                        } else {
                            Log.w("AAA", "Error getting documents.", task.getException());
                            ToastAnnotation("Có Lỗi Xảy Ra");
                        }
                    }
                });
    }

    private void getSongsFromList(SongIDList songIDList, final SongCallBack songCallBack) {
        Log.d("songIDList", songIDList.getData().toString());
        for (int i = 0; i < songIDList.getData().size(); i++) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Songs").whereEqualTo("ID", songIDList.getData().get(i)).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                    Log.d("songIDList", snapshot.getId() + "=>" + snapshot.getData());
                                    song = snapshot.toObject(Song.class);
                                    songIdList.add(song);
                                    songCallBack.getCallBack(songIdList);
                                }
                            } else {
                                Log.w("songIDList", "Error getting documents.", task.getException());
                                ToastAnnotation("Có Lỗi Xảy Ra");
                            }
                        }
                    });
        }
    }
    public void getNewSongs(final SongCallBack songCallBack) {
        // Truy cập phiên bản Cloud Firestore từ Hoạt động của bạn
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Songs").orderBy("ID", Query.Direction.DESCENDING).limit(5).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                Log.d("BannerSongs", snapshot.getId() + "=>" + snapshot.getData());
                                song = snapshot.toObject(Song.class);
                                songNewList.add(song);
                            }
                            songCallBack.getCallBack(songNewList);
                        } else {
                            Log.w("BannerSongs", "Error getting documents.", task.getException());
                            ToastAnnotation("Có Lỗi Xảy Ra");
                        }
                    }
                });
    }

    private void ToastAnnotation(String mesage) {
        Toast.makeText(context, mesage, Toast.LENGTH_SHORT).show();
    }
}
