package com.withpet.iot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.withpet.R;

import java.io.File;
import java.io.IOException;

public class iot_recorder extends AppCompatActivity {

    private static final int SCALE = 2 ;
    MediaRecorder recorder;
    String filename;
    ImageButton img2;
    ImageButton img3;

    MediaPlayer player;
    int position = 0; // 다시 시작 기능을 위한 현재 재생 위치 확인 변수
    int click = 0;
    // <음성녹음(MediaRecorder) 순서도>
    // (1) 미디어레코더 객체생성
    // (2) 오디오 입력 및 출력 형식 설정
    // (3) 오디오 인코더와 파일 지정
    // (4) 녹음시작
    // (5) 매니페스트에 권한설정

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iot_recorder);

        img2 = (ImageButton) findViewById(R.id.play);
        img3 = (ImageButton) findViewById(R.id.stop);

        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.iconrecorder_start_); //비트맵 선언
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE, true); //비트맵 커스텀
        img2.setImageBitmap(bitmap);


        Bitmap bitmap1 = BitmapFactory.decodeResource(this. getResources(),R.drawable.iconrecorder_stop_); //비트맵 선언
        bitmap1 = Bitmap.createScaledBitmap(bitmap1, bitmap1.getWidth() / SCALE, bitmap1.getHeight() / SCALE, true); //비트맵 커스텀
        img3.setImageBitmap(bitmap1);


        permissionCheck();

        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, "recorded.mp4");
        filename = file.getAbsolutePath();
        Log.d("MainActivity", "저장할 파일 명 : " + filename);

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAudio();
            }
        });

        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAudio();
            }
        });

//        findViewById(R.id.record).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                recordAudio();
//            }
//        });
//
//        findViewById(R.id.recordStop).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                stopRecording();
//            }
//        });


        image = (ImageView) findViewById(R.id.image);

        Glide.with(iot_recorder.this).load(R.drawable.iconrecorder_mic).into(image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click%2 == 0 || click == 0) {
                    Glide.with(iot_recorder.this).load(R.drawable.iconrecorder_mic).into(image);
                    if (recorder != null) {
                        recorder.stop();
                        recorder.release();
                        recorder = null;
                        Toast.makeText(getApplicationContext(),"녹음 중지됨", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Glide.with(iot_recorder.this).load(R.raw.audio_active).into(image);
                    recorder = new MediaRecorder();

                    /* 그대로 저장하면 용량이 크다.
                     * 프레임 : 한 순간의 음성이 들어오면, 음성을 바이트 단위로 전부 저장하는 것
                     * 초당 15프레임 이라면 보통 8K(8000바이트) 정도가 한순간에 저장됨
                     * 따라서 용량이 크므로, 압축할 필요가 있음 */
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 어디에서 음성 데이터를 받을 것인지
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); // 압축 형식 설정, 포멧설정
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT); // Encoder를 성정해주는 함수

                    // setOutputFormat함수는 setAudioSource함수보다 뒤에 그리고 prepare보다 앞에서 호출되어야한다.

                    recorder.setOutputFile(filename);

                    try {
                        recorder.prepare(); //  recording 을 준비하는 단계
                        recorder.start();

                        Toast.makeText(getApplicationContext(),"녹음 시작됨",Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                click++;
                if(click==3){
                    click=0;
                }

            }
        });

    }


//    private void recordAudio() {
//        recorder = new MediaRecorder();
//
//        /* 그대로 저장하면 용량이 크다.
//         * 프레임 : 한 순간의 음성이 들어오면, 음성을 바이트 단위로 전부 저장하는 것
//         * 초당 15프레임 이라면 보통 8K(8000바이트) 정도가 한순간에 저장됨
//         * 따라서 용량이 크므로, 압축할 필요가 있음 */
//        recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 어디에서 음성 데이터를 받을 것인지
//        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); // 압축 형식 설정, 포멧설정
//        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT); // Encoder를 성정해주는 함수
//
//        // setOutputFormat함수는 setAudioSource함수보다 뒤에 그리고 prepare보다 앞에서 호출되어야한다.
//
//        recorder.setOutputFile(filename);
//
//        try {
//            recorder.prepare(); //  recording 을 준비하는 단계
//            recorder.start();
//
//            Toast.makeText(this, "녹음 시작됨.", Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void stopRecording() {
//        if (recorder != null) {
//            recorder.stop();
//            recorder.release();
//            recorder = null;
//            Toast.makeText(this, "녹음 중지됨.", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void playAudio() {
        try {
            closePlayer();

            player = new MediaPlayer();
            player.setDataSource(filename);
            player.prepare();
            player.start();

            Toast.makeText(this, "재생 시작됨.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void stopAudio() {
        if (player != null && player.isPlaying()) {
            player.stop();

            Toast.makeText(this, "중지됨.", Toast.LENGTH_SHORT).show();
        }
    }

    public void closePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    public void permissionCheck(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1);
        }
    }


}