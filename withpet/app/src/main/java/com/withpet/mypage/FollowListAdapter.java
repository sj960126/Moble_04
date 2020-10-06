package com.withpet.mypage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.withpet.R;
import com.withpet.main.MainActivity;
import com.withpet.main.TransUser;
import com.withpet.main.User;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowListAdapter extends RecyclerView.Adapter<FollowListAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<User> followList;  // 채팅내역을 담은 리스트
    private String meid;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tv_nickname;
        public CircleImageView iv_profilephoto;
        public Button btn_delete;
        public MyViewHolder(@NonNull View v) {
            super(v);
            tv_nickname = v.findViewById(R.id.followTv_NickName);
            iv_profilephoto = v.findViewById(R.id.followListIv_profile);
            btn_delete = v.findViewById(R.id.followListBtn_Delete);
        }

    }
    public FollowListAdapter(Context context, ArrayList<User> followList) {
        mContext = context;
        this.followList = followList;
    }

    // 뷰홀더 생성(리스트뷰의 아이템 한줄에 해당)
    @Override
    public FollowListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.followlist_item, parent, false);
        FollowListAdapter.MyViewHolder holder = new FollowListAdapter.MyViewHolder(v);
        return holder;
    }
    // 리스트 뷰 아이템의 내용 설정
    @Override
    public void onBindViewHolder(FollowListAdapter.MyViewHolder holder, int position) {
        User followuserinfo = followList.get(position);
        holder.tv_nickname.setText(followuserinfo.getNickname());
        Glide.with(holder.itemView).load(followuserinfo.getImgUrl()).override(800).into(holder.iv_profilephoto);
        holder.btn_delete.setTag(R.integer.userinfo, followuserinfo.getUid());
        holder.tv_nickname.setTag(R.integer.userinfo, followuserinfo);
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Follow").child(firebaseUser.getUid());
                databaseReference.child((String)v.getTag(R.integer.userinfo)).removeValue();
            }
        });
        holder.tv_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransUser tuser = new TransUser((User)v.getTag(R.integer.userinfo));
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.putExtra("frag", 3);
                intent.putExtra("userinfo", tuser);
                mContext.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() { return followList != null ? followList.size() : 0 ;   }

    // 어댑터에 내용 추가
    public void addfollowUser(User user) {
        followList.add(user);
        notifyItemInserted(followList.size()-1); //갱신, position인수는 어디에 넣을지를 작성해야함, 리스트 마지막에 추가가되기 때문에 사이즈에서 -1
    }

}
