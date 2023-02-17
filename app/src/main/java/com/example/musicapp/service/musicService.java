package com.example.musicapp.service;


import static com.example.musicapp.activity.Nofication.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.musicapp.R;
import com.example.musicapp.activity.Receiver;
import com.example.musicapp.activity.playmusic;
import com.example.musicapp.model.Song;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class musicService extends Service {
    public static final int ACTION_PLAY = 1;
    public static final int ACTION_PAUSE = 2;
    public static final int ACTION_CLOSE =3;
    public static final int ACTION_REWIND = 4;
    public static final String ACTION_FAST_FORWARD = "action_fast_foward";
    public static final int ACTION_NEXT = 6;
    public static final int ACTION_PREVIOUS = 5;
    public static final int ACTION_STOP = 7;
    public static final String EVENT_MUSIC = "EVENt_MUSIC";
    Song song;
    private MediaSessionManager mManager;
    private MediaSession mSession;
    private MediaController mController;
    Context ctx;
    private static MediaPlayer mediaPlayer;
    String url;
    String chuoi;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean stopnofi,pauseSer;
    public musicService() {
    }
    IBinder binder = new musicServiceBinder();
    Notification notification;
    boolean isPlaying;

    public class musicServiceBinder extends Binder
    {
        public musicService getService()
        {
            return musicService.this;
        }
    }
   ArrayList<Song> albumList = new ArrayList<>();
    int onoff;
    boolean pre,next;
    @Override
    public IBinder onBind(Intent intent) {
      return  null;



    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        chuoi=intent.getStringExtra("link");
       song = intent.getParcelableExtra("song");
        albumList = intent.getParcelableArrayListExtra("albumlist");
        onoff = intent.getIntExtra("statusbt",  0);
        pre = intent.getBooleanExtra("pre",false);
        next = intent.getBooleanExtra("next",false);
        stopnofi = intent.getBooleanExtra("stopNofi",false);
        pauseSer = intent.getBooleanExtra("pasuSer",false);

        Intent i = new Intent(EVENT_MUSIC);
        i.putExtra("statusbt",onoff);
        i.putExtra("playSongPre",pre);
        i.putExtra("playSongNext",next);
        i.putExtra("stoptrue",stopnofi);
        i.putExtra("pause",pauseSer);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        if(stopnofi == true)

        {
            stopSelf();
            isPlaying = false;
        }
       int action = intent.getIntExtra("action",0);
       if(action != 0)
       {
           handleAction(action,song,albumList);
       }else {

               mediaPlayer = new MediaPlayer();
               mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
               mediaPlayer.setLooping(true);
               mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                   @Override
                   public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                       return false;
                   }

               });
               mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                   @Override
                   public void onPrepared(MediaPlayer mediaPlayer) {
                       if (mediaPlayer.isPlaying()) {
                           mediaPlayer.stop();
                       } else {
                           mediaPlayer.start();
                       }
                   }
               });
               try {
                   mediaPlayer.setDataSource(chuoi);
               } catch (IOException e) {
                   e.printStackTrace();
               }

               mediaPlayer.prepareAsync();
               // mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.anni);

               mediaPlayer.start();
               isPlaying = true;

           noficationshow(song, albumList);
           //  sendNotification();
       }
        return START_STICKY;
    }
    private void sendNotification(String srtData)
    {

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }

   /* public void batnhac(String chuoi) {
        mediaPlayer = new MediaPlayer();


        url = chuoi;

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setLooping(true);

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    return false;
                }
            });

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                    }else {
                        mediaPlayer.start();
                    }
                }
            });
            try {
                mediaPlayer.setDataSource(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mediaPlayer.prepareAsync();

            }*/
    private void handleAction(int action,Song song,ArrayList<Song> albumList2)
    {

        switch (action)
        {
            case 1 :
                if(mediaPlayer == null && isPlaying == false ) {
                    playSong(song,albumList2,1);
                    isPlaying = true;
                }else if(mediaPlayer !=null && isPlaying == false)
                {
                    mediaPlayer.start();
                    isPlaying = true;
                }else if(mediaPlayer != null && isPlaying == true)
                {
                    mediaPlayer.stop();
                    playSong(song,albumList2,1);
                    isPlaying = false;
                }else {playSong(song,albumList2,1);
                isPlaying =false;}
                break;

            case 2:
                mediaPlayer.pause();

                    isPlaying = false;
                noficationshow(song, albumList2);


                break;
            case 3:
                // mediaPlayer.pause();

               isPlaying = false;
                break;
            case 4:
                if(mediaPlayer != null && !isPlaying)
                {
                    mediaPlayer.start();
                    isPlaying = false;
                }
                break;


            case 5:

             /*       stopSelf();
                    playSong(song,albumList2,1);*/




                break;
            case 6:

                  /*  stopSelf();
                    playSong(song,albumList2,1);*/


              break;
            default:
                playSong(song,albumList2,1);
                break;

        }
    }

    public void playSong(Song song,ArrayList<Song> albumList4,int kol)
    {


                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setLooping(true);
                mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                        return false;
                    }

                });
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                        } else {
                            mediaPlayer.start();
                        }
                    }
                });
                try {
                    mediaPlayer.setDataSource(chuoi);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.prepareAsync();
                // mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.anni);

                mediaPlayer.start();
                noficationshow(song, albumList4);

    }
    private PendingIntent getpendingintent(Context context, int action, Song song1, ArrayList<Song> alBumList)
    {
            Intent intent = new Intent(this, Receiver.class);
            intent.putExtra("action",action);
            intent.putExtra("song",(Parcelable) song1);
            intent.putParcelableArrayListExtra("alBumList",alBumList);
            return PendingIntent.getBroadcast(context.getApplicationContext(),action,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }
        private void noficationshow(Song song,ArrayList<Song> albumList)
        {



         //   int vitri = albumList.indexOf(song);
          //  Song song5 = albumList.get(vitri);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dongnhi);
            MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(this,"tag");
            notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                    .setSmallIcon(R.drawable.musicicon)
                   .setSubText(song.getName())
                    .setContentTitle("")
                    .setContentText(song.getSinger())
                    .setLargeIcon(bitmap)

                    .addAction(R.drawable.ic_baseline_prev,"Previous",getpendingintent(this,ACTION_PREVIOUS,song,albumList))
                    .addAction(R.drawable.ic_baseline_play,"Play",getpendingintent(this,1,song,albumList))
                    .addAction(R.drawable.ic_baseline_next,"Next",getpendingintent(this,ACTION_NEXT,song,albumList))
                    .addAction(R.drawable.ic_baseline_pause,"Pause",getpendingintent(this,ACTION_PAUSE,song,albumList))
                    .addAction(R.drawable.ic_baseline_stop_circle_24,"Stop",getpendingintent(this,ACTION_CLOSE,song,albumList))
                    .setSound(null)

                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                            .setShowActionsInCompactView(4)
                            .setMediaSession(mediaSessionCompat.getSessionToken()))
                    .build();



              startForeground(1,notification);

        }

    }
