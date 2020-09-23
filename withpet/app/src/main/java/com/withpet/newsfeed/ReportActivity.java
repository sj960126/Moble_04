package com.withpet.newsfeed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.withpet.*;

public class ReportActivity extends AppCompatActivity {

    private Button btnBefore, btnOk;
    private EditText etTitle, etContext;
    private String strFeedName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        btnBefore = findViewById(R.id.reportBtn_before);
        btnOk = findViewById(R.id.reportBtn_ok);
        etTitle =findViewById(R.id.reportEt_title);
        etContext = findViewById(R.id.reportEt_context);

        btnBefore.setBackgroundResource(R.drawable.iconbefore);

        Intent intent = getIntent();
        strFeedName = intent.getStringExtra("feedName");
    }

    @Override
    protected void onResume() {
        super.onResume();

        btnBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString();
                String context = etContext.getText().toString();
                finish();
            }
        });
    }
}