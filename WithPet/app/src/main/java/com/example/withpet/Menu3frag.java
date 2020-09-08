package com.example.withpet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Menu3frag extends Fragment {

    private View rootview;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<Walk_boardUpload> walkfeed;
    private RecyclerView.LayoutManager layoutManager;
    private Walk_Adapter walk_adpater;
    private FloatingActionButton walkFab_write;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_walk,container,false);

        recyclerView = rootview.findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        walkfeed = new ArrayList<>();

        database = FirebaseDatabase.getInstance();

        databaseReference = database.getReference("walk-board");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                walkfeed.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Walk_boardUpload walk_boardUpload = snapshot.getValue(Walk_boardUpload.class);
                    Log.i("confirm", walk_boardUpload.getWalkboard_content());
                    Log.i("confirm", walk_boardUpload.getWalkboard_title());
                    Log.i("confirm", snapshot.getValue().toString());
                    walkfeed.add(walk_boardUpload);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //******************
        adapter = new Walk_Adapter(walkfeed, getContext());
        recyclerView.setAdapter(adapter);

        //글작성 버튼
        walkFab_write = (FloatingActionButton) rootview.findViewById(R.id.fab);
        walkFab_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Walk_boardwrite.class);
                startActivity(intent);

            }
        });

        return rootview;
    }



}
