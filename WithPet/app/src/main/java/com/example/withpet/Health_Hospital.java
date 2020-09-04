package com.example.withpet;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.TMapView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Health_Hospital extends AppCompatActivity{
  final static String TAG = "XML";
  TMapView tMapView;
  private boolean TrackingMode = true;
  private  TMapGpsManager tMapGpsManager =null;
  private Context mcontext =null;
  private final String APK ="l7xxfa281c47f54b4b8d866946553f981932";
  private final int SCALE = 8;
  private final int SCALE2 = 10;
  double lat;
  double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_health_hospital);

//    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//    StrictMode.setThreadPolicy(policy);
    //네트워크 사용하기 위한 쓰레드

        mcontext =this;
        LinearLayout health_hospital_map =(LinearLayout)findViewById(R.id.health_hospital_map);
        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey(APK);
        health_hospital_map.addView(tMapView);

        marker();
//        setGps();
        tMapView.setCenterPoint(126.988205, 37.551135);

        tMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {

                lat = tMapMarkerItem.latitude; //위도
                lon = tMapMarkerItem.longitude; //경도

                TMapTapi tMapTapi = new TMapTapi(mcontext);
                
                tMapTapi.invokeRoute("출발지",(float)lon,(float)lat);
            }
        });

    }

    public void marker(){
        ArrayList<Hospital> list = parser();
        for (int i=0;i<list.size();i++) {
            TMapPoint point = new TMapPoint(Double.parseDouble(list.get(i).getX()), Double.parseDouble(list.get(i).getY()));
            TMapMarkerItem markerItem = new TMapMarkerItem();

            Bitmap bitmap = BitmapFactory.decodeResource(mcontext.getResources(),R.drawable.marker);
            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE, true);

            markerItem.setPosition(0.5f, 1.0f);
            markerItem.setTMapPoint(point);
            markerItem.setIcon(bitmap);

            Bitmap bitmap2 = BitmapFactory.decodeResource(mcontext.getResources(),R.drawable.marker2);
            bitmap2 = Bitmap.createScaledBitmap(bitmap2, bitmap2.getWidth() / SCALE2, bitmap2.getHeight() / SCALE2, true);

            markerItem.setCalloutRightButtonImage(bitmap2);
            markerItem.setCalloutTitle(list.get(i).getName()); //풍선뷰 메시지
            markerItem.setCalloutSubTitle(list.get(i).getAddres());
            markerItem.setCanShowCallout(true);
            markerItem.setAutoCalloutVisible(true);

            tMapView.addMarkerItem("marker" + i, markerItem);


        }
    }


    private ArrayList<Hospital> parser() {
        Log.i(TAG, "parser");
        ArrayList<Hospital> arrayList = new ArrayList<Hospital>();

        InputStream inputStream = getResources().openRawResource(R.raw.pet);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

        XmlPullParserFactory factory = null;
        XmlPullParser xmlParser = null;

        try {
            factory = XmlPullParserFactory.newInstance();
            xmlParser = factory.newPullParser();
            xmlParser.setInput(inputStreamReader);
            Hospital hospital = null;

            int eventType = xmlParser.getEventType();

            while (eventType!=XmlPullParser.END_DOCUMENT) {
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        Log.i(TAG, "xml START");
                        break;
                    case XmlPullParser.START_TAG:
                        //Log.i(TAG, "Start TAG :" + xmlParser.getName());
                        try {
                            String startTag = xmlParser.getName();
                            if (startTag.equals("row")) {
                                hospital = new Hospital();
                            }
                            if (startTag.equals("REFINE_LOTNO_ADDR")) {
                                hospital.setAddres(xmlParser.nextText());
                                /*Log.i(TAG, "TEXT : " + xmlParser.getText());
                                Log.i(TAG, "name : " + xmlParser.getName());
                                Log.i(TAG, "add : " + hospital.getAddres());*/
                            }
                            if (startTag.equals("BIZPLC_NM")) {
                                hospital.setName(xmlParser.nextText());
                                /*Log.i(TAG, "TEXT : " + xmlParser.getText());
                                Log.i(TAG, "TEXT : " + xmlParser.getName());
                                Log.i(TAG, "TEXT : " + hospital.getName());*/
                            }

                            if (startTag.equals("REFINE_WGS84_LAT")) {
                                hospital.setX(xmlParser.nextText().replace(" ", ""));
                                /*Log.i(TAG, "TEXT : " + xmlParser.getName());
                                Log.i(TAG, "TEXT : " + xmlParser.getName());
                                Log.i(TAG, "TEXT : " + hospital.getX());*/
                            }
                            if (startTag.equals("REFINE_WGS84_LOGT")) {
                                hospital.setY(xmlParser.nextText().replace(" ", ""));
                                /*Log.i(TAG, "TEXT : " + xmlParser.getName());
                                Log.i(TAG, "TEXT : " + xmlParser.getName());
                                Log.i(TAG, "TEXT : " + hospital.getY());*/
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        //Log.i(TAG, "End TAG : " + xmlParser.getName());
                        String endTag = xmlParser.getName();
                        if (endTag.equals("row")) {
                            arrayList.add(hospital);
                        }
                        break;
                }
                try {
                    eventType = xmlParser.next();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } finally{
            try{
                if(inputStreamReader !=null) inputStreamReader.close();
                if(inputStream !=null) inputStream.close();
            }catch(Exception e2){
                e2.printStackTrace();
            }
        }
        return arrayList;
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if(location !=null){
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                tMapView.setLocationPoint(longitude, latitude);
                tMapView.setCenterPoint(longitude, latitude);

                tMapView.setCompassMode(true); //현재 보는 방향
                tMapView.setIconVisibility(true); //현위치 아이콘 표시

                /*줌 레벨 설정 */
                tMapView.setZoomLevel(15);
                tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
                tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);

                /* 화면중심을 단말의 현재위치로 이동 */
                tMapView.setTrackingMode(true);
                tMapView.setSightVisible(true);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    public void setGps(){
        final LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치권한 탐색 허용 관련 내용
            }
            return;
        }

        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자(실내에선 NETWORK_PROVIDER 권장)
                1000, // 통지사이의 최소 시간간격 (miliSecond)
                1, // 통지사이의 최소 변경거리 (m)
                mLocationListener);
    }

}




