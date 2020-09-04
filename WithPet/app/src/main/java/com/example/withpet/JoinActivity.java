package com.example.withpet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class JoinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        //화면전환시 아래에서 위로 올라오는 애니메이션 제거
        overridePendingTransition(0, 0);

    }

    public void checkClick(View view) {
        switch (view.getId()){
            case R.id.joinBtn_id:
                Toast.makeText(this, "ID 중복확인", Toast.LENGTH_SHORT).show();
                break;
            case R.id.joinBtn_nickname:
                Toast.makeText(this, "닉네임 중복확인", Toast.LENGTH_SHORT).show();
                break;
            case R.id.joinBtn_email:
                Toast.makeText(this, "이메일인증확인", Toast.LENGTH_SHORT).show();
                break;
            case R.id.joinBtn_ok:
                Toast.makeText(this, "회원가입", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}