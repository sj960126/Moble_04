package com.withpet.newsfeed;

public class Feed {
    String uid;
    String imgUrl;
    String context;
    String date;
    String newsName;

    Feed(){}
    Feed(String uid, String imgUrl, String context, String newsName, String date){
        this.uid = uid;
        this.imgUrl = imgUrl;
        this.context = context;
        this.newsName = newsName;
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String id) {
        this.uid = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getNewsName() {
        return newsName;
    }

    public void setNewsName(String newsName) {
        this.newsName = newsName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
