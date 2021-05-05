package com.example.nvhuy.wallpaper.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.nvhuy.wallpaper.Activity.DetailActivity;
import com.example.nvhuy.wallpaper.Activity.ItemByCategoryActivity;
import com.example.nvhuy.wallpaper.Activity.MainActivity;
import com.example.nvhuy.wallpaper.R;
import com.example.nvhuy.wallpaper.model.Album;
import com.example.nvhuy.wallpaper.model.Image;
import com.kobakei.ratethisapp.RateThisApp;

import java.util.List;

import me.angeldevil.autoscrollviewpager.AutoScrollViewPager;


public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.RecyclerViewHolder> {
    private Context context;
    public static final int IS_HEADER = 0;
    public static final int IS_NORMAL = 1;


    private List<Image> listImage;
    private List<Album> listAlbum;

    //constructor
    public CustomerAdapter(Context context, List<Image> listImage, List<Album> listAlbum) {
        this.context = context;
        this.listImage = listImage;
        this.listAlbum = listAlbum;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerViewHolder holder;
        // Create different view holder with different flag
        if (viewType == IS_HEADER) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_header, viewGroup, false);
            holder = new RecyclerViewHolder(view, IS_HEADER);
            return holder;
        } else if (viewType == IS_NORMAL) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image, viewGroup, false);
            holder = new RecyclerViewHolder(view, IS_NORMAL);
            return holder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int position) {
        if (position != 0 && recyclerViewHolder.viewType == IS_NORMAL) {
            final Image currentItem = listImage.get(position);
            Glide.with(context).load(listImage.get(position).getThumbnail())
                    .thumbnail(0.1f)
                    .dontAnimate()
                    .placeholder(R.drawable.icon)
                    .into(recyclerViewHolder.imgView);

            recyclerViewHolder.txtView.setText(listImage.get(position).getTitle());
            if (currentItem.getKind().equals("video")) {

                recyclerViewHolder.ImgView_live.setVisibility(View.VISIBLE);
            }

            recyclerViewHolder.setItemClickListener((view, position1) -> {
                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                intent.putExtra("id_image", currentItem.getId().toString());
                intent.putExtra("id_user", currentItem.getUserid().toString());
                intent.putExtra("tittle", currentItem.getTitle());
                intent.putExtra("image", currentItem.getOriginal());
                intent.putExtra("kind", currentItem.getKind());
                intent.putExtra("user", currentItem.getUser());
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

    }

    @Override
    public int getItemCount() {

        if (listImage.size() == 0) {
            return 0;
        }
        return listImage.size();

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
        public ImageView imgView, ImgView_live;
        public TextView txtView;
        MyAdapter myAdapter;


        public RecyclerViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            if (viewType == IS_HEADER) {

                AutoScrollViewPager viewPager = itemView.findViewById(R.id.vpheader);
                myAdapter = new MyAdapter(context, listAlbum);
                viewPager.setAdapter(myAdapter);
                viewPager.setInterval(4000);
                viewPager.startAutoScroll();

                //viewPagerClick listener
                viewPager.setOnPageClickListener((pager, position) -> {
                    final Album currentItem = listAlbum.get(position);

                    if (position == 1) {
                        RateThisApp rateThisApp = null;
                        rateThisApp.onCreate(context);
                        rateThisApp.showRateDialog(context);
//            RateThisApp.setCallback(new RateThisApp.Callback() {
//              @Override
//              public void onYesClicked() {
//                Toast.makeText(context, "Yes event", Toast.LENGTH_SHORT).show();
//              }
//
//              @Override
//              public void onNoClicked() {
//                Toast.makeText(context, "No event", Toast.LENGTH_SHORT).show();
//              }
//
//              @Override
//              public void onCancelClicked() {
//                Toast.makeText(context, "Cancel event", Toast.LENGTH_SHORT).show();
//              }
//            });
//            RateThisApp.stopRateDialog(context);
//            RateThisApp.Config config = new RateThisApp.Config();
//            config.setUrl("http://www.example.com");
//            RateThisApp.init(config);
                    } else if (position == 5) {
                        Uri uri = Uri.parse("http://instagram.com/_u/xxx");
                        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                        likeIng.setPackage("com.instagram.android");

                        try {
                            pager.getContext().startActivity(likeIng);
                        } catch (ActivityNotFoundException e) {
                            pager.getContext().startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://instagram.com/xxx")));
                        }
                    } else {
                        Toast.makeText(pager.getContext(), currentItem.getCategory(), Toast.LENGTH_SHORT).show();
                        Log.e("~~~~", currentItem.getCategory());
                        Intent intent = new Intent(pager.getContext(), ItemByCategoryActivity.class);
                        intent.putExtra("id_category", currentItem.getId_category());
                        intent.putExtra("category", currentItem.getCategory());
                        pager.getContext().startActivity(intent);
                    }

                });
            }

            if (viewType == IS_NORMAL) {
                txtView = itemView.findViewById(R.id.item_text);
                imgView = itemView.findViewById(R.id.item_image);
                ImgView_live = itemView.findViewById(R.id.item_is_live);
                itemView.setOnClickListener(this);
            }

        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition());
        }
    }

    //adapter for viewPager Header
    private class MyAdapter extends PagerAdapter {
        List<Album> listAlbum1;
        Context context;

        public MyAdapter(Context context, List<Album> listAlbum1) {
            this.listAlbum1 = listAlbum1;
            this.context = context;
        }

        @Override
        public int getCount() {

            if (listAlbum.size() == 0) {
                return 0;
            }
            return listAlbum.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(context);
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_viewpager_header, container,
                    false);
            ImageView imageView = layout.findViewById(R.id.image);
            Glide.with(context).load(listAlbum1.get(position).getImages()).into(imageView);


            container.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

}
