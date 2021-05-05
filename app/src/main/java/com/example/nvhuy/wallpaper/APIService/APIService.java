package com.example.nvhuy.wallpaper.APIService;

public class APIService {
    private  static String base_url = "http://litwallz-admin.filesdroid.com/";
    public  static DataService getService(){
        return APIRetrofitClient.getClient(base_url).create(DataService.class);
    }
}
