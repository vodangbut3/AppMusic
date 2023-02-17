package com.example.musicapp.frament.home.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.musicapp.R;
import com.example.musicapp.Services.CallBack.AlbumCallBack;
import com.example.musicapp.Services.CallBack.SongCallBack;
import com.example.musicapp.dao.AlbumDAO;
import com.example.musicapp.dao.SongsDAO;
import com.example.musicapp.model.Album;
import com.example.musicapp.model.Song;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    RecyclerView rcvalbum, rcvsuggest, rcvtop;
    DotsIndicator dotsIndicator;
    ViewPager viewPager;
    Handler handler;
    Runnable runnable;
    LinearLayout linearLayout;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ImageView imghinh, imgrank, imgsong;
    TextView textviewplaynhactenbaihat, textviewplaynhactencasi, tvTypesong, tvindex, tvsongname, tvsongsinger, tvLike;


    int currentSong;
    ArrayList<Album> albumInRank;
    ArrayList<Song> SongList;
    ArrayList<Song> SongList1;
    ArrayList<Song> songsInRank;
    ArrayList<Song> songNew;
    AlbumAdapter albumAdapter;
    RankAdapter rankAdapter;
    SuggestAdapter suggestAdapter;
    SongNewAdapter songNewAdapter;
    ListSongAdapter listSongAdapter;
    ArrayList<Song> songs = new ArrayList<>();

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        init(root);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Songs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
//                                Log.d("suggesst", snapshot.getId() + " => " + snapshot.getData());

                            songs.add(new Song(snapshot.getId().toString(),
                                    snapshot.get("Image").toString(),
                                    snapshot.get("Link").toString(),
                                    snapshot.get("Name").toString(),
                                    snapshot.get("Singer").toString(),
                                    snapshot.get("Type").toString(),
                                    (ArrayList<String>) snapshot.get("userPlayList")));

                    }



                }
            }
        });
        SongsDAO songsDAO = new SongsDAO(getContext());
        rcvsuggest.setLayoutManager(new LinearLayoutManager(getContext()));
        listSongAdapter = new ListSongAdapter(getContext(), songsDAO.getSuggest(new SongCallBack() {
            @Override
            public void getCallBack(ArrayList<Song> song) {
                SongList = song;
//                suggestAdapter = new SuggestAdapter(getContext(), SongList, HomeFragment.this);
                rcvsuggest.setLayoutManager(new LinearLayoutManager(getContext()));
//                rcvsuggest.setAdapter(suggestAdapter);
            }
        }), HomeFragment.this);
        rcvsuggest.setAdapter(listSongAdapter);
//
        SongsDAO songsDAO1 = new SongsDAO(getContext());
        rcvtop.setLayoutManager(new LinearLayoutManager(getContext()));
        rankAdapter = new RankAdapter(getContext(), songsDAO1.getSuggest(new SongCallBack() {

            @Override
            public void getCallBack(ArrayList<Song> song) {
                SongList1 = song;
           //     rcvtop.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        }), HomeFragment.this);

        rcvtop.setAdapter(listSongAdapter);

        autoSwipe();
        return root;
    }

    private void init(View root) {
        rcvalbum = root.findViewById(R.id.rcvalbum);
        rcvsuggest = root.findViewById(R.id.rcvsuggest);
        rcvtop = root.findViewById(R.id.rcvtop);
        dotsIndicator = root.findViewById(R.id.dots_indicator);
        viewPager = root.findViewById(R.id.viewPager);
        imghinh = root.findViewById(R.id.imghinh);
        textviewplaynhactencasi = root.findViewById(R.id.textviewplaynhactencasi);
        textviewplaynhactenbaihat = root.findViewById(R.id.textviewplaynhactenbaihat);
        tvTypesong = root.findViewById(R.id.tvTypesong);
        linearLayout = root.findViewById(R.id.homeFragment);


    }

    public int intColor(int color)
    {

        return color;
    }


    @Override
    public void onStart() {
        getData();
        super.onStart();
    }

    private void getData() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.loading);
        dialog.show();

        final SongsDAO songsDAO = new SongsDAO(getContext());
        final AlbumDAO albumDAO = new AlbumDAO(getContext());


        songsDAO.getRankSongs(new SongCallBack() {
            @Override
            public void getCallBack(ArrayList<Song> song) {
                songsInRank = song;
//                rankAdapter = new RankAdapter(getContext(), song, HomeFragment.this);
//                rcvtop.setLayoutManager(new LinearLayoutManager(getActivity()));
//                rcvtop.setAdapter(rankAdapter);
            }
        });
        albumDAO.getAlbum(new AlbumCallBack() {
            @Override
            public void getCallback(ArrayList<Album> album) {
                albumInRank = album;
                albumAdapter = new AlbumAdapter(getContext(), albumInRank, HomeFragment.this,songs);
                rcvalbum.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                rcvalbum.setHasFixedSize(true);
                rcvalbum.setAdapter(albumAdapter);
            }

            @Override
            public void getCallBack(ArrayList<Album> album) {
                albumInRank = album;
                albumAdapter = new AlbumAdapter(getContext(), albumInRank, HomeFragment.this,songs);
                rcvalbum.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                rcvalbum.setHasFixedSize(true);
                rcvalbum.setAdapter(albumAdapter);
            }
        });

        songsDAO.getSuggest(new SongCallBack() {
            @Override
            public void getCallBack(ArrayList<Song> song) {
                SongList = song;
                suggestAdapter = new SuggestAdapter(getContext(), SongList, HomeFragment.this);
                rcvsuggest.setLayoutManager(new LinearLayoutManager(getActivity()));
                rcvsuggest.setAdapter(suggestAdapter);
            }
        });

        songsDAO.getNewSongs(new SongCallBack() {
            @Override
            public void getCallBack(ArrayList<Song> song) {
                songNew = song;
                Log.d("songNew", String.valueOf(songNew.size()));
                songNewAdapter = new SongNewAdapter(getContext(), songNew, HomeFragment.this);
                viewPager.setAdapter(songNewAdapter);
                dotsIndicator.setViewPager(viewPager);
            }
        });
        dialog.dismiss();
    }

    private void autoSwipe() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                currentSong = viewPager.getCurrentItem();
                currentSong++;
                try {
                    if (currentSong > viewPager.getAdapter().getCount()) {
                        currentSong = 0;
                    }
                    viewPager.setCurrentItem(currentSong, true);
                    handler.postDelayed(runnable, 4500);
                } catch (Exception e) {
                }
            }
        };
        handler.postDelayed(runnable, 4500);
    }
}


