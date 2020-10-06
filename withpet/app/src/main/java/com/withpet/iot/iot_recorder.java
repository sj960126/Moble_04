package com.withpet.iot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.withpet.R;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class iot_recorder extends AppCompatActivity {

    private DatabaseReference databaseReference_;
    private FirebaseDatabase database;
    private FirebaseUser firebaseUser;
    private static final int SCALE = 2 ;

    ImageButton img2;
    ImageButton img3;
    Button upload_btn;
    int click = 0;
    ImageView image;

    String AudioSavePathInDevice;
    MediaRecorder mediaRecorder;

    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iot_recorder);
        database = FirebaseDatabase.getInstance();
       // storage = FirebaseStorage.getInstance().getReference();
        img2 = (ImageButton) findViewById(R.id.play);
        img3 = (ImageButton) findViewById(R.id.stop);
        upload_btn = findViewById(R.id.iot_btn_audiosend);

        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.iconrecorder_start_); //비트맵 선언
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE, true); //비트맵 커스텀
        img2.setImageBitmap(bitmap);

        Bitmap bitmap1 = BitmapFactory.decodeResource(this. getResources(),R.drawable.iconrecorder_stop_); //비트맵 선언
        bitmap1 = Bitmap.createScaledBitmap(bitmap1, bitmap1.getWidth() / SCALE, bitmap1.getHeight() / SCALE, true); //비트맵 커스텀
        img3.setImageBitmap(bitmap1);


        AudioSavePathInDevice = this.getExternalCacheDir() + "/realAudioRecording.mp4";


        img2.setOnClickListener(new View.OnClickListener() { //play
            @Override
            public void onClick(View view) {

                mediaPlayer = new MediaPlayer();

                try {
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();

                Toast.makeText(iot_recorder.this, "녹음파일 재생", Toast.LENGTH_LONG).show();

            }
        });

        img3.setOnClickListener(new View.OnClickListener() { //stop
            @Override
            public void onClick(View view) {
                if(mediaPlayer != null){

                    mediaPlayer.stop();
                    mediaPlayer.release();

                    MediaRecorderReady();

                    Toast.makeText(iot_recorder.this, "녹음파일 정지", Toast.LENGTH_SHORT).show();
                }
            }
        });

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReferenceFromUrl("gs://practice-d557f.appspot.com").child("recorder").child(firebaseUser.getUid()+"/recorder.mp4");
                Toast.makeText(iot_recorder.this, "경로 : "+new File(AudioSavePathInDevice), Toast.LENGTH_SHORT).show();
                Uri recorderfile = Uri.fromFile(new File(AudioSavePathInDevice));
                storageReference.putFile(recorderfile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(iot_recorder.this, "업로드 완료", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(iot_recorder.this, "업로드 실패", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });



        image = (ImageView) findViewById(R.id.image);

        Glide.with(iot_recorder.this).load(R.drawable.iconrecorder_mic).into(image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click++;  // 버튼 눌렀을 때마다 이미지 변환 및 액티비티 실행 (홀짝으로 나눔)

                if(click%2 == 1 || click == 1) { //start
                    Glide.with(iot_recorder.this).load(R.raw.audio_active).into(image);
                    Log.i("path :: ", AudioSavePathInDevice);
                    if(checkPermission()) {

                        MediaRecorderReady();

                        try {
                            mediaRecorder.prepare();
                            mediaRecorder.start();

                        } catch (IllegalStateException e) {
                            // TODO Auto-generated catch block

                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block

                            e.printStackTrace();
                        }

                        Toast.makeText(iot_recorder.this, "녹음 시작.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        requestPermission();
                    }

                }else if(click%2 == 0){ //stop
                    Glide.with(iot_recorder.this).load(R.drawable.iconrecorder_mic).into(image);

                    mediaRecorder.stop();
                    //    mediaPlayer.release();
                    Toast.makeText(getApplicationContext(), "녹음 중지", Toast.LENGTH_SHORT).show();
                    click = 0;
                }

            }
        });


    }


    public void MediaRecorderReady(){

        mediaRecorder=new MediaRecorder();

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);

        mediaRecorder.setOutputFile(AudioSavePathInDevice);

    }



    private void requestPermission() {

        ActivityCompat.requestPermissions(iot_recorder.this, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {

                    boolean StoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {

                        Toast.makeText(iot_recorder.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(iot_recorder.this,"Permission Denied",Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }

    public boolean checkPermission() {

        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }


}
