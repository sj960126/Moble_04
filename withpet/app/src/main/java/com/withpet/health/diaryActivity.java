package com.withpet.health;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.withpet.*;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class diaryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<diary> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ImageButton add;
    private CalendarView cal;
    private String date;
    private TextView change;
    private ArrayList<Diary_Day_Info> day_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //로그인 처리 //새로고침 //삭제
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        //로그인에서 id가지고 오기
        day_list = new ArrayList<Diary_Day_Info>();
        change = findViewById(R.id.day_change);
        Date system_date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String today = sdf.format(system_date);
        change.setText(today);
        recyclerView = findViewById(R.id.diary_rc);
        recyclerView.setHasFixedSize(true); //리사이클러뷰 기존 성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList= new ArrayList<diary>(); //객체를 담을 (어댑터쪽으로)
        database =FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동
        try {
            databaseReference = database.getReference("Diary").child("song");//db테이블 연결 경로 액세스
        }catch (Exception e){
            Toast.makeText(this,"없어요",Toast.LENGTH_SHORT).show();
        }

        cal = findViewById(R.id.calendar);
        add = findViewById(R.id.plus);


        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                date = String.valueOf(i)+"-0"+String.valueOf(i1+1)+"-"+String.valueOf(i2);
                arrayList.clear();
                for(Diary_Day_Info day : day_list ){
                    if(day.getDate().equals(date)){
                        for(diary d : day.getDiaryArrayList()){
                            arrayList.add(d);
                        }
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
                change.setText(date);

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), diary_addActivity.class);
                intent.putExtra("day",date);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() { // 액티비티 단위, 액티비티가 다시 켜질 때(인텐트로 넘어간 후 다시 돌아올때, intent startactivity)
        super.onResume();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){  //  ds : 날짜 (2020-09-15)
                    Log.i("con : ", ds.getValue().toString());
                    Diary_Day_Info td = new Diary_Day_Info();
                    td.setDate(ds.getKey());
                    for(DataSnapshot ids : ds.getChildren()){
                        td.addDiaryArrayList(ids.getValue(diary.class));
                    }
                    day_list.add(td);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new diary_Adapter(arrayList,this);
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어뎁터 연결

    }
}