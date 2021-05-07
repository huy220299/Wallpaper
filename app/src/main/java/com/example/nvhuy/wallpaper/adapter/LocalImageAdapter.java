package com.example.nvhuy.wallpaper.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nvhuy.wallpaper.R;
import com.example.nvhuy.wallpaper.Ultility.Common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class LocalImageAdapter extends RecyclerView.Adapter<LocalImageAdapter.ViewHolder> {
    ItemLongClick itemLongClickListener;
    Context context;
    ArrayList<File> list;
    int[] listSelected;
    String path = "/storage/emulated/0/Android/data/com.example.nvhuy.wallpaper/files/autoChange/";


    public LocalImageAdapter(Context context, ArrayList<File> list, ItemLongClick itemLongClickListener) {
        this.list = list;
        this.context = context;
        this.itemLongClickListener = itemLongClickListener;
        listSelected = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            listSelected[i] = 0;
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View categoryView = inflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(categoryView);

        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(Uri.fromFile(list.get(position)))
                .placeholder(R.drawable.icon).into(holder.img);
        holder.itemView.setOnLongClickListener(v -> {
            itemLongClickListener.onLongClick(v, position);
            return true;
        });
        holder.itemView.setOnClickListener(view -> {
            if (listSelected[position] == 0) {
//                holder.itemView.setBackground(context.getDrawable(R.drawable.bg_login_button));
                holder.itemView.setForeground(context.getDrawable(R.drawable.bg_login_button));
                listSelected[position] = 1;
                try {
                    File file = new File(path);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    Files.copy(list.get(position).toPath(),
                            (new File(path + list.get(position).getName())).toPath(),
                            StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();

                }
            } else {
                holder.itemView.setForeground(null);
                listSelected[position] = 0;
                if (checkDownload(list.get(position).getName())) {
                    File file = new File(path + list.get(position).getName());
                    file.delete();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        if (list.size() == 0) {
            return 0;
        }
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageView);


        }


    }

    public interface ItemLongClick {
        void onLongClick(View view, int position);
    }

    public boolean checkDownload(String image_name) {
        String path = Environment.getExternalStorageDirectory().toString() +
                File.separator + "Android"
                + File.separator + "data"
                + File.separator + context.getPackageName()
                + File.separator + "files"
                + File.separator + "autoChange";
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

