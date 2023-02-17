package com.example.musicapp.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.musicapp.R;

import com.example.musicapp.model.UserInfor;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {
    EditText txtUserName, txtPassword, txtEmail;
    TextView tvForgetPass;
    Button btnSignIn;
    private String TAG;
    TextView tvSignUp;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int values;
    String favo = "";
    String ID = "";
    ArrayList<UserInfor> userInfors = new ArrayList<UserInfor>();
    String userNameCk, passWordCk,IDChk;
    UserInfor userIn;
    ArrayList<String> listUser = new ArrayList<>();
    ArrayList<String> listPass = new ArrayList<>();
    GoogleSignInClient googleSignInClient;
    SharedPreferences preferences;
    String userPlayList = "";
    boolean remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        printHashKey(LoginActivity.this);
        boolean trueLogin = getIntent().getExtras().getBoolean("trueLogin");
        if (trueLogin == true) {
           readLogin();
//             preferences = getSharedPreferences("Login", MODE_PRIVATE);
//             IDChk=preferences.getString("Id",null);
////            remember = preferences.getBoolean("remember", false);
////            if (remember == true) {
////                userNameCk = preferences.getString("UserName", null);
////                passWordCk = preferences.getString("Pass", null);
////
////                Login(userNameCk, passWordCk);
//
////            }
//            db.collection("Users")
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//
//                                    if(IDChk.equals(document.getId()))
//                                    {
//                                        userNameCk = document.get("UserName").toString();
//                                        passWordCk = document.get("PassWord").toString();
//                                        Login(userNameCk,passWordCk);
//
//                                        break;
//                                    }
//                                }
//                            }
//                        }
//
//                    });

        } else {
            checkLogin(null,null, null, false);
            remember = false;
        }
        ;

        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listUser.add(new String(document.get("UserName").toString()));
                                listPass.add(new String(document.get("PassWord").toString()));

                            }
                        }
                    }
                });

        txtUserName = findViewById(R.id.edUserName);
        txtPassword = findViewById(R.id.edPassword);


        tvSignUp = findViewById(R.id.tvSignUp);

        btnSignIn = findViewById(R.id.btnSignIn);
        tvForgetPass = findViewById(R.id.tvForgetpass);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInButton = findViewById(R.id.btnLogingoogle);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = googleSignInClient.getSignInIntent();
                startActivityForResult(i, 333);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userNameCk = txtUserName.getText().toString();
                passWordCk = txtPassword.getText().toString();

                Login(userNameCk, passWordCk);

            }
        });
        tvForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //layuser


                int ckUser = checkUserisAvai(txtUserName.getText().toString());
                if (ckUser == 1) {
                    //openDialogChangePass(txtUserName.getText().toString());
                    Intent i = new Intent(LoginActivity.this,ForGotPassWord.class);
                    i.putExtra("UserName",txtUserName.getText().toString());
                    startActivity(i);
                } else {
                    Toast.makeText(LoginActivity.this, "User khong ton tai", Toast.LENGTH_SHORT).show();
                }

            }
        });
