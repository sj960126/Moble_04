package com.withpet.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.withpet.walk.*;
import com.withpet.*;

import java.util.ArrayList;

public class WalkFrag extends Fragment {

    private View rootview;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<Walk_boardUpload> walkfeed;
    private RecyclerView.LayoutManager layoutManager;
    private Walk_Adapter walk_adpater;
    private FloatingActionButton walkFab_write;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference_replyid;
    int board_nb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_walk,container,false);

        recyclerView = rootview.findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        walkfeed = new ArrayList<>();



        //글작성 버튼
        walkFab_write = (FloatingActionButton) rootview.findViewById(R.id.fab);
        walkFab_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Walk_boardwriteActivity.class);
                intent.putExtra("id", board_nb);
                startActivityForResult(intent, 8);



            }
        });

        return rootview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("walk-board");
        //최신순 정렬
        Query orderwalk = databaseReference.orderByChild("walkboard_nb");

        orderwalk.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Walk_boardUpload walk_boardUpload = snapshot.getValue(Walk_boardUpload.class);
                //배열리스트에 역순으로 게시글 저장
                walkfeed.add(0,walk_boardUpload);

                //마지막 게시물 번호에 +1(게시글 번호 곂치지 않게 하기위해서)
                board_nb = walk_boardUpload.getWalkboard_nb()+1;

                adapter.notifyDataSetChanged();

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


        adapter = new Walk_Adapter(walkfeed, getContext(), getActivity());
        recyclerView.setAdapter(adapter);
    }
}
