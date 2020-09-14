package com.withpet.mypage;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.withpet.*;
// 종희
public class Modify_help extends Dialog implements View.OnClickListener {
    Context mContext;
    public Modify_help(@NonNull Context context) {
        super(context);
        mContext = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_help);
        Button btn_ok = findViewById(R.id.helpBtn_OK);
        btn_ok.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        dismiss();
    }
}
