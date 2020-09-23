package com.withpet.Chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.withpet.R;
import com.withpet.main.*;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

//종희
public class FindUserAdapter extends RecyclerView.Adapter<FindUserAdapter.FindUserViewHolder> {
    private ArrayList<User> userlist;
    private Context mContext;

    public FindUserAdapter(Context context, ArrayList<User> userlist) {
        this.userlist = userlist;
        this.mContext = context;
    }

    @NonNull
    @Override
    public FindUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.finduser_item, parent, false);
        FindUserViewHolder holder = new FindUserViewHolder(view);
        return holder;
    }

    @Override   // 입력한 닉네임이 들어가는 유저 리스트 출력
    public void onBindViewHolder(@NonNull final FindUserViewHolder holder, int position) {
        holder.getUserNickname().setText(userlist.get(position).getNickname());
        Glide.with(holder.itemView).load(userlist.get(position).getImgUrl()).override(800).into(holder.getUserProfilePhoto());
        holder.itemView.setTag(R.integer.userinfo, userlist.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                //검색해서 나온 유저 리스트 목록 클릭 이벤트
                TransUser choiceuser = new TransUser((User)view.getTag(R.integer.userinfo));
                Intent intent = new Intent(view.getContext(), ChattingActivity.class);
                intent.putExtra("Opponent", choiceuser);
                mContext.startActivity(intent);                     // 채팅방 실행
                ((Activity)holder.itemView.getContext()).finish();  // 유저검색 창 종료
            }
        });
    }

    @Override
    public int getItemCount() { return (userlist != null) ? userlist.size() : 0 ;  }

    class FindUserViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView userProfilePhoto;
        private TextView userNickname;
        public FindUserViewHolder(@NonNull View itemView) {
            super(itemView);
            userProfilePhoto = itemView.findViewById(R.id.finduseritemIv_profile);  // 유저 검색 리스트의 레이아웃 중 이미지뷰를 홀더에 설정
            userNickname = itemView.findViewById(R.id.finduseritemTv_nickname);     // 유저 검색 리스트의 레이아웃 중 닉네임이 적히는 텍스트 뷰를 홀더에 설정
        }

        public CircleImageView getUserProfilePhoto() {
            return userProfilePhoto;
        }

        public TextView getUserNickname() {
            return userNickname;
        }
    }
}
