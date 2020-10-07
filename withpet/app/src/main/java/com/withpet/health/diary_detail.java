package com.withpet.health;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import com.withpet.*;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.util.ArrayList;

public class diary_detail extends AppCompatActivity {

    private ArrayList<Integer> imageList;
    private static final int DP = 24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_detail);
        BarChart bar = (BarChart)findViewById(R.id.bar);
        int morning = getIntent().getIntExtra("morning",0);
        int launcher = getIntent().getIntExtra("launcher",0);
        int diner = getIntent().getIntExtra("diner",0);
        int total = morning+launcher+diner;
        String input_day = getIntent().getStringExtra("day");
        TextView day = findViewById(R.id.setdate);

        day.setText(input_day);

        bar.clearChart();

        bar.addBar(new BarModel("아침",morning,0xFF63CBB0));
        bar.addBar(new BarModel("점심",launcher,0xFF63CBB0));
        bar.addBar(new BarModel("저녁",diner,0xFF63CBB0));
        bar.addBar(new BarModel("총 급여량",total,0xFF63CBB0));
        bar.startAnimation();

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
}