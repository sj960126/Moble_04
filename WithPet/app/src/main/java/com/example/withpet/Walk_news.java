package com.example.withpet;

public class Walk_news {
    String title;
    String context;
    int imgUri;

    Walk_news (String title, String context, int imgUri){
        this.title = title;
        this.context = context;
        this.imgUri = imgUri;
    }

    public String getTitle() { return title;}

    public void setTitle(String title){ this.title = title;}

    public String getContext() { return context;}

    public void setContext(String context){ this.context = context;}

    public int getImgUri() { return imgUri;}

    public void setImgUri() { this.imgUri = imgUri;}
}
