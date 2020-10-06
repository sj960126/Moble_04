package com.withpet.health;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.withpet.*;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class diaryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<diary> arrayList;
    private ArrayList<String> arrayKeyList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ImageButton add;
    private CalendarView cal;
    private String date;
    private TextView change;
    private ArrayList<Diary_Day_Info> day_list;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseUser firebaseUser;
    private ImageButton search;
    private Context context =this;
    private int morning;
    private int launcher;
    private int diner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //유저정보 가져오기
        day_list = new ArrayList<Diary_Day_Info>();
        change = findViewById(R.id.day_change);
        Date system_date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String today = sdf.format(system_date);
        change.setText(today);
        recyclerView = findViewById(R.id.diary_rc);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.diary_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Refresh();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        recyclerView.setHasFixedSize(true); //리사이클러뷰 기존 성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList= new ArrayList<diary>(); //객체를 담을 (어댑터쪽으로)
        arrayKeyList= new ArrayList<String>(); //내용 추가
        database =FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("Diary").child(firebaseUser.getUid());//db테이블 연결 경로 액세스
        cal = findViewById(R.id.calendar);
        add = findViewById(R.id.plus);
        search =findViewById(R.id.diary_search);


        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                date = String.valueOf(i)+"-"+String.valueOf(i1+1)+"-"+String.valueOf(i2);
                arrayList.clear();
                arrayKeyList.clear();
                morning = 0; launcher=0; diner=0; int count =0;
                for(Diary_Day_Info day : day_list ){
                    if(day.getDate().equals(date)){
                        for(diary d : day.getDiaryArrayList()){
                            arrayList.add(d);
                            if (arrayList.get(count).getTime().equals("아침")){
                                morning = morning + Integer.parseInt(arrayList.get(count).getEat());
                            }else  if (arrayList.get(count).getTime().equals("점심")){
                                launcher = launcher + Integer.parseInt(arrayList.get(count).getEat());
                            }else  if (arrayList.get(count).getTime().equals("저녁")){
                                diner = diner +Integer.parseInt(arrayList.get(count).getEat());
                            }
                            count++;
                        }
                        for(int index = 0; index<day.getDiaryArrayList().size();index++){
                            arrayKeyList.add(day.getDiaryKeyArrayList().get(index)); //키값 등록
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
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),diary_detail.class);
                intent.putExtra("morning",morning);
                intent.putExtra("launcher",launcher);
                intent.putExtra("diner",diner);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onResume() { // 액티비티 단위, 액티비티가 다시 켜질 때(인텐트로 넘어간 후 다시 돌아올때, intent startactivity)
        super.onResume();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){  //  ds : 날짜 (2020-09-15)
                    Diary_Day_Info td = new Diary_Day_Info();
                    td.setDate(ds.getKey()); //td에 날짜를 저장
                    for(DataSnapshot ids : ds.getChildren()){
                        td.addDiaryArrayList(ids.getValue(diary.class)); //날짜의 정보를 넣는다
                        td.addDiaryKeyArrayList(ids.getKey()); //날짜의 각 행 키값저장
                    }
                    day_list.add(td); //날짜 한줄씩 저장
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new diary_Adapter(arrayList,this, arrayKeyList);
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어뎁터 연결
    }
    public void Refresh(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
    public String getDate() {return date;}
}