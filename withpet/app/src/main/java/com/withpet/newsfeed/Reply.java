package com.withpet.newsfeed;

public class Reply {
    String context;
    String uid;
    String replyName;
    String boardName;

    Reply(){
    }

    Reply(String replyName, String uid, String boardName, String context) {
        this.replyName = replyName;
        this.boardName = boardName;
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

}
