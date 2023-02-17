package com.example.musicapp.frament.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.RankViewHolder> {
    Context context;
    ArrayList<Song> songArrayList;
    HomeFragment homeFragment;
//    ImageView imgsong,imgrank;

    public RankAdapter(Context context, ArrayList<Song> songArrayList, HomeFragment homeFragment) {
        this.context = context;
        this.songArrayList = songArrayList;
        this.homeFragment = homeFragment;
    }

    @NonNull
    @Override
    public RankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view =inflater.inflate(R.layout.item_song_info,parent,false);
        RankAdapter.RankViewHolder viewholderRank = new RankAdapter.RankViewHolder(view);



        return  viewholderRank;
    }

    @Override
    public void onBindViewHolder(@NonNull RankViewHolder holder, int position) {
        Song song = songArrayList.get(position);
        holder.tvsongname.setText(song.getName());
        holder.tvsongsinger.setText(song.getSinger());
       // holder.tvLike.setText(song.getLike()+"");
//        holder.chuyen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(context, playmusic.class);
//                i.putExtra("link", song.getLink());
//                i.putExtra("id",song.getID());
//                i.putExtra("song",song);
//                i.putExtra("album",(ArrayList<Song>)songArrayList);
//                context.startActivity(i);
//            }
//        });


        switch (position){
            case 0:
                holder.imgrank.setImageResource(R.drawable.top1);
                break;
            case 1:
                holder.imgrank.setImageResource(R.drawable.top2);
                break;
            case 2:
                holder.imgrank.setImageResource(R.drawable.top3);
                break;
            case 3:
                holder.imgrank.setImageResource(R.drawable.top4);
                break;
            default:
                holder.imgrank.setImageResource(R.drawable.top5);
                break;
        }
        Picasso.get().load(song.getImage()).into(holder.imgsong);
    }

    @Override
    public int getItemCount() {
        return songArrayList.size();
    }

    public class RankViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imgrank,imgsong;
        TextView tvsongname,tvsongsinger,tvLike;
//        RelativeLayout chuyen;
        public RankViewHolder(@NonNull View itemView) {
            super(itemView);
            imgrank=itemView.findViewById(R.id.imgrank);
            imgsong=itemView.findViewById(R.id.imgsong);
            tvsongname=itemView.findViewById(R.id.tvsongname);
            tvsongsinger=itemView.findViewById(R.id.tvsongsinger);
            tvLike=itemView.findViewById(R.id.tvLike);
//            chuyen=itemView.findViewById(R.id.chuyen);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
