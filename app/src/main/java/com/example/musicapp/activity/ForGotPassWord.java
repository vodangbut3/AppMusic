package com.example.musicapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapp.R;
import com.example.musicapp.model.UserInfor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ForGotPassWord extends AppCompatActivity {
    EditText edtFbForGot,edtEmailForGot;
    Button btnFavoVn,btnFavoAnDo,btnFavoHanQuoc,btnFavoTrungQuoc,btnCompleteForGot;
    TextView tvShowFavoForGot;
    String favo;
   UserInfor userInfors ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_got_pass_word);

        edtFbForGot = findViewById(R.id.edtFBForgot);
        edtEmailForGot = findViewById(R.id.edtEmailForgot);

        btnFavoVn = findViewById(R.id.favoVNForgot);
        btnFavoAnDo = findViewById(R.id.favoAnDoForgot);
        btnFavoHanQuoc = findViewById(R.id.favoHQForgot);
        btnFavoTrungQuoc = findViewById(R.id.favoTQForgot);

        btnCompleteForGot = findViewById(R.id.btComplete);

        tvShowFavoForGot = findViewById(R.id.tvshowFavoForgot);

       favo ="";
        ArrayList<String> Favorites = new ArrayList<>();

        String userName = getIntent().getExtras().getString("UserName");



        db.collection("Users")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (userName.equals(document.get("UserName").toString())) {
                            userInfors = new UserInfor(document.getId().toString()
                                    , document.get("UserName").toString()
                                    , (ArrayList<String>) document.get("Favorites")
                                    , document.get("PassWord").toString()
                                    , document.get("Email").toString()
                                    , document.get("FaceBook").toString()
                            , document.get("userPlayList").toString());
                            break;
                        }
                    }}

            }
        });


        btnFavoVn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Favorites.add("Việt Nam");
                favo += "Việt Nam" + " ";
                tvShowFavoForGot.setText(favo);
                btnFavoVn.setEnabled(false);

            }
        });
        btnFavoTrungQuoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Favorites.add("Trung Quốc");
                favo += "Trung Quốc" + " ";
                tvShowFavoForGot.setText(favo);

                btnFavoTrungQuoc.setEnabled(false);
            }
        });
        btnFavoAnDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Favorites.add("Ấn độ");
                favo += "Ấn độ" + " ";
                tvShowFavoForGot.setText(favo);

                btnFavoAnDo.setEnabled(false);
            }
        });
        btnFavoHanQuoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Favorites.add("Hàn Quốc");
                favo += "Hàn Quốc" + " ";
                tvShowFavoForGot.setText(favo);

                btnFavoHanQuoc.setEnabled(false);

            }
        });



        btnCompleteForGot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmailForGot.getText().toString();
                String fb = edtFbForGot.getText().toString();
                if(email.equals("") && fb.equals(""))
                {
                    Toast.makeText(ForGotPassWord.this, "Hãy nhập email và facebook!", Toast.LENGTH_SHORT).show();
                }else if(favo == null)
                {
                    Toast.makeText(ForGotPassWord.this, "Hãy chọn ít nhất 1 sở thích!", Toast.LENGTH_SHORT).show();
                }else
                    {
                        if(userInfors.getEmail().equals(email) == false)
                        {
                            Toast.makeText(ForGotPassWord.this, "Email không trùng khớp !", Toast.LENGTH_SHORT).show();
                        }
                        if(userInfors.getFaceBook().equals(fb) == false)
                        {
                            Toast.makeText(ForGotPassWord.this, "Facebook không trùng khớp !", Toast.LENGTH_SHORT).show();
                        }else if(userInfors.getEmail().equals(email) == true && userInfors.getEmail().equals(email) == true)
                            {
                                openDialogChagePass(email,fb,Favorites,userName,userInfors.getUserPlaylist());
                            }
                    }
            }
        });

    }
    public void openDialogChagePass(String email, String fb,ArrayList<String>Favorites,String UserName,String userPlaylist )
    {


        AlertDialog.Builder builder = new AlertDialog.Builder(ForGotPassWord.this);
        LayoutInflater inflater = ForGotPassWord.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.changepassss, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        EditText passnew = view.findViewById(R.id.edtPWnew);
        Button change = view.findViewById(R.id.btnChangePassForgot);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<>();
                map.put("PassWord", passnew.getText().toString());
                db.collection("Users")
                        .whereEqualTo("UserName", UserName)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("Users")
                                        .document(document.getId())
                                        .update(map)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(ForGotPassWord.this, "Đã thay đổi mật khẩu !", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                                Intent i = new Intent(ForGotPassWord.this,LoginActivity.class);
                                                startActivity(i);
                                            }
                                        });

                            }

                        }
                    }
                });
            }
        });



    }
}