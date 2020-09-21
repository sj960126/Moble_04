package com.withpet.walk;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;
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
    private double spot[][] = new double[100][2];
    private TMapView tMapView;
    private final String APK ="l7xxfa281c47f54b4b8d866946553f981932";
    private LinearLayout tmap;
    int line_nb = 0;
    int repeat;
    double a;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_walk_boarddetail,container,false);
        tmap = view.findViewById(R.id.detail_tmap);
        tMapView = new TMapView(getContext());
        tMapView.setSKTMapApiKey(APK);
        tmap.addView(tMapView);

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

                    spot[0][0] = tmp.getLat0();
                    spot[0][1] = tmp.getLong0();
                    spot[1][0] = tmp.getLat1();
                    spot[1][1] = tmp.getLong1();
                    spot[2][0] = tmp.getLat2();
                    spot[2][1] = tmp.getLong2();
                    spot[3][0] = tmp.getLat3();
                    spot[3][1] = tmp.getLong3();

                    title_tv = (TextView) view.findViewById(R.id.walkTv_title);
                    content_tv = (TextView) view.findViewById(R.id.walkTv_content);

                    title_tv.setText(title);
                    content_tv.setText(content);

                    if(spot[3][0] == 0.0)repeat=2;
                    else if(spot[2][0] == 0.0)repeat=1;
                    else repeat = 3;
                    for(int i =0; i<repeat; i++) {
                        path(spot[i][0], spot[i][1], spot[i+1][0], spot[i+1][1]);
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        return view;
    }

    public void path(final double a, final double b, final double c, final double d){

        new Thread(){
            @Override
            public void run(){
                try{

                    TMapPoint start_point = new TMapPoint(a,b);
                    TMapPoint end_point = new TMapPoint(c,d);

                    TMapPolyLine tMapPolyLine = new TMapData().findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, start_point, end_point);
                    tMapPolyLine.setLineColor(Color.BLUE);
                    tMapPolyLine.setLineWidth(3);
                    tMapView.addTMapPolyLine("Line"+line_nb, tMapPolyLine);
                    line_nb++;
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }
}