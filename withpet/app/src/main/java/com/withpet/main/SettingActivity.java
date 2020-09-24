package com.withpet.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.withpet.*;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    private Button btn_delete, btn_logout, btn_before;
    private Intent main;
    private DatabaseReference dbreference;
    private FirebaseDatabase db;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        btn_before =(Button) findViewById(R.id.settingBtn_before);
        btn_logout = (Button) findViewById(R.id.settingBtn_logout);
        btn_delete = (Button)findViewById(R.id.settingBtn_delete);

        db = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        btn_before.setBackgroundResource(R.drawable.iconbefore);
    }

    @Override
    protected void onResume() {
        super.onResume();

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                main = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(main);
                finish();
            }
        });

        btn_before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbreference = db.getReference("User");
                dbreference.child(firebaseUser.getUid()).removeValue();
                firebaseUser.delete();
                finish();
            }
        });
    }

}