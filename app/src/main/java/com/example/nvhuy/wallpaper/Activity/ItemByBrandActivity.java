package com.example.nvhuy.wallpaper.Activity;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nvhuy.wallpaper.R;
import com.example.nvhuy.wallpaper.APIService.APIService;
import com.example.nvhuy.wallpaper.APIService.DataService;
import com.example.nvhuy.wallpaper.adapter.RecyclerViewAdapter;

import com.example.nvhuy.wallpaper.model.Image;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemByBrandActivity extends BaseActivity {
    String id_brand, name_brand;
    TextView tv_brand;
    ImageView back;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    ArrayList<Image> listBrand;
    SwipeRefreshLayout swipeRefreshLayout;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    int page=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_by_category);
        getFromIntent();
        back = findViewById(R.id.imageBack);
        tv_brand = findViewById(R.id.tv_title);
        tv_brand.setText(name_brand);
        back.setOnClickListener(v -> ItemByBrandActivity.super.onBackPressed());
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            page=0;
            listBrand.clear();
            loadMore(page);
            swipeRefreshLayout.setRefreshing(false);
        });
        setRecyclerView();

        recyclerView = findViewById(R.id.recyclerView);
        listBrand = new ArrayList<>();
        adapter = new RecyclerViewAdapter(listBrand);
        recyclerView.setAdapter(adapter);
        GridLayoutManager mLayoutManager =new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(mLayoutManager);
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
                            loading = true;
                            page++;
                            loadMore(page);
                        }
                    }
                }
            }
        });
    }

    private void setRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);

        DataService dataservice = APIService.getService();
        Call<List<Image>> callback = dataservice.getImageByBrand(id_brand,String.valueOf(page));
        callback.enqueue(new Callback<List<Image>>() {
            @Override
            public void onResponse(Call<List<Image>> call, Response<List<Image>> response) {

                ArrayList<Image> results = (ArrayList<Image>) response.body();
                listBrand.addAll(results);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Image>> call, Throwable t) {
                Log.i("_______", new Gson().toJson(t));
            }
        });

    }

    private void getFromIntent() {
        id_brand = getIntent().getExtras().getString("id_brand");
        Log.e("~~~~",id_brand);
        name_brand = getIntent().getExtras().getString("name_brand");
    }
    private void loadMore(int index){
        DataService dataservice = APIService.getService();
        Call<List<Image>> callback = dataservice.getImageByBrand(id_brand,String.valueOf(index));

        callback.enqueue(new Callback<List<Image>>() {
            @Override
            public void onResponse(Call<List<Image>> call, Response<List<Image>> response) {
                if(response.isSuccessful()){

                    ArrayList<Image> result = (ArrayList<Image>) response.body();
                    assert result != null;
                    if(result.size()>0){
                        //add loaded data
                        listBrand.addAll(result);
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