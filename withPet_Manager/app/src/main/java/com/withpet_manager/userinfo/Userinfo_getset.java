package com.withpet_manager.userinfo;


public class Userinfo_getset {
    private String email;
    private String imgUri;
    private String name;
    private String nickname;
    private String pw;
    private String uid;
    private int meal;
    private int petcode;
    private String shape;


    Userinfo_getset(){

    }
    Userinfo_getset(String imgUri, String name, String nickname){
        this.imgUri = imgUri;
        this.name = name;
        this.nickname = nickname;
    }

    public Userinfo_getset(String email, String imgUri, String name, String nickname, String pw, String uid, int meal, int petcode, String shape){
        this.email = email;
        this.imgUri = imgUri;
        this.name =name;
        this.nickname = nickname;
        this.pw =pw;
        this.uid = uid;
        this.meal = meal;
        this.petcode = petcode;
        this.shape = shape;
    }
    Userinfo_getset(String email, String imgUri, String name, String nickname, String pw,String uid){
        this.email = email;
        this.imgUri = imgUri;
        this.name =name;
        this.nickname = nickname;
        this.pw =pw;
        this.uid = uid;
    }

    public int getMeal() {
        return meal;
    }

    public void setMeal(int meal) {
        this.meal = meal;
    }

    public int getPetcode() {
        return petcode;
    }

    public void setPetcode(int petcode) {
        this.petcode = petcode;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


}
