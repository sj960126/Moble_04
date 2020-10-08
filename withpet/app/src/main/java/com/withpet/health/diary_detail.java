package com.withpet.health;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.withpet.*;
import com.withpet.main.User;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class diary_detail extends AppCompatActivity {

    private ArrayList<Integer> imageList;
    private static final int DP = 24;
    private BarChart bar;
    private int morning;
    private int launcher;
    private int diner;
    private int total;
    private String input_day;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private TextView day;
    private User user;
    private CircleImageView user_img;
    private TextView user_name;
    private TextView user_kind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_detail);
        day = (TextView) findViewById(R.id.setdate);
        user_img = (CircleImageView) findViewById(R.id.diary_img);
        user_name = (TextView) findViewById(R.id.diary_title);
        user_kind = (TextView) findViewById(R.id.diary_kind);
        user = new User();
        initData();


        CreateBar();
        this.initializeData();

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setClipToPadding(false);
        float density = getResources().getDisplayMetrics().density;
        int margin = (int) (DP * density);
        viewPager.setPadding(margin, 0, margin, 0);
        viewPager.setPageMargin(margin/2);
        viewPager.setAdapter(new diary_detail_Adapter(this, imageList));

    }
    public void initializeData()
    {
        imageList = new ArrayList();
        imageList.add(R.drawable.amountofmeal1);
        imageList.add(R.drawable.amountofmeal2);
        imageList.add(R.drawable.amountofmeal3);
    }
    public void CreateBar(){
        bar = (BarChart)findViewById(R.id.bar);
        morning = getIntent().getIntExtra("morning",0);
        launcher = getIntent().getIntExtra("launcher",0);
        diner = getIntent().getIntExtra("diner",0);
        total = morning+launcher+diner;

        bar.clearChart();

        bar.addBar(new BarModel("아침",morning,0xFF63CBB0));
        bar.addBar(new BarModel("점심",launcher,0xFF63CBB0));
        bar.addBar(new BarModel("저녁",diner,0xFF63CBB0));
        bar.addBar(new BarModel("총 급여량",total,0xFF63CBB0));
        bar.startAnimation();
    }
    public void initData(){
        input_day = getIntent().getStringExtra("day");
        day.setText(input_day);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        database=FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User");
    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    if(snapshot1.getKey().equals(firebaseUser.getUid())){
                        user = snapshot1.getValue(User.class);
                        Glide.with(getApplicationContext()).load(user.getImgUrl()).override(200).into(user_img);
                        user_kind.setText(user.getShape());
                        user_name.setText(user.getName());
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}