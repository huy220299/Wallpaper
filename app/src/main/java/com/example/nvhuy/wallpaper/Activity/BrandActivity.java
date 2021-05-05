package com.example.nvhuy.wallpaper.Activity;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nvhuy.wallpaper.R;
import com.example.nvhuy.wallpaper.APIService.APIService;
import com.example.nvhuy.wallpaper.APIService.DataService;
import com.example.nvhuy.wallpaper.adapter.RecyclerViewAdapter_brand;
import com.example.nvhuy.wallpaper.model.Brand;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrandActivity extends BaseActivity {
    TextView tv_title;
    ImageView back;
    RecyclerView recyclerView;
    RecyclerViewAdapter_brand adapter;
    ArrayList<Brand> listBrand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_by_category);
        back = findViewById(R.id.imageBack);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("Brand");
        back.setOnClickListener(v -> BrandActivity.super.onBackPressed());

        setRecyclerView();
    }

    private void setRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);

        DataService dataservice = APIService.getService();
        Call<List<Brand>> callback = dataservice.getAllBrand();
        callback.enqueue(new Callback<List<Brand>>() {
            @Override
            public void onResponse(Call<List<Brand>> call, Response<List<Brand>> response) {

                listBrand = (ArrayList<Brand>) response.body();
                adapter = new RecyclerViewAdapter_brand(listBrand);
                recyclerView.setAdapter(adapter);
                StaggeredGridLayoutManager mLayoutManager =
                        new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(mLayoutManager);
            }

            @Override
            public void onFailure(Call<List<Brand>> call, Throwable t) {
                Log.i("_______", new Gson().toJson(t));
            }
        });

    }


}