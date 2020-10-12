package com.withpet_manager.userfeed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.withpet_manager.*;

public class feedDetailActivity extends AppCompatActivity {

    private Button btnCancle, btnDelete;
    private TextView tvCategroy, tvName, tvDate, tvContext;
    private ImageView ivImg;
    private Intent intent;
    private String boardName,category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);

        //화면전환시 아래에서 위로 올라오는 애니메이션 제거
        overridePendingTransition(0, 0);

        tvCategroy =(TextView) findViewById(R.id.fdTv_category);
        tvName = (TextView)findViewById(R.id.fdTv_name);
        tvDate = (TextView) findViewById(R.id.fdTv_date);
        tvContext = (TextView) findViewById(R.id.fdTv_context);
        ivImg = (ImageView) findViewById(R.id.fdIv);
        btnCancle = (Button) findViewById(R.id.fdBtn_cancel);
        btnDelete = (Button) findViewById(R.id.fdBtn_del);

        btnCancle.setOnClickListener(onClickListener);
        btnDelete.setOnClickListener(onClickListener);

        intent = getIntent();
        boardName = getIntent().getStringExtra("boardName");
        category = getIntent().getStringExtra("category");
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Feed");
        databaseReference.child(boardName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Feed feed = snapshot.getValue(Feed.class);
                    if(boardName.equals(feed.getNewsName())){
                        tvCategroy.setText(category);
                        tvName.setText(feed.getUid());
                        tvContext.setText(feed.getContext());
                        tvDate.setText(feed.getDate());
                        Glide.with(feedDetailActivity.this).load(feed.getImgUrl()).into(ivImg);
                    }
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(feedDetailActivity.this);
                    builder.setTitle("[ 알림 ]");
                    builder.setMessage("해당하는 게시글을 찾을 수가 없습니다."); // 메시지
                    //버튼 클릭시 동작
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.fdBtn_cancel:
                    AlertDialog.Builder cancel = new AlertDialog.Builder(feedDetailActivity.this);
                    cancel.setTitle("[ 알림 ]");
                    cancel.setMessage("어떻게 하려고오오옹~ 신고 접수된 게시글 취소할 땐 어떻게 할꺼야~~~~~~~"); // 메시지
                    //버튼 클릭시 동작
                    cancel.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                    cancel.show();
                    break;
                case R.id.fdBtn_del:
                    AlertDialog.Builder builder = new AlertDialog.Builder(feedDetailActivity.this);
                    builder.setTitle("[ 알림 ]");
                    builder.setMessage("신고 접수된 해당 게시글을 삭제하시겠습니까?"); // 메시지
                    //버튼 클릭시 동작
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = firebaseDatabase.getReference("Feed");
                            databaseReference.child(boardName).removeValue();
                            Toast.makeText(feedDetailActivity.this, "정상적으로 처리했습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    builder.show();
                    break;
            }
        }
    };
}