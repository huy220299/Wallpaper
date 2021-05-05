package com.example.nvhuy.wallpaper.APIService;


import com.example.nvhuy.wallpaper.model.Brand;
import com.example.nvhuy.wallpaper.model.Category;
import com.example.nvhuy.wallpaper.model.Image;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface DataService {


    @GET("api/wallpaper/random/{page}/p2pqlsnjHgdxX21GFRAYyLvNBe3zcsSz/16edd7cf-2525-485e-b11a-3dd35f382457/")
    Call<List<Image>> getRandomImage(@Path(value = "page", encoded = true) String page);

    @GET("api/wallpaper/category/{page}/{category}/p2pqlsnjHgdxX21GFRAYyLvNBe3zcsSz/16edd7cf-2525-485e-b11a-3dd35f382457/")
    Call<List<Image>> getImageByCategory(@Path(value = "category", encoded = true) String category, @Path(value = "page",encoded = true) String page);

    @GET("api/wallpaper/query/{page}/{keyword}/p2pqlsnjHgdxX21GFRAYyLvNBe3zcsSz/16edd7cf-2525-485e-b11a-3dd35f382457/")
    Call<List<Image>> getImageBySearch(@Path(value = "keyword", encoded = true) String keyword, @Path(value = "page",encoded = true) String page);

    @GET("/api/wallpaper/pack/{page}/{keyword}/p2pqlsnjHgdxX21GFRAYyLvNBe3zcsSz/16edd7cf-2525-485e-b11a-3dd35f382457/")
    Call<List<Image>> getImageByBrand(@Path(value = "keyword", encoded = true) String id_brand, @Path(value = "page",encoded = true) String page);

    @GET("api/pack/all/p2pqlsnjHgdxX21GFRAYyLvNBe3zcsSz/16edd7cf-2525-485e-b11a-3dd35f382457/")
    Call<List<Brand>> getAllBrand();

    @GET("api/category/all/p2pqlsnjHgdxX21GFRAYyLvNBe3zcsSz/16edd7cf-2525-485e-b11a-3dd35f382457/")
    Call<List<Category>> getAllCategory();

}
