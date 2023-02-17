package com.example.musicapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.musicapp.R;

import java.lang.annotation.Annotation;

public class ChaoActivity extends AppCompatActivity {
    ImageView image;
    Animation fadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chao);

        image = findViewById(R.id.image);

        fadeIn= AnimationUtils.loadAnimation(this,R.anim.fadein);
        image.startAnimation(fadeIn);

        Thread bamgio=new Thread(){
            public void run()
            {
                try {
                    sleep(2000);
                } catch (Exception e) {

                }
                finally
                {
                    Intent i=new Intent(getBaseContext(),LoginActivity.class);
                    i.putExtra("trueLogin",true);
                    startActivity(i);
                }
            }
        };
        bamgio.start();
    }
    //sau khi chuyển sang màn hình chính, kết thúc màn hình chào
    protected void onPause(){
        super.onPause();
        finish();
    }
}