package com.example.nvhuy.wallpaper.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.developer.kalert.KAlertDialog;
import com.example.nvhuy.wallpaper.R;
import com.example.nvhuy.wallpaper.adapter.LocalImageAdapter;

import java.io.File;
import java.util.ArrayList;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class FavoriteActivity extends BaseActivity implements LocalImageAdapter.ItemLongClick {
    LocalImageAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<File> listFile;
    ImageView imgBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        imgBack = findViewById(R.id.imageBack);
        recyclerView = findViewById(R.id.recyclerView);
        listFile = new ArrayList<>();
        getFile();
        if (listFile.size()==0){
            new KAlertDialog(FavoriteActivity.this, KAlertDialog.WARNING_TYPE)
                    .setTitleText("No image in here")
                    .setContentText("Pick icon love in image and comeback here ")
                    .setConfirmText("OK")
                    .show();
        }
        adapter = new LocalImageAdapter(this,listFile,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        StaggeredGridLayoutManager mLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        imgBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onLongClick(View view, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    boolean results = listFile.get(position).delete();
                    listFile.remove(position);
                    adapter.notifyDataSetChanged();
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

//    public static boolean delete(final Context context, final File file) {
//        final String where = MediaStore.MediaColumns.DATA + "=?";
//        final String[] selectionArgs = new String[]{
//                file.getAbsolutePath()
//        };
//        final ContentResolver contentResolver = context.getContentResolver();
//        final Uri filesUri = MediaStore.Files.getContentUri("external");
//        contentResolver.delete(filesUri, where, selectionArgs);
//
//        if (file.exists()) {
//            contentResolver.delete(filesUri, where, selectionArgs);
//        }
//        return !file.exists();
//    }

    public ArrayList<File> getFile() {
        String path = Environment.getExternalStorageDirectory().toString() + File.separator + "Android" + File.separator + "data" + File.separator + getPackageName() + File.separator + "files" + File.separator + "myWallpaper";
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".jpg")) {
                    listFile.add(file);
                }
            }
        }else
        {
            new KAlertDialog(FavoriteActivity.this, KAlertDialog.WARNING_TYPE)
                    .setTitleText("No image in here")
                    .setContentText("Pick icon love in image and comeback here ")
                    .setConfirmText("OK")
                    .show();
        }
        return listFile;
    }
}