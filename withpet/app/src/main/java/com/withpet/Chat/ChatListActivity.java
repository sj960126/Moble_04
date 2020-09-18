package com.withpet.Chat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.withpet.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

//종희
public class ChatListActivity extends AppCompatActivity {
    final int requestcode = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        findViewById(R.id.chatListBtn_Find).setOnClickListener(onClickListener);
        findViewById(R.id.chatListBtn_Back).setOnClickListener(onClickListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.chatListBtn_Back:
                    finish();
                    break;
                case R.id.chatListBtn_Find:
                    Intent intent = new Intent(view.getContext(), FindUserActivity.class);
                    startActivityForResult(intent, requestcode);
                    break;
            }
        }
    };

}