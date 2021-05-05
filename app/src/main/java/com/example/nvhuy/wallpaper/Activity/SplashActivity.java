package com.example.nvhuy.wallpaper.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.nvhuy.wallpaper.R;
import com.example.nvhuy.wallpaper.model.Image;

import java.util.ArrayList;

public class SplashActivity extends BaseActivity {
    public  static ArrayList<Image> mImageList1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        if (isNetworkConnected()){
            progressBar.setVisibility(View.INVISIBLE);
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
            finish();
        }

    }
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}