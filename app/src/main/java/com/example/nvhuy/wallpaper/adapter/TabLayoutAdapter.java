package com.example.nvhuy.wallpaper.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.nvhuy.wallpaper.fragment.CategoryFragment;
import com.example.nvhuy.wallpaper.fragment.LiveFragment;
import com.example.nvhuy.wallpaper.fragment.RecentFragment;
import com.example.nvhuy.wallpaper.fragment.TrendingFragment;

public class TabLayoutAdapter extends FragmentStatePagerAdapter {
    private Context context;
    int totalsTabs;
    public TabLayoutAdapter(FragmentManager fm, Context context, int totalsTabs){
        super(fm);
        this.context = context;
        this.totalsTabs = totalsTabs;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                RecentFragment recentFragment = new RecentFragment();
                return recentFragment;

            case 1:
                LiveFragment liveFragment = new LiveFragment();
                return liveFragment;

            case 2:
                CategoryFragment categoryFragment = new CategoryFragment();
                return categoryFragment;

            case 3:
                TrendingFragment randomFragment = new TrendingFragment();
                return randomFragment;

            case 4:
                TrendingFragment trendingFragment = new TrendingFragment();
                return trendingFragment;




            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalsTabs;
    }
//    public CharSequence getPageTitle(int position) {
//        switch (position) {
//            case 0:
//                return "Recent";
//            case 1:
//                return "Favorite";
//            case 2:
//                return "Category";
//            default:
//                return null;
//        }
//    }
}