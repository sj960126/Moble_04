package com.withpet.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.withpet.*;
import com.withpet.Chat.NotifyApplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPw;
    private String strEmail, strPw;
    private Button btnLogin, btnJoin, btnFind;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private ArrayList<User> allUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        etEmail = (EditText) findViewById(R.id.loginEt_id);
        etPw = (EditText) findViewById(R.id.loginEt_pw);

        btnFind = (Button) findViewById(R.id.loginBtn_findpw);
        btnJoin = (Button) findViewById(R.id.loginBtn_join);
        btnLogin = (Button) findViewById(R.id.loginBtn_login);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        DatabaseReference databaseReference = firebaseDatabase.getReference("User");
        allUser = new ArrayList<>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allUser.clear();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor removeEditor = preferences.edit();
                removeEditor.clear();
                removeEditor.commit();

                //회원정보 xml파일 추가
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    allUser.add(0, dataSnapshot.getValue(User.class));
                    SharedPreferences sharedPreferences = getSharedPreferences(allUser.get(0).getUid(), MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("name", allUser.get(0).getName());
                    editor.putString("nickName", allUser.get(0).getNickname());
                    editor.putString("img", allUser.get(0).getImgUrl());
                    editor.commit();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser LoginUser = firebaseAuth.getCurrentUser();
        //로그인 유저가 있고, 이메일 인증이 되었다면
        /*if (LoginUser != null) {
            if(LoginUser.isEmailVerified()){
                Login();
            }
            else{
                Toast.makeText(this, "인증메일을 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        }*/
        if(((NotifyApplication)getApplication()).getIslogin()){
            if(LoginUser.isEmailVerified()){
                Login();
            }
            else{
                Toast.makeText(this, "인증메일을 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void btnClick(View view){
        switch (view.getId()){
            case R.id.loginBtn_login:
                //로그인할 이메일과 비밀번호
                Log.i("login","d");
                strEmail = etEmail.getText().toString().trim();
                strPw = etPw.getText().toString().trim();
                if(strEmail.equals("") || strPw.equals("")){
                    Toast.makeText(this, "로그인 정보를 정확히 기입하세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    //파이어베이스 '인증' >> 이메일로그인
                    firebaseAuth.signInWithEmailAndPassword(strEmail, strPw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // 가입  && 이메일인증 완료
                            if(task.isSuccessful()){
                                FirebaseUser login = firebaseAuth.getCurrentUser();
                                if(login.isEmailVerified()){
                                    Login();
                                }else{
                                    Toast.makeText(LoginActivity.this, "가입한 이메일로 전송한 인증메일을 수락하세요.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "로그인 오류 : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                try{

                                }catch (Exception e){
                                    Toast.makeText(LoginActivity.this, "시스템 오류" + e, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
                break;
            case R.id.loginBtn_findpw:
                Intent find = new Intent(LoginActivity.this, FindPwActivity.class);
                startActivity(find);
                break;
            case R.id.loginBtn_join:
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void Login(){
        ((NotifyApplication)getApplication()).setIslogin(true);
        Intent main = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(main);
        finish();
    }
}