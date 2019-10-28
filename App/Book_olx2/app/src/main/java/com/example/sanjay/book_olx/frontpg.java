package com.example.sanjay.book_olx;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class frontpg extends AppCompatActivity {
    LinearLayout l1;
    AnimationDrawable ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontpg);
        l1 = (LinearLayout) findViewById(R.id.frontpg);
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(getApplicationContext(), nav_home.class);
                    startActivity(i);
                    finish();
                }
            }, 3000);
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i1 = new Intent(getApplicationContext(), login.class);
                    startActivity(i1);
                    finish();
                }
            }, 3000);
        }
        ad = (AnimationDrawable) l1.getBackground();
        ad.setEnterFadeDuration(750);
        ad.setExitFadeDuration(750);
        ad.start();
    }
}
