package com.withpet.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.withpet.*;
import com.withpet.newsfeed.*;

import java.util.ArrayList;

public class SclistActivity extends AppCompatActivity {

    private ArrayList<Report> scArrayList;
    private FirebaseDatabase firebaseDatabase;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private Button btnBefore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sclist);

        recyclerView = (RecyclerView) findViewById(R.id.sclistRv);

        btnBefore = (Button) findViewById(R.id.sclistBtn_before);
        btnBefore.setBackgroundResource(R.drawable.iconbefore);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        //리사이클러뷰 설정
        recyclerView.setHasFixedSize(true); //리사이클러뷰 기존 성능 강화
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();

        scArrayList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("SC");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                scArrayList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    scArrayList.add(0,dataSnapshot.getValue(Report.class));
                    Log.i("sc",""+scArrayList.get(0).getContext());
                }
                adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        adapter = new ScAdapter(scArrayList, this);
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어댑터 연결

        //이전 페이지 이동
        btnBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}