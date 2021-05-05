package com.example.nvhuy.wallpaper.Activity;


import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.kalert.KAlertDialog;
import com.example.nvhuy.wallpaper.HamsiWallpaperSlideshow;
import com.example.nvhuy.wallpaper.R;


import com.example.nvhuy.wallpaper.Ultility.ZoomRecyclerGridLayout;
import com.example.nvhuy.wallpaper.adapter.LocalImageAdapter;

import java.io.File;
import java.util.ArrayList;


public class AutoChangerActivity extends BaseActivity implements LocalImageAdapter.ItemLongClick {
    RecyclerView recyclerView;
    LocalImageAdapter adapter;
    ArrayList<File> listFile;
    Button btn_setWallpaper, btn_settings;
    ImageView btn_back;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_changer);
        recyclerView = findViewById(R.id.recyclerView);
        btn_settings = findViewById(R.id.btn_settings);
        btn_setWallpaper = findViewById(R.id.btn_setWallpaper);
        btn_back = findViewById(R.id.imageBack);
        btn_back.setOnClickListener(v -> onBackPressed());

        listFile = new ArrayList<>();
        getFile();
        if (listFile.size()==0){
            new KAlertDialog(AutoChangerActivity.this, KAlertDialog.WARNING_TYPE)
                    .setTitleText("No image downloaded!")
                    .setContentText("Please download more 2 images ")
                    .setConfirmText("OK")
                    .show();
        }
        adapter = new LocalImageAdapter(this, listFile, this);
        recyclerView.setAdapter(adapter);
//        ZoomRecyclerLayout linearLayoutManager  =new  ZoomRecyclerLayout(this);
//        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);

//        StaggeredGridLayoutManager mLayoutManager =
//                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        ZoomRecyclerGridLayout linearLayoutManager = new ZoomRecyclerGridLayout(this,1,LinearLayoutManager.HORIZONTAL,false);


        recyclerView.setLayoutManager(linearLayoutManager);

        btn_setWallpaper.setOnClickListener(v -> {
            if (listFile.size()<2){
                Toast.makeText(this, "Please pick more 2 pics", Toast.LENGTH_SHORT).show();
            }else {

                Intent i = new Intent();
                if (Build.VERSION.SDK_INT > 15) {
                    i.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                    String p = HamsiWallpaperSlideshow.class.getPackage().getName();
//                    String p = HamsiWallpaperSlideshow.class.getPackage().getName();
                    String c = HamsiWallpaperSlideshow.class.getCanonicalName();
                    i.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(p, c));
                } else {
                    i.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
                }
                startActivityForResult(i, 0);
            }
        });
        btn_settings.setOnClickListener(v -> {
            startActivity(new Intent(AutoChangerActivity.this,SettingsActivity.class));

        });

    }


    public ArrayList<File> getFile() {
        String path = Environment.getExternalStorageDirectory().toString() + File.separator + "Android" + File.separator + "data" + File.separator + getPackageName() + File.separator + "files" + File.separator + "downloaded";
        File directory = new File(path);
        Log.e("~~~",path);

        File[] files = directory.listFiles();
        if (files != null) {
            Log.e("~~~",files.length+"");
            recyclerView.setVisibility(View.VISIBLE);
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().endsWith(".jpg")){
                    listFile.add(files[i]);
                    Log.e("~~~",files[i].getName());
                }
            }
        }else {

        }
        return listFile;
    }

    @Override
    public void onLongClick(View view, int position) {

        new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        listFile.get(position).delete();
                        listFile.remove(position);
                        if (listFile.size()==0){
                            new KAlertDialog(AutoChangerActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("No image downloaded!")
                                    .setContentText("Please download more 2 images ")
                                    .setConfirmText("OK")
                                    .show();
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }



}