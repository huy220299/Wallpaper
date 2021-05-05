package com.example.nvhuy.wallpaper.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.example.nvhuy.wallpaper.Activity.DetailActivity;
import com.example.nvhuy.wallpaper.R;
import com.example.nvhuy.wallpaper.model.Image;

import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    Context context;
    private ArrayList<Image> mImageList;
    public RecyclerViewAdapter(ArrayList<Image> mImage) {
        this.mImageList = mImage;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View categoryView = inflater.inflate(R.layout.item_image, parent, false);
        ViewHolder viewHolder = new ViewHolder(categoryView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         Image currentItem =mImageList.get(position);
         if (currentItem.getKind().equals("video")){
             holder.is_live.setVisibility(View.VISIBLE);
         }
        Glide.with(context).load(currentItem.getThumbnail())
                .thumbnail(0.1f)
                .placeholder(R.drawable.icon)
                .into(holder.img);
        holder.img_name.setText(currentItem.getTitle());

        holder.setItemClickListener((view, position1) -> {
            Intent intent = new Intent(view.getContext(), DetailActivity.class);
            intent.putExtra("id_image",currentItem.getId().toString());
            intent.putExtra("id_user",currentItem.getUserid().toString());
            intent.putExtra("tittle",currentItem.getTitle());
            intent.putExtra("image",currentItem.getOriginal());
            intent.putExtra("user", currentItem.getUser());
            intent.putExtra("kind", currentItem.getKind());
            intent.putExtra("download", currentItem.getDownloads());
            intent.putExtra("views", currentItem.getViews());
            intent.putExtra("set", currentItem.getSets());
            intent.putExtra("resolution", currentItem.getResolution());
            intent.putExtra("created", currentItem.getCreated());
            intent.putExtra("size", currentItem.getSize());
            intent.putExtra("img_user", currentItem.getUserimage());
            intent.putExtra("myObject", currentItem);
            view.getContext().startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        if (mImageList.size()==0)
        {
            return 0;
        }

        return mImageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemClickListener itemClickListener;

        ImageView img, is_live;
        TextView img_name;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
             img  = itemView.findViewById(R.id.item_image);
             img_name = itemView.findViewById(R.id.item_text);
             is_live = itemView.findViewById(R.id.item_is_live);
            itemView.setOnClickListener(this);

        }

        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition());
        }
    }
    interface ItemClickListener {
        void onClick(View view, int position);
    }
}
