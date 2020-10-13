package com.withpet_manager.main;

import android.Manifest;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.withpet_manager.R;
import com.withpet_manager.userfeed.Feed;
import com.withpet_manager.userinfo.Reply;
import com.withpet_manager.userinfo.Userinfo_getset;
import com.withpet_manager.walkreport.Walk_ReplyUpload;
import com.withpet_manager.walkreport.Walk_boardUpload;

import java.util.concurrent.Executor;


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
    private FirebaseDatabase db;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private String petcode;
    private String uid;
    private String shape,name,nickname,email,password,img;
    private FirebaseAuth auth;
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
        uid = data.getString("uid");

        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null){
            auth.signOut();
        }

        db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("User").child(uid);

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

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser login = auth.getCurrentUser();
                    if(login.isEmailVerified()){

                }
                else{
                    Toast.makeText(view.getContext(), "로그인 오류 : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    try{

                    }catch (Exception e){
                        Toast.makeText(view.getContext(), "시스템 오류" + e, Toast.LENGTH_SHORT).show();
                    }
                }
            }
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
        //회원 탈퇴 버튼
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUserHistory();
                auth.getCurrentUser().delete();

            }
        });


        return view;
    }

    private void deleteUserHistory(){
        //관심삭제

        db.getReference("Follow").child(uid).removeValue();
        db.getReference("Follow").addListenerForSingleValueEvent(valueEventListener);

        //피드 삭제
        db.getReference("Feed").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Feed feed = dataSnapshot.getValue(Feed.class);
                    if(feed.getUid().equals(uid)){
                        db.getReference("Feed").child(feed.getNewsName()).removeValue();
                        //좋아요 삭제
                        DatabaseReference likeReference = db.getReference("Like");
                        likeReference.child(feed.getNewsName()).removeValue();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        //댓글 삭제

        db.getReference("Reply").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        Reply reply = ds.getValue(Reply.class);
                        if(reply.getUid().equals(uid)){
                            db.getReference("Reply").child(reply.getBoardName()).child(reply.getReplyName()).removeValue();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        db.getReference("walk-board").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Walk_boardUpload walk_boardUpload = dataSnapshot.getValue(Walk_boardUpload.class);
                    if(walk_boardUpload.getUid().equals(uid)){
                        db.getReference("walk-board").child(Integer.toString(walk_boardUpload.getWalkboard_nb())).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        db.getReference("walk-reply").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        Walk_ReplyUpload walk_replyUpload = ds.getValue(Walk_ReplyUpload.class);

                        if(walk_replyUpload.getWalk_uid().equals(uid)){
                            db.getReference("walk-reply").child(Integer.toString(walk_replyUpload.getBoard_nb())).child(Integer.toString(walk_replyUpload.getReply_nb())).removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for(DataSnapshot followUser : snapshot.getChildren()){  // 모든 유저의 팔로우 목록 가져오기
                for (DataSnapshot followUserData : followUser.getChildren()){    // 해당 유저의 팔로우 유저 정보 가져오기
                    if(followUserData.getKey().equals(uid)){   // 팔로우 유저 정보에 회원 탈퇴하는 유저가 있는지 찾기
                        // 팔로우 유저 정보에 회원탈퇴하는 유저가 있으면 정보 삭제
                        db.getReference("Follow").child(followUser.getKey()).child(followUserData.getKey()).removeValue();
                    }
                }
            }
            db.getReference("Follow").removeEventListener(valueEventListener);    // 모든 삭제가 끝나면 이벤트 리스너 삭제
            Log.i("실행", "실행끝");
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };


}