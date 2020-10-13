package com.withpet_manager.userinfo;

public class Reply {
    String context;
    String uid;
    String replyName;
    String boardName;
    String date;

    Reply(){
    }

    Reply(String replyName, String uid, String boardName, String context, String date) {
        this.replyName = replyName;
        this.boardName = boardName;
        this.context = context;
        this.uid = uid;
        this.date = date;
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

    public String getReplyName() {
        return replyName;
    }

    public void setReplyName(String replyName) {
        this.replyName = replyName;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
