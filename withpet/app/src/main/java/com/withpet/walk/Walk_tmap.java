package com.withpet.walk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;
import com.withpet.R;

import java.util.ArrayList;

public class Walk_tmap extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {

    private final String APK ="l7xxfa281c47f54b4b8d866946553f981932";
    private TMapView tMapView;
    // tMapView.setIconVisibility(true);// 현재위치로 표시될 아잉콘을 표시할 여부 설정

    private TMapGpsManager tMapGpsManager  = null;
    private double currentLatitude;
    private double currentLongitude;
    private Context context =null;
    private Button walkclear_Btn;
    private Button walksave_Btn;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    String Address = " ";
    TMapData tmapdata;
    TMapMarkerItem tItem = new TMapMarkerItem();
    TMapPoint start_point;// = new TMapPoint(36.809685, 127.147962);
    TMapPoint end_point;
    private int check;
    private String mod_title;
    private String mod_content;
    private int mod_nb;
    private String mod_userImg;
    private String mod_uid;
    private double center_lat;
    private double center_long;
    int a = -1;
    int id = 0;
    int array_nb = -1;
    double spot[][] = new double[100][2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_tmap);
        LinearLayout walk_map = findViewById(R.id.walk_map);
        walkclear_Btn = findViewById(R.id.clearBtn);
        walksave_Btn = findViewById(R.id.mapsavebtn);
        context= this;

        Intent uploadid = getIntent();
        final int uploadId = uploadid.getIntExtra("uploadId",99);
        check = uploadid.getIntExtra("check",101);
        mod_title = uploadid.getStringExtra("walkboard_title");
        mod_content = uploadid.getStringExtra("walkboard_content");
        mod_nb = uploadid.getIntExtra("walkboard_nb",0);
        mod_uid = uploadid.getStringExtra("uid");
        mod_userImg = uploadid.getStringExtra("userimg");

        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey(APK);
        walk_map.addView(tMapView);

        tMapGpsManager = new TMapGpsManager(context);
        tMapGpsManager.setMinTime(1000);
        tMapGpsManager.setMinDistance(5);
        tMapGpsManager.setProvider(tMapGpsManager.GPS_PROVIDER);

        tMapGpsManager.OpenGps();
        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);
        tMapView.setZoomLevel(14);
        //tmapview 클릭 이벤트
        tMapView.setOnLongClickListenerCallback(new TMapView.OnLongClickListenerCallback() {
            @Override
            public void onLongPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint) {
                array_nb++;
                spot[array_nb][0] = tMapPoint.getLatitude();
                spot[array_nb][1] = tMapPoint.getLongitude();

                if(array_nb <= 3) {
                    marker(spot[array_nb][0], spot[array_nb][1]);
                }
                if(array_nb >= 1 && array_nb <= 3) {
                    path();
                }
                if(array_nb > 3) Toast.makeText(context, "경유지는 최대 2개 입니다. ", Toast.LENGTH_SHORT).show();

            }

        });

        //tmapview 초기화
        walkclear_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i <50; i++) {
                    tMapView.removeMarkerItem("marker" + i);
                    tMapView.removeTMapPolyLine("Line"+i);
                    spot[i][0] = 0;
                    spot[i][1] = 0;
                }
                array_nb = -1;
                a = -1;
            }
        });

        //tmap view에 저장된 위도 경도 게시글 작성 클래스에 데이터 전달
        walksave_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spot[0][0] == 0){
                    Toast.makeText(context, "경로를 지정하세요", Toast.LENGTH_SHORT).show();
                }else{
                    if(check == 99) {
                        Intent intent = new Intent(Walk_tmap.this, Walk_boardwriteActivity.class);
                        intent.putExtra("lat0", spot[0][0]);
                        intent.putExtra("long0", spot[0][1]);
                        intent.putExtra("lat1", spot[1][0]);
                        intent.putExtra("long1", spot[1][1]);
                        intent.putExtra("lat2", spot[2][0]);
                        intent.putExtra("long2", spot[2][1]);
                        intent.putExtra("lat3", spot[3][0]);
                        intent.putExtra("long3", spot[3][1]);
                        intent.putExtra("upload", uploadId);
                        startActivity(intent);
                    }else if(check == 100){


                        Point_Long();
                        databaseReference = firebaseDatabase.getInstance().getReference("walk-board").child(Integer.toString(uploadId));
                        Walk_boardUpload upload = new Walk_boardUpload(mod_title,mod_content,mod_nb,spot[0][0],spot[0][1],spot[1][0],spot[1][1],spot[2][0],spot[2][1],spot[3][0],spot[3][1],mod_uid,mod_userImg);
                        databaseReference.setValue(upload);
                        Intent intent = new Intent(Walk_tmap.this,Walk_boardmod.class);
                        intent.putExtra("board_nb",mod_nb);
                        intent.putExtra("centerLat",Point_Lat());
                        intent.putExtra("centerLong",Point_Long());
                        startActivity(intent);
                    }

                }
            }
        });

    }

    private double Point_Lat(){
        double Lat = spot[0][0]+spot[1][0]+spot[2][0]+spot[3][0];
        if(spot[3][0] == 0 && spot[2][0] ==0){
            center_lat = Lat/2;
        }else if (spot[3][0] == 0 && spot[2][0] != 0){
            center_lat = Lat/3;
        }else {
            center_lat = Lat/4;
        }
        return center_lat;
    }

    private double Point_Long(){
        double Long = spot[0][1]+spot[1][1]+spot[2][1]+spot[3][1];
        if(spot[3][1] == 0 && spot[2][1] ==0){
            center_long = Long/2;
        }else if (spot[3][1] == 0 && spot[2][1] != 0){
            center_long = Long/3;
        }else {
            center_long = Long/4;
        }
        return center_long;
    }

    // 보행자 경로 그어주기
    public void path(){
        new Thread(){
            @Override
            public void run(){
                try{

                            a++;
                            start_point = new TMapPoint(spot[a][0],spot[a][1]);
                            end_point = new TMapPoint(spot[a+1][0],spot[a+1][1]);

                            TMapPolyLine tMapPolyLine = new TMapData().findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, start_point, end_point);
                            tMapPolyLine.setLineColor(Color.BLUE);
                            tMapPolyLine.setLineWidth(2);
                            tMapView.addTMapPolyLine("Line"+a, tMapPolyLine);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }




    public void marker(double lattitude , double longitude){ //마커 찍기
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.marker);
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 8, bitmap.getHeight() / 8, true);

        TMapMarkerItem markerItem = new TMapMarkerItem();
        TMapPoint point = new TMapPoint(lattitude,longitude);
        markerItem.setTMapPoint(point);
        markerItem.setVisible(TMapMarkerItem.VISIBLE);
        markerItem.setIcon(bitmap);
        tMapView.addMarkerItem("marker"+id,markerItem);
        id++;
    }

    @Override
    public void onLocationChange(Location location) {
        tMapView.setLocationPoint(location.getLongitude(),location.getLatitude());
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}