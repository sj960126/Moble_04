package com.withpet.health;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.TMapView;
import com.withpet.*;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class HealthHospitalActivity extends AppCompatActivity {
    //개선점 : Tmap 다운로드 경로 제공 / 코드 길이 최소화 / 주석 삭제
  private TMapView tMapView;
  private boolean TrackingMode = true;
  private  TMapGpsManager tMapGpsManager =null;
  private Context mcontext =null;
  private final String APK ="l7xxfa281c47f54b4b8d866946553f981932";
  private final int SCALE = 8;
  private final int SCALE2 = 10;
  private double lat;
  private double lon;
  private String way;
  private Thread tr;
  private TMapTapi tMapTapi;
  private final static String TAG = "XML";
  private ArrayList<Hospital> list; //파싱 결과값
  private ProgressDialog dialog; // 로딩창

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_health_hospital);

    dialog = new ProgressDialog(this);
    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    dialog.setMessage("Loading");
    dialog.show();

    mcontext =this;
    LinearLayout health_hospital_map =(LinearLayout)findViewById(R.id.health_hospital_map);

    tMapView = new TMapView(this);
    tMapView.setSKTMapApiKey(APK);
    health_hospital_map.addView(tMapView);

    //setGps();
    tMapView.setCenterPoint(126.988205, 37.551135);

    tMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() { //풍선뷰 클릭 이벤트
        @Override
        public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {
            lat = tMapMarkerItem.latitude; //위도
            lon = tMapMarkerItem.longitude; //경도
            way = tMapMarkerItem.getName();
            tMapTapi = new TMapTapi(mcontext);
            boolean isTmapApp = tMapTapi.isTmapApplicationInstalled();
            if(isTmapApp ==true){
                tMapTapi.invokeRoute(way,(float)lon,(float)lat);
            }else{
                tmapinstall(); // 티맵  다운받는 메소드
            }
        }
    });
    new GetWeatherTask().execute();

    }
    public void tmapinstall(){
        ArrayList<String> arrayList = tMapTapi.getTMapDownUrl(); // 통신사별로 티맵 다운로드  uri를 arraylist로 저장한다
        if (arrayList !=null && arrayList.size() > 0){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(arrayList.get(0)));  //저장 받은 값을 intent한다
            startActivity(intent);
        }
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


    // MAIN UI 그리면서 백그라운드에서는 파싱을 해준다.
    class GetWeatherTask extends AsyncTask<Void, Void, Void> {

        // MAIN 스레드 중간에 백그라운드에서 작업할 내용
        @Override
        protected Void doInBackground(Void... params) {

            //파싱한 결과값을 담을 해쉬맵
            ArrayList<Hospital> arrayList = new ArrayList<Hospital>();

            InputStream inputStream = mcontext.getResources().openRawResource(R.raw.pet);
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
                            try {
                                String startTag = xmlParser.getName();
                                if (startTag.equals("row")) {
                                    hospital = new Hospital();
                                }
                                if (startTag.equals("REFINE_LOTNO_ADDR")) {
                                    hospital.setAddres(xmlParser.nextText());
                                }
                                if (startTag.equals("BIZPLC_NM")) {
                                    hospital.setName(xmlParser.nextText());
                                }

                                if (startTag.equals("REFINE_WGS84_LAT")) {
                                    hospital.setX(xmlParser.nextText().replace(" ", ""));
                                }
                                if (startTag.equals("REFINE_WGS84_LOGT")) {
                                    hospital.setY(xmlParser.nextText().replace(" ", ""));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case XmlPullParser.END_TAG:
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

            list = arrayList;

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

//         마커 완료 후 로딩 끄기
            dialog.cancel();

            return null;
        }

    }
}




