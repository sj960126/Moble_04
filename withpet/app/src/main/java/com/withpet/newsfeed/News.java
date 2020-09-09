package com.withpet.newsfeed;

public class News {
    String id;
    String imgUrl;
    String context;

    News(){}

    News(String id,String imgUrl, String context){
        this.id = id;
        this.imgUrl = imgUrl;
        this.context = context;
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
}
