package com.withpet_manager.Client;

public class Client {
    private  String category;
    private  String context;
    private  String date;
    private  String feedName;
    private  String title;
    private  String uid;
    public Client(){}
    public Client(String category , String context , String date , String feedName , String title , String uid){
        this.category = category;
        this.context = context;
        this.date = date;
        this.feedName =feedName;
        this.title = title;
        this.uid = uid;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
