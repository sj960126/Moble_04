package com.withpet.main;


import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import com.google.firebase.database.ValueEventListener;
import com.withpet.Chat.ChatListActivity;
import com.withpet.Search.Search_FeedActivity;
import com.withpet.newsfeed.*;
import com.withpet.*;

import java.util.ArrayList;

//NewsFeed page
public class HomeFrag extends Fragment {
    private View rootview;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Feed> myfeed;
    private ArrayList<String> followUserlist;
    private FirebaseDatabase db;
    private DatabaseReference dbreference;

    private SwipeRefreshLayout refreshLayout;
    private Button btnWrite, btnChatt ,btnSearch;
    private String[] permission_list = {Manifest.permission.READ_EXTERNAL_STORAGE};

    //액티비티 onCreate와 동일
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_news,container,false);

        btnSearch =(Button) rootview.findViewById(R.id.mainBtn_search);
        btnWrite = (Button) rootview.findViewById(R.id.walkBtn_write);
        btnChatt = (Button) rootview.findViewById(R.id.mainBtn_chatt);
        refreshLayout = (SwipeRefreshLayout) rootview.findViewById(R.id.refresh);
        recyclerView = (RecyclerView) rootview.findViewById(R.id.mainRv);

        //버튼 기본 이미지 설정
        btnWrite.setBackgroundResource(R.drawable.iconadd);
        btnChatt.setBackgroundResource(R.drawable.iconchatt);
        btnSearch.setBackgroundResource(R.drawable.iconsearch);

        layoutManager = new LinearLayoutManager(getActivity());

        //리사이클러뷰 설정
        recyclerView.setHasFixedSize(true); //리사이클러뷰 기존 성능 강화
        recyclerView.setLayoutManager(layoutManager);

        return rootview;
    }

    //사용자와의 상호작용
    @Override
    public void onResume() {
        super.onResume();

        myfeed = new ArrayList<Feed>(); //유저 객체를 담을 (어댑터쪽으로)
        followUserlist = new ArrayList<>();
        db = FirebaseDatabase.getInstance(); //파이어베스 데이터베이스 연동
        //내용추가
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        dbreference = db.getReference("Follow");
        dbreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followUserlist.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getKey().equals(firebaseUser.getUid())){    // 로그인한 유저가 등록한 관심 유저 정보 가져오기
                        for(DataSnapshot followUserdata : dataSnapshot.getChildren()){
                                followUserlist.add(followUserdata.getKey());
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myfeed.clear();
        dbreference = db.getReference("Feed");//연동한 DB의 테이블 연결

        //최근 순서

        Query latelyFeed = dbreference.orderByChild("date");
        //실시간으로 앱데이터를 업데이트 함수
        latelyFeed.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                //배열리스트에 역순으로 게시글을 저장
                Feed feed = snapshot.getValue(Feed.class);
                if(feed.getUid().equals(firebaseUser.getUid())){
                    //myfeed.add(0,snapshot.getValue(Feed.class));
                    myfeed.add(0,feed);
                    adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
                }
                else{
                    for(String followUserid : followUserlist){
                        if(feed.getUid().equals(followUserid)){
                            myfeed.add(0,feed);
                            adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
                        }

                    }
                }
                /*myfeed.add(0,snapshot.getValue(Feed.class));
                adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침*/
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new FeedAdapter(myfeed, getContext());
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어댑터 연결

        //리사이클러뷰 새로고침 이벤트
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Refresh();
                refreshLayout.setRefreshing(false);
            }
        });

        //작성버튼 클릭이벤트 : 권한 설정 함수 실행
        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //권한설정 함수 실행
                Permission();
            }
        });

        //채팅 버튼 클릭 이벤트 : 채팅방 목록 페이지로 이동
        btnChatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChatListActivity.class);
                startActivity(intent);
            }
        });
        //검색 버튼 클리 이벤트 : 검색 페이지로 이동
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent search_intent = new Intent(getActivity(), Search_FeedActivity.class);
                startActivity(search_intent);
            }
        });
    }


    //프레그먼트 새로고침
    public void Refresh(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    //권한설정 _ 안드로이드스튜디오개발가이드 + 구글링
    public void Permission(){
        for(String permission : permission_list){
            //권한 확인 여부
            int access = ContextCompat.checkSelfPermission(getContext(), permission);
            //권한 x
            if(access == PackageManager.PERMISSION_DENIED){
                //권한 설정 확인 창이 뜸!!
                //requestCode는 아래의 onRequestPermissionsResult 함수 실행의 매개변수!
                requestPermissions(permission_list,0);
            }
            //권한 o
            else{
                //해당 페이지로 이동
                Intent intent = new Intent(getContext(), FeedGalleryActivity.class);
                intent.putExtra("request", R.integer.newsRequestcode);
                startActivity(intent);
            }
        }
    }

    //권한 요청 후 어떻게 관리할 것인지에 대한 함수 _ 개발가이드에 자세히 나와있음
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 0)
        {
            for(int i=0; i<grantResults.length; i++)
            {
                //사용자가 허용했을 경우!
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    //페이지 이동
                    Intent intent = new Intent(getContext(), FeedGalleryActivity.class);
                    startActivity(intent);
                }
                //사용자가 거부했을 경우!
                else {
                    Toast.makeText(getContext(),"저장소 권한을 설정하세요.",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}




