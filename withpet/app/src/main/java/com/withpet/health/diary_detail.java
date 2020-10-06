package com.withpet.health;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.withpet.*;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.util.ArrayList;

public class diary_detail extends AppCompatActivity {
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ArrayList<diary> diaryArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_detail);
        BarChart bar = (BarChart)findViewById(R.id.bar);
        int morning = getIntent().getIntExtra("morning",0);
        int launcher = getIntent().getIntExtra("launcher",0);
        int diner = getIntent().getIntExtra("diner",0);
        int total = morning+launcher+diner;
        bar.clearChart();

        bar.addBar(new BarModel("아침",morning,0xFF63CBB0));
        bar.addBar(new BarModel("점심",launcher,0xFF63CBB0));
        bar.addBar(new BarModel("저녁",diner,0xFF63CBB0));
        bar.addBar(new BarModel("총 급여량",total,0xFF63CBB0));



        bar.startAnimation();
    }
}