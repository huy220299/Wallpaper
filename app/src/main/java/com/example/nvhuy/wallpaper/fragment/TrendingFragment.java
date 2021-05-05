package com.example.nvhuy.wallpaper.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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


public class TrendingFragment extends Fragment {

    RecyclerView rCategory;
    ArrayList<Image> mImageList;
    RecyclerViewAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    int page = 0;


    public TrendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_recent, container, false);
        rCategory = viewGroup.findViewById(R.id.rRecent);

        swipeRefreshLayout = viewGroup.findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page=0;
                mImageList.clear();
                adapter.notifyDataSetChanged();
                loadMore(page);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        mImageList= new ArrayList<>();
        adapter = new RecyclerViewAdapter(mImageList);
        rCategory.setAdapter(adapter);

        DataService dataservice = APIService.getService();
        Call<List<Image>> callback = dataservice.getRandomImage("0");
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

        ZoomRecyclerGridLayout mLayoutManager =new ZoomRecyclerGridLayout(getContext(),2,RecyclerView.VERTICAL,false);
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
    private void loadMore(int index){
        DataService dataservice = APIService.getService();
        Call<List<Image>> callback = dataservice.getRandomImage(String.valueOf(index));

        callback.enqueue(new Callback<List<Image>>() {
            @Override
            public void onResponse(Call<List<Image>> call, Response<List<Image>> response) {
                if(response.isSuccessful()){

                    ArrayList<Image> result = (ArrayList<Image>) response.body();
                    if(result.size()>0){
                        //add loaded data
                        mImageList.addAll(result);
                    }else{
                        //result size 0 means there is no more data available at server
                        Toast.makeText(getContext(),"No More Data Available",Toast.LENGTH_LONG).show();
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