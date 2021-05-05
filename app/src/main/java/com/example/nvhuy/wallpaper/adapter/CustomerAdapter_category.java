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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.nvhuy.wallpaper.Activity.BrandActivity;
import com.example.nvhuy.wallpaper.Activity.ItemByBrandActivity;
import com.example.nvhuy.wallpaper.Activity.ItemByCategoryActivity;
import com.example.nvhuy.wallpaper.R;

import com.example.nvhuy.wallpaper.model.Brand;
import com.example.nvhuy.wallpaper.model.Category;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CustomerAdapter_category extends RecyclerView.Adapter<CustomerAdapter_category.RecyclerViewHolder> {
    private Context context;
    public static final int IS_HEADER = 0;
    public static final int IS_NORMAL = 1;

    private List<Category> listCategory;
    private List<Brand> listBrand;

    //constructor
    public CustomerAdapter_category(Context context, List<Category> listCategory, List<Brand> listBrand) {
        this.context = context;
        this.listCategory = listCategory;
        this.listBrand = listBrand;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerViewHolder holder;
        // Create different view holder with different flag
        if (viewType == IS_HEADER) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_album_type_header, viewGroup, false);
            holder = new RecyclerViewHolder(view, IS_HEADER);
            return holder;
        } else if (viewType == IS_NORMAL) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_album_type_normal, viewGroup, false);
            holder = new RecyclerViewHolder(view, IS_NORMAL);
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int position) {
        if (position != 0 && recyclerViewHolder.viewType == IS_NORMAL) {
            final Category currentItem = listCategory.get(position);
            Glide.with(context).load(listCategory.get(position).getImage())
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.icon)
                    .into(recyclerViewHolder.imgView);
            recyclerViewHolder.txtView.setText(listCategory.get(position).getTitle());
            recyclerViewHolder.setItemClickListener((view, position1) -> {
                Intent intent = new Intent(view.getContext(), ItemByCategoryActivity.class);
                intent.putExtra("id_category", currentItem.getId().toString());
                intent.putExtra("category", currentItem.getTitle());

                view.getContext().startActivity(intent);
            });
        } else if (position == 0) {
            recyclerViewHolder.txt_view_all.setOnClickListener(v -> v.getContext().startActivity(new Intent(v.getContext(), BrandActivity.class)));
        }

    }

    @Override
    public int getItemCount() {
        return listCategory.size();

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return IS_HEADER;
        } else {
            return IS_NORMAL;
        }
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemClickListener itemClickListener;

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public int viewType;

        public ImageView imgView;
        public TextView txtView, txt_view_all;

        MyAdapter myAdapter;

        public RecyclerViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            if (viewType == IS_HEADER) {
                txt_view_all = itemView.findViewById(R.id.view_all);
                RecyclerView recyclerView = itemView.findViewById(R.id.recyclerView_brand);
                myAdapter = new MyAdapter(listBrand);
                recyclerView.setAdapter(myAdapter);
                StaggeredGridLayoutManager mLayoutManager =
                        new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(mLayoutManager);
            }
            if (viewType == IS_NORMAL) {
                txtView = itemView.findViewById(R.id.item_album_text);
                imgView = itemView.findViewById(R.id.item_album_image);
                itemView.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition());
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        Context context;
        private List<Brand> mListBrands;

        public MyAdapter(List<Brand> mImage) {
            this.mListBrands = mImage;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View categoryView = inflater.inflate(R.layout.item_album_type_header_item_recycler, parent, false);
            ViewHolder viewHolder = new ViewHolder(categoryView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final Brand currentItem = mListBrands.get(position);
            Glide.with(context).load(currentItem.getUrl())
                    .placeholder(R.drawable.icon).into(holder.img);
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

            return mListBrands.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private ItemClickListener itemClickListener;
            ImageView img;
            TextView img_name;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.item_album_image);
                img_name = itemView.findViewById(R.id.item_album_text);
                itemView.setOnClickListener(this);
            }

            public void setItemClickListener(ItemClickListener itemClickListener) {
                this.itemClickListener = itemClickListener;
            }

            @Override
            public void onClick(View v) {
                itemClickListener.onClick(v, getAdapterPosition());
            }
        }

    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

}

