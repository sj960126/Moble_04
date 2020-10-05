package com.withpet.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.withpet.*;
import com.withpet.newsfeed.ReportActivity;

public class ScActivity extends AppCompatActivity {
    
    private Button btnBefore;
    private ListView listView;
    static final String[] listMenu = {"고객문의", "문의내역"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sc);

        //화면전환시 아래에서 위로 올라오는 애니메이션 제거
        overridePendingTransition(0, 0);
        
        btnBefore= (Button) findViewById(R.id.scBtn_before);
        btnBefore.setBackgroundResource(R.drawable.iconbefore);
        
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listMenu);
        listView = (ListView) findViewById(R.id.scLv);
        listView.setAdapter(adapter);
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
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedText = (String) parent.getItemAtPosition(position);
                if(selectedText.equals("고객문의")){
                    Intent serviceCenter = new Intent(ScActivity.this, ReportActivity.class);
                    startActivity(serviceCenter);
                }
                else if(selectedText.equals("문의내역")){
                    Intent serviceCenter = new Intent(ScActivity.this, SclistActivity.class);
                    startActivity(serviceCenter);
                }
            }
        });
    }
}