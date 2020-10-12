package com.withpet_manager.main;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.withpet_manager.R;
import com.withpet_manager.userinfo.Userinfo_getset;




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
    private Button imgchangeBtn;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private int petcode;
    private String shape,name,nickname,email,password,img;

    private int meal;
    private String[] permission_list = {Manifest.permission.READ_EXTERNAL_STORAGE};
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
        imgchangeBtn = view.findViewById(R.id.imgchangeBtn);

        Bundle data = getArguments();
        final String uid = data.getString("uid");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User").child(uid);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Userinfo_getset userinfo = snapshot.getValue(Userinfo_getset.class);

                name = userinfo.getName();
                nickname = userinfo.getNickname();
                email = userinfo.getEmail();
                password = userinfo.getPw();
                meal =userinfo.getMeal();
                petcode = userinfo.getPetcode();
                shape = userinfo.getShape();
                img = userinfo.getImgUri();

                moduid.setText(userinfo.getUid());
                modname.setText(userinfo.getName());
                modnickname.setText(userinfo.getNickname());
                modemail.setText(userinfo.getEmail());
                modpassword.setText(userinfo.getPw());

                shape = userinfo.getShape();
                petcode = userinfo.getPetcode();
                meal = userinfo.getMeal();
                moduid.setEnabled(false);
                modname.setEnabled(false);
                modemail.setEnabled(false);
                modpassword.setEnabled(false);


                Glide.with(view).load(userinfo.getImgUri()).into(modimg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //기본 이미지로 변경
        imgchangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img = "https://firebasestorage.googleapis.com/v0/b/practice-d557f.appspot.com/o/Profile%2Fuserdefault.png?alt=media&token=bc81fd8f-be4d-40a5-a76d-268ad90085af";
                Glide.with(view).load(img).into(modimg);

            }
        });
        //정보 수정 버튼
        modBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickname = modnickname.getText().toString();
                Userinfo_getset userinfo_getset = new Userinfo_getset(email,img,name,nickname,password,uid,meal,petcode,shape);
                databaseReference.setValue(userinfo_getset);



            }
        });


        return view;
    }


}