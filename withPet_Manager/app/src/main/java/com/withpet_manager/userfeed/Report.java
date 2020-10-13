package com.withpet_manager.userfeed;

public class Report {
    String uid;
    String category;
    String feedName;
    String context;
    String title;
    String date;
    String state;

    Report(){}
    Report(String uid, String category, String feedName, String title, String context, String date, String state){
        this.uid = uid;
        this.category =category;
        this.feedName = feedName;
        this.title = title;
        this.context = context;
        this.date = date;
        this.state =state;
    }

    Report(String uid, String category, String title, String context, String date){
        this.uid = uid;
        this.category =category;
        this.title = title;
        this.context = context;
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
