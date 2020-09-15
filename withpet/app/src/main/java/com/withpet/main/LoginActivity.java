package com.withpet.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.withpet.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPw;
    private String strEmail, strPw;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        etEmail = (EditText) findViewById(R.id.loginEt_id);
        etPw = (EditText) findViewById(R.id.loginEt_pw);

    }

    public void btnClick(View view){
        switch (view.getId()){
            case R.id.loginBtn_login:
                //로그인할 이메일과 비밀번호
                strEmail = etEmail.getText().toString().trim();
                strPw = etPw.getText().toString().trim();
                //파이어베이스 '인증' >> 이메일로그인
                firebaseAuth.signInWithEmailAndPassword(strEmail, strPw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent main = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(main);
                            finish();
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "일치하는 회원이 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.loginBtn_join:
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}