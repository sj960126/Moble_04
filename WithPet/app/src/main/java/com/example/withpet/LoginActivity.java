package com.example.withpet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //화면전환시 아래에서 위로 올라오는 애니메이션 제거
        overridePendingTransition(0, 0);

    }

    public void btnClick(View view){
        switch (view.getId()){
            case R.id.loginBtn_login:
                Intent main = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(main);
                finish();
                break;
            case R.id.loginBtn_join:
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}