package com.withpet.Search;

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
import com.withpet.main.MainActivity;
import com.withpet.main.TransUser;
import com.withpet.main.User;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Search_findAdapter extends RecyclerView.Adapter<Search_findAdapter.FindViewHolder>{
    private ArrayList<User> userlist;
    private Context mContext;

    public Search_findAdapter(Context context, ArrayList<User> userlist) {
        this.userlist = userlist;
        this.mContext = context;
    }
    @NonNull
    @Override
    public FindViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.finduser_item, parent, false);
        FindViewHolder holder = new FindViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FindViewHolder holder, int position) {
        holder.getUserNickname().setText(userlist.get(position).getNickname());
        Glide.with(holder.itemView).load(userlist.get(position).getImgUrl()).override(800).into(holder.userProfilePhoto);
        holder.itemView.setTag(R.integer.userinfo, userlist.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransUser selectuser = new TransUser((User)view.getTag(R.integer.userinfo));
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra("userinfo",selectuser);
                intent.putExtra("frag",3);
                mContext.startActivity(intent);
                ((Activity)holder.itemView.getContext()).finish();  // 유저검색 창 종료
            }
        });
    }

    @Override
    public int getItemCount() { return (userlist != null) ? userlist.size() : 0 ;  }

    public class FindViewHolder extends RecyclerView.ViewHolder{
       private CircleImageView userProfilePhoto;
       private TextView userNickname;
        public FindViewHolder(@NonNull View itemView) {
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
