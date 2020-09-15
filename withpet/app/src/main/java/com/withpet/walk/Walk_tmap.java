package com.withpet.walk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
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
    String Address = " ";
    TMapData tmapdata;
    TMapMarkerItem tItem = new TMapMarkerItem();
    TMapPoint start_point;// = new TMapPoint(36.809685, 127.147962);
    TMapPoint end_point;
    int id = 0;
    int array_nb = -1;
    double spot[][] = new double[100][2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_tmap);
        LinearLayout walk_map = findViewById(R.id.walk_map);


        context= this;
        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey(APK);
        walk_map.addView(tMapView);
     //   tMapView.setIconVisibility(true);// 현재위치로 표시될 아잉콘을 표시할 여부 설정
        setGps(); //초기 화면 현위치 지정
       // path();//보행자가 지정한 경로 그어주기



        tMapView.setOnLongClickListenerCallback(new TMapView.OnLongClickListenerCallback() {
            @Override
            public void onLongPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint) {
                Toast.makeText(context, "유럽 가봤니?" + tMapPoint.getLatitude()+","+tMapPoint.getLongitude() , Toast.LENGTH_SHORT).show();
                array_nb++;
                spot[array_nb][0] = tMapPoint.getLatitude();
                spot[array_nb][1] = tMapPoint.getLongitude();

                marker(spot[array_nb][0],spot[array_nb][1]);


                if(array_nb >= 1) path();
            }

        });
        tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, final TMapPoint tMapPoint, PointF pointF) {

                return false;
            }

            @Override
            public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                return false;
            }
        });



    }
    // 보행자 경로 그어주기
    public void path(){
        new Thread(){
            @Override
            public void run(){
                try{
                        for(int i = 0; i <array_nb; i++) {
                            start_point = new TMapPoint(spot[i][0],spot[i][1]);
                            end_point = new TMapPoint(spot[i+1][0],spot[i+1][1]);

                            TMapPolyLine tMapPolyLine = new TMapData().findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, start_point, end_point);
                            tMapPolyLine.setLineColor(Color.BLUE);
                            tMapPolyLine.setLineWidth(5);
                            tMapView.addTMapPolyLine("Line1"+i, tMapPolyLine);
                        }
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
                tMapView.setLocationPoint(longitude,latitude);
                tMapView.setCenterPoint(longitude,latitude);
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