package com.example.withpet;

import java.io.Serializable;

public class ProfileInfo implements Serializable {
    private String username;
    private String shape;
    private String imgUrl;
    private int meal;

    public ProfileInfo(String username, String shape, int meal){
        this.username = username;
        this.shape = shape;
        this.meal= meal;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public int getMeal() {
        return meal;
    }

    public void setMeal(int meal) {
        this.meal = meal;
    }

}
