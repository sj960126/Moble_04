package com.withpet.Chat;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.withpet.R;
import com.withpet.main.TransUser;
import com.withpet.main.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<ChattingRoom> chattingroomList;  // 채팅내역을 담은 리스트
    private ArrayList<User> userlist;  // 채팅내역을 담은 리스트
    private String meid;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tv_title;
        public TextView tv_msg;
        private CircleImageView iv_profilephoto;
        public View rootView;
        public MyViewHolder(@NonNull View v) {
            super(v);
            tv_title = v.findViewById(R.id.chatListTv_title);
            tv_msg = v.findViewById(R.id.chatListTv_Content);
            iv_profilephoto = v.findViewById(R.id.chatListIv_profile);
            rootView = v;
        }

    }
    public ChatListAdapter(Context context, ArrayList<ChattingRoom> chattingList, ArrayList<User> userlist, String meid) {
        mContext = context;
        this.chattingroomList = chattingList;
        this.userlist = userlist;
        this.meid = meid;

    }
    // Create new views (invoked by the layout manager)
    @Override
    public ChatListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.chatlist_item, parent, false);
        ChatListAdapter.MyViewHolder holder = new ChatListAdapter.MyViewHolder(v);
        return holder;
    }
    @Override
    public void onBindViewHolder(ChatListAdapter.MyViewHolder holder, int position) {
        ChattingRoom chattingroominfo = chattingroomList.get(position);
        for(User userdata : userlist){
            // 모든 user 정보와 로그인한 사람의 대화방 상대 유저정보를 비교해 같은 사람이 있으면 대화방정보(프로필 사진, 닉네임) 리스트를 작성
            if(chattingroominfo.getChatroomname().contains(userdata.getUid()) && !userdata.getUid().equals(meid)){
                final User user = userdata;
                holder.tv_title.setText(user.getNickname());
                Glide.with(holder.rootView).load(user.getImgUrl()).override(800).into(holder.iv_profilephoto);
                holder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {   // 해당 대화방 클릭 시 채팅방으로 이동
                        Intent chattingintent = new Intent(v.getContext(), ChattingActivity.class);
                        TransUser tuser = new TransUser(user);
                        chattingintent.putExtra("Opponent", tuser);
                        v.getContext().startActivity(chattingintent);
                    }
                });
                break;
            }
        }

    }
    @Override
    public int getItemCount() {  return chattingroomList != null ? chattingroomList.size() : 0 ;  }

    // 어댑터에 내용 추가
    public void addChatRoom(ChattingRoom chattingroom) {
        chattingroomList.add(chattingroom);
        notifyItemInserted(chattingroomList.size()-1); //갱신, position인수는 어디에 넣을지를 작성해야함, 리스트 마지막에 추가가되기 때문에 사이즈에서 -1
    }
    // 유저정보 내용 추가
    public void addUserinfo(User user) {
        userlist.add(user);
    }
}