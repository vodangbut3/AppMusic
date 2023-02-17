package com.example.musicapp.frament.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.R;
import com.example.musicapp.activity.playmusic;
import com.example.musicapp.model.Song;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListSongAdapter extends RecyclerView.Adapter<ListSongAdapter.ListSongViewHolder>{
    Context context;
    ArrayList<Song> songArrayList;
    HomeFragment homeFragment;
    ImageView imghinh,imgHinh2;

    public ListSongAdapter(Context context, ArrayList<Song> songArrayList, HomeFragment homeFragment) {
        this.context = context;
        this.songArrayList = songArrayList;
        this.homeFragment = homeFragment;
    }

    @NonNull
    @Override
    public ListSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view =inflater.inflate(R.layout.item_play_bai_hat,parent,false);
        ListSongAdapter.ListSongViewHolder viewholderListSong = new ListSongAdapter.ListSongViewHolder(view);



        return  viewholderListSong;

    }

    @Override
    public void onBindViewHolder(@NonNull ListSongViewHolder holder, int position) {
            Song song = songArrayList.get(position);

            holder.textviewplaynhactenbaihat.setText(song.getName());
            holder.textviewplaynhactencasi.setText(song.getSinger());
            holder.tvTypesong.setText(song.getType());
            holder.nhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, playmusic.class);
                    i.putExtra("link", song.getLink());
                    i.putExtra("id",song.getID());
                    i.putExtra("song",song);
                    i.putExtra("album",(ArrayList<Song>)songArrayList);
                    context.startActivity(i);
                }
            });

        switch(position) {
            case 0:
                holder.imghinh.setImageResource(R.drawable.top1);
                break;
            case 1:
                holder.imghinh.setImageResource(R.drawable.top2);
                break;
            case 2:
                holder.imghinh.setImageResource(R.drawable.top3);
                break;
            case 3:
                holder.imghinh.setImageResource(R.drawable.top4);
                break;
            default:
                holder.imghinh.setImageResource(R.drawable.top5);
                break;
        }
        Picasso.get().load(song.getImage()).into(holder.imghinh2);




//           Uri uri = Uri.parse("https://photo-resize-zmp3.zadn.vn/w240_r1x1_jpeg/cover/a/8/d/b/a8dbfe7c0a3d5c4cbf7fb58d7db97ff6.jpg");
         //  holder.imghinh.setImageResource(as);


    }

    @Override
    public int getItemCount() {
        return songArrayList.size();
    }

    public class ListSongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imghinh,imghinh2;
        RelativeLayout nhan;
        TextView textviewplaynhactenbaihat,textviewplaynhactencasi,tvTypesong;
        public ListSongViewHolder(@NonNull View itemView) {
            super(itemView);
            imghinh = itemView.findViewById(R.id.imghinh);
            textviewplaynhactencasi=itemView.findViewById(R.id.textviewplaynhactencasi);
            textviewplaynhactenbaihat=itemView.findViewById(R.id.textviewplaynhactenbaihat);
            tvTypesong=itemView.findViewById(R.id.tvTypesong);
            imghinh2 = itemView.findViewById(R.id.imgHinh2);
            nhan = itemView.findViewById(R.id.nhan);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
