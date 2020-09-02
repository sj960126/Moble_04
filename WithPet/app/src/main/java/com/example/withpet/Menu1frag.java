package com.example.withpet;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//NewsFeed page
public class Menu1frag extends Fragment {
    private View rootview;
    private RecyclerView list;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<News> myfeed;

    private FirebaseDatabase db;
    private DatabaseReference dbreference;

    private Button btn_wirte;

    private String[] permission_list = {Manifest.permission.READ_EXTERNAL_STORAGE};



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_news,container,false);
        btn_wirte = rootview.findViewById(R.id.walkBtn_write);

        Button btn1 = (Button) rootview.findViewById(R.id.walkBtn_write);
        Button btn2 = (Button) rootview.findViewById(R.id.mainBtn_chatt);

        btn1.setBackgroundResource(R.drawable.iconadd);
        btn2.setBackgroundResource(R.drawable.iconchatt);

        list = rootview.findViewById(R.id.mainRv);
        list.setHasFixedSize(true); //리사이클러뷰 기존 성능 강화
        layoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(layoutManager);
        myfeed = new ArrayList<>(); //유저 객체를 담을 (어댑터쪽으로)

        db = FirebaseDatabase.getInstance(); //파이어베스 데이터베이스 연동
        dbreference = db.getReference("Feed");//연동한 DB의 테이블 연결

        //실시간으로 앱데이터를 업데이트 함수
        dbreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                myfeed.clear(); //기존 배열가 존재하지 않게 초기화 방지차원
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    //반복문으로 데이터 리스트를 추출
                    News news = snapshot.getValue(News.class); //만들어뒀던 news 객체에 데이터를 담음
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
        adapter = new MyFeedAdapter(myfeed, getContext());
        list.setAdapter(adapter); //리사이클러뷰에 어댑터 연결

        btn_wirte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "클릭햇어......", Toast.LENGTH_SHORT).show();
                checkPermission();
            }
        });
        return rootview;
    }

    public void checkPermission(){
        //현재 안드로이드 버전이 6.0미만이면 메서드를 종료한다.
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;
        for(String permission : permission_list){
            //권한 허용 여부를 확인한다.
            int chk = ContextCompat.checkSelfPermission(getContext(), permission);

            if(chk == PackageManager.PERMISSION_DENIED){
                //권한 허용을여부를 확인하는 창을 띄운다
                requestPermissions(permission_list,0);
            }
            else{
                //페이지 이동
                Intent intent = new Intent(getContext(), NewsWriteActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 0)
        {
            for(int i=0; i<grantResults.length; i++)
            {
                //허용됬다면
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    //페이지 이동
                    Intent intent = new Intent(getContext(), NewsWriteActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getContext(),"앱권한설정하세요",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}




