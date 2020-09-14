package com.withpet.walk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapView;
import com.withpet.R;

public class Walk_tmap extends AppCompatActivity {

    private final String APK ="l7xxfa281c47f54b4b8d866946553f981932";
    private TMapView tMapView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_tmap);

        LinearLayout walk_map = findViewById(R.id.walk_map);

        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey(APK);
        walk_map.addView(tMapView);

        tMapView.setCenterPoint(126.988205, 37.551135);
    }
}