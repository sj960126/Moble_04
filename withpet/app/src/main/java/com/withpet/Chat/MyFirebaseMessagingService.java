package com.withpet.Chat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.withpet.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    // 새 토큰 이 생성 되었을 때 마다 호출 되는 onNewToken 함수 호출
    // 1. 앱에서 인스턴스 id삭제
    // 2. 앱 재설치
    // 3. 사용자 앱 데이터 소거
    // 4. 새 기기에 앱 복원
    private final String TAG = "Myfirebase";
    private String msg, title;
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.i("firebase 클래스", " 생성토큰" + s);
    }

    //foreground 상태에서 메시지 수신되면 호출출
   @Override
   public void onMessageReceived(RemoteMessage remoteMessage) {
       Log.i("확인용", "실행");
       title = remoteMessage.getNotification().getTitle();
       msg = remoteMessage.getNotification().getBody();

       Intent intent = new Intent(this, tmepActivity.class);
       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

       PendingIntent contentIntent  = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
       String channelId = getString(R.string.chanel_id);
       NotificationCompat.Builder mBulider = new NotificationCompat.Builder(this, channelId)
               .setSmallIcon(R.mipmap.ic_launcher)
               .setContentTitle(title)
               .setContentText(msg)
               .setAutoCancel(true)         // 자동 닫기
               .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
               .setVibrate(new long[] {1, 1000});
       mBulider.setContentIntent(contentIntent);

       NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           // Create channel to show notifications.
           String channelName = getString(R.string.chanel_name);
           NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
           notificationManager.createNotificationChannel(channel);
       }
       notificationManager.notify(0, mBulider.build());


   }

}
