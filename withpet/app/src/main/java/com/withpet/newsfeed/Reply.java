package com.withpet.newsfeed;

public class Reply {
    String context;
    String uid;

    Reply(){
    }

    Reply( String context, String uid) {
        this.context = context;
        this.uid = uid;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
