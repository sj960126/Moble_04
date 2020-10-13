package com.withpet_manager.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.withpet_manager.*;
import com.withpet_manager.userfeed.*;

import java.util.ArrayList;

public class UserfeedFrag extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Report> reports, seleteReports;
    private Spinner spCategory, spState;
    ArrayAdapter categorAdapter, stateAdapter;
    private String category, state;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_userfeed_frag, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.feedRv);
        layoutManager = new LinearLayoutManager(getActivity());

        //리사이클러뷰 설정
        recyclerView.setHasFixedSize(true); //리사이클러뷰 기존 성능 강화
        recyclerView.setLayoutManager(layoutManager);

        spCategory = view.findViewById(R.id.category);
        spState = view.findViewById(R.id.state);

        categorAdapter = ArrayAdapter.createFromResource(getContext(), R.array.reportCategory, R.layout.support_simple_spinner_dropdown_item);
        spCategory.setAdapter(categorAdapter);

        stateAdapter = ArrayAdapter.createFromResource(getContext(), R.array.reportState, R.layout.support_simple_spinner_dropdown_item);
        spState.setAdapter(stateAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //모든 신고내역
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Report");
        Query query = databaseReference.orderByChild("date");
        reports = new ArrayList<>();
        seleteReports = new ArrayList<>();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot user : snapshot.getChildren()) {
                        reports.add(0, user.getValue(Report.class));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        adapter = new userFeedAdapter(reports, getContext());
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어댑터 연결

        //카테고리 spinner
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //아이템 선택 시 String
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                category = (String) categorAdapter.getItem(position);
                seleteOptions(state, category);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                category = "";
            }
        });

        //답변상태 spinner
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                state = (String) stateAdapter.getItem(position);
                seleteOptions(state, category);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                state = "";
            }
        });
    }


    public void seleteOptions(String stateOptions, String categoryOptions) {
        if (reports.size() != 0) {
            seleteReports.clear();

            //분류에 관한 옵션 리스트
            if(!category.equals("분류")){
                for(int i  =0; i < reports.size(); i ++){
                    if(reports.get(i).getCategory().equals(categoryOptions)){
                        seleteReports.add(reports.get(i));
                    }
                }
            }else{
                for (int i = 0; i < reports.size(); i++) {
                    seleteReports.add(reports.get(i));
                }
            }

            //상태에 관한 옵션 리스트
            if(!state.equals("상태")){
                for(int i  =0; i < seleteReports.size(); i ++){
                    if(!(seleteReports.get(i).getState().equals(stateOptions))){
                        seleteReports.remove(i);
                        i=-1;
                    }

                }
            }
            adapter = new userFeedAdapter(seleteReports, getContext());
            recyclerView.setAdapter(adapter);
        }
    }
}