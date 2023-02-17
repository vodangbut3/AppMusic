package com.example.musicapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import android.os.Bundle;
import android.widget.Toast;


import com.example.musicapp.R;
import com.example.musicapp.model.Song;
import com.example.musicapp.model.UserInfor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private SlidingUpPanelLayout slidingPaneLayout;
    private BottomNavigationView bottomNavigationView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Song> songList= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        slidingPaneLayout = (SlidingUpPanelLayout) findViewById(R.id.slidingUpPanel);
        bottomNavigationView=findViewById(R.id.nav_view);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        slidingPaneLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);


    }
    public UserInfor returnUser()
    {

        UserInfor userInfor = getIntent().getParcelableExtra("user");

        return userInfor;
    }
    public ArrayList<Song> returnSong(ArrayList<Song> songList)
    {
        ArrayList<Song> songA = new ArrayList<>();
        db.collection("Songs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                songA.add(new Song(document.get("ID").toString(),
                                        document.get("Image").toString(),
                                        document.get("Link").toString(),
                                        document.get("Name").toString(),
                                        document.get("Singer").toString(),
                                        document.get("Type").toString(),
                                        (ArrayList<String>) document.get("userPlayList")
                                ));


                            }
                        }
                    }
                });
        songList = songA;

        return songList;
    }
}
