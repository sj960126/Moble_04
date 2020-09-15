package com.withpet.newsfeed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.withpet.*;

public class ReplyActivity extends AppCompatActivity {

    private String boardName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        Intent intent = getIntent();
        boardName = intent.getStringExtra("boardName");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("wirteUserId",""+boardName);
    }

}