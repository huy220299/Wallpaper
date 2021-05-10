package com.example.nvhuy.wallpaper.Activity;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nvhuy.wallpaper.R;
import com.example.nvhuy.wallpaper.APIService.APIService;
import com.example.nvhuy.wallpaper.APIService.DataService;
import com.example.nvhuy.wallpaper.Ultility.ZoomRecyclerGridLayout;
import com.example.nvhuy.wallpaper.adapter.RecyclerViewAdapter;
import com.example.nvhuy.wallpaper.model.Image;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemBySearchActivity extends BaseActivity {
    TextView tv_keyword, noImage;
    ImageView back;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    ArrayList<Image> mImageList;

    int page=0;
    SwipeRefreshLayout swipeRefreshLayout;
    private boolean loading = true;
    String keyword;
    int pastVisiblesItems, visibleItemCount, totalItemCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_by_search);
        keyword  = getIntent().getExtras().getString("keyword");
        back = findViewById(R.id.imageBack);
        noImage = findViewById(R.id.noImage);
        tv_keyword = findViewById(R.id.keyword);
        tv_keyword.setText(keyword);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);

        back.setOnClickListener(v -> {
            noImage.setVisibility(View.GONE);
            startActivity(new Intent(ItemBySearchActivity.this,MainActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });
        recyclerView = findViewById(R.id.recyclerView_bySearch);
        mImageList = new ArrayList<>();
        adapter = new RecyclerViewAdapter(mImageList);
        recyclerView.setAdapter(adapter);
        GridLayoutManager mLayoutManager =new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(mLayoutManager);
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        swipeRefreshLayout.setOnRefreshListener(() -> {
            page=0;
            mImageList.clear();
            loadMore(page);
            swipeRefreshLayout.setRefreshing(false);
        });
        DataService dataservice = APIService.getService();
        Call<List<Image>> callback = dataservice.getImageBySearch(keyword,String.valueOf(page));
        callback.enqueue(new Callback<List<Image>>() {
            @Override
            public void onResponse(Call<List<Image>> call, Response<List<Image>> response) {
                ArrayList<Image> results = (ArrayList<Image>) response.body();
                mImageList.addAll(results);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<List<Image>> call, Throwable t) {
                Log.i("_______", new Gson().toJson(t));
            }
        });


    }

    private void loadMore(int index){
        DataService dataservice = APIService.getService();
        Call<List<Image>> callback = dataservice.getImageBySearch(keyword,String.valueOf(index));

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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}