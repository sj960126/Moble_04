package com.withpet.walk;

public class Walk_boardUpload {
    // 임시로 만든 데베 업로드값임 더 추가해야함!!
    private String walkboard_title;
    private String walkboard_content;
    private int walkboard_nb;

    public Walk_boardUpload(){

    }
    public Walk_boardUpload(String walkboard_title, String walkboard_content, int walkboard_nb){
        this.walkboard_title = walkboard_title;
        this.walkboard_content = walkboard_content;
        this.walkboard_nb = walkboard_nb;
    }

    public int getWalkboard_nb() {
        return walkboard_nb;
    }

    public void setWalkboard_nb(int walkboard_nb) {
        this.walkboard_nb = walkboard_nb;
    }

    public Walk_boardUpload(String walkboard_title, String walkboard_content){
        this.walkboard_title = walkboard_title;
        this.walkboard_content = walkboard_content;
    }

    public String getWalkboard_title() {
        return walkboard_title;
    }

    public void setWalkboard_title(String walkboard_title) {
        this.walkboard_title = walkboard_title;
    }

    public String getWalkboard_content() {
        return walkboard_content;
    }

    public void setWalkboard_content(String walkboard_content) {
        this.walkboard_content = walkboard_content;
    }
}
