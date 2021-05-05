package com.example.nvhuy.wallpaper.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.nvhuy.wallpaper.fragment.CategoryFragment;
import com.example.nvhuy.wallpaper.fragment.LiveFragment;
import com.example.nvhuy.wallpaper.fragment.RecentFragment;
import com.example.nvhuy.wallpaper.fragment.TrendingFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                RecentFragment recentFragment = new RecentFragment();
                return recentFragment;

            case 1:
                LiveFragment liveFragment = new LiveFragment();
                return liveFragment;
            case 2:
                TrendingFragment trendingFragment = new TrendingFragment();
                return trendingFragment;
            case 3:
                TrendingFragment randomFragment = new TrendingFragment();
                return randomFragment;
            case 4:
                CategoryFragment categoryFragment = new CategoryFragment();
                return categoryFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }
}
