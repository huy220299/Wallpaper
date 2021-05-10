package com.example.nvhuy.wallpaper.model;

public class Category {
    String id_category, category;
    int images;

    public String getId_category() {
        return id_category;
    }

    public void setId_category(String id_category) {
        this.id_category = id_category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getImages() {
        return images;
    }

    public void setImages(int images) {
        this.images = images;
    }

    public Category(String id_category, String category, int images) {
        this.id_category = id_category;
        this.category = category;
        this.images = images;
    }
}
