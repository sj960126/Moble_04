package com.withpet.iot;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.withpet.*;

public class iot_streaming_area extends AppCompatActivity {


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

        webView.loadUrl("http://192.168.0.4:8091/stream_simple.html");

    }
}