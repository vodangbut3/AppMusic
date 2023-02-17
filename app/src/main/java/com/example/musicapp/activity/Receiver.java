package com.example.musicapp.activity;

import static com.example.musicapp.service.musicService.ACTION_CLOSE;
import static com.example.musicapp.service.musicService.ACTION_NEXT;
import static com.example.musicapp.service.musicService.ACTION_PREVIOUS;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.musicapp.model.Song;
import com.example.musicapp.service.musicService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;

public class Receiver extends BroadcastReceiver {
    ArrayList<Song> albumList = new ArrayList<>();
    int next,pre;
    Song song;
    @Override
    public void onReceive(Context context, Intent intent) {
        int actionmusic = intent.getIntExtra("action",0);
        song = intent.getParcelableExtra("song");

      //  FirebaseFirestore db = FirebaseFirestore.getInstance();

        albumList = intent.getParcelableArrayListExtra("alBumList");
        if(actionmusic == 5)
        {

            Song song1 = prev(song);

            albumList.size();
            pre = albumList.indexOf(song1);

            String vitri = String.valueOf(pre);
            Song album = albumList.get(pre);

            String link = album.getLink();

            Intent intent1 = new Intent(context, musicService.class);
            intent1.putExtra("action",ACTION_PREVIOUS);
            intent1.putExtra("link",link);
            intent1.putExtra("song",(Parcelable) album);
            intent1.putParcelableArrayListExtra("albumlist",albumList);
            intent1.putExtra("pre",true);
            intent1.putExtra("next",false);
            intent1.putExtra("stopNofi",false);
            intent1.putExtra("pasuSer",false);
            context.startService(intent1);
            context.startService(intent1);

        }else if(actionmusic == 6 )
        {
            Song song1 = next(song);

            albumList.size();
            next = albumList.indexOf(song1);

            String vitri = String.valueOf(next);
            Song album = albumList.get(next);

            String link = album.getLink();

            Intent intent1 = new Intent(context, musicService.class);
            intent1.putExtra("action",ACTION_NEXT);
            intent1.putExtra("link",link);
            intent1.putExtra("song",(Parcelable) album);
            intent1.putExtra("pre",false);
            intent1.putExtra("next",true);
            intent1.putExtra("stopNofi",false);
            intent1.putExtra("pasuSer",false);
            intent1.putParcelableArrayListExtra("albumlist",albumList);
            context.startService(intent1);
        }else if(actionmusic == 1)
        {


           String link = song.getLink();
            Intent intent1 = new Intent(context, musicService.class);
            intent1.putExtra("action",actionmusic);
            intent1.putExtra("link",link);
            intent1.putExtra("song",(Parcelable) song);
            intent1.putExtra("statusbt",10);
            intent1.putExtra("pre",false);
            intent1.putExtra("next",false);
            intent1.putExtra("stopNofi",false);
            intent1.putExtra("pasuSer",false);
            intent1.putParcelableArrayListExtra("albumlist",albumList);
            context.startService(intent1);
        }else if(actionmusic == 2){
            Intent intent1 = new Intent(context, musicService.class);
            intent1.putExtra("action", actionmusic);
            intent1.putExtra("song",(Parcelable) song);
            intent1.putExtra("pre",false);
            intent1.putExtra("next",false);
            intent1.putExtra("link",song.getLink());
            intent1.putParcelableArrayListExtra("albumlist",albumList);
            intent1.putExtra("statusbt",11);
            intent1.putExtra("stopNofi",false);
            intent1.putExtra("pasuSer",true);
            context.startService(intent1);
        }else if(actionmusic == 3)
            {
                Intent intent1 = new Intent(context, musicService.class);
                intent1.putExtra("action", ACTION_CLOSE);
                intent1.putExtra("song",(Parcelable) song);
                intent1.putExtra("pre",false);
                intent1.putExtra("next",false);
                intent1.putExtra("link",song.getLink());
                intent1.putParcelableArrayListExtra("albumlist",albumList);
                intent1.putExtra("statusbt",12);
                intent1.putExtra("stopNofi",true);
                intent1.putExtra("pasuSer",false);
            }

    }
    public Song next(Song currentSong) {
        Song nextSong = null;
        int dem = 0;
        for (int i = 0; i <= albumList.size(); i++) {
            if (albumList.get(i).getID().equals(currentSong.getID())) {
                dem = i + 1;
                if (dem == albumList.size()) {
                    dem = 0;
                }
                nextSong = albumList.get(dem);
                break;

            }

        }

        return nextSong;
    }

    public Song prev(Song currentSong) {
        Song prevSong = null;
        int dem = 0;
        for (int i = 0; i <= albumList.size(); i++) {
            if (albumList.get(i).getID().equals(currentSong.getID())) {
                dem = i - 1;
                if (dem < 0) {
                    dem = albumList.size() - 1;
                }
                prevSong = albumList.get(dem);
                break;

            }

        }

        return prevSong;
        //Hàm Khi Kéo Seekbar bài hát sẽ tua đến vị trí tương ứng
    }

}

