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

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;
import com.withpet.R;

import java.util.ArrayList;

public class Walk_tmap extends AppCompatActivity  {

    private final String APK ="l7xxfa281c47f54b4b8d866946553f981932";
    private TMapView tMapView;
    private double currentLatitude;
    private double currentLongitude;
    private Context context =null;
    private Button walkclear_Btn;
    private Button walksave_Btn;
    String Address = " ";
    TMapData tmapdata;
    TMapMarkerItem tItem = new TMapMarkerItem();
    TMapPoint start_point;// = new TMapPoint(36.809685, 127.147962);
    TMapPoint end_point;
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

        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey(APK);
        walk_map.addView(tMapView);
       // tMapView.setIconVisibility(true);// 현재위치로 표시될 아잉콘을 표시할 여부 설정
        setGps(); //초기 화면 현위치 지정



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

        walksave_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Walk_tmap.this,Walk_boardwriteActivity.class);
                intent.putExtra("lat0",spot[0][0]);
                intent.putExtra("long0",spot[0][1]);
                intent.putExtra("lat1",spot[1][0]);
                intent.putExtra("long1",spot[1][1]);
                intent.putExtra("lat2", spot[2][0]);
                intent.putExtra("long2", spot[2][1]);
                intent.putExtra("lat3", spot[3][0]);
                intent.putExtra("long3", spot[3][1]);
                intent.putExtra("upload",uploadId);
                startActivity(intent);
            }
        });

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
                            tMapPolyLine.setLineWidth(3);
                            tMapView.addTMapPolyLine("Line"+a, tMapPolyLine);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }



    private final LocationListener  mLocationListener = new LocationListener() { //현위치
        @Override
        public void onLocationChanged(@NonNull Location location) {
            if(location != null){
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                tMapView.setLocationPoint(latitude,longitude);
                tMapView.setCenterPoint(latitude,longitude);
            }
        }
    };
    public void setGps(){ //현위치
        final LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,100,1,mLocationListener);
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
}