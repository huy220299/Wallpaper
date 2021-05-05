package com.example.nvhuy.wallpaper.model;

public class Liked_image {
    String idImage, idUser;

    public Liked_image(String idImage, String idUser) {
        this.idImage = idImage;
        this.idUser = idUser;
    }

    public String getIdImage() {
        return idImage;
    }

    public void setIdImage(String idImage) {
        this.idImage = idImage;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
