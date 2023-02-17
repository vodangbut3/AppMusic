package com.example.musicapp.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.musicapp.Services.CallBack.AlbumCallBack;
import com.example.musicapp.model.Album;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AlbumDAO {
    Context context;

    public AlbumDAO(Context context) {
        this.context = context;
    }

    Album album = new Album();
    ArrayList<Album> danhsachalbum = new ArrayList<>();

    public void getAlbum(final AlbumCallBack albumCallBack) {
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Album").limit(4).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                Log.d("albumtest", snapshot.getId() + "=>" + snapshot.getData());
                                //Hello
                                album = snapshot.toObject(Album.class);
                                danhsachalbum.add(album);
                                albumCallBack.getCallback(danhsachalbum);
                            }
                        } else {
                            Log.w("AAA", "error geting document", task.getException());
                            ToastAnnotation("Có Lỗi Xảy Ra");
                        }
                    }
                });

    }

    private void ToastAnnotation(String mesage) {
        Toast.makeText(context, mesage, Toast.LENGTH_SHORT).show();
        Toast.makeText(context, mesage, Toast.LENGTH_SHORT).show();


    }
}
