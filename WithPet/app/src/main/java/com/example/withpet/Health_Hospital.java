package com.example.withpet;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.skt.Tmap.TMapView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*
public class Health_Hospital extends AppCompatActivity {
/*    final static String TAG = "XML";
    Tmap tMapView;
    ArrayList<Hospital> list = parser();
    String[] data = new String[list.size()];*/

//        for(int i = 0; i < list.size(); i++){
//            data[i] = list.get(i).getName()+" "+list.get(i).getAddres()
//                    +" "+list.get(i).getX()+" "+list.get(i).getY();
//
//            Log.d("데이터 : ",""+data[i]);
//        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_health__hospital);
/*

        LinearLayout health_hospital_map =(LinearLayout) findViewById(R.id.health_hospital_map);
        tMapView = new TMapView(this);

        tMapView.setSKTMapApiKey("l7xxfa281c47f54b4b8d866946553f981932");
        health_hospital_map.addView(tMapView);
*/




    }
    private void setupMap(){

    }

/*    private ArrayList<Hospital> parser() {
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
}
*/
*/
