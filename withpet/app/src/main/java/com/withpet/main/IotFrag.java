package com.withpet.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.withpet.iot.*;
import com.withpet.mypage.*;
import com.withpet.newsfeed.*;
import com.withpet.health.*;
import com.withpet.walk.*;
import com.withpet.*;

//Mypage page
public class IotFrag extends Fragment {

    private View rootview;
    private Button gostreaming_btn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_iot,container,false);
        // test주석입니다.
        gostreaming_btn = rootview.findViewById(R.id.button6);

        gostreaming_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), iot_streaming_area.class);
                startActivity(intent);
            }
        });

        return rootview;
    }
}
