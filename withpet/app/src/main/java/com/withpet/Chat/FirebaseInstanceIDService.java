package com.withpet.Chat;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.i("확인", token);

        sendRegistrationToServer(token);
    }
    private  void sendRegistrationToServer(String token){

    }
}
