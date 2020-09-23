package com.withpet.newsfeed;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.withpet.*;
import com.withpet.main.*;

import java.io.File;
import java.util.Date;

public class FeedWriteActivity extends AppCompatActivity {

    private FirebaseDatabase db;
    private DatabaseReference dbreference;
    private FirebaseStorage storage;
    private StorageReference storageRf;
    private StorageReference imgRf;
    private String inputContext, strImage,userNickname;
    private String modifyContext, modifyImg, modifyName, modifyDate, modifyUid;
    private Button btnUpload;
    private ImageView iv;
    private EditText et;

    //필요한 리소스 초기화
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_write);

        //화면전환시 아래에서 위로 올라오는 애니메이션 제거
        overridePendingTransition(0, 0);

        btnUpload = (Button) findViewById(R.id.mainwBtn_finish);
        btnUpload.setBackgroundResource(R.drawable.iconcheck);

        //파베 연결 및 연동
        db = FirebaseDatabase.getInstance();
        dbreference = db.getReference("Feed");
        storage= FirebaseStorage.getInstance();
        storageRf = storage.getReference();

        //선택한 갤러리 사진 이름 받기
        strImage = getIntent().getStringExtra("imgId");

        //수정할 게시글 내용 받기
        modifyImg = getIntent().getStringExtra("feedImg");
        modifyContext = getIntent().getStringExtra("feedContext");
        modifyName = getIntent().getStringExtra("feedName");
        modifyDate = getIntent().getStringExtra("feedDate");
        modifyUid = getIntent().getStringExtra("feedUid");

        //선택한 사진 불러오기
        iv = (ImageView) findViewById(R.id.mainwIv_thumbnail);
    }

    // 사용자와의 상호작용 (터치이벤트 및 등등)
    @Override
    protected void onResume() {
        super.onResume();
        //게시글 작성
        if(modifyContext == null && modifyImg == null && modifyName == null){
            //선택한 사진 불러오기
            Glide.with(this).load(strImage).override(1000).into(iv);
            //버튼이벤트
            btnUpload.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View view) {
                    //파베 저장소의 feed 폴더에 사진 업로드
                    Uri file = Uri.fromFile(new File(strImage));
                    imgRf = storageRf.child("feed/"+file.getLastPathSegment());
                    UploadTask uploadTask = imgRf.putFile(file);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //저장소에 업로드가 실패했을 경우
                            Toast.makeText(FeedWriteActivity.this, "Photo upload failure", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //파베 게시글 등록
                            uploadBoard(strImage);

                            //작성 페이지 > 메인페이지 이동
                            Intent intent = new Intent(FeedWriteActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });
        }
        //게시글 수정
        else{
            //선택한 사진 불러오기
            Glide.with(this).load(modifyImg).override(1000).into(iv);
            et =findViewById(R.id.mainwEt_context);
            et.setText(modifyContext);
            //버튼이벤트
            btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputContext = et.getText().toString();
                    writeNewUser(modifyName , modifyUid, inputContext, modifyImg, modifyName, modifyDate );

                    //작성 페이지 > 메인페이지 이동
                    Intent intent = new Intent(FeedWriteActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        }


    }

    //파베 데베에 데이터 작성
    private void writeNewUser(String boardName, String id, String context, String imgUrl, String newsName, String date) {
        //객체 데이터
        Feed feed = new Feed(id, imgUrl, context, newsName, date);

        //dbreference 는 feed 테이블과 연결
        //feed > boardName > news data 추가
        //addOnSuccessListener 와 addOnFailureListener 개발가이드
        dbreference.child(boardName).setValue(feed)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Toast.makeText(FeedWriteActivity.this, "Upload success.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Toast.makeText(FeedWriteActivity.this, "Upload failure.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //파베 저장소에서 url 다운로드 해서 데베 등록
    public void uploadBoard(final String input){
        final String[] path = input.split("/");

        storageRf.child("feed/" +path[path.length - 1]).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(Uri uri) {
                //게시글 내용
                EditText et =findViewById(R.id.mainwEt_context);
                inputContext = et.getText().toString();

/*                //사진이미지에서 . 대신에 공백 why? 파이어베이스 데베에서 . # $ [] 등과 같은 특수문자 허용 안됨.
                //보통 사진이미지에서 사용되는 특수문자는 ㅡ . 이기에 .만 제거했움...
                String board_name = path[path.length-1];
                board_name = board_name.replace(".","");*/

                userNickname =getIntent().getStringExtra("loginUserNickname");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                //휴대푠 현재 날짜 시간 값 가져오기
                //2020911_110935
                long now = System.currentTimeMillis();
                Date mDate = new Date(now);
                SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String getTime = mFormat.format(mDate);

                //게시글 이름을 사용자닉네임 + 현재 날짜시간
                writeNewUser( userNickname+getTime, user.getUid(), inputContext, uri.toString(),userNickname + getTime, getTime);
            }
        });
    }
}