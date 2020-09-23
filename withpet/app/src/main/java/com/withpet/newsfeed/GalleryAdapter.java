package com.withpet.newsfeed;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.withpet.*;
import com.withpet.main.User;

import java.util.ArrayList;

//리사이클러뷰 어댑터 함수 _ 개발가이드에 나와있음
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {
    private ArrayList<String> mDataset; //사진은 경로로 저장하기 때문에 String 타입의 ArrayList 사용
    private Activity activity; //어댑터는 activity가 존재하지 않음! 현재 어댑터가 실행되는 activity의 정보를 가져오기 위해 선언!
    private Intent intentw;
    private int clickposition = -1;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView layout;
        public MyViewHolder(CardView l) {
            super(l);
            layout = l;
        }
    }

    public GalleryAdapter(Activity activity, ArrayList<String> myDataset) {
        this.activity = activity;
        mDataset = myDataset;
    }

    // Create new views
    @Override
    public GalleryAdapter.MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.news_galleryitems, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(cardView);
        return viewHolder;
    }

    //어댑터에서 가장 중요한 함수 _ 데이터 매칭
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        //news_galleryitems 레이아웃은 cardview 안에 ImageView가 포함된 형식
        //하나의 사진마다 이 둘이 한세트로 생성됨!
        CardView cardView = holder.layout.findViewById(R.id.mainCv);
        ImageView iv_galleryItem = holder.layout.findViewById(R.id.mainIv_gal);

        //사용자의 갤러리에서 가져온 갤러리 하나하나 불러오는 이미지라이브러리
        //load 이미지 경로로 사진을 불러옴!
        //override 화소설정
        //into 어느위치에 사진을 나오게할지
        Glide.with(activity).load(mDataset.get(position)).override(300).into(iv_galleryItem);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView iv_choice = (ImageView) activity.findViewById(R.id.mainwIv_choice);
                //mDataset.get(holder.getAdapterPosition()) : 상수
                Glide.with(activity).load(mDataset.get(holder.getAdapterPosition())).override(800).into(iv_choice);
                //다음페이지에 선택한 값을 전달하기 위해 intent 작성
                intentw = new Intent(activity, FeedWriteActivity.class);
                intentw.putExtra("imgId",mDataset.get(holder.getAdapterPosition()));
                clickposition = holder.getAdapterPosition();
            }
        });

        Button btn_next  = (Button) activity.findViewById(R.id.mainwBtn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 메인에서 갤러리 사진 선택 액티비티 띄웠을 때
                if((int)view.getTag() == R.integer.newsRequestcode){
                    //로그인한 회원의 닉네임 찾기
                    FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
                    DatabaseReference dbRef= firebaseDatabase.getReference("User");
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                    Query findUserNickName =dbRef.orderByChild("uid").equalTo(firebaseUser.getUid());

                    findUserNickName.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                for(DataSnapshot user : snapshot.getChildren()){
                                    //Log.i("user uid",""+user);
                                    User tmp = user.getValue(User.class);
                                    //해당 페이지로 이동 + 닉네임 전달
                                    intentw.putExtra("loginUserNickname", tmp.getNickname());
                                    activity.startActivity(intentw);
                                    activity.finish();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.i("query error :: ", "fail");
                        }
                    });
                }
                // 프로필 수정에서 갤러리 사진 선택 액티비티 띄웠을 때
                else if((int)view.getTag() == R.integer.profileModifyRequestcode){
                    if(clickposition != -1){
                        Intent intent = activity.getIntent();
                        intent.putExtra("choiceimgId",mDataset.get(clickposition));
                        activity.setResult(activity.RESULT_OK,intent);
                        activity.finish();
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

