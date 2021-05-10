package com.example.nvhuy.wallpaper.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nvhuy.wallpaper.R;
import com.example.nvhuy.wallpaper.adapter.CustomerAdapter_category;
import com.example.nvhuy.wallpaper.model.Category;
import com.example.nvhuy.wallpaper.model.Brand;

import java.util.ArrayList;


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
        setupCategory();
        createRecyclerView();
        return viewGroup;
    }

    private void createRecyclerView() {
        adapter_category = new CustomerAdapter_category(getContext(),listCategory,listBrand);
        rCategory.setAdapter(adapter_category);
        GridLayoutManager mLayoutManager =new GridLayoutManager(getContext(),1);
        rCategory.setLayoutManager(mLayoutManager);
    }

    private void setupBrand() {
        listBrand = new ArrayList<>();
        listBrand.add(new Brand(30,"Android","http://litwallz-admin.filesdroid.com//uploads//cache//category_thumb_api//uploads//png//032574f7e5be76465a299d3ef504e15d.png"));
        listBrand.add(new Brand(29,"Apple","http://litwallz-admin.filesdroid.com//uploads//cache//category_thumb_api//uploads//jpeg//74bf5d225007175a877e2bc22a929d0f.jpeg"));
        listBrand.add(new Brand(28,"ASUS","http://litwallz-admin.filesdroid.com//uploads//cache//category_thumb_api//uploads//jpeg//36d1dd7ce42f211de77163a55bb42b15.jpeg"));
        listBrand.add(new Brand(25,"Google","http://litwallz-admin.filesdroid.com//uploads//cache//category_thumb_api//uploads//jpeg//52841312c1a7300aba26ebc5eec5b659.jpeg"));
        listBrand.add(new Brand(30,"HTC","http://litwallz-admin.filesdroid.com//uploads//cache//category_thumb_api//uploads//jpeg//3e0c09caf38f1c4fc63c228a633927c5.jpeg    "));
    }
    private void setupCategory(){
        listCategory = new ArrayList<>();
        listCategory.add(new Category(""," ",R.drawable.live_wallpaper));
        listCategory.add(new Category("26","Live Wallpapers",R.drawable.live_wallpaper));
        listCategory.add(new Category("28","Premium",R.drawable.premium_wallpaper));
        listCategory.add(new Category("12","Abstract",R.drawable.abstract_wallpaper));
        listCategory.add(new Category("1","Amoled",R.drawable.amoled_wallpaper));
        listCategory.add(new Category("31","Animal & Birds",R.drawable.dope_wallpaper));
        listCategory.add(new Category("22","Anime",R.drawable.anime_wallpaper));
        listCategory.add(new Category("30","Architecture",R.drawable.achitecture_wallpaper));
        listCategory.add(new Category("11","Beaches",R.drawable.beaches_wallpaper));
        listCategory.add(new Category("20","Bikes",R.drawable.bike_wallpaper));
        listCategory.add(new Category("5","Cartoon",R.drawable.cartoon_wallpaper));
        listCategory.add(new Category("4","Cars",R.drawable.car_wallpaper));
        listCategory.add(new Category("10","City",R.drawable.city_wallpaper));
        listCategory.add(new Category("16","City Street",R.drawable.city_street_wallpaper));
        listCategory.add(new Category("9","Dope",R.drawable.dope_wallpaper));
        listCategory.add(new Category("3","Forest",R.drawable.forest_wallpaper));
        listCategory.add(new Category("18","Games",R.drawable.game_wallpaper));
        listCategory.add(new Category("17","Girl",R.drawable.girl_wallpaper));
        listCategory.add(new Category("32","Gradient",R.drawable.gradient_wallpaper));
        listCategory.add(new Category("29","Illustration",R.drawable.illustration_wallpaper));
        listCategory.add(new Category("24","Celebrities",R.drawable.celebrity_wallpaper));
        listCategory.add(new Category("2","Minimal ",R.drawable.minimal_wallpaper));
        listCategory.add(new Category("21","Movies & Web Series",R.drawable.movie_wallpaper));
        listCategory.add(new Category("13","Nature",R.drawable.nature_wallpaper));
        listCategory.add(new Category("33","Religious & Festivals",R.drawable.festival_wallpaper));
        listCategory.add(new Category("15","Roads",R.drawable.road_wallpaper));
        listCategory.add(new Category("7","Sky",R.drawable.sky_wallpaper));
        listCategory.add(new Category("23","Stock Wallpapers",R.drawable.stock_wallpaper));
        listCategory.add(new Category("6","Superheros & Villains ",R.drawable.superhero));
        listCategory.add(new Category("25","Technology",R.drawable.technology_wallpaper));
        listCategory.add(new Category("27","Lockscreen Wallpapers",R.drawable.lockscreen_wallpaper));
        listCategory.add(new Category("34","Typography",R.drawable.typography_wallpaper));
        listCategory.add(new Category("35","Desktop Wallpapers",R.drawable.desktop_wallpaper));

    }
}