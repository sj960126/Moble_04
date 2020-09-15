package com.withpet.main;

import java.io.Serializable;

public class TransUser implements Serializable {
    private String name;
    private String nickname;
    private String email;
    private String uid;
    private String shape;
    private String imgUrl;
    private int meal;

    TransUser(User user){
        name = user.getName();
        nickname = user.getNickname();
        email = user.getEmail();
        uid = user.getUid();
        shape = user.getShape();
        imgUrl = user.getImgUrl();
        meal = user.getMeal();
    }
    public User TransformUser(){
        User user = new User();
        user.setEmail(email);
        user.setImgUrl(imgUrl);
        user.setMeal(meal);
        user.setName(name);
        user.setNickname(nickname);
        user.setShape(shape);
        user.setUid(uid);
        return user;
    }
    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public String getShape() {
        return shape;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getMeal() {
        return meal;
    }


}
