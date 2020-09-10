package com.withpet.walk;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.withpet.*;
import com.withpet.main.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;


public class Walk_boardwriteActivity extends AppCompatActivity {

    private Button walkBtn_add;
    private TextView walkTv_title;
    private TextView walkTv_content;
    private DatabaseReference databaseReference;
    private int[] nb_arr;
    int id_int;
    int uploadId;
    String uploadId_str;

    private int nb = 0;
    private boolean a = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_boardwrite);

        Intent intent = getIntent();
        uploadId = intent.getIntExtra("id",0);

        walkBtn_add = findViewById(R.id.addbtn);
        walkTv_title = findViewById(R.id.walk_title);
        walkTv_content = findViewById(R.id.walk_content);

        //
        databaseReference = FirebaseDatabase.getInstance().getReference("walk-board");
        walkBtn_add.setOnClickListener(new Button.OnClickListener() { //글작성하기
            @Override
            public void onClick(View view) {
                walk_upload();

                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private  void a(){

    }
    //산책 글 작성 제목, 내용 firebase에 업로드
    private void walk_upload(){
        uploadId_str = Integer.toString(uploadId);
        Walk_boardUpload upload = new Walk_boardUpload(walkTv_title.getText().toString().trim(), walkTv_content.getText().toString().trim(),uploadId);
        databaseReference.child(uploadId_str).setValue(upload);
    }

}