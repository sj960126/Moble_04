package com.example.withpet;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Walk_boardwrite extends AppCompatActivity {

    private Button walkBtn_add;
    private TextView walkTv_title;
    private TextView walkTv_content;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_boardwrite);

        walkBtn_add = findViewById(R.id.addbtn);
        walkTv_title = findViewById(R.id.walk_title);
        walkTv_content = findViewById(R.id.walk_content);

        databaseReference = FirebaseDatabase.getInstance().getReference("walk-board");
        walkBtn_add.setOnClickListener(new Button.OnClickListener() { //글작성하기
            @Override
            public void onClick(View view) {

                walk_upload();

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("frag",2); // 산책 frag로 가기위해 intent값 전달
                startActivity(intent);
                finish();
            }
        });


    }

    private void walk_upload(){
        Walk_boardUpload upload = new Walk_boardUpload(walkTv_title.getText().toString().trim(), walkTv_content.getText().toString().trim());
        String uploadId = "99999999"; //사용자 아이디로 하면 될거같다
        //databaseReference.child(uploadId).setValue(upload);
        databaseReference.push().setValue(upload);
    }

}