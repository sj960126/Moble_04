package com.withpet.main;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.withpet.*;

public class JoinActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private EditText nickname;
    private EditText email;
    private EditText pw;
    private EditText pwcheck;
    private String etEmail, etNickname, etPw, etPw2, etName;


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

        nickname = (EditText)  findViewById(R.id.joinEt_nickname);
        email = (EditText) findViewById(R.id.joinEt_email);
        pw = (EditText) findViewById(R.id.joinEt_pw);
        pwcheck = (EditText) findViewById(R.id.joinEt_pw2);


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
                etEmail = email.getText().toString().trim();
                etNickname = nickname.getText().toString().trim();
                etPw = pw.getText().toString().trim();
                etPw2 = pwcheck.getText().toString().trim();

                //비밀번호 = 비밀번호 확인
                if(etPw.equals(etPw2)){
                    //신규 계정 등록
                    firebaseAuth.createUserWithEmailAndPassword(etEmail,etPw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //가입 성공시
                            if(task.isSuccessful()){
                               /* FirebaseUser user = firebaseAuth.getCurrentUser();
                                String email = user.getEmail();
                                String uid = user.getUid();

                                //해쉬맵으로 파베 저장
                                HashMap<Object, String> hashMap = new HashMap<>();

                                hashMap.put("uid",uid);
                                hashMap.put("email", email);
                                hashMap.put("name", etName);

                                reference.child(uid).setValue(hashMap);
*/
                                Toast.makeText(JoinActivity.this, "회원가입", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(JoinActivity.this, "이미 존재하는 회원정보입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}