package com.withpet.Chat;

import java.util.ArrayList;
import com.withpet.main.*;

//채팅방 정보를 저장하는 클래스 (채팅방 이름, 해당 채팅방의 채팅내역)
public class ChattingRoom {
    private String chatroomname;
    private ArrayList<Chat> chattingList = new ArrayList<Chat>();

    public String getChatroomname() {
        return chatroomname;
    }

    public void setChatroomname(String chatroomname) {
        this.chatroomname = chatroomname;
    }

    public ArrayList<Chat> getChattingList() {
        return chattingList;
    }

    public void setChattingList(ArrayList<Chat> chattingList) {
        this.chattingList = chattingList;
    }
    public void addChattingList(Chat chat){
        chattingList.add(chat);
    }
}
