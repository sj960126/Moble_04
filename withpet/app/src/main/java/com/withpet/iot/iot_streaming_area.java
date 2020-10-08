package com.withpet.iot;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.withpet.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class iot_streaming_area extends AppCompatActivity {

    private TextView Show_Time_TextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iot_streaming_area);

        WebView webView = (WebView)findViewById((R.id.webview));
        webView.setWebViewClient(new WebViewClient()); //영상view선언
        webView.setBackgroundColor(255); //영상컬러

        webView.getSettings().setLoadWithOverviewMode(true); //영상꽉차게하기 true
        webView.getSettings().setUseWideViewPort(true); //포트번호 true

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // 화면 비율
        webSettings.setUseWideViewPort(true);       // wide viewport를 사용하도록 설정
        webSettings.setLoadWithOverviewMode(true);  // 컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정

        // 웹뷰 멀티 터치 가능하게 (줌기능)
        webSettings.setBuiltInZoomControls(true);   // 줌 아이콘 사용
        webSettings.setSupportZoom(true);

        webView.loadUrl("http://192.168.0.4:8091/stream_simple.html");

        Show_Time_TextView = (TextView)findViewById(R.id.iot_dateNow) ;
        ShowTimeMethod();

        //dateNow=(TextView)findViewById(R.id.iot_dateNow);
        //dateNow.setText(formatDate);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    public void ShowTimeMethod(){
        final Handler handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(Message msg){
                Show_Time_TextView.setText(DateFormat.getDateTimeInstance().format(new Date()));
            }
        };
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(1);
                }
            }

        };
            Thread thread = new Thread(task);
            thread.start();
        }
}



