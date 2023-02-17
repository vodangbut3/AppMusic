package com.example.musicapp.frament.search;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.R;
import com.example.musicapp.activity.playmusic;
import com.example.musicapp.model.Song;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.AlbumViewholder> implements Filterable {
    Context context;
    private List<Song> albumList;
    private List<Song> albumListold;
    ArrayList<Song> songs;
    MediaPlayer mediaPlayer;

    public SongsAdapter(Context context, List<Song> albumList) {
        this.context=context;
        this.albumList = albumList;
        this.albumListold=albumList;

    }

    @NonNull
    @Override
    public AlbumViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search,parent,false);
        return new AlbumViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewholder holder, int position) {

        Song album=albumList.get(position);
        holder.tvSong.setText(album.getName());
        holder.tvSinger.setText(album.getSinger());
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent i = new Intent(context, playmusic.class);
                    i.putExtra("link", album.getLink());
                    i.putExtra("id",album.getID());
                    i.putExtra("song",(Parcelable) album);
                    i.putExtra("album",(ArrayList<Song>)albumList);
                    i.putExtra("tenbaihat",album.getName());
                    context.startActivity(i);

            }
        });
        Picasso.get().load(album.getImage()).into(holder.img);
//        holder.img.setImageResource(album.getImage());

    }

    @Override
    public int getItemCount() {
            return albumList.size();
    }

    public class AlbumViewholder extends RecyclerView.ViewHolder{
        private TextView tvSong;
        private TextView tvSinger;
        private ImageView img;
        private ImageButton btn;


        public AlbumViewholder(@NonNull View itemView) {
            super(itemView);
            btn=itemView.findViewById(R.id.playButton);
            tvSong=itemView.findViewById(R.id.textviewplaynhactenbaihat);
            tvSinger=itemView.findViewById(R.id.textviewplaynhactencasi);
            img=itemView.findViewById(R.id.imageView);

        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch =constraint.toString();
                if (strSearch.isEmpty()){
                    albumList=albumListold;
                }else {
                    List<Song> list=new ArrayList<>();
                    for (Song album : albumListold){
                        if (album.getName().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(album);
                        }
                    }
                        albumList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values= albumList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                albumList= (List<Song>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
