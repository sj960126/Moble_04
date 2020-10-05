package com.withpet.Chat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.withpet.R;
import com.withpet.main.MainActivity;
import com.withpet.main.TransUser;

import java.util.ArrayList;
import java.util.Random;

// 메인 ( 프래그먼트 )에서 실행법  : startForegroundService(intent)
// 사용 시 데이터 로딩이 늦음 채팅방
public class ForegroundService extends Service {
    private final int MAXRANDNUM = 5000;
    private NotifyApplication applicationinfo;
    private final int foregroundNotificationId = 1000;
    private ArrayList<ChattingRoom> chattingRoomdata;
    private Random rnd;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        applicationinfo = (NotifyApplication)getApplication();
        chattingRoomdata = new ArrayList<ChattingRoom>();
        rnd = new Random();
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, getString(R.string.chanel_id))
                .setContentTitle("With Pet Foreground Service")
                .setContentText("실행중")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(foregroundNotificationId, notification);

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = db.getReference("Chat");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    DatabaseReference chattingroomreference; // 내가 속한 채팅방의 정보를 갖고잇는 데이터베이스의 주소를 저장하는 변수
                    // 채팅방 목록 중 내가 속한 리스트 찾기(맨 처음 어플을 시작한 경우 로그인 정보가 없어 firebaseUser가 null이 아닐 때 확인)
                    if(firebaseUser != null &&ds.getKey().contains(firebaseUser.getUid())){
                        chattingroomreference = db.getReference("Chat/"+ds.getKey());    // 내가 속한 채팅방의 정보를 갖고있는 데이터 베이스 주소
                        ChattingRoom ctr = new ChattingRoom();
                        ctr.setChatroomname(ds.getKey());       // 채팅방 이름 저장
                        ctr.setChildcount(ds.getChildrenCount());   // 해당 채팅방의 채팅내역 수 저장
                        ctr.setNotificationid(createNotificationId());
                        chattingRoomdata.add(ctr);
                        // 내가 속한 1개의 채팅방의 ValueEventListener, 해당 채팅방에서 메시지를 보냈는지 감시하기 위함, 메시지를 보낸 내역이 있다면
                        chattingroomreference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // 맨처음 데이터를 읽었을 때보다 child의 숫자가 커졌을 때 push알림 발생
                                // 이 구문에서의 snapshot은 1개의 채팅방 정보, 이 구문 snapshot의 child 수는 해당 채팅방의 채팅내역수
                                // 따라서 해당 방에서 채팅내역 수가 처음 저장했던 채팅내역 수보다 많다는 것은 누군가 채팅을 쳤다는 것 그래서 알림 발생
                                for(ChattingRoom chattingRoom : chattingRoomdata) {
                                    // 이 구문에서의 snapshot.getKey() : 채팅방 이름
                                    // 내가 속한 채팅방의 정보들 중에서 채팅이 온 채팅방 이름을 비교 (이유 : 해당 채팅방의 child 수 비교하기 위함)
                                    if(snapshot.getKey().equals(chattingRoom.getChatroomname())){
                                        // snapshot.getChildrenCount() : 해당 채팅방의 실시간 채팅내역 수
                                        // chattingRoom.getChildcount() : 맨 처음 내가 속한 채팅방 정보를 읽을 때의 채팅내역 수
                                        if(snapshot.getChildrenCount() > chattingRoom.getChildcount()){
                                            // 마지막 채팅내역 가져오기
                                            Chat chat = new Chat();
                                            // 해당 채팅방의 모든 채팅내역이 snapshot에 있음, for문이 끝났을 때의 chat의 정보는 채팅방의 마지막 채팅 내용
                                            for(DataSnapshot chatdata : snapshot.getChildren()){
                                                chat = chatdata.getValue(Chat.class);
                                            }

                                            Log.i("확인", "메시지 보냄"+chat.getUid());
                                            String enterchattingroom  = (applicationinfo.getEnterChattingRoom() != null ) ? applicationinfo.getEnterChattingRoom() : "";
                                            if(!chat.getUid().equals(firebaseUser.getUid()) && !(snapshot.getKey().equals(enterchattingroom))){
                                                NotificationStart(chat.getUid(), chat.getContent(), chattingRoom.getNotificationid());
                                            }
                                        }
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    // 채널생성
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.chanel_name);
            String description = getString(R.string.channel_description);         // 알림 길게 눌렀을 때 설명 설정
            int importance = NotificationManager.IMPORTANCE_DEFAULT;            // 중요도 설정
            NotificationChannel channel = new NotificationChannel(getString(R.string.chanel_id), name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    // push 알림 발생 메소드
    private void NotificationStart(String uid, String content, int notificationid){
        // 알림 선택시 실행시킬 인텐트
        Intent chattingintent = new Intent(this, ChattingActivity.class);
        TransUser tuser = new TransUser(applicationinfo.getUser(uid));      // chat에서 가져온 uid로 유저 정보 가져오기
        chattingintent.putExtra("Opponent", tuser);     // 가져온 유저 정보 intent에 추가
        Log.i("유저확인", ""+((TransUser)chattingintent.getSerializableExtra("Opponent")).getUid());
        chattingintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // 알림 메시지 만들기
        // 알림 눌렀을 때 실행될 인텐트 생성
        PendingIntent contentIntent  = PendingIntent.getActivity(this, 0, chattingintent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = getString(R.string.chanel_id);
        NotificationCompat.Builder mBulider = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(tuser.getNickname())              // 알림 메시지 제목 설정
                .setContentText(content)                           // 알림 메시지 내용 설정
                .setAutoCancel(true)                               // 알림 클릭 시 자동 닫기
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))      // 알림 메시지 도착 시 알림음
                .setVibrate(new long[] {1, 1000});                  // 진동 설정
        mBulider.setContentIntent(contentIntent);                   // 알림을 눌렀을 때 실행될 인텐트 설정


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelName = getString(R.string.chanel_name);
        // API 26 이상에선 채널 필수, 채널 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {   // 오레오 이상 버전에서는 drawable을 사용
            // Create channel to show notifications.
            mBulider.setSmallIcon(R.drawable.ic_launcher_foreground);  // 아이콘 이미지 설정
        }else { // 오레오 하위 버전에서는 mipmap 으로 사용
            mBulider.setSmallIcon(R.mipmap.ic_launcher);  // 아이콘 이미지 설정
        }
        // 알림 채널 생성
        // 인수 1 : 채널 id, 2: 채널 이름, 3: 채널의 알림 중요도 정도
        // 알림 중요도
        // - IMPORTANCE_HIGH(긴급) : 알림음 울리며 헤드업 알림으로 표시, - IMPORTANCE_DEFAULT(높음) : 알림음 울림
        // - IMPORTANCE_LOW(중간) : 알림음이 없음, IMPORTANCE_MIN(낮음) : 알림음 없고 상태표시줄 표시 x
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);  // 노티피케이션 채널을 시스템에 등록
        notificationManager.notify(notificationid, mBulider.build());    // 노티피케이션 실행, id는 방마다 다르게 줘야할듯
    }
    // notificationid 생성(채팅방마다 다른 push알림을 출력하기 위함)
    public int createNotificationId(){
        int notificationid = 0;
        boolean codeoverlap = false;
        while(codeoverlap){
            codeoverlap = false;
            notificationid = rnd.nextInt(MAXRANDNUM);
            for(ChattingRoom cr : chattingRoomdata){
                if(cr.getNotificationid() == notificationid || notificationid == foregroundNotificationId){
                    codeoverlap = true;
                }
            }
        }
        return notificationid;
    }


}
