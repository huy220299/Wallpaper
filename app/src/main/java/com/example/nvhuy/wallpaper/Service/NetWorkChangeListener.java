package com.example.nvhuy.wallpaper.Service;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.nvhuy.wallpaper.R;
import com.example.nvhuy.wallpaper.Ultility.Common;

public class NetWorkChangeListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Common.isConnected(context)){
            AlertDialog.Builder builder= new AlertDialog.Builder(context);
            View layout_dialog = LayoutInflater.from(context).inflate(R.layout.check_connection_dialog,null);
            builder.setView(layout_dialog);
            Button retry = layout_dialog.findViewById(R.id.retry);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.setCancelable(false);
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    onReceive(context,intent);
                }
            });
        }
    }
}
