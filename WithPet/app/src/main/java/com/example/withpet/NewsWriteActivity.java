package com.example.withpet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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

    //파베 데이터 작성 함수
    private void writeNewUser(String num, String id, String context, String imgUrl) {
        News news = new News(id, imgUrl, context);

        dbreference.child(num).setValue(news)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Toast.makeText(NewsWriteActivity.this, "저장을 완료했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //파베 저장소에 feed 폴더에 사진 저장
    public void uploadImage(String input){
        Uri file = Uri.fromFile(new File(input));
        imgRf = storageRf.child("feed/"+file.getLastPathSegment());
        UploadTask uploadTask = imgRf.putFile(file);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(NewsWriteActivity.this, "업로드 성공", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //파베 저장소에서 url 다운로드 해서 데베 등록
    public void Image(String input){
        String[] path = input.split("/");
        storageRf.child("feed/" +path[path.length - 1]).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                EditText et =findViewById(R.id.mainwEt_context);
                inputContext = et.getText().toString();
                writeNewUser("ang","ang",inputContext, uri.toString());
            }
        });
    }
}