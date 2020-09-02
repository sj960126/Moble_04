package com.example.withpet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

//Health page
public class Menu2frag extends Fragment {
    private View rootview;
    ImageView hospital_btn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_health,container,false);

        //건강 메인 선택창 Java
        hospital_btn = (ImageView) rootview.findViewById(R.id.H_btn);
        hospital_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Health_Hospital.class);
                startActivity(intent);
            }
        });

        return rootview;
    }

}
