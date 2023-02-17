package com.example.musicapp.frament.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.R;
import com.example.musicapp.interfaces.ItemClickListener;
import com.example.musicapp.model.Song;

import java.util.ArrayList;

public class SuggestAdapter extends RecyclerView.Adapter<SuggestAdapter.ViewHolder> {
    Context context;
    ArrayList<Song> songArrayList;
    HomeFragment homeFragment;

    public SuggestAdapter(Context context, ArrayList<Song> songArrayList, HomeFragment homeFragment) {
        this.context = context;
        this.songArrayList = songArrayList;
        this.homeFragment = homeFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_song_info,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        LinearLayout like_layout;
        TextView tvindex,tvsongname,tvsongsinger;
        ImageView imgrank,imgsong;
        ItemClickListener itemClickListener;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvsongname=itemView.findViewById(R.id.tvsongname);
            tvsongsinger=itemView.findViewById(R.id.tvsongsinger);
            imgrank=itemView.findViewById(R.id.imgrank);
            imgsong=itemView.findViewById(R.id.imgsong);
            like_layout = itemView.findViewById(R.id.like_layout);
            like_layout.setVisibility(View.GONE);

            // ẩn dao diện
            imgrank.setVisibility(View.GONE);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);

        }
    }
    private void ChangeFragment(int position, View view){
        //chưa viết
    }

}
