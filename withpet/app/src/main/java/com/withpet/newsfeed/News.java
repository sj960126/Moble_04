package com.withpet.newsfeed;

public class News {
    String id;
    String imgUrl;
    String context;
    String date;
    String newsName;

    News(){}
    News(String id, String imgUrl, String context, String newsName, String date){
        this.id = id;
        this.imgUrl = imgUrl;
        this.context = context;
        this.newsName = newsName;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
