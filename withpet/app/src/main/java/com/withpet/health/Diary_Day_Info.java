package com.withpet.health;

import java.util.ArrayList;

public class Diary_Day_Info {
    private String date;
    private ArrayList<diary> diaryArrayList = new ArrayList<diary>();
    private ArrayList<String>diaryKeyArrayList = new ArrayList<String>(); //키값 리스트

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<diary> getDiaryArrayList() {
        return diaryArrayList;
    }

    public void addDiaryArrayList(diary Diary) {
        diaryArrayList.add(Diary);
    }

    public void addDiaryKeyArrayList(String key) {
        diaryKeyArrayList.add(key);
    }
    public ArrayList<String> getDiaryKeyArrayList(){ return diaryKeyArrayList;}
}
