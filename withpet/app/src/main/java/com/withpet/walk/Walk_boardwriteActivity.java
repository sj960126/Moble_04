package com.withpet.walk;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;
import com.withpet.*;
import com.withpet.main.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;


public class Walk_boardwriteActivity extends AppCompatActivity {

    private final String APK ="l7xxfa281c47f54b4b8d866946553f981932";
    private Button walkBtn_add;
    private Button walkBtn_tmap;
    private TMapView tMapView;
    private TextView walkTv_title;
    private TextView walkTv_content;
    private DatabaseReference databaseReference;
    private LinearLayout writetmap;
    private Button check_btn;
    int uploadId;
    String uploadId_str;
    Context context;
    int line_nb = 0;
    int band = 0;
    double spot[][] = new double[100][2];
    int repeat;
    private Button check;
    int last_uploadId;

    // TMapPoint start_point;// = new TMapPoint(36.809685, 127.147962);
   // TMapPoint end_point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_boardwrite);
        context = this;
        Intent intent = getIntent();
        uploadId = intent.getIntExtra("id",0);
        walkBtn_tmap = findViewById(R.id.walkBtn_tmap);
        walkBtn_add = findViewById(R.id.addbtn);
        walkTv_title = findViewById(R.id.walk_title);
        walkTv_content = findViewById(R.id.walk_content);
        writetmap = findViewById(R.id.walkwrite_tmap);
        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey(APK);
        writetmap.addView(tMapView);

        databaseReference = FirebaseDatabase.getInstance().getReference("walk-board");

        Intent tmap_intent = getIntent();
        spot[0][0] = tmap_intent.getDoubleExtra("lat0",0);
        spot[0][1] = tmap_intent.getDoubleExtra("long0",0);
        spot[1][0] = tmap_intent.getDoubleExtra("lat1",0);
        spot[1][1] = tmap_intent.getDoubleExtra("long1",0);
        spot[2][0] = tmap_intent.getDoubleExtra("lat2",0);
        spot[2][1] = tmap_intent.getDoubleExtra("long2",0);
        spot[3][0] = tmap_intent.getDoubleExtra("lat3",0);
        spot[3][1] = tmap_intent.getDoubleExtra("long3",0);
        last_uploadId = tmap_intent.getIntExtra("upload",99);



        if(spot[3][0] == 0.0)repeat=2;
        else if(spot[2][0] == 0.0)repeat=1;
        else repeat = 3;
        for(int i =0; i<repeat; i++) {
            path(spot[i][0], spot[i][1], spot[i+1][0], spot[i+1][1]);
        }


        walkBtn_add.setOnClickListener(new Button.OnClickListener() { //글작성하기
            @Override
            public void onClick(View view) {
                walk_upload();
                Intent back = new Intent(Walk_boardwriteActivity.this,MainActivity.class);
                back.putExtra("frag",2);
                startActivity(back);
            }
        });

        walkBtn_tmap.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_tmap = new Intent(Walk_boardwriteActivity.this,Walk_tmap.class);
                intent_tmap.putExtra("uploadId",uploadId);
                startActivity(intent_tmap);
            }
        });
    }


    //산책 글 작성 제목, 내용 firebase에 업로드
    private void walk_upload(){
        if(spot[0][0] == 0){
            Toast.makeText(context, "경로 설정 하세요!!", Toast.LENGTH_SHORT).show();
        }else {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            Walk_boardUpload upload = new Walk_boardUpload(walkTv_title.getText().toString().trim(), walkTv_content.getText().toString().trim(), last_uploadId, spot[0][0], spot[0][1], spot[1][0], spot[1][1], spot[2][0], spot[2][1], spot[3][0], spot[3][1],firebaseUser.getUid());
            databaseReference.child(Integer.toString(last_uploadId)).setValue(upload);
        }
       /* Intent go_walk = new Intent(this,MainActivity.class);
        go_walk.putExtra("frag",2);
        startActivity(go_walk);*/
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