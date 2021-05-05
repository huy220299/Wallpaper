package com.example.nvhuy.wallpaper.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nvhuy.wallpaper.R;
import com.example.nvhuy.wallpaper.APIService.APIService;
import com.example.nvhuy.wallpaper.APIService.DataService;
import com.example.nvhuy.wallpaper.Ultility.ZoomRecyclerGridLayout;
import com.example.nvhuy.wallpaper.adapter.CustomerAdapter_category;
import com.example.nvhuy.wallpaper.model.Brand;
import com.example.nvhuy.wallpaper.model.Category;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoryFragment extends Fragment {
    RecyclerView rCategory;
    ArrayList<Category>  listCategory;
    ArrayList<Brand> listBrand;
    CustomerAdapter_category adapter_category;




    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_category, container, false);
        rCategory = viewGroup.findViewById(R.id.rCategory);

        setupBrand();
        createRecyclerView();

        return viewGroup;
    }

    private void createRecyclerView() {
        DataService dataService = APIService.getService();
        Call<List<Category>> callback = dataService.getAllCategory();
        callback.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                listCategory = new ArrayList<>();
                listCategory = (ArrayList<Category>) response.body();
                adapter_category = new CustomerAdapter_category(getContext(),listCategory,listBrand);
                rCategory.setAdapter(adapter_category);
                ZoomRecyclerGridLayout mLayoutManager =
                        new ZoomRecyclerGridLayout(getContext(),1, StaggeredGridLayoutManager.VERTICAL,true);
                rCategory.setLayoutManager(mLayoutManager);
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
    }

    private void setupBrand() {
        listBrand = new ArrayList<>();
        listBrand.add(new Brand(30,"Android","http://litwallz-admin.filesdroid.com//uploads//cache//category_thumb_api//uploads//png//032574f7e5be76465a299d3ef504e15d.png"));
        listBrand.add(new Brand(29,"Apple","http://litwallz-admin.filesdroid.com//uploads//cache//category_thumb_api//uploads//jpeg//74bf5d225007175a877e2bc22a929d0f.jpeg"));
        listBrand.add(new Brand(28,"ASUS","http://litwallz-admin.filesdroid.com//uploads//cache//category_thumb_api//uploads//jpeg//36d1dd7ce42f211de77163a55bb42b15.jpeg"));
        listBrand.add(new Brand(25,"Google","http://litwallz-admin.filesdroid.com//uploads//cache//category_thumb_api//uploads//jpeg//52841312c1a7300aba26ebc5eec5b659.jpeg"));
        listBrand.add(new Brand(30,"HTC","http://litwallz-admin.filesdroid.com//uploads//cache//category_thumb_api//uploads//jpeg//3e0c09caf38f1c4fc63c228a633927c5.jpeg    "));
    }
}