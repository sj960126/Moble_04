package com.withpet.walk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.withpet.*;

public class Walk_boarddetailFrag extends Fragment {
    private View view;
    private int borad_nb;
    private String title ="title";
    private String content = "content";
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private TextView title_tv;
    private TextView content_tv;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_walk_boarddetail,container,false);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            bundle = getArguments();
            borad_nb = bundle.getInt("board_nb");
            title = bundle.getString("board_title");
            content = bundle.getString("board_content");
        }
        // 클릭한 게시물 번호에 관련된 firebase 제목 내용 갖고 오기
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("walk-board").child(Integer.toString(borad_nb));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Walk_boardUpload tmp = dataSnapshot.getValue(Walk_boardUpload.class);
                    title = tmp.getWalkboard_title();
                    content = tmp.getWalkboard_content();
                    title_tv = (TextView) view.findViewById(R.id.walkTv_title);
                    content_tv = (TextView) view.findViewById(R.id.walkTv_content);

                    title_tv.setText(title);
                    content_tv.setText(content);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        return view;
    }
}