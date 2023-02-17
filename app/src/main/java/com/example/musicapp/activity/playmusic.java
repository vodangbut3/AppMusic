package com.example.musicapp.activity;

import static com.example.musicapp.service.musicService.ACTION_PAUSE;
import static com.example.musicapp.service.musicService.ACTION_PLAY;
import static com.example.musicapp.service.musicService.EVENT_MUSIC;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapp.R;
import com.example.musicapp.model.Song;
import com.example.musicapp.service.musicService;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class playmusic extends AppCompatActivity {
    Button btnPlay, btnStop, btnPrev, btnNext;
    TextView tenbaihat,tencasy;
    musicService service;
    private List<Song> albumList;
    ArrayList<Song> songs;
    boolean binded = false;

    MediaPlayer player;
    String linkbh;
    SeekBar seekBar;
    Handler handler = new Handler();
    Runnable runnable;
    int next, prev;
    Song song, song2;
    TextView position, playerDuration;
    MediaPlayer mediaPlayer = new MediaPlayer() ;
    String linkbhht;
    boolean pre;
    boolean nextSong,stopnofi,pause;
    int onoff;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
                onoff = intent.getIntExtra("statusbt",0);

               pre = intent.getBooleanExtra("playSongPre",false);
               nextSong = intent.getBooleanExtra("playSongNext",false);
               stopnofi = intent.getBooleanExtra("stoptrue",false);
               pause = intent.getBooleanExtra("pause",false);

            if(stopnofi == true)
            {
                btnPlay.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.GONE);
                Intent i = new Intent(playmusic.this, musicService.class);

                stopService(i);
            }
            if (onoff == 10) {
                btnPlay.setVisibility(View.GONE);
                btnStop.setVisibility(View.VISIBLE);
            }else if(onoff == 11)
            {
                btnPlay.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.GONE);

            }



            if(pre == true && nextSong == false)
            {
               /* Intent i = new Intent(playmusic.this, musicService.class);

                stopService(i);*/
                prevService(song,albumList);

            }else if(pre == false && nextSong == true)
                {
                     /* Intent i = new Intent(playmusic.this, musicService.class);

                stopService(i);*/
                    nextService(song ,albumList);
                }



        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playmusic);
        btnPlay = findViewById(R.id.bt_play);
        btnStop = findViewById(R.id.bt_pause);
        btnPrev = findViewById(R.id.bt_prev);
        btnNext = findViewById(R.id.bt_next);
        position = findViewById(R.id.player_position);
        playerDuration = findViewById(R.id.player_duration);
        seekBar = findViewById(R.id.seek_bar);
        tenbaihat=findViewById(R.id.tenbaihat);
        tencasy=findViewById(R.id.tencasy);


        Toast.makeText(this, "onoff"+onoff, Toast.LENGTH_SHORT).show();

