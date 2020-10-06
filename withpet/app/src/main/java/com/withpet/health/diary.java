package com.withpet.health;

public class diary {
    private String kind; //종류
    private String eat; // 사료량
    private String time; // 날짜
    private String brand; //브랜드
    diary(){

    }
    diary(String kind,String eat,String time,String brand){
        this.kind=kind;
        this.eat=eat;
        this.time=time;
        this.brand=brand;
    }
    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEat() {
        return eat;
    }

    public void setEat(String eat) {
        this.eat = eat;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
