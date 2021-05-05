package com.example.nvhuy.wallpaper.Activity;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;

import com.example.nvhuy.wallpaper.model.Image;

import java.util.ArrayList;



public class MyApplication extends Application {
    public static final String Channel_id = "channel_service_example";
    public static boolean activityVisible;
    public static ArrayList<Image> randomList,liveList,trendingList,recentList, categoryList;
    public static boolean isActivityVisible() {
        return activityVisible;
    }


    public static void activityResumed() {
        activityVisible = true;

    }
    public static void activityPaused() {
        activityVisible = false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        randomList=new ArrayList<>();
        liveList=new ArrayList<>();
        trendingList=new ArrayList<>();
        recentList=new ArrayList<>();
        categoryList=new ArrayList<>();
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(Channel_id,"Channel service example", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
