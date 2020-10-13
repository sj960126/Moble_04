package com.withpet.Chat;

import android.content.Context;
import android.util.Log;
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
    int count = 0;

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
        Log.i("홀더 포지션",""+holder.getAdapterPosition());

        Chat chat = chattingList.get(position);
        if(chat.getUid().equals(this.me)){
           setMyChat(holder);   // 나의 채팅 레이아웃 설정
           holder.tv_msg.setText(chat.getContent());

        }
        else{
            setOtherChat(holder);   // 상대방 채팅 레이아웃 설정
            holder.tv_nickname.setText(other.getNickname());
            holder.tv_msg.setText(chat.getContent());
            Glide.with(holder.rootView).load(other.getImgUrl()).override(800).into(holder.iv_profilephoto);

        }
        previousId = chat.getUid();
    }
    @Override
    public int getItemCount() {  return chattingList != null ? chattingList.size() : 0 ;  }

    //채팅 내용 추가
    public void addChat(Chat chat) {
        chattingList.add(chat);
        notifyItemInserted(chattingList.size()-1); //갱신, position인수는 어디에 넣을지를 작성해야함, 리스트 마지막에 추가가되기 때문에 사이즈에서 -1
    }

    //채팅 내역 레이아웃 설정(내 채팅)
    public void setMyChat(MyViewHolder holder){
        holder.tv_nickname.setVisibility(View.GONE);                        // 채팅 레이아웃의 닉네임(Textview 레이아웃) 지움
        holder.iv_profilephoto.setVisibility(View.GONE);                    // 채팅 레이아웃의 프로필 사진 부분 지움
        holder.tv_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);       // 채팅 레이아웃 메시지 부분 오른쪽 정렬
    }
    // 채팅 내역 레이아웃 설정(상대방)
    public void setOtherChat(MyViewHolder holder){
        int position = holder.getAdapterPosition();
        if(chattingList.get(position).getUid().equals(chattingList.get(position-1).getUid())){
            holder.iv_profilephoto.setVisibility(View.INVISIBLE);                     // 내 채팅 레이아웃으로 사용됐던 부분이 상대방 채팅의 레이아웃으로 사용될 수 있어서 visible 다시 사용
        }
        else{
            holder.iv_profilephoto.setVisibility(View.VISIBLE);                     // 내 채팅 레이아웃으로 사용됐던 부분이 상대방 채팅의 레이아웃으로 사용될 수 있어서 visible 다시 사용
        }
        holder.tv_nickname.setVisibility(View.VISIBLE);                         // 내 채팅 레이아웃으로 사용됐던 부분이 상대방 채팅의 레이아웃으로 사용될 수 있어서 visible 다시 사용

        holder.tv_nickname.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);    // 텍스트 왼쪽정렬
        holder.tv_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);         // 텍스트 왼쪽정렬
    }

}