//        db.collection("Album")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful())
//                        {
//                            for(QueryDocumentSnapshot document : task.getResult())
//                            {
//                                Toast.makeText(LoginActivity.this, "Id:  "+ document.getId(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogDangKy();
            }
        });

    }

    public int checkUserisAvai(String userName) {

        int checkTF = 0;
        for (int i = 0; i < listUser.size(); i++) {
            if (userName.equals(listUser.get(i)) == true) {
                checkTF = 1;
                break;
            } else {
                checkTF = 0;
            }
        }
        return checkTF;
    }

    public String getPass(String userName) {
        String pass = null;
        for (int i = 0; i <= listUser.size(); i++) {
            if (userName.equals(listUser.get(i))) {
                pass = listPass.get(i);
                break;
            }
        }

        return pass;
    }

    public void openDialogChangePass(String userName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.changepass, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        EditText edtMkOld = view.findViewById(R.id.edtMkOld);

        EditText edtMKNew = view.findViewById(R.id.edtMkNew);
        EditText edtMkNew1 = view.findViewById(R.id.edtMkNew1);

        Button btSua = view.findViewById(R.id.btnThayDoi);
        Button btHuy = view.findViewById(R.id.btnHuyThayDoi);


        btSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String passCk = getPass(userName);
                if (edtMkOld.getText().toString().equalsIgnoreCase(passCk) == false) {
                    Toast.makeText(LoginActivity.this, "Mat khau sai", Toast.LENGTH_SHORT).show();
                } else if (edtMKNew.getText().toString().equalsIgnoreCase(edtMkNew1.getText().toString()) == false) {
                    Toast.makeText(LoginActivity.this, "Mat khau khong trung nhau", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("PassWord", edtMkNew1.getText().toString());
                    db.collection("Users")
                            .whereEqualTo("UserName", userName)
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
                                                    Toast.makeText(LoginActivity.this, "Đã thay đổi mật khẩu !", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }

                            }
                        }
                    });


                    Intent i = new Intent(LoginActivity.this, LoginActivity.class);
                    startActivity(i);
                }


            }
        });
        btHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }


    private void checkLogin(String IDlo, String userCh, String passCk, boolean remember) {
         preferences = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("remember", remember);
        editor.putString("UserName", userCh);
        editor.putString("Pass", passCk);
        editor.putString("Id",IDlo);

        editor.commit();
    }

    private void readLogin() {
        SharedPreferences preferences = getSharedPreferences("Login", MODE_PRIVATE);
        remember = preferences.getBoolean("remember", false);
        if (remember == true) {
            userNameCk = preferences.getString("UserName", null);
            passWordCk = preferences.getString("Pass", null);

            Login(userNameCk, passWordCk);

        }
    }

    public void Login(String userName, String passWord) {
        if (userName.equals("") && passWord.equals("")) {
            Toast.makeText(LoginActivity.this, "Không được bỏ trống trường nào!", Toast.LENGTH_SHORT).show();

        } else // Kiểm tra dữ liệu trong firebase
        {
            db.collection("Users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                //check thông tin login
                                int error = 0;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (userName.equals(document.get("UserName")) == true
                                            && passWord.equals(document.get("PassWord")) == true) {

                                       // Toast.makeText(LoginActivity.this, "Dang nhap thanh cong", Toast.LENGTH_SHORT).show();
                                        userIn = new UserInfor(document.getId().toString()
                                                , document.get("UserName").toString()
                                                , (ArrayList<String>) document.get("Favorites")
                                                , document.get("PassWord").toString()
                                                , document.get("Email").toString()
                                                , document.get("FaceBook").toString()
                                                ,document.get("userPlayList").toString());
//                                        ,document.get("Image").toString());
                                        error = 0;
                                      /*  Toast.makeText(LoginActivity.this, "Id "+ userIn.getID()+"\n"
                                                +"UserName "+userIn.getUsername()+"\n"
                                                +"Favorites " + userIn.getFavorites().get(0)+"\n"
                                                +"PassWord "+userIn.getPassword()+"\n"
                                                +"Email " + userIn.getEmail()+"\n"
                                                +"FaceBook " + userIn.getFaceBook()+"\n"
                                                +"Userplaylist "+userIn.getUserPlaylist(), Toast.LENGTH_LONG).show();*/

                                        checkLogin(userIn.getID(),userIn.getUsername(), userIn.getPassword(), true);

                                        remember = true;

                                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);

                                        i.putExtra("user", (Parcelable) userIn);
                                        startActivity(i);
                                        break;


                                    } else {
                                        error = 1;
                                    }
                                }
                                if (error == 1) {
                                    Toast.makeText(LoginActivity.this, "Vui lòng kiểm tra lại thông tin đăng nhập!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        }
    }

    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 333) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                db.collection("Users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                //check email có tồn tại hay ko
                                int checkAvai = 0;
                                if (task.isSuccessful()) {

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (account.getEmail().equals(document.get("Email")) == true) {
                                            checkLogin(document.getId().toString(),document.get("UserName").toString(), document.get("PassWord").toString(), true);
                                            remember = true;
                                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                            userIn = new UserInfor(document.getId().toString()
                                                    , document.get("UserName").toString()
                                                    , (ArrayList<String>) document.get("Favorites")
                                                    , document.get("PassWord").toString()
                                                    , document.get("Email").toString()
                                                    , document.get("FaceBook").toString()
                                            ,document.get("userPlayList").toString()
                                            ,document.get("Image").toString());

                                            i.putExtra("user", (Parcelable) userIn);
                                            startActivity(i);
                                            checkAvai = 0;
                                            break;
                                        } else {
                                            checkAvai = 1;
                                        }
                                    }
                                    // Nếu check ko có email trùng hợp
                                    if (checkAvai == 1) {
                                        // Tạo user mới
                                        Map<Object, Object> user = new HashMap<>();

                                        user.put("UserName", account.getEmail());
                                        ArrayList<String> Favorites = new ArrayList<>();
                                        Favorites.add("Việt Nam");
                                        Favorites.add("Hàn quốc");
                                        String userPlayList ;
                                        userPlayList = "0A";
                                        user.put("Favorites", Favorites);
                                        user.put("PassWord", account.getEmail());
                                        user.put("Email", account.getEmail());
                                        user.put("FaceBook", "");
                                        user.put("userPlayList", userPlayList);
                                        user.put("Image","https://firebasestorage.googleapis.com/v0/b/humanresourcemanager-57f05.appspot.com/o/Vi%E1%BB%87t%20Nam%2Fkhac%2F92d1087e7b366b4cf7d1539d37e5f610.jpg?alt=media&token=2c4444a6-3052-4b05-94fb-73d4a6395fe8");
                                        Random random = new Random();
                                        values = random.nextInt((99999 - 10000) + 1) + 10000;
                                        ID = "PS" + values;
                                        db.collection("Users")
                                                .document(ID).set(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công !", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                        userIn = new UserInfor(ID
                                                , account.getEmail()
                                                , Favorites
                                                , account.getEmail()
                                                , account.getEmail()
                                                , ""
                                                , userPlayList
                                        ,"https://firebasestorage.googleapis.com/v0/b/humanresourcemanager-57f05.appspot.com/o/Vi%E1%BB%87t%20Nam%2Fkhac%2F92d1087e7b366b4cf7d1539d37e5f610.jpg?alt=media&token=2c4444a6-3052-4b05-94fb-73d4a6395fe8");

                                        i.putExtra("user", (Parcelable) userIn);
                                        startActivity(i);
                                    }
                                }
                            }
                        });
            } catch (Exception e) {

            }
        }
    }

    public void openDialogDangKy() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        LayoutInflater inflater = LoginActivity.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dangky, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        EditText edtUserName = view.findViewById(R.id.edtUserNameNew);
        EditText PassWord = view.findViewById(R.id.edtPassWordNew);
        EditText againPass = view.findViewById(R.id.edtPassAgain);

        EditText Email = view.findViewById(R.id.edtEmail);
        EditText FaceBook = view.findViewById(R.id.edtFaceBook);
        Button btnFavoVn = view.findViewById(R.id.favoVN);
        Button btnFavoTQ = view.findViewById(R.id.favoTQ);
        Button btnFavoAnDo = view.findViewById(R.id.favoAnDo);
        Button btnFavoHanQuoc = view.findViewById(R.id.favoHQ);
        TextView tvShowFavo = view.findViewById(R.id.tvshowFavo);

        ArrayList<String> Favorites = new ArrayList<>();


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


        Random random = new Random();
        values = random.nextInt((99999 - 10000) + 1) + 10000;
        ID = "PS" + values;
        Toast.makeText(LoginActivity.this, "Random: " + ID, Toast.LENGTH_LONG).show();

        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (ID.equals(document.getId())) {
                                    values = random.nextInt((99999 - 10000) + 1) + 10000;
                                    ID = "PS" + values;
                                    break;
                                }
                            }

                        }
                    }
                });



        userPlayList = "50A";


        Button btnCheckSignIn = view.findViewById(R.id.btnCheckSignIn);
        btnCheckSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String patern = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

                String UserName = edtUserName.getText().toString();
                Map<Object, Object> user = new HashMap<>();

                user.put("UserName", UserName);

                user.put("Favorites", Favorites);
                user.put("PassWord", PassWord.getText().toString());
                user.put("Email", Email.getText().toString());
                user.put("FaceBook", FaceBook.getText().toString());

                user.put("userPlayList", userPlayList);

                for (int i = 0; i < listUser.size(); i++) {
                    if (UserName.equals(listUser.get(i)) == true) {
                        Toast.makeText(LoginActivity.this, "Đã có tên đăng nhập này", Toast.LENGTH_SHORT).show();
                    }
                }
                if (UserName.equals("") == true || PassWord.getText().toString().equals("") == true ||
                        againPass.getText().toString().equals("") == true) {
                    Toast.makeText(LoginActivity.this, "Không được bỏ trống trường này", Toast.LENGTH_SHORT).show();
                } else if (PassWord.getText().toString().equals(againPass.getText().toString()) == false) {
                    Toast.makeText(LoginActivity.this, "2 mật khẩu không trùng nhau", Toast.LENGTH_SHORT).show();
                } else if (Email.getText().toString().matches(patern) == false) {
                    Toast.makeText(LoginActivity.this, "Bạn đã không nhập dúng email", Toast.LENGTH_SHORT).show();
                } else {
//                        UserInfor userInfor = new UserInfor(ID,UserName,Favorites,PassWord.getText().toString(),
//                                Email.getText().toString(),FaceBook.getText().toString(),userPlayList);
                    db.collection("Users")
                            .document(ID).set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(LoginActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                                }
                            });
//                        db.collection("Users")
//                                .document(ID).set(userInfor)
//
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//                                        Toast.makeText(LoginActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
//                                    }
//                                });

                }
            }
        });
    }
}