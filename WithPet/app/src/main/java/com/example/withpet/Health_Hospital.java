package com.example.withpet;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.List;


public class Health_Hospital extends AppCompatActivity implements  TMapGpsManager.onLocationChangedCallback{
  final static String TAG = "XML";
  TMapView tMapView;
  private boolean TrackingMode = true;
  private  TMapGpsManager tMapGpsManager =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_health_hospital);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        LinearLayout health_hospital_map =(LinearLayout)findViewById(R.id.health_hospital_map);
        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("l7xxfa281c47f54b4b8d866946553f981932");
        health_hospital_map.addView(tMapView);

        marker();

        tMapView.setCompassMode(true);
        tMapView.setIconVisibility(true);

        tMapView.setZoomLevel(15);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);

        tMapGpsManager = new TMapGpsManager(Health_Hospital.this);
        tMapGpsManager.setMinTime(1000);
        tMapGpsManager.setMinDistance(5);
//        tMapGpsManager.setProvider(tMapGpsManager.NETWORK_PROVIDER);
//        tMapGpsManager.OpenGps();

        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);



    }

    public void marker(){
        ArrayList<Hospital> list = parser();
        //i<list.size()
        for (int i=0;i<20;i++) {
                TMapPoint point = new TMapPoint(Double.parseDouble(list.get(i).getX()), Double.parseDouble(list.get(i).getY()));
                TMapMarkerItem markerItem = new TMapMarkerItem();
                markerItem.setPosition(0.5f, 1.0f);
                markerItem.setTMapPoint(point);
                tMapView.setCenterPoint(Double.parseDouble(list.get(i).getX()), Double.parseDouble(list.get(i).getY()));
                tMapView.addMarkerItem("marker" + i, markerItem);
        }
    }
    //문제점 전체 사이즈 다 돌리면 시스템 꺼짐 20개나 사이즈가 작아지면 돌아감  지금 돌리면 돌아감

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
                            if (startTag.equals("REFINE_LOTNO_ADDR")) {
                                hospital.setAddres(xmlParser.nextText());
                                Log.i(TAG, "TEXT : " + xmlParser.getText());
                                Log.i(TAG, "name : " + xmlParser.getName());
                                Log.i(TAG, "add : " + hospital.getAddres());
                            }
                            if (startTag.equals("BIZPLC_NM ")) {
                                hospital.setName(xmlParser.nextText());
                                Log.i(TAG, "TEXT : " + xmlParser.getText());
                                Log.i(TAG, "TEXT : " + xmlParser.getName());
                                Log.i(TAG, "TEXT : " + hospital.getName());
                            }

                            if (startTag.equals("REFINE_WGS84_LAT")) {
                                hospital.setX(xmlParser.nextText().replace(" ", ""));
                                Log.i(TAG, "TEXT : " + xmlParser.getName());
                                Log.i(TAG, "TEXT : " + xmlParser.getName());
                                Log.i(TAG, "TEXT : " + hospital.getX());
                            }
                            if (startTag.equals("REFINE_WGS84_LOGT")) {
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


    @Override
    public void onLocationChange(Location location) {
        if (TrackingMode){
            tMapView.setLocationPoint(location.getLongitude(),location.getLatitude());
        }
    }
}




