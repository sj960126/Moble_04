package com.withpet.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.withpet.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPw;
    private String strEmail, strPw;

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
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser LoginUser = firebaseAuth.getCurrentUser();
        loginCheck(LoginUser);
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
                            Login();
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

    public void Login(){
        Intent main = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(main);
        finish();
    }

    //로그인 정보확인인
  private void loginCheck(FirebaseUser user) {
        //로그인 유저가 있다면
        if (user != null) {
            Login();
        }
    }
}