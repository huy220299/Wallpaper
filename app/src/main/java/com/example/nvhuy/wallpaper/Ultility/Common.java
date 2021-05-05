package com.example.nvhuy.wallpaper.Ultility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Environment;

import java.io.File;

public class Common {
    public static boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        if (connectivityManager!=null){
            NetworkInfo[] info =    connectivityManager.getAllNetworkInfo();
            if (info!=null){
                for (int i=0; i<info.length;i++){
                    if (info[i].getState()== NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean checkDownload(String image_name,Context context) {
        String path = Environment.getExternalStorageDirectory().toString() +
                File.separator + "Android"
                + File.separator + "data"
                + File.separator + context.getPackageName()
                + File.separator + "files"
                + File.separator + "downloaded";
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().contains(image_name)) {
                    return true;
                }
            }
        }
        return false;
    }



}
