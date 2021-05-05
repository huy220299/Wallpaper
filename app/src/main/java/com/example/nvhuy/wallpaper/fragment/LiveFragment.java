package com.example.nvhuy.wallpaper.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nvhuy.wallpaper.R;
import com.example.nvhuy.wallpaper.APIService.APIService;
import com.example.nvhuy.wallpaper.APIService.DataService;
import com.example.nvhuy.wallpaper.Ultility.ZoomRecyclerGridLayout;
import com.example.nvhuy.wallpaper.adapter.RecyclerViewAdapter;

import com.example.nvhuy.wallpaper.model.Image;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LiveFragment extends Fragment {

    RecyclerView rCategory;
    ArrayList<Image> mImageList;
    RecyclerViewAdapter adapter;
    ViewPager viewPager;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    int page = 0;
    SwipeRefreshLayout swipeRefreshLayout;


    public LiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_recent, container, false);
        rCategory = viewGroup.findViewById(R.id.rRecent);
        swipeRefreshLayout = viewGroup.findViewById(R.id.swipeLayout);
        refreshLayout();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            page=0;
            mImageList.clear();
            adapter.notifyDataSetChanged();
            loadMore(page);
            swipeRefreshLayout.setRefreshing(false);
        });

        mImageList= new ArrayList<>();
        adapter = new RecyclerViewAdapter(mImageList);
        rCategory.setAdapter(adapter);


        DataService dataservice = APIService.getService();
        Call<List<Image>> callback = dataservice.getImageByCategory("26",String.valueOf(page));
        callback.enqueue(new Callback<List<Image>>() {
            @Override
            public void onResponse(Call<List<Image>> call, Response<List<Image>> response) {

                ArrayList<Image> result = (ArrayList<Image>) response.body();
                assert result != null;
                mImageList.addAll(result);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<Image>> call, Throwable t) {
            }
        });

        ZoomRecyclerGridLayout mLayoutManager =new ZoomRecyclerGridLayout(getContext(),2);
        rCategory.setLayoutManager(mLayoutManager);

        rCategory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            Log.v("...", "Last Item Wow !");
                            // Do pagination.. i.e. fetch new data
                            loading = true;
                            page++;
                            loadMore(page);
                        }
                    }
                }
            }
        });

        return viewGroup;
    }

    private void refreshLayout() {
    }

    private void loadMore(int index){
        if (index<5){
            DataService dataservice = APIService.getService();
            Call<List<Image>> callback = dataservice.getImageByCategory("26",String.valueOf(index));

            callback.enqueue(new Callback<List<Image>>() {
                @Override
                public void onResponse(Call<List<Image>> call, Response<List<Image>> response) {
                    if(response.isSuccessful()){

                        ArrayList<Image> result = (ArrayList<Image>) response.body();
                        assert result != null;
                        if(result.size()>0){
                            //add loaded data
                            mImageList.addAll(result);
                        }else{
                            //result size 0 means there is no more data available at server

                        }
                        adapter.notifyDataSetChanged();
                    }else{
                        Log.e("TAG"," Load More Response Error "+String.valueOf(response.code()));
                    }
                }

                @Override
                public void onFailure(Call<List<Image>> call, Throwable t) {
                    Log.e("TAG"," Load More Response Error "+t.getMessage());
                }
            });
        }

    }
}