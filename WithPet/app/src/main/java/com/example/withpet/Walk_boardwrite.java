package com.example.withpet;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Walk_boardwrite extends AppCompatActivity {

    private Button walkBtn_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_boardwrite);

        walkBtn_add = findViewById(R.id.addbtn);

        walkBtn_add.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);

                intent.putExtra("frag",2);
                startActivity(intent);
                finish();
            }
        });

    }





}