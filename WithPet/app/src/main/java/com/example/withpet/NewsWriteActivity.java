package com.example.withpet;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NewsWriteActivity extends AppCompatActivity {

    GridView gv;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_write);

        iv = (ImageView) findViewById(R.id.mainwIv_choice);

        //그리드뷰 Adapter 연결
        gv = (GridView) findViewById(R.id.mainwGv);
        gv.setAdapter(new GalleryAdapter(this));

        //그리드뷰 클릭 이벤트 _ setOnItemClickListener
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(NewsWriteActivity.this, ""+i, Toast.LENGTH_SHORT).show();
                iv.setImageResource(R.drawable.dog);
            }
        });
    }

}