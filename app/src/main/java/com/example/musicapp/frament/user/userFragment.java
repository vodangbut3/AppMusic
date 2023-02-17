package com.example.musicapp.frament.user;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.R;
import com.example.musicapp.Services.CallBack.SongCallBack;
import com.example.musicapp.activity.HomeActivity;
import com.example.musicapp.activity.LoginActivity;
import com.example.musicapp.activity.UserPlayList;
import com.example.musicapp.dao.SongsDAO;
import com.example.musicapp.frament.home.adapter.AlbumAdapter;
import com.example.musicapp.frament.home.adapter.HomeFragment;
import com.example.musicapp.frament.home.adapter.ListSongAdapter;
import com.example.musicapp.model.Album;
import com.example.musicapp.model.Song;
import com.example.musicapp.model.UserInfor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class userFragment extends Fragment {

    TextView tvChangeInfo, tvSeeUserPlay, tvLogout, tvName,tvBlackMode;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    UserInfor userInfor;
    String favo = "";
    ImageView imgI;
    ArrayList<String> Favorites;
    UserInfor userInfor1;
    ArrayList<String> listUser = new ArrayList<>();
    SharedPreferences sharedPreferences;
SongsDAO songsDAO;
    ArrayList<Song> songArrayList1 = new ArrayList<>();
    ArrayList<Song> songArrayList = new ArrayList<>();
    ArrayList<Song> songUserList = new ArrayList<>();
    ArrayList<String> userPL = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.userfragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tvName = view.findViewById(R.id.tvName);
        tvChangeInfo = view.findViewById(R.id.tvChangeProfile);
        tvLogout = view.findViewById(R.id.tvLogOut);
        songsDAO = new SongsDAO(getContext());
        tvSeeUserPlay = view.findViewById(R.id.tvSeeUserPlay);
        tvBlackMode = view.findViewById(R.id.tvBlackMode);
        imgI = view.findViewById(R.id.imgImageUser);


        userInfor = ((HomeActivity) getContext()).returnUser();
        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(userInfor.getID())) {
                                    userInfor1 = new UserInfor(document.getId().toString(),
                                            document.get("UserName").toString(),
                                            (ArrayList<String>) document.get("Favorites")
                                            , document.get("PassWord").toString()
                                            , document.get("Email").toString()
                                            , document.get("FaceBook").toString()
                                    ,document.get("userPlayList").toString()
                                    ,document.get("Image").toString());
                               //     Picasso.get().load(document.get("Image").toString()).into(imgI);
                                    Favorites = new ArrayList<>();
                                    Favorites = userInfor1.getFavorites();
                                    songArrayList = songsDAO.getSongList(document.getId().toString());
                                    break;
                                }
                            }
                        }
                    }
                });




        tvBlackMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        }
                                       });
        tvSeeUserPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), UserPlayList.class);
                i.putParcelableArrayListExtra("arrayList",songArrayList);

                startActivity(i);
            }
        });


      db.collection("Songs")
              .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
          @Override
          public void onComplete(@NonNull Task<QuerySnapshot> task) {
              if (task.isSuccessful()) {
                  for (QueryDocumentSnapshot document : task.getResult()) {
                      if (document.getId().equals(userInfor.getID())) {

                      }}}
          }
      });

        tvName.setText(userInfor.getUsername());

        tvChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogChangeProfile(userInfor);
            }
        });
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), LoginActivity.class);

                i.putExtra("remember", false);
                startActivity(i);
                ((HomeActivity) getContext()).finish();
            }
        });
    }



    public ArrayList<Song> returnSong()
    {
        ArrayList<Song> songList = new ArrayList<>();
        db.collection("Songs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                songList.add(new Song(document.get("ID").toString(),
                                        document.get("Image").toString(),
                                        document.get("Link").toString(),
                                        document.get("Name").toString(),
                                        document.get("Singer").toString(),
                                        document.get("Type").toString(),
                                        (ArrayList<String>) document.get("userPlayList")));


                            }
                        }
                    }
                });
        Toast.makeText(getContext(), "size"+ songList.size(), Toast.LENGTH_SHORT).show();
        return songList;
    }
    public void openDialogChangeProfile(UserInfor userInfor) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.changefrofile, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        EditText edtUserName = view.findViewById(R.id.edtUserNameNew);
        EditText PassWord = view.findViewById(R.id.edtPassWordNew);


        EditText Email = view.findViewById(R.id.edtEmail);
        EditText FaceBook = view.findViewById(R.id.edtFaceBook);
        Button btnFavoVn = view.findViewById(R.id.favoVN);
        Button btnFavoTQ = view.findViewById(R.id.favoTQ);
        Button btnFavoAnDo = view.findViewById(R.id.favoAnDo);
        Button btnFavoHanQuoc = view.findViewById(R.id.favoHQ);
        TextView tvShowFavo = view.findViewById(R.id.tvshowFavo);


        edtUserName.setText(userInfor1.getUsername());
        PassWord.setText(userInfor1.getPassword());

        Email.setText(userInfor1.getEmail());
        FaceBook.setText(userInfor1.getFaceBook());
        for (int i = 0; i < Favorites.size(); i++) {
            favo += Favorites.get(i) + " ";

            if (Favorites.get(i).equals("Việt Nam") == true) {
                btnFavoVn.setEnabled(false);
            }
            if (Favorites.get(i).equals("Ấn độ") == true) {
                btnFavoAnDo.setEnabled(false);
            }
            if (Favorites.get(i).equals("Trung Quốc") == true) {
                btnFavoTQ.setEnabled(false);
            }
            if (Favorites.get(i).equals("Hàn Quốc") == true) {
                btnFavoHanQuoc.setEnabled(false);
            }

        }
        tvShowFavo.setText(favo);
        btnFavoVn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Favorites.add("Việt Nam");
                favo += "Việt Nam" + " ";
                tvShowFavo.setText(favo);
                btnFavoVn.setEnabled(false);

            }
        });
        btnFavoTQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Favorites.add("Trung Quốc");
                favo += "Trung Quốc" + " ";
                tvShowFavo.setText(favo);

                btnFavoTQ.setEnabled(false);
            }
        });
        btnFavoAnDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Favorites.add("Ấn độ");
                favo += "Ấn độ" + " ";
                tvShowFavo.setText(favo);

                btnFavoAnDo.setEnabled(false);
            }
        });
        btnFavoHanQuoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Favorites.add("Hàn Quốc");
                favo += "Hàn Quốc" + " ";
                tvShowFavo.setText(favo);

                btnFavoHanQuoc.setEnabled(false);

            }
        });
        Button btnCheckSignIn = view.findViewById(R.id.btnCheckSignIn);
        btnCheckSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String patern = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

                String UserName = edtUserName.getText().toString();
                Map<String, Object> user = new HashMap<>();

                user.put("UserName", UserName);

                user.put("Favorites", Favorites);
                user.put("PassWord", PassWord.getText().toString());
                user.put("Email", Email.getText().toString());
                user.put("FaceBook", FaceBook.getText().toString());

                tvName.setText(UserName);


                for (int i = 0; i < listUser.size(); i++) {
//                    if(UserName.equals(listUser.get(i))==true)
//                    {
//                        Toast.makeText(getContext(), "Đã có tên đăng nhập này", Toast.LENGTH_SHORT).show();
                }
                if (UserName.equals("") == true || PassWord.getText().toString().equals("") == true
                ) {
                    Toast.makeText(getContext(), "Không được bỏ trống trường này", Toast.LENGTH_SHORT).show();
                } else if (Email.getText().toString().matches(patern) == false) {
                    Toast.makeText(getContext(), "Bạn đã không nhập dúng email", Toast.LENGTH_SHORT).show();
                } else {
//                        UserInfor userInfor = new UserInfor(ID,UserName,Favorites,PassWord.getText().toString(),
//                                Email.getText().toString(),FaceBook.getText().toString(),userPlayList);
                    db.collection("Users")
                            .whereEqualTo("UserName", userInfor1.getUsername())
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    db.collection("Users")
                                            .document(document.getId())
                                            .update(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(getContext(), "Đã thay đổi thông tin thành công!", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                }

                                            });
                                }
                            }
                        }
                    });
                }
            }

        });


    }
}
