package com.example.musicapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.musicapp.R;
import com.example.musicapp.frament.home.adapter.HomeFragment;
import com.example.musicapp.frament.home.adapter.ListSongAdapter;
import com.example.musicapp.frament.search.SongsAdapter;
import com.example.musicapp.model.Song;

import java.util.ArrayList;

public class UserPlayList extends AppCompatActivity {
    RecyclerView rcvUserList;
    SongsAdapter songsAdapter;
    HomeFragment homeFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_play_list);
        rcvUserList = findViewById(R.id.rcvUserPlayList);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UserPlayList.this );
        rcvUserList.setLayoutManager(layoutManager);
        ArrayList<Song> songList = new ArrayList<>();
        songList = getIntent().getParcelableArrayListExtra("arrayList");

        songsAdapter = new SongsAdapter(UserPlayList.this,songList);
        rcvUserList.setAdapter(songsAdapter);
    }
}