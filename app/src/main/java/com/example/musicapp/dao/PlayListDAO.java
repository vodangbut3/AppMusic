package com.example.musicapp.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.musicapp.Services.CallBack.PlayListCallBack;

import com.example.musicapp.model.PlayList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;
import java.util.jar.Attributes;

public class PlayListDAO {
    Context context;
    PlayList playList = new PlayList();
    ArrayList<PlayList> danhsachPlaylist = new ArrayList<>();

    public PlayListDAO(Context context) {
        this.context = context;
    }

    public void getPlayList(String UserID, final PlayListCallBack playListCallBack) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Users").document(UserID);
        documentReference.collection("MyPlaylist").whereEqualTo("deleted", false).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                Log.d("MyPlaylist", snapshot.getId() + "=>" + snapshot.getData());
                                playList = snapshot.toObject(PlayList.class);
                                danhsachPlaylist.add(playList);
                            }
                            playListCallBack.getCallback(danhsachPlaylist);
                            Log.d("danhsachPlaylist", danhsachPlaylist.toString());
                        } else {
                            Log.w("MyPlaylist", "Error Writing document.", task.getException());
                            ToastAnnotation("Có lỗi xảy ra");
                        }
                    }
                });
    }

    public void createPlaylist(final String UserID, String Name, final PlayListCallBack playListCallBack) {
        randomString();
        Log.d("random", randomString());
        String ID = "PL" + UserID + randomString();
        playList = new PlayList(null, Name, ID, false);
        //      danhsachPlaylist.add(playList) ;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Users").document(UserID);
        documentReference.collection("MyPlaylist").document(ID).set(playList).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("MyPlaylist", "DocumentSnapshot successfully updated!");
                        ToastAnnotation("Thêm playlist thành công");
                        danhsachPlaylist.clear();
                        playListCallBack.getCallback(danhsachPlaylist);
                        getPlayList(UserID, playListCallBack);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("MyPlaylist", "Error Writing documet", e);
            }
        });
    }

    public static String randomString() {
        String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }
        String result = String.valueOf(DATA.charAt(RANDOM.nextInt(DATA.length())));
        return result;

    }

    private void ToastAnnotation(String mesage) {
        Toast.makeText(context, mesage, Toast.LENGTH_SHORT).show();
    }

}
