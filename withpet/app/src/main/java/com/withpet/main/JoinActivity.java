package com.withpet.main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.withpet.*;

import java.util.HashMap;

public class JoinActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private EditText etNickName, etEmail, etPw, etPwcheck, etName;
    private String strEmail, strNickname, strPw, strPw2, strName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        //화면전환시 아래에서 위로 올라오는 애니메이션 제거
        overridePendingTransition(0, 0);

        //파베 접근 설정
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("User");

        etName = (EditText) findViewById(R.id.joinEt_name);
        etNickName = (EditText)  findViewById(R.id.joinEt_nickname);
        etEmail = (EditText) findViewById(R.id.joinEt_email);
        etPw = (EditText) findViewById(R.id.joinEt_pw);
        etPwcheck = (EditText) findViewById(R.id.joinEt_pw2);
    }

    public void checkClick(View view) {
        switch (view.getId()){
            case R.id.joinBtn_ok:
                strEmail = etEmail.getText().toString().trim();
                strPw = etPw.getText().toString().trim();
                strPw2 = etPwcheck.getText().toString().trim();

                //비밀번호 = 비밀번호 확인
                if(strPw.equals(strPw2)){
                    firebaseAuth.createUserWithEmailAndPassword(strEmail, strPw)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //신규계정 등록
                                    if (task.isSuccessful()) {
                                        Log.i("joinS :: ", "createUserWithEmail:success");
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        String email = user.getEmail();
                                        String uid = user.getUid();
                                        strNickname = etNickName.getText().toString().trim();
                                        strName = etName.getText().toString().trim();

                                        //파베 데이터베이스에 저장
                                        HashMap<Object,String> hashMap = new HashMap<>();

                                        hashMap.put("uid",uid);
                                        hashMap.put("email",email);
                                        hashMap.put("name",strName);
                                        hashMap.put("ncikname", strNickname);

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference reference = database.getReference("Users");
                                        reference.child(email).setValue(hashMap);

                                    } else {
                                        // 1. 회원정보가 존재할때
                                        // 2. 입력한 이메일과 비밀번호가 일치하지 않을때
                                        Log.i("joinF ::", "createUserWithEmail:failure", task.getException());
                                    }
                                }
                            });
                }
                //비밀번호가 틀렸을때
                else{
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}