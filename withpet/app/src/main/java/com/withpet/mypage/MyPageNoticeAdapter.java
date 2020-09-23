package com.withpet.mypage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.withpet.*;
import com.withpet.main.*;
import com.withpet.newsfeed.*;
import java.util.ArrayList;

//종희
public class MyPageNoticeAdapter extends RecyclerView.Adapter<MyPageNoticeAdapter.MyPageNoticeViewHolder> {

    private ArrayList<Feed> myfeed;
    private TransUser user;
    private Context context; //선택한 activity action 내용

    //생성자
    public MyPageNoticeAdapter(ArrayList<Feed> myfeed, Context context, TransUser tuser) {
        this.myfeed = myfeed;
        this.context = context;
        user = tuser;
    }

    //리스트뷰아이템 생성 실행되는 곳
    @NonNull
    @Override
    public MyPageNoticeAdapter.MyPageNoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView view = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.mypagenotice_item, parent, false);
        MyPageNoticeAdapter.MyPageNoticeViewHolder holder = new MyPageNoticeAdapter.MyPageNoticeViewHolder(view);
        return holder;
    }

    //매칭
    //이미지 서버에서 이미지 불러오기
    //코드내용 줄여보기
    @Override
    public void onBindViewHolder(@NonNull MyPageNoticeAdapter.MyPageNoticeViewHolder holder, int position) {
        CardView cardView = holder.layout.findViewById(R.id.myPageCv);
        ImageView iv_noticeitem = holder.layout.findViewById(R.id.mypageNoticeIv_item);
        /*
        if(myfeed.get(position).getId().equals(user.getNickname())){
            Glide.with(context).load(myfeed.get(position).getImgUrl()).override(800).into(iv_noticeitem);
            iv_noticeitem.setTag(R.integer.MyNoticeItem, myfeed.get(position));
        }*/
        Glide.with(context).load(myfeed.get(position).getImgUrl()).override(800).into(iv_noticeitem);
        iv_noticeitem.setTag(R.integer.MyNoticeItem, myfeed.get(position));
        iv_noticeitem.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return (myfeed != null ? myfeed.size():0);
    }

    //viewHolder = listItem
    public class MyPageNoticeViewHolder extends RecyclerView.ViewHolder {
        public CardView layout;
        public MyPageNoticeViewHolder(CardView l) {
            super(l);
            layout = l;
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.mypageNoticeIv_item:
                    Feed feed = (Feed)view.getTag(R.integer.MyNoticeItem);
                    Toast.makeText(context, "nick : "+ feed.getUid() + ", context : " + feed.getContext()+ ", date : "+ feed.getDate(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
