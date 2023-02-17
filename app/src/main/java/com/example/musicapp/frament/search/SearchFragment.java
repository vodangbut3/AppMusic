package com.example.musicapp.frament.search;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.R;
import com.example.musicapp.model.Song;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    RecyclerView rcv;
    Context ctx;
    SongsAdapter adapter;
    ImageButton btn;
    Button btn2;
    FirebaseFirestore databaseReference;
    List<Song> albumList;

    private SearchView searchView;
    ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        albumList =new ArrayList<Song>();
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        View view1=inflater.inflate(R.layout.item_search,container,false);
        ctx=inflater.getContext();
        setHasOptionsMenu(true);
        rcv=view.findViewById(R.id.lvSong);

        databaseReference= FirebaseFirestore.getInstance();
        progressDialog=new ProgressDialog(ctx);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("chdfklsaj");
        progressDialog.show();

//        databaseReference = FirebaseDatabase.getInstance().getReference("Album");
        databaseReference=FirebaseFirestore.getInstance();
        rcv.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ctx);
        rcv.setLayoutManager(linearLayoutManager);


        adapter=new SongsAdapter(ctx,albumList);
        rcv.setAdapter(adapter);

//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot  dataSnapshot: snapshot.getChildren()){
//                    Album album= dataSnapshot.getValue(Album.class);
//                    albumList.add(album);
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        evetchangeList();
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
    }
    private void evetchangeList() {
        databaseReference.collection("Songs").orderBy("Name", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("dsfds", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                albumList.add(dc.getDocument().toObject(Song.class));
                            }
                            adapter.notifyDataSetChanged();
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }

                    }
                });
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.search_menu,menu);
//        menu.clear();
//        SearchManager searchManager=(SearchManager)this.getContext().getSystemService(Context.SEARCH_SERVICE);
//        searchView= (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
//        searchView.setMaxWidth(Integer.MAX_VALUE);
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                adapter.getFilter().filter(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                adapter.getFilter().filter(newText);
//                return false;
//            }
//        });
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem item=menu.findItem(R.id.action_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);

        SearchView searchView=(SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });


    }
    public void playMusic() {


    }
}
