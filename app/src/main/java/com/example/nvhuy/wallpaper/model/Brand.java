package com.example.nvhuy.wallpaper.model;



import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Brand {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("title")
@Expose
private String title;
private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Brand(Integer id, String title, String url) {
        this.id = id;
        this.title = title;
        this.url = url;
    }

    public Brand(Integer id, String title, List<String> images) {
        this.id = id;
        this.title = title;
        this.images = images;
    }

    @SerializedName("images")
@Expose
private List<String> images = null;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public List<String> getImages() {
return images;
}

public void setImages(List<String> images) {
this.images = images;
}

}