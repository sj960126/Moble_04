package com.withpet.mypage;

import android.media.Image;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Tcp_Client extends AsyncTask {
    protected static String Serv_ip = "192.168.0.12"; //서버의 ip주소
    protected static int port = 9999; //port번호
    private DataOutputStream dos;
    private DataInputStream dis;
    private String Uid;
    private Socket socket;

    Tcp_Client(String Uid) {
        this.Uid = Uid;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            InetAddress serveraddr = InetAddress.getByName(Serv_ip);
            socket = new Socket(serveraddr, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //안드로이드에 서버로 연결 요청
        try{
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            //버퍼생성 잘못됨
        }
        //버퍼생성 잘됨
        try{
            dos.writeUTF(Uid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
