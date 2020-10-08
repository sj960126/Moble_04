package com.withpet.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.withpet.*;
import com.withpet.newsfeed.ReportActivity;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    private Button btn_before;
    private Intent main;
    private DatabaseReference dbreference;
    private FirebaseDatabase db;
    private FirebaseUser firebaseUser;
    private ListView listView;
    static final String[] listMenu = {"친구초대", "고객센터", "정보", "로그아웃", "회원탈퇴"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //화면전환시 아래에서 위로 올라오는 애니메이션 제거
        overridePendingTransition(0, 0);

        btn_before =(Button) findViewById(R.id.settingBtn_before);
        btn_before.setBackgroundResource(R.drawable.iconbefore);

        db = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listMenu);
        listView = (ListView) findViewById(R.id.settingLv);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //이전페이지로 이동
        btn_before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //설정
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedText = (String) parent.getItemAtPosition(position);
                if(selectedText.equals("로그아웃")){
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SettingActivity.this.getApplicationContext());
                    sp.edit().remove("clientToken").commit();
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(SettingActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    main = new Intent(SettingActivity.this, LoginActivity.class);
                    startActivity(main);
                    finish();
                }
                else if(selectedText.equals("회원탈퇴")){
                    Toast.makeText(SettingActivity.this, "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    dbreference = db.getReference("User");
                    dbreference.child(firebaseUser.getUid()).removeValue();
                    firebaseUser.delete();
                    ActivityCompat.finishAffinity(SettingActivity.this);
                    System.exit(0);
                }
                else if(selectedText.equals("친구초대")){
                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    //로그인유저 정보
                    SharedPreferences preferences = getSharedPreferences(firebaseUser.getUid(), Context.MODE_PRIVATE);
                    String nickName = preferences.getString("nickName", "host");
                    String name = preferences.getString("name", "host");
                    //공유할 내용
                    String message ="제 WithPet 계정은"+ name +"(@"+nickName +")입니다. 반려견과의 추억을 공유하며, 반려견의 건강을 챙겨보아요! ";
                    String link ="https://withpet.page.link/u9DC/";
                    intent.putExtra(Intent.EXTRA_SUBJECT, message);
                    intent.putExtra(Intent.EXTRA_TEXT, link);
                    intent.setType("text/plain");
                    Intent chooser = Intent.createChooser(intent, "친구 초대하기");
                    startActivity(chooser);
                }
                else if(selectedText.equals("고객센터")){
                    Intent serviceCenter = new Intent(SettingActivity.this, ScActivity.class);
                    startActivity(serviceCenter);
                }
                else if(selectedText.equals("정보")){
                    Toast.makeText(SettingActivity.this, "WithPet 이용약관", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}