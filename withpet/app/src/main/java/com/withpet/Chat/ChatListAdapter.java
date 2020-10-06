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

// 종희
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<ChattingRoom> chattingroomList;  // 채팅내역을 담은 리스트
    private ArrayList<User> userlist;  // 채팅내역을 담은 리스트
    private String meid;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title;
        public TextView tv_msg;
        public TextView tv_newchatnum;
        private CircleImageView iv_profilephoto;
        public View rootView;
        public MyViewHolder(@NonNull View v) {
            super(v);
            tv_title = v.findViewById(R.id.chatListTv_title);
            tv_msg = v.findViewById(R.id.chatListTv_Content);
            iv_profilephoto = v.findViewById(R.id.chatListIv_profile);
            tv_newchatnum = v.findViewById(R.id.chatListTv_NewChatNum);
            rootView = v;
        }
    }
    public ChatListAdapter(Context context, ArrayList<ChattingRoom> chattingList, ArrayList<User> userlist, String meid) {
        mContext = context;
        this.chattingroomList = chattingList;
        this.userlist = userlist;
        this.meid = meid;

    }
    // 뷰홀더 생성(리스트뷰의 아이템 한줄에 해당)
    @Override
    public ChatListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.chatlist_item, parent, false);
        ChatListAdapter.MyViewHolder holder = new ChatListAdapter.MyViewHolder(v);
        return holder;
    }
    // 리스트 뷰 아이템의 내용 설정
    @Override
    public void onBindViewHolder(ChatListAdapter.MyViewHolder holder, int position) {
        boolean userfind = false;
        ChattingRoom chattingroominfo = chattingroomList.get(position);
        for(User userdata : userlist) {
            // 모든 user 정보와 로그인한 사람의 대화방 상대 유저정보를 비교해 같은 사람이 있으면 대화방정보(프로필 사진, 닉네임) 리스트를 작성
            // !userdata.getUid().equals(meid) 사용이유 : 이미 어댑터에 넘긴 채팅방 리스트는 로그인한 유저가 속한 채팅방들이다.
            // 따라서 채팅 상대를 찾기 위해 유저 전체 정보에서 비교하고, 그 과정에서 로그인 유저 정보는 건너 뛰어야함
            if(chattingroominfo.getChatroomname().contains(userdata.getUid()) && !userdata.getUid().equals(meid)){
                User user = userdata;
                holder.tv_title.setText(user.getNickname());    // 대화상대 닉네임 출력
                holder.tv_msg.setText(chattingroominfo.getChattingList().get(chattingroominfo.getChattingList().size()-1).getContent());    // 대화상대와의 마지막 채팅 내용 출력
                Glide.with(holder.rootView).load(user.getImgUrl()).override(800).into(holder.iv_profilephoto);  // 대화상대 프로필 사진 출력
                holder.itemView.setTag(R.integer.userinfo, user);
                userfind = true;
                // Application 불러옴
                // mContext.getApplicationContext() : 액티비티에서 getApplication()과 같은 결과
                ChattingRoom getctr = ((NotifyApplication)mContext.getApplicationContext()).getChattingroom(chattingroominfo.getChatroomname());
                // 현재 채팅방이름으로 된 정보가 어플리케이션에 있으면 실행
                if(getctr != null){
                    // chattingroominfo.getChildcount() : 실시간 채팅방의 채팅내역 개수
                    // getctr.getChildcount() : 어플리케이션에 저장되어 있는 채팅방의 채팅내역 개수(맨 처음 로딩했을 때의 채티방 채팅 내역 개수)
                    long newchatnum = chattingroominfo.getChildcount() - getctr.getChildcount();    // 새로운 채팅 개수
                    Log.i("어플리케이션 카운트", ""+getctr.getChildcount());
                    Log.i("스냅샷 카운트", ""+chattingroominfo.getChildcount());
                    if(newchatnum != 0){
                        holder.tv_newchatnum.setText(""+newchatnum);
                    }
                    else{
                        holder.tv_newchatnum.setText("");
                    }
                }
                // 브레이크 사용 이유 : 1개의 채팅방 상대 유저는 무조건 1명, 전체 유저 정보 중 1개만 매칭되기 때문에 모든 for문을 다 확인할 필요가 없음
                break;
            }
        }

        // 상대 유저 정보가 없을 때 (상대 유저가 회원 탈퇴한 경우)
        if(!userfind){
            // 기본 유저 정보를 저장하기 위한 객체 생성
            User defualtuser = new User();
            // 기본 유저 이미지 저장
            defualtuser.setImgUrl("https://firebasestorage.googleapis.com/v0/b/practice-d557f.appspot.com/o/Profile%2Fuserdefault.png?alt=media&token=bc81fd8f-be4d-40a5-a76d-268ad90085af");
            for(Chat chat : chattingroominfo.getChattingList()){
                if(!chat.getUid().equals(meid)){
                    // 회원탈퇴 했기 때문에 유저리스트에는 채팅내역에 남아있는 id로 회원정보를 불러 올 수 없음, 채팅방 액티비티에 넘겨줄 유저 정보를 불러올 수 없음
                    // 따라서 채팅 내역에 남아있는 uid를 기본 유저 정보 uid에 저장
                    defualtuser.setUid(chat.getUid());
                    break;
                }
            }
            defualtuser.setNickname("알 수 없음");  // 기본 유저의 닉네임 설정
            holder.itemView.setTag(R.integer.userinfo, defualtuser);    // holder 레이아웃에 유저정보 저장(해당 홀더에 매칭되는 유저 정보를 식별하기 위함)
            holder.tv_title.setText(defualtuser.getNickname());    // 대화상대 닉네임 출력
            holder.tv_msg.setText(chattingroominfo.getChattingList().get(chattingroominfo.getChattingList().size()-1).getContent());    // 대화상대와의 마지막 채팅 내용 출력
            Glide.with(holder.rootView).load(defualtuser.getImgUrl()).override(800).into(holder.iv_profilephoto);  // 대화상대 프로필 사진 출력
        }

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // 해당 대화방 클릭 시 채팅방으로 이동
                Intent chattingintent = new Intent(v.getContext(), ChattingActivity.class);
                TransUser tuser = new TransUser((User)v.getTag(R.integer.userinfo));    // 홀더 레이아웃에 저장했던 유저 정보를 가져옴.
                chattingintent.putExtra("Opponent", tuser);
                v.getContext().startActivity(chattingintent);
            }
        });

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
