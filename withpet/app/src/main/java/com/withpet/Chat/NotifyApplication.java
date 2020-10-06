package com.withpet.Chat;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.withpet.R;
import com.withpet.main.*;

import java.util.ArrayList;

public class NotifyApplication extends Application {
    private String channel_id ="withpet_channel";
    private ArrayList<User> userlist;
    private ArrayList<ChattingRoom> chattingroomlist;
    NotificationManager notificationManager;
    public String enterChattingRoom;
    public boolean isStart = false;


    @Override
    public void onCreate() {
        userlist = new ArrayList<User>();
        chattingroomlist = new ArrayList<ChattingRoom>();
        startService();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot userdata : snapshot.getChildren()){
                    userlist.add(userdata.getValue(User.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        super.onCreate();
    }

    public  void startService(){
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
        isStart = true;
    }

    public String getChannel_id(){
        return channel_id;
    }
    public NotificationManager getNotificationManager(){
        return notificationManager;
    }
    public ArrayList<User> getUserlist() {return userlist;}

    public User getUser(String userid){
        for(User user : userlist){
            if(user.getUid().equals(userid)){
                return user;
            }
        }
        return null;
    }

    public void setEnterChattingRoom(String chattingRoom){
       this.enterChattingRoom =  chattingRoom;
    }
    public String getEnterChattingRoom() { return enterChattingRoom;}

    public ArrayList<ChattingRoom> getChattingroomlist() {
        return chattingroomlist;
    }

    public void setChattingroomlist(ArrayList<ChattingRoom> chattingroomlist) {
        this.chattingroomlist = chattingroomlist;
    }

    public ChattingRoom getChattingroom(String chattingroomname){
        for(ChattingRoom ctr : chattingroomlist){
            if(ctr.getChatroomname().equals(chattingroomname)){
                return ctr;
            }
        }
        return null;
    }

    public void refreshChattingChildCount(String chattingroomname, long childcount){
        // 내가 속한 채팅방의 정보 반복문으로 돌림
        for(ChattingRoom ctr : chattingroomlist){
            // 내가 속한 채팅방의 정보 중 매개변수로 받은 채팅방의 이름과 같은 채팅방 찾기
            if(ctr.getChatroomname().equals(chattingroomname)){
                // 매개변수로 받은 채팅방의 이름과 같은 채팅방의 childcount 수 갱신
                ctr.setChildcount(childcount);
            }
        }
    }
}
