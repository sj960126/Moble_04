package com.example.withpet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

//Walk page
public class Menu3frag extends Fragment {

    private View rootview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_walk,container,false);
        Button btn1 = (Button) rootview.findViewById(R.id.walkBtn_write);

        btn1.setBackgroundResource(R.drawable.iconadd);
        return rootview;
    }
}
