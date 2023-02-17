package com.example.musicapp.frament.home.adapter;

import android.content.Context;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.example.musicapp.R;
import com.example.musicapp.model.Song;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SongNewAdapter extends PagerAdapter {
    Context context;
    ArrayList<Song> songs;
    HomeFragment homeFragment;

    public SongNewAdapter(Context context, ArrayList<Song> songs, HomeFragment homeFragment) {
        this.context = context;
        this.songs = songs;
        this.homeFragment = homeFragment;
    }
    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_songnew, null);
        ImageView imgSongnew = view.findViewById(R.id.imgSongNew);
        Picasso.get().load(songs.get(position).getImage()).into(imgSongnew);
        container.addView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFrament(position,v);
            }
        });
        return view;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
    private void ChangeFrament(int position, View view){

        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        Bundle bundle = new Bundle();
        bundle.putParcelable("Songs",songs.get(position));
        bundle.putInt("fragment",4);
       // Fragment myFragment = new PlayMusicFragment();
       // myFragment.setArguments(bundle);
      //  activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
    }
}
