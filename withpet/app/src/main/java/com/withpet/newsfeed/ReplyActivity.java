package com.withpet.newsfeed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.withpet.*;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReplyActivity extends AppCompatActivity {

    private String boardName;
    private Button btnBefore;
    private CircleImageView civNewsUser;
    private TextView tvNewsId, tvNewsContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        Intent intent = getIntent();
        boardName = intent.getStringExtra("boardName");

        btnBefore = (Button) findViewById(R.id.replyBtn_before);
        civNewsUser = (CircleImageView) findViewById(R.id.replyIv_newimg);
        tvNewsId = (TextView) findViewById(R.id.replyTv_newid);
        tvNewsContext =(TextView) findViewById(R.id.replyTv_newcontext);

        //기본설정
        civNewsUser.setImageResource(R.drawable.userdefault);
        btnBefore.setBackgroundResource(R.drawable.iconbefore);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("wirteUserId",""+boardName);
    }

}