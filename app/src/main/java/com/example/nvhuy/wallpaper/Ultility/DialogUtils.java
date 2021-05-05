package com.example.nvhuy.wallpaper.Ultility;

import android.content.Context;

import com.developer.kalert.KAlertDialog;


public class DialogUtils {
    public static void showDialog(Context mContext, String message, int type) {
        KAlertDialog pDialog = new KAlertDialog(mContext, type);
        pDialog.setTitleText(message)
                .show();
    }

}