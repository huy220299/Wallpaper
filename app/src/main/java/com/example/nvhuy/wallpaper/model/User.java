package com.example.nvhuy.wallpaper.model;

public class User {
    private String name;
    private String url;
    private String phone;
    private String password;
    private String email;
    private String date_of_birth;
    private boolean sale_notification = true;
    private boolean new_arrival_notification = true;
    private boolean delivery_status_notification = true;

    public User() {
    }

    public User(String name, String url, String phone, String password, String email) {
        this.name = name;
        this.url = url;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    public User(String name, String url, String phone, String email, String date_of_birth, String password, boolean sale_notification, boolean new_arrival_notification, boolean delivery_status_notification) {
        this.name = name;
        this.url = url;
        this.phone = phone;
        this.email = email;
        this.date_of_birth = date_of_birth;
        this.password = password;
        this.sale_notification = sale_notification;
        this.new_arrival_notification = new_arrival_notification;
        this.delivery_status_notification = delivery_status_notification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSale_notification() {
        return sale_notification;
    }

    public void setSale_notification(boolean sale_notification) {
        this.sale_notification = sale_notification;
    }

    public boolean isNew_arrival_notification() {
        return new_arrival_notification;
    }

    public void setNew_arrival_notification(boolean new_arrival_notification) {
        this.new_arrival_notification = new_arrival_notification;
    }

    public boolean isDelivery_status_notification() {
        return delivery_status_notification;
    }

    public void setDelivery_status_notification(boolean delivery_status_notification) {
        this.delivery_status_notification = delivery_status_notification;
    }
}
