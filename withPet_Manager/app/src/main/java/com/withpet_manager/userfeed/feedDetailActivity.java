package com.withpet_manager.userfeed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.withpet_manager.*;

public class feedDetailActivity extends AppCompatActivity {

    private TextView tvCategroy, tvName, tvDate, tvContext;
    private ImageView ivImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);

        //화면전환시 아래에서 위로 올라오는 애니메이션 제거
        overridePendingTransition(0, 0);

        tvCategroy =(TextView) findViewById(R.id.fdTv_category);
        tvName = (TextView)findViewById(R.id.fdTv_name);
        tvDate = (TextView) findViewById(R.id.fdTv_date);
        tvContext = (TextView) findViewById(R.id.fdTv_context);
        ivImg = (ImageView) findViewById(R.id.fdIv);
    }
}