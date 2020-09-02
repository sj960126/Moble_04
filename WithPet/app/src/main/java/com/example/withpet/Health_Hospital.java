package com.example.withpet;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.skt.Tmap.TMapCircle;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;



public class Health_Hospital extends AppCompatActivity{
  final static String TAG = "XML";
  TMapView tMapView;
  final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_health_hospital);
    Toast.makeText(this,"하이",Toast.LENGTH_SHORT).show();


//        LinearLayout health_hospital_map =(LinearLayout)findViewById(R.id.health_hospital_map);
//
//        tMapView = new TMapView(this);
//        tMapView.setSKTMapApiKey("l7xxfa281c47f54b4b8d866946553f981932");
//        health_hospital_map.addView(tMapView);

//        setUpMap();
//        setGps();

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
                        Log.i(TAG, "Start TAG :" + xmlParser.getName());
                        try {
                            String startTag = xmlParser.getName();
                            if (startTag.equals("row")) {
                                hospital = new Hospital();
                            }
                            if (startTag.equals("siteWhlAddr")) {
                                hospital.setAddres(xmlParser.nextText());
                                Log.i(TAG, "TEXT : " + xmlParser.getText());
                                Log.i(TAG, "name : " + xmlParser.getName());
                                Log.i(TAG, "add : " + hospital.getAddres());
                            }
                            if (startTag.equals("bplcNm")) {
                                hospital.setName(xmlParser.nextText());
                                Log.i(TAG, "TEXT : " + xmlParser.getText());
                                Log.i(TAG, "TEXT : " + xmlParser.getName());
                                Log.i(TAG, "TEXT : " + hospital.getName());
                            }

                            if (startTag.equals("x")) {
                                hospital.setX(xmlParser.nextText().replace(" ", ""));
                                Log.i(TAG, "TEXT : " + xmlParser.getName());
                                Log.i(TAG, "TEXT : " + xmlParser.getName());
                                Log.i(TAG, "TEXT : " + hospital.getX());
                            }
                            if (startTag.equals("y")) {
                                hospital.setY(xmlParser.nextText().replace(" ", ""));
                                Log.i(TAG, "TEXT : " + xmlParser.getName());
                                Log.i(TAG, "TEXT : " + xmlParser.getName());
                                Log.i(TAG, "TEXT : " + hospital.getY());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        Log.i(TAG, "End TAG : " + xmlParser.getName());
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

                TMapPoint tMapPoint = new TMapPoint(latitude, longitude);

                TMapCircle tMapCircle = new TMapCircle();
                tMapCircle.setCenterPoint( tMapPoint );
                tMapCircle.setRadius(300);
                tMapCircle.setCircleWidth(2);
                tMapCircle.setLineColor(Color.BLUE);
                tMapCircle.setAreaColor(Color.GRAY);
                tMapCircle.setAreaAlpha(100);
                tMapView.addTMapCircle("circle1", tMapCircle);

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

    private void setUpMap(){
        ArrayList<Hospital> list = parser();
        for (int i=0;i<list.size();i++){
            TMapPoint point = new TMapPoint(Double.parseDouble(list.get(i).getX()),Double.parseDouble(list.get(i).getY()));
            TMapMarkerItem markerItem1 = new TMapMarkerItem();
            markerItem1.setIcon(bitmap);
            markerItem1.setTMapPoint(point);
            tMapView.addMarkerItem("marker"+i,markerItem1);
        }
    }

    public void setGps(){
        final LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//           ContextCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자(실내에선 NETWORK_PROVIDER 권장)
                1000, // 통지사이의 최소 시간간격 (miliSecond)
                1, // 통지사이의 최소 변경거리 (m)
                mLocationListener);
    }

}




