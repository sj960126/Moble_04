package com.withpet.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.withpet.health.*;
import com.withpet.*;



//Health page
public class HealthFrag extends Fragment {
    private View rootview;
    ImageView hospital_btn;
    ImageView diary_btn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_health,container,false);

        //건강 메인 선택창 Java
        hospital_btn = (ImageView) rootview.findViewById(R.id.H_btn);
        diary_btn =(ImageView) rootview.findViewById(R.id.D_btn);

        hospital_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), HealthHospitalActivity.class);
                startActivity(intent);
            }
        });

        diary_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), diaryActivity.class);
                startActivity(intent);
            }
        });


        return rootview;
    }

}
