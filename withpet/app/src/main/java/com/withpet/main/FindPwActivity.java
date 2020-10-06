package com.withpet.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.withpet.*;
import com.withpet.newsfeed.Feed;

import java.util.ArrayList;

public class FindPwActivity extends AppCompatActivity {

    private EditText etEmail, etName;
    private Button btnOk;
    private String strEmail, strName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        etEmail = (EditText) findViewById(R.id.findEt_email);
        etName =(EditText) findViewById(R.id.findEt_name);
        btnOk = (Button) findViewById(R.id.findBtn_ok);
    }

    @Override
    protected void onResume() {
        super.onResume();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEmail = etEmail.getText().toString().trim();
                strName = etName.getText().toString().trim();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("User");
                Query findPw = reference.orderByChild("email").equalTo(strEmail);
                findPw.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            User user = dataSnapshot.getValue(User.class);
                            AlertDialog.Builder builder = new AlertDialog.Builder(FindPwActivity.this);
                            builder.setTitle("[ 알림 ]");
                            builder.setMessage("회원님의 비밀번호는 "+ user.getPw()+ " 입니다.");
                            builder.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            builder.show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            }
        });
    }
}