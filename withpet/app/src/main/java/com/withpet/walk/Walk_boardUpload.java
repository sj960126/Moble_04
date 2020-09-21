package com.withpet.walk;

public class Walk_boardUpload {
    // 임시로 만든 데베 업로드값임 더 추가해야함!!
    private String walkboard_title;
    private String walkboard_content;
    private int walkboard_nb;
    private double lat0;
    private double long0;
    private double lat1;
    private double long1;
    private double lat2;
    private double long2;
    private double lat3;
    private double long3;


    public Walk_boardUpload(String walkboard_title, String walkboard_content, int walkboard_nb, double lat0, double long0,double lat1, double long1,double lat2, double long2,double lat3, double long3){
        this.walkboard_title = walkboard_title;
        this.walkboard_content = walkboard_content;
        this.walkboard_nb = walkboard_nb;
        this.lat0 = lat0;
        this.long0 = long0;
        this.lat1 = lat1;
        this.long1 = long1;
        this.lat2 = lat2;
        this.long2 = long2;
        this.lat3 = lat3;
        this.long3 = long3;

    }
    public double getLat0() {
        return lat0;
    }

    public void setLat0(double lat0) {
        this.lat0 = lat0;
    }

    public double getLong0() {
        return long0;
    }

    public void setLong0(double long0) {
        this.long0 = long0;
    }

    public double getLat1() {
        return lat1;
    }

    public void setLat1(double lat1) {
        this.lat1 = lat1;
    }

    public double getLong1() {
        return long1;
    }

    public void setLong1(double long1) {
        this.long1 = long1;
    }

    public double getLat2() {
        return lat2;
    }

    public void setLat2(double lat2) {
        this.lat2 = lat2;
    }

    public double getLong2() {
        return long2;
    }

    public void setLong2(double long2) {
        this.long2 = long2;
    }

    public double getLat3() {
        return lat3;
    }

    public void setLat3(double lat3) {
        this.lat3 = lat3;
    }

    public double getLong3() {
        return long3;
    }

    public void setLong3(double long3) {
        this.long3 = long3;
    }


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
