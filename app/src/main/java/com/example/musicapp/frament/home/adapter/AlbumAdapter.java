package com.example.musicapp.frament.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.R;
import com.example.musicapp.activity.UserPlayList;
import com.example.musicapp.interfaces.ItemClickListener;
import com.example.musicapp.model.Album;
import com.example.musicapp.model.Song;
import com.example.musicapp.model.UserInfor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Random;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    Context context;
    ArrayList<Album> mangalbum;
    HomeFragment homeFragment;
    ArrayList<Song> songArrayList = new ArrayList<>();
    public AlbumAdapter(Context context, ArrayList<Album> mangalbum, HomeFragment homeFragment,ArrayList<Song> song) {
        this.context = context;
        this.mangalbum = mangalbum;
        this.homeFragment = homeFragment;
        this.songArrayList = song;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Album album = mangalbum.get(position);
        holder.textviewtenalbum.setText(album.getName());
        holder.textviewtencasialbum.setText(album.getSinger());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                UserInfor userInfor = UserInfor.getInstance();
                //Xác định danh sách gửi tới không là từ playlist người dùng
//                userInfor.setisPlayList(false);
//                userInfor.setCurrentAlbum(mangalbum.get(position).getSong());
//                userInfor.setisPlayList(false);
//                userInfor.setisFavorites(false);
//                //ChangeFragment(mangalbum.get(position),new );
            }
        });
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context, UserPlayList.class);
                ArrayList<Song> song2 = new ArrayList<>();
                for (int i= 0;i<songArrayList.size();i++)
                {
                    if(album.getSinger().equals(songArrayList.get(i).getSinger())==true)
                    {
                        song2.add(songArrayList.get(i));
                    }
                }
                intent.putParcelableArrayListExtra("arrayList",song2);
                context.startActivity(intent);



            }
        });

    }

    @Override
    public int getItemCount() {
        return mangalbum.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageviewalbum;
        TextView textviewtenalbum, textviewtencasialbum;
        ItemClickListener itemClickListener;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageviewalbum = itemView.findViewById(R.id.imageviewalbum);
            textviewtenalbum = itemView.findViewById(R.id.textviewtenalbum);
            textviewtencasialbum = itemView.findViewById(R.id.textviewtencasialbum);
            relativeLayout = itemView.findViewById(R.id.albumitem);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }
    }

    private void ChangeFragment(Album album, Fragment fragment){
        Bundle bundle = new Bundle();
        bundle.putParcelable("Album", album);
        bundle.putInt("fragment",1);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = homeFragment.getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.commit();
    }
}
