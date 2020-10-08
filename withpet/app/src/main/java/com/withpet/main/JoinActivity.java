package com.withpet.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private EditText etNickName, etEmail, etPw, etPwcheck, etName;
    private String strEmail, strNickname, strPw, strPw2, strName;
    private Button btnEmail;
    private Spinner spShpae;
    private String shape;
    private ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        etName = (EditText) findViewById(R.id.joinEt_name);
        etNickName = (EditText) findViewById(R.id.joinEt_nickname);
        etEmail = (EditText) findViewById(R.id.joinEt_email);
        etPw = (EditText) findViewById(R.id.joinEt_pw);
        etPwcheck = (EditText) findViewById(R.id.joinEt_pw2);
        btnEmail = (Button) findViewById(R.id.joinBtn_email);
        spShpae =(Spinner)findViewById(R.id.joinSp_shape);

        btnEmail.setBackgroundResource(R.drawable.iconhelp);

        //파베 접근 설정
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("User");

        // Spinner(콤보박스)에 사용할 아이템 리스트 adapter 생성(R.array.shape : 아이템리스트, R.layout.support~ : 안드로이드 제공 콤보박스 아이템 기본 레이아웃)
        adapter = ArrayAdapter.createFromResource(this, R.array.shape, R.layout.support_simple_spinner_dropdown_item);
        spShpae.setAdapter(adapter);


    }

    @Override
    protected void onResume() {
        super.onResume();

        //NcikName filter :: 영문자(소, 대)/숫자/특수문자_ 만 입력가능
        InputFilter filter_nickName = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                Pattern ps = Pattern.compile("^[a-zA-Z0-9_]*$");
                if (!ps.matcher(charSequence).matches()) {
                    Toast.makeText(JoinActivity.this, "영문자, 숫자, 특수문자 '_'만 사용가능합니다.", Toast.LENGTH_SHORT).show();
                    return "";
                }
                return null;
            }
        };
        etNickName.setFilters(new InputFilter[]{filter_nickName});

        // 아이템 선택 이벤트
        spShpae.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //아이템 선택 시
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                shape = (String)adapter.getItem(position);
            }
            //잘 모르겠음
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                shape = "";
            }
        });
    }

    public void checkClick(View view) {
        switch (view.getId()) {
            case R.id.joinBtn_email:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("[ 알림 ]");
                builder.setMessage("회원님께서 입력한 이메일로 인증메일이 전송될 예정이니 정확하게 기입해주세요."); // 메시지
//                버튼 클릭시 동작
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        etEmail.setHint("ex) 000@naver.com");
                    }
                });
                builder.show();
                break;
            case R.id.joinBtn_ok:
                strEmail = etEmail.getText().toString().trim();
                strPw = etPw.getText().toString().trim();
                strPw2 = etPwcheck.getText().toString().trim();
                Log.i("견종", ""+shape);
                //비밀번호 = 비밀번호 확인
                if (strPw.equals(strPw2)) {
                    firebaseAuth.createUserWithEmailAndPassword(strEmail, strPw)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //신규계정 등록
                                    if (task.isSuccessful()) {
                                        Log.i("joinS :: ", "createUserWithEmail:success");
                                        FirebaseUser user = firebaseAuth.getCurrentUser();

                                        //인증메일 전송함수수
                                       sendVerificationEmail();

                                        String email = user.getEmail();
                                        String uid = user.getUid();
                                        strNickname = etNickName.getText().toString().trim();
                                        strName = etName.getText().toString().trim();
                                        strPw = etPw.getText().toString().trim();
                                        //파베 데이터베이스에 저장
                                        HashMap<Object, String> hashMap = new HashMap<>();
                                        hashMap.put("uid", uid);
                                        hashMap.put("email", email);
                                        hashMap.put("name", strName);
                                        hashMap.put("nickname", strNickname);
                                        hashMap.put("pw", strPw);
                                        hashMap.put("imgUrl", "https://firebasestorage.googleapis.com/v0/b/practice-d557f.appspot.com/o/Profile%2Fuserdefault.png?alt=media&token=bc81fd8f-be4d-40a5-a76d-268ad90085af");

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference reference = database.getReference("User");
                                        reference.child(uid).setValue(hashMap);

                                        finish();
                                    } else if (!task.isSuccessful()) {
                                        // 1. 회원정보가 존재할때
                                        // 2. 입력한 이메일과 비밀번호가 일치하지 않을때
                                        Log.i("joinF ::", "createUserWithEmail:failure", task.getException());
                                    }
                                }
                            });
                }
                //비밀번호가 틀렸을때
                else {
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //메일 인증 요청
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(JoinActivity.this, "인증메일이 발송되었습니다.", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                }
                else{
                    Toast.makeText(JoinActivity.this, "인증메일 발송이 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}