package com.withpet_manager;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UserdetailinfoFrag extends Fragment {
    private View view;
    private ImageView modimg;
    private TextView moduid;
    private TextView modname;
    private TextView modnickname;
    private TextView modemail;
    private TextView modpassword;
    private Button modBtn;
    private Button deleteBtn;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_userdetailinfo,container,false);

        modimg = view.findViewById(R.id.modimg);
        moduid = view.findViewById(R.id.moduid);
        modname = view.findViewById(R.id.modname);
        modnickname = view.findViewById(R.id.modnickname);
        modemail = view.findViewById(R.id.modemail);
        modpassword = view.findViewById(R.id.modpassword);
        modBtn = view.findViewById(R.id.userinfomod);
        deleteBtn = view.findViewById(R.id.userinfodelete);

        Bundle data = getArguments();
        String uid = data.getString("uid");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User").child(uid);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Userinfo_getset userinfo = snapshot.getValue(Userinfo_getset.class);

                moduid.setText(userinfo.getUid());
                modname.setText(userinfo.getName());
                modnickname.setText(userinfo.getNickname());
                modemail.setText(userinfo.getEmail());
                modpassword.setText(userinfo.getPw());

                moduid.setEnabled(false);
                modname.setEnabled(false);
                modemail.setEnabled(false);

                Glide.with(view).load(userinfo.getImgUri()).into(modimg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
}