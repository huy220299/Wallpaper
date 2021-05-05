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

import com.bumptech.glide.Glide;
import com.example.nvhuy.wallpaper.Activity.ItemByBrandActivity;
import com.example.nvhuy.wallpaper.R;
import com.example.nvhuy.wallpaper.model.Brand;

import java.util.ArrayList;

public class RecyclerViewAdapter_brand extends RecyclerView.Adapter<RecyclerViewAdapter_brand.ViewHolder>{
    Context context;
    private ArrayList<Brand> listBrand;
    public RecyclerViewAdapter_brand(ArrayList<Brand> list) {
        this.listBrand = list;

    }

    @NonNull
    @Override
    public RecyclerViewAdapter_brand.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View categoryView = inflater.inflate(R.layout.item_album_type_header_item_recycler, parent, false);
        ViewHolder viewHolder = new ViewHolder(categoryView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Brand currentItem =listBrand.get(position);
        Glide.with(context).load(currentItem.getImages().get(0))
                .placeholder(R.drawable.icon)
                .thumbnail(0.5f)
                .into(holder.img);
        holder.img_name.setText(currentItem.getTitle());

        holder.setItemClickListener((view, position1) -> {
            Intent intent = new Intent(view.getContext(), ItemByBrandActivity.class);
            intent.putExtra("id_brand", currentItem.getId().toString());
            intent.putExtra("name_brand", currentItem.getTitle());
            view.getContext().startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        if (listBrand.size()==0)
        {
            return 0;
        }

        return listBrand.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemClickListener itemClickListener;

        ImageView img;
        TextView img_name;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            img  = itemView.findViewById(R.id.item_album_image);
            img_name = itemView.findViewById(R.id.item_album_text);
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
