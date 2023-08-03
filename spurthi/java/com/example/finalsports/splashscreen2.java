package com.example.finalsports;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class splashscreen2 extends AppCompatActivity {

    private TextView V;
    private ImageView logo,logoCover;
    public static int SPLASH_TIMEOUT = 3000;
    Thread thread;
    College college;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen2);
        college=new College("Vesp");//Object of class College is created with college name as VESP
        V = findViewById(R.id.v);
               logo = findViewById(R.id.logo);
        logoCover = findViewById(R.id.logoCover);


        Animation animate_v= AnimationUtils.loadAnimation(this,R.anim.v);

        Animation animate_logo= AnimationUtils.loadAnimation(this,R.anim.logo);

        V.startAnimation(animate_v);

        logo.startAnimation(animate_logo);
        logoCover.startAnimation(animate_logo);

        thread=new Thread(new RetrieveTournaments());
        thread.start();
        while(thread.isAlive()){}
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splash= new Intent(splashscreen2.this, HomeActivity.class);
                startActivity(splash);
                finish();

            }
        },SPLASH_TIMEOUT);
    }
}
