package com.example.withpet_manager;

public class Userinfo_getset {
    private String email;
    private String imgUri;
    private String name;
    private String nickname;
    private String pw;
    private String uid;

    Userinfo_getset(){

    }
    Userinfo_getset(String imgUri, String name, String nickname){
        this.imgUri = imgUri;
        this.name = name;
        this.nickname = nickname;
    }

    Userinfo_getset(String email, String imgUri, String name, String nickname, String pw,String uid){
        this.email = email;
        this.imgUri = imgUri;
        this.name =name;
        this.nickname = nickname;
        this.pw =pw;
        this.uid = uid;
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
