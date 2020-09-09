package com.withpet.newsfeed;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.withpet.*;
import com.withpet.main.*;

import java.io.File;

public class NewsWriteActivity extends AppCompatActivity {

    private FirebaseDatabase db;
    private DatabaseReference dbreference;
    private String input;
    private String inputContext;
    private FirebaseStorage storage;
    private StorageReference storageRf;
    private StorageReference imgRf;
    private String imgToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_write);
        //화면전환시 아래에서 위로 올라오는 애니메이션 제거
        overridePendingTransition(0, 0);

        Button btn = (Button) findViewById(R.id.mainwBtn_finish);
        btn.setBackgroundResource(R.drawable.iconcheck);

        //파베 연결 및 연동
        db = FirebaseDatabase.getInstance();
        dbreference = db.getReference("Feed");
        storage= FirebaseStorage.getInstance();
        storageRf = storage.getReference();

        input = getIntent().getStringExtra("imgId");
        //선택한 사진 불러오기
        ImageView iv = (ImageView) findViewById(R.id.mainwIv_thumbnail);
        Glide.with(this).load(input).override(1000).into(iv);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage(input);
                Image(input);
                //페이지 이동
                Intent intent = new Intent(NewsWriteActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //파베 데베에 데이터 작성
    private void writeNewUser(String boardName, String id, String context, String imgUrl) {
        //객체 데이터
        News news = new News(id, imgUrl, context);

        //dbreference 는 feed 테이블과 연결
        //feed > boardName > news data 추가
        //addOnSuccessListener 와 addOnFailureListener 개발가이드
        dbreference.child(boardName).setValue(news)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Toast.makeText(NewsWriteActivity.this, "저장을 완료했습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Toast.makeText(NewsWriteActivity.this, "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //파베 저장소의 feed 폴더에 사진 저장
    public void uploadImage(String input){
        Uri file = Uri.fromFile(new File(input));
        imgRf = storageRf.child("feed/"+file.getLastPathSegment());
        UploadTask uploadTask = imgRf.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(NewsWriteActivity.this, "업로드 성공", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //파베 저장소에서 url 다운로드 해서 데베 등록
    public void Image(final String input){
        final String[] path = input.split("/");

        storageRf.child("feed/" +path[path.length - 1]).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //게시글 내용
                EditText et =findViewById(R.id.mainwEt_context);
                inputContext = et.getText().toString();

                //사진이미지에서 . 대신에 공백 why? 파이어베이스 데베에서 . # $ [] 등과 같은 특수문자 허용 안됨.
                //보통 사진이미지에서 사용되는 특수문자는 ㅡ . 이기에 .만 제거했움...
                String board_name = path[path.length-1];
                board_name = board_name.replace(".","");
                //Log.i("name ::: ", board_name);

                //파이어베이스에 등록
                //게시글 이름을 사용자ID + 작성한이미지이름
                writeNewUser( "ididid" + board_name ,"ididididdi",inputContext, uri.toString());
            }
        });
    }
}