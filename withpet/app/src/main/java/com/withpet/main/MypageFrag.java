package com.withpet.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.withpet.iot.*;
import com.withpet.mypage.*;
import com.withpet.newsfeed.*;
import com.withpet.health.*;
import com.withpet.walk.*;
import com.withpet.*;

import java.util.ArrayList;

//Mypage page
public class MypageFrag extends Fragment {

    private View rootview;
    private RecyclerView list;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<News> myfeed;
    private FirebaseDatabase db;
    private DatabaseReference dbreference;
    final  int requestcode = 1001;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_mypage,container,false);
        Button btn1 = (Button) rootview.findViewById(R.id.mypageBtn_setting);
        btn1.setBackgroundResource(R.drawable.iconsetting);

        list = rootview.findViewById(R.id.myPageListview_mynotice);
        list.setHasFixedSize(true); //리사이클러뷰 기존 성능 강화
        layoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(layoutManager);
        myfeed = new ArrayList<>(); //유저 객체를 담을 (어댑터쪽으로)

        db = FirebaseDatabase.getInstance(); //파이어베스 데이터베이스 연동
        dbreference = db.getReference("Feed");//연동한 DB의 테이블 연결
        dbreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                myfeed.clear(); //기존 배열가 존재하지 않게 초기화 방지차원
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    //반복문으로 데이터 리스트를 추출
                    News news = snapshot.getValue(News.class); //만들어뒀던 news 객체에 데이터를 담음

                    //Log.i("tIIIIIDDDDDDD ::", ""+news.imgUrl);
                    //Log.i("tqtqtqtqtqtq ::", ""+news.id);
                    myfeed.add(news); //담은 데이터들을 배열리스터에 넣고 리사이클뷰로 보낼 준비
                }
                adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //데베 데이터를 가져오던 중 에러 발생 시
                Toast.makeText(getActivity(), "에러라라고오오옹", Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new MyPageNoticeAdapter(myfeed, getContext());
        list.setAdapter(adapter); //리사이클러뷰에 어댑터 연결
        rootview.findViewById(R.id.myPageBtn_modify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent modify = new Intent(getActivity(), ProfileModifyActivity.class);
                startActivityForResult(modify, requestcode);
            }
        });
        return rootview;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == getActivity().RESULT_OK){
            ProfileInfo pinfo = (ProfileInfo) data.getSerializableExtra("profileinfo");
            Log.i("username : ",pinfo.getUsername());
            Log.i("shape : ",pinfo.getShape());
            Log.i("meal : ",""+pinfo.getMeal());
        }
    }
}
