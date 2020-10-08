package com.withpet.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.withpet.*;
import com.withpet.newsfeed.ReportActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    private Button btn_before;
    private Intent main;
    private DatabaseReference dbreference;
    private DatabaseReference followreference;
    private FirebaseDatabase db;
    private FirebaseUser firebaseUser;
    private ListView listView;
    private ArrayList<User> allUser;
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
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("User");
                    allUser = new ArrayList<>();
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            allUser.clear();
                            //회원정보 xml파일 추가
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                allUser.add(0, dataSnapshot.getValue(User.class));
                                SharedPreferences pref = getSharedPreferences(allUser.get(0).getUid(), MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.clear();
                                editor.commit();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) { }
                    });

                    //로그아웃
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser user = auth.getCurrentUser();
                    try{
                        if(user != null){
                            auth.signOut();
                            Toast.makeText(SettingActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                            main = new Intent(SettingActivity.this, LoginActivity.class);
                            startActivity(main);
                            finish();
                        }
                        else{
                            Toast.makeText(SettingActivity.this, "파이어베이스 세션 null", Toast.LENGTH_SHORT).show();
                        }
                    }catch(Exception e){
                        Toast.makeText(SettingActivity.this, "오류발생", Toast.LENGTH_SHORT).show();
                        Log.i("로그아웃 실행 중 오류 발생 :",""+e);
                    }

                }
                else if(selectedText.equals("회원탈퇴")){
                    firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //알림 문구
                                Toast.makeText(SettingActivity.this, "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();

                                //파베 삭제
                                dbreference = db.getReference("User");
                                dbreference.child(firebaseUser.getUid()).removeValue();

                                //어플 종료
                                ActivityCompat.finishAffinity(SettingActivity.this);
                                System.exit(0);
                            }
                        }
                    });
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
    private void deleteFollow(){
        followreference = db.getReference("Follow");
        //followreference.child(firebaseUser.getUid()).removeValue();
        followreference.addListenerForSingleValueEvent(valueEventListener);
    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            for(DataSnapshot followUser : snapshot.getChildren()){

               for (DataSnapshot followUserData : followUser.getChildren()){

                   if(followUserData.getKey().equals(firebaseUser.getUid())){

                       //followreference.child(followUser.getKey()).child(followUserData.getKey()).removeValue();
                   }
               }
            }
            //followreference.removeEventListener(valueEventListener);
            Log.i("실행", "실행끝");
            /// 일단 문제 : 리스너 나중에 실행되는데 시스템 꺼짐
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

}
