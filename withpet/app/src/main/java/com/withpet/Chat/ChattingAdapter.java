package com.withpet.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.style.Circle;
import com.withpet.*;
import com.withpet.main.*;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChattingAdapter extends RecyclerView.Adapter<ChattingAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Chat> chattingList;  // 채팅내역을 담은 리스트
    private String me;  // 로그인한 사람의 uid를 담는 변수
    private User other; // 채팅 상대의 정보를 담은 객체
    private String previousId;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tv_nickname;
        public TextView tv_msg;
        private CircleImageView iv_profilephoto;
        public View rootView;
        public MyViewHolder(@NonNull View v) {
            super(v);
            tv_nickname = v.findViewById(R.id.chattingTv_Nickname);
            tv_msg = v.findViewById(R.id.chattingTv_content);
            iv_profilephoto = v.findViewById(R.id.chattingIv_profile);
            rootView = v;
        }

    }
    public ChattingAdapter(Context context, ArrayList<Chat> chattingList, String me, User other) {
        mContext = context;
        this.chattingList = chattingList;
        this.me = me;
        this.other = other;

    }
    // Create new views (invoked by the layout manager)
    @Override
    public ChattingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_item, parent, false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Chat chat = chattingList.get(position);
        if(chat.getUid().equals(this.me)){
            holder.tv_nickname.setVisibility(View.GONE);
            holder.iv_profilephoto.setVisibility(View.GONE);
            holder.tv_msg.setText(chat.getContent());
            holder.tv_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        }
        else{
            holder.tv_nickname.setText(other.getNickname());
            holder.tv_msg.setText(chat.getContent());
            if(previousId != null && previousId.equals(other.getUid())){
                holder.iv_profilephoto.setVisibility(View.INVISIBLE);
            }
            else{
                holder.iv_profilephoto.setVisibility(View.VISIBLE);
                Glide.with(holder.rootView).load(other.getImgUrl()).override(800).into(holder.iv_profilephoto);
            }

            holder.tv_nickname.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holder.tv_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }
        previousId = chat.getUid();
    }
    @Override
    public int getItemCount() {  return chattingList != null ? chattingList.size() : 0 ;  }

    public Chat getChat(int position) {
        return chattingList != null ? chattingList.get(position) : null;
    }

    public void addChat(Chat chat) {
        chattingList.add(chat);
        notifyItemInserted(chattingList.size()-1); //갱신, position인수는 어디에 넣을지를 작성해야함, 리스트 마지막에 추가가되기 때문에 사이즈에서 -1
    }


}
