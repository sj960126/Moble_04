package com.withpet.Chat;

public class Chat {
    private String uid;
    private String content;
    public Chat(){

    }
    public Chat(String uid, String content){
        this.uid = uid;
        this.content = content;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
