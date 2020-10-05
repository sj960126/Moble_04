package com.withpet.newsfeed;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.withpet.*;

import java.util.Date;

public class ReportActivity extends AppCompatActivity {

    private Button btnBefore, btnOk;
    private EditText etTitle, etContext;
    private String strFeedName;
    private Spinner spCategory;
    ArrayAdapter adapter;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        //화면전환시 아래에서 위로 올라오는 애니메이션 제거
        overridePendingTransition(0, 0);

        btnBefore = findViewById(R.id.reportBtn_before);
        btnOk = findViewById(R.id.reportBtn_ok);
        etTitle =findViewById(R.id.reportEt_title);
        etContext = findViewById(R.id.reportEt_context);
        spCategory = findViewById(R.id.reportSp_category);

        btnBefore.setBackgroundResource(R.drawable.iconbefore);

        Intent intent = getIntent();
        strFeedName = intent.getStringExtra("feedName");

        if(strFeedName != null){
            // Spinner(콤보박스)에 사용할 아이템 리스트 adapter 생성(R.array.shape : 아이템리스트, R.layout.support~ : 안드로이드 제공 콤보박스 아이템 기본 레이아웃)
            adapter = ArrayAdapter.createFromResource(this, R.array.reportCategory, R.layout.support_simple_spinner_dropdown_item);
            spCategory.setAdapter(adapter);
        }
        else{
            // Spinner(콤보박스)에 사용할 아이템 리스트 adapter 생성(R.array.shape : 아이템리스트, R.layout.support~ : 안드로이드 제공 콤보박스 아이템 기본 레이아웃)
            adapter = ArrayAdapter.createFromResource(this, R.array.serviceCenter, R.layout.support_simple_spinner_dropdown_item);
            spCategory.setAdapter(adapter);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        // 아이템 선택 이벤트
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //아이템 선택 시 String
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                category = (String)adapter.getItem(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                category = "";
            }
        });

        btnBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString();
                String context = etContext.getText().toString();

                //신고글 번호
                long now = System.currentTimeMillis();
                Date mDate = new Date(now);
                SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String getTime = mFormat.format(mDate);

                if(strFeedName != null){
                    //Report 테이블 파베 연동
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference dbRefReply = firebaseDatabase.getReference("Report");
                    Report inputReport = new Report(user.getUid(), category, strFeedName, title, context, getTime);
                    dbRefReply.child(user.getUid()+getTime).setValue(inputReport);
                    finish();
                } else{
                    SimpleDateFormat today = new SimpleDateFormat("yyyy-MM-dd");
                    String strtoday = today.format(mDate);

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference dbRefReply = firebaseDatabase.getReference("SC");
                    Report inputReport = new Report(user.getUid(), category,"x", title, context, strtoday);
                    dbRefReply.child(user.getUid()+getTime).setValue(inputReport);
                    finish();
                }
            }
        });
    }
}