//        player = MediaPlayer.create(this, Uri.parse("link"));
//        btnPlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                player.start();
//            }
//        });
//        seekBar.setMax(player.getDuration());
//        new Timer().scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                seekBar.setProgress(player.getCurrentPosition());
//            }
//
//        },0,900);
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                player.seekTo(progress);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });


        song = getIntent().getParcelableExtra("song");
        String id = getIntent().getStringExtra("id");
        linkbh = getIntent().getExtras().getString("link");
        linkbhht = linkbh;
        albumList = getIntent().getParcelableArrayListExtra("album");
        song2 = song;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        tenbaihat.setText(song2.getName());
        tencasy.setText(song2.getSinger());
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,new IntentFilter(EVENT_MUSIC));
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Intent i = new Intent(playmusic.this, musicService.class);
                playService(linkbhht);
               /* Intent i = new Intent(playmusic.this, musicService.class);

                i.setAction(musicService.ACTION_PLAY);
                i.putExtra("link", linkbh);
                laychuoi(linkbh);
                stopService(i);
                startService(i);
                btnPlay.setVisibility(View.GONE);
                btnStop.setVisibility(View.VISIBLE);*/
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        stopSer(song2, albumList);

            /*    btnStop.setVisibility(View.GONE);
                btnPlay.setVisibility(View.VISIBLE);
                Intent i = new Intent(playmusic.this, musicService.class);
                i.putExtra("song",(Parcelable) song2);
                i.putExtra("action",ACTION_PAUSE);
                i.putExtra("albumlist",(ArrayList<Song>)albumList);
                i.putExtra("statusbt",0);
                i.putExtra("pre",false);
                i.putExtra("next",false);
                i.putExtra("stopNofi",false);

               startService(i);*/

            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextService(song2,albumList);
             /*   Song song1 = next(song2);
                song2 = next(song2);
                albumList.size();
                next = albumList.indexOf(song1);
                tenbaihat.setText(song2.getName());
                tencasy.setText(song2.getSinger());
                String vitri = String.valueOf(next);
                Song album = albumList.get(next);
                btnPlay.setVisibility(View.GONE);
                btnStop.setVisibility(View.VISIBLE);
                String link = album.getLink();
                Intent i = new Intent(playmusic.this, musicService.class);
                i.putExtra("link", link);
//                Toast.makeText(playmusic.this, "" + next, Toast.LENGTH_SHORT).show();
                stopService(i);
                startService(i);*/
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevService(song2,albumList);
               /* Song song1 = prev(song2);
                song2 = prev(song2);
                albumList.size();
                prev = albumList.indexOf(song1);
                btnPlay.setVisibility(View.GONE);
                btnStop.setVisibility(View.VISIBLE);
                String vitri = String.valueOf(prev);
                Song album = albumList.get(prev);
                tenbaihat.setText(song2.getName());
                tencasy.setText(song2.getSinger());
                String link = album.getLink();
                Intent i = new Intent(playmusic.this, musicService.class);
                i.putExtra("link", link);
//                Toast.makeText(playmusic.this, "" + prev, Toast.LENGTH_SHORT).show();
                stopService(i);
                startService(i);*/
            }
        });
        //Hàm Khi Kéo Seekbar bài hát sẽ tua đến vị trí tương ứng
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        }

    public String laychuoi(String chuoi) {
        return chuoi;
    }
    public void playService(String linkbtht)
    {
        Intent i = new Intent(playmusic.this, musicService.class);
        linkbh = linkbtht;
     //   i.setAction(ACTION_PLAY);
        i.putExtra("link", linkbh);
        laychuoi(linkbh);
        i.putExtra("song",(Parcelable) song2);
        i.putExtra("action",ACTION_PLAY);
        i.putExtra("albumlist",(ArrayList<Song>)albumList);
        i.putExtra("statusbt",0);
        i.putExtra("pre",false);
        i.putExtra("next",false);
        i.putExtra("stopNofi",false);
        i.putExtra("pasuSer",false);
        startService(i);
        btnPlay.setVisibility(View.GONE);
        btnStop.setVisibility(View.VISIBLE);
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
    public void nextService(Song song,List<Song> albumList)
    {
        Song song1 = next(song);
        song2 = next(song);
        albumList.size();
        next = albumList.indexOf(song1);
        tenbaihat.setText(song2.getName());
        tencasy.setText(song2.getSinger());
        String vitri = String.valueOf(next);
        Song album = albumList.get(next);
        btnPlay.setVisibility(View.GONE);
        btnStop.setVisibility(View.VISIBLE);
        String link = album.getLink();
        linkbh = link;
        Intent i = new Intent(playmusic.this, musicService.class);
        i.putExtra("link", link);
//                Toast.makeText(playmusic.this, "" + next, Toast.LENGTH_SHORT).show();
        i.putExtra("song",(Parcelable)song2);
        i.putExtra("action",7);
        i.putExtra("albumlist",(ArrayList<Song>)albumList);
        i.putExtra("statusbt",0);
        i.putExtra("pre",false);
        i.putExtra("next",false);
        i.putExtra("stopNofi",false);
        i.putExtra("pasuSer",false);
        stopService(i);
        startService(i);
    }
    public void stopSer(Song song,List<Song> albumList)
    {
        btnStop.setVisibility(View.GONE);
        btnPlay.setVisibility(View.VISIBLE);
        Intent i = new Intent(playmusic.this, musicService.class);
        i.putExtra("song",(Parcelable) song);
        i.putExtra("action",ACTION_PAUSE);
        i.putExtra("albumlist",(ArrayList<Song>)albumList);
        i.putExtra("statusbt",0);
        i.putExtra("pre",false);
        i.putExtra("pasuSer",false);
        i.putExtra("next",false);
        i.putExtra("stopNofi",false);

        startService(i);
    }
    public void prevService(Song song, List<Song> albumList)
    {
        Song song1 = prev(song);
        song2 = prev(song);
        albumList.size();
        prev = albumList.indexOf(song1);
        btnPlay.setVisibility(View.GONE);
        btnStop.setVisibility(View.VISIBLE);
        String vitri = String.valueOf(prev);
        Song album = albumList.get(prev);
        tenbaihat.setText(song.getName());
        tencasy.setText(song.getSinger());
        String link = album.getLink();
         linkbh = link;
        Intent i = new Intent(playmusic.this, musicService.class);
        i.putExtra("link", link);
//                Toast.makeText(playmusic.this, "" + prev, Toast.LENGTH_SHORT).show();
        i.putExtra("song",(Parcelable) song2);
        i.putExtra("action",7);
        i.putExtra("albumlist",(ArrayList<Song>)albumList);
        i.putExtra("statusbt",0);
        i.putExtra("pre",false);
        i.putExtra("next",false);
        i.putExtra("stopNofi",false);
        i.putExtra("pasuSer",false);
        stopService(i);
        startService(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }
}
