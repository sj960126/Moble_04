package com.withpet.walk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;
import com.withpet.R;
import com.withpet.main.MainActivity;

public class Walk_boardmod extends AppCompatActivity {

    private TMapView tMapView;
    private LinearLayout walkLinear_tmap;
    private int line_nb;
    private int repeat;
    private Button walkBtn_modify;
    private Button walkBtn_tmap;
    private double centerLat;
    private double centerLong;

    private int walkboard_nb;

    private TextView walkTv_title;
    private TextView walkTv_content;
    private final String APK ="l7xxfa281c47f54b4b8d866946553f981932";

    private double spot[][] = new double[100][2];
    private int uploadId;
    private int board_nb;
    private String userImg;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_boardmod);

        walkBtn_tmap = findViewById(R.id.walkBtn_tmapmodify);
        walkBtn_modify = findViewById(R.id.walkBtn_modify);
        walkTv_title = findViewById(R.id.walkEt_titlemodify);
        walkTv_content = findViewById(R.id.walkEt_contentmodify);
        walkLinear_tmap = findViewById(R.id.walkLayout_tmap);

        final Intent intent = getIntent();
        board_nb = intent.getIntExtra("board_nb",0);
        centerLat = intent.getDoubleExtra("centerLat", 0.0);
        centerLong = intent.getDoubleExtra("centerLong",0.0);

        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey(APK);
        walkLinear_tmap.addView(tMapView);
        tMapView.setZoomLevel(13);
        tMapView.setCenterPoint(centerLong,centerLat);
        tMapView.setCenterPoint(centerLong,centerLat);


        //firebase에서 데이터 가져옴
        databaseReference = firebaseDatabase.getInstance().getReference("walk-board").child(Integer.toString(board_nb));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Walk_boardUpload walk_boardUpload = snapshot.getValue(Walk_boardUpload.class);

                walkboard_nb = walk_boardUpload.getWalkboard_nb();

                walkTv_title.setText(walk_boardUpload.getWalkboard_title());
                walkTv_content.setText(walk_boardUpload.getWalkboard_content());

                spot[0][0] = walk_boardUpload.getLat0();
                spot[0][1] = walk_boardUpload.getLong0();
                spot[1][0] = walk_boardUpload.getLat1();
                spot[1][1] = walk_boardUpload.getLong1();
                spot[2][0] = walk_boardUpload.getLat2();
                spot[2][1] = walk_boardUpload.getLong2();
                spot[3][0] = walk_boardUpload.getLat3();
                spot[3][1] = walk_boardUpload.getLong3();

                userImg = walk_boardUpload.getUserImg();
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


        //tmap 수정 버튼
        walkBtn_tmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Walk_boardmod.this,Walk_tmap.class);
                intent1.putExtra("check",100);
                intent1.putExtra("uploadId",board_nb);
                intent1.putExtra("uid",firebaseUser.getUid());
                intent1.putExtra("userimg",userImg);
                intent1.putExtra("walkboard_content",walkTv_content.getText().toString().trim());
                intent1.putExtra("walkboard_nb",walkboard_nb);
                intent1.putExtra("walkboard_title",walkTv_title.getText().toString().trim());
                startActivity(intent1);
            }
        });


        //수정 버튼
        walkBtn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Walk_boardmod.this, "수정 완료", Toast.LENGTH_SHORT).show();
                walk_mod();

                Intent intent1 = new Intent(Walk_boardmod.this, MainActivity.class);
                intent1.putExtra("frag",2);
                startActivity(intent1);
                finish();

            }
        });
    }

    //수정된 정보 firebase에 업로드
    private void walk_mod(){
        Walk_boardUpload upload = new Walk_boardUpload(walkTv_title.getText().toString().trim(),walkTv_content.getText().toString().trim(),board_nb,spot[0][0],spot[0][1],spot[1][0],spot[1][1],spot[2][0],spot[2][1],spot[3][0],spot[3][1],firebaseUser.getUid(),userImg);
        databaseReference.setValue(upload);
    }

    //경로 그려주는 메소드
    public void path(final double a, final double b, final double c, final double d){

        new Thread(){
            @Override
            public void run(){
                try{

                    TMapPoint start_point = new TMapPoint(a,b);
                    TMapPoint end_point = new TMapPoint(c,d);

                    TMapPolyLine tMapPolyLine = new TMapData().findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, start_point, end_point);
                    tMapPolyLine.setLineColor(Color.BLUE);
                    tMapPolyLine.setLineWidth(2);
                    tMapView.addTMapPolyLine("Line"+line_nb, tMapPolyLine);

                    line_nb++;
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }
}