package com.withpet.Chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.withpet.*;
import com.withpet.main.*;

//종희
public class ChattingActivity extends AppCompatActivity {
    private TransUser opponent;
    private String meid;
    EditText et_sendmeaasge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        Intent intent = getIntent();
        et_sendmeaasge = findViewById(R.id.chattingEt_input);
        opponent = (TransUser)intent.getSerializableExtra("Opponent");
        meid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        findViewById(R.id.chattingBtn_send).setOnClickListener(onClickListener);
        Toast.makeText(this, "nickname"+opponent.getNickname()+", uid : "+opponent.getUid(), Toast.LENGTH_SHORT).show();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference dbreference = db.getReference("Message");
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.chattingBtn_send:
                    Chat chat = new Chat(meid, et_sendmeaasge.getText().toString());
                    break;
            }
        }
    };

}