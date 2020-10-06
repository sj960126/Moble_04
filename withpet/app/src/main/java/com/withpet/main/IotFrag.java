package com.withpet.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.withpet.iot.*;
import com.withpet.*;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

//Mypage page
public class IotFrag extends Fragment {

    private View rootview;
    final String TAG = "TAG+MainActivity";
    private Handler mHandler;
    public InputStream dataInputStream;
    public OutputStream dataOutputStream;
    private Socket clientsocket;
    private String ip = "192.168.0.2";
    private int port =8035;
    private Button meal;
    private Button voicesend;
    private Button recorder;
    private Button gostreaming_btn;

    String socket_id;
    String uid;
    Thread thread;
    ImageButton lightButton;

    public static final int sub=1001;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_iot,container,false);
        // test주석입니다.
        meal = rootview.findViewById(R.id.iotBtn_AutoMeal);
        voicesend = rootview.findViewById(R.id.iotBtn_RecorderSend);
        recorder = rootview.findViewById(R.id.iotBtn_Recorder);
        gostreaming_btn = rootview.findViewById(R.id.iotBtn_WebViewStreaming);

        meal.setOnClickListener(onClickListener);
        voicesend.setOnClickListener(onClickListener);
        recorder.setOnClickListener(onClickListener);
        gostreaming_btn.setOnClickListener(onClickListener);
        return rootview;
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            ConnectThread th = new ConnectThread();
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            uid = firebaseUser.getUid();
            switch (v.getId()){
                case R.id.iotBtn_AutoMeal:
                    socket_id="1";
                    th.start();
                    break;
                case R.id.iotBtn_RecorderSend:
                    socket_id="2"+uid;

                    th.start();
                    break;
                case R.id.iotBtn_Recorder:
                    intent = new Intent(v.getContext(), iot_recorder.class);
                    startActivity(intent);
                    break;
                case R.id.iotBtn_WebViewStreaming:
                    intent = new Intent(v.getContext(), iot_streaming_area.class);
                    startActivity(intent);
                    break;
            }

        }
    };

    class ConnectThread extends Thread{//소켓통신을 위한 스레드
        public void run(){
            try{
                //소켓 생성
                InetAddress serverAddr = InetAddress.getByName(ip);
                clientsocket =  new Socket(serverAddr,port);
                //입력 메시지
                String sndMsg = socket_id;
                Log.d("=============", sndMsg);
                //데이터 전송
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter
                        (clientsocket.getOutputStream())),true);
                out.println(sndMsg);

                clientsocket.close();

            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
