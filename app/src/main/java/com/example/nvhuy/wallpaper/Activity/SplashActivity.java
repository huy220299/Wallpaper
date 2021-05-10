package com.example.nvhuy.wallpaper.Activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.nvhuy.wallpaper.HamsiWallpaperSlideshow;
import com.example.nvhuy.wallpaper.R;

import java.io.File;


public class SplashActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //start
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFirstRun) {
                    //show start activity
                    startActivity(new Intent(SplashActivity.this, StartedActivity.class));
                    finish();
                } else if (!checkPermission()) {
                    startActivity(new Intent(SplashActivity.this, ActivityPermission.class));
                    finish();
                }else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
                getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                        .putBoolean("isFirstRun", false).apply();
            }
        }, 5000);

    }
}