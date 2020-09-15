package com.withpet.mypage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.withpet.*;
import com.withpet.newsfeed.*;
import com.withpet.main.*;
import java.io.File;
//종희
//사진을 선택안했을 때 예외 처리 해야함 - 사진 변화 체크한 다음 값 하나하나씩 넣어줘야할듯

public class ProfileModifyActivity extends AppCompatActivity {

    ArrayAdapter adapter;
    private String[] permission_list = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private FirebaseStorage storage;
    private StorageReference storageRf; // 스토리지 주소 담는 객체
    private StorageReference imgRf;
    private FirebaseUser firebaseUser;
    private ImageView iv_profilephoto;
    private User loginuser;     // 로그인 한 사용자 정보 담은 객체
    private String shape;
    private String imgId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_modify);
        Intent intent = getIntent();
        TransUser tuser = (TransUser) intent.getSerializableExtra("loginuser");
        loginuser = tuser.TransformUser();

        Spinner spinner = findViewById(R.id.modifySp_shape);
        iv_profilephoto = findViewById(R.id.modifyIv_profile);
        ((EditText)findViewById(R.id.modifyEt_Meal)).setText(""+loginuser.getMeal());
        ((EditText)findViewById(R.id.modifyEt_UserName)).setText(loginuser.getNickname());
        Glide.with(this).load(loginuser.getImgUrl()).override(800).into(iv_profilephoto);

        findViewById(R.id.modifyBtn_Ok).setOnClickListener(onclickListener);
        findViewById(R.id.modifyBtn_cancel).setOnClickListener(onclickListener);
        findViewById(R.id.modifyBtn_help).setOnClickListener(onclickListener);
        iv_profilephoto.setOnClickListener(onclickListener);

        //파베저장소 연결 및 연동
        storage= FirebaseStorage.getInstance();
        storageRf = storage.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Spinner(콤보박스)에 사용할 아이템 리스트 adapter 생성(R.array.shape : 아이템리스트, R.layout.support~ : 안드로이드 제공 콤보박스 아이템 기본 레이아웃)
        adapter = ArrayAdapter.createFromResource(this, R.array.shape, R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // 아이템 선택 이벤트
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //아이템 선택 시
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
               shape = (String)adapter.getItem(position);
            }
            //잘 모르겠음
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                shape = "";
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Intent mintent = data;
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            ImageView iv = findViewById(R.id.modifyIv_profile);
            // 갤러리에서 선택한 이미지 가져오기
            imgId = data.getStringExtra("choiceimgId");
            Glide.with(this).load(imgId).override(800).into(iv);
        }
    }
    // 입력 정보를 판단(입력 정보 중 공백을 판단)해서 ProfileInfo 객체를 반환하는 메소드
    public void checkProfileInfo(String shape, String meal){
        loginuser.setShape((shape.equals("종류를 선택하세요.") ? "" : shape));
        loginuser.setMeal((meal.equals("")) ? -1 : Integer.parseInt(meal));
    }

    // 버튼 클릭 메소드
    View.OnClickListener onclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = getIntent();
            switch (view.getId()){
                case R.id.modifyBtn_cancel:
                    setResult(RESULT_CANCELED,intent);
                    finish();
                    break;
                case R.id.modifyBtn_Ok:
                    String meal = ((EditText)findViewById(R.id.modifyEt_Meal)).getText().toString();
                    String username = ((EditText)findViewById(R.id.modifyEt_UserName)).getText().toString();
                    loginuser.setNickname(username);
                    checkProfileInfo(shape, meal);
                    //파베 저장소의 feed 폴더에 사진 업로드
                    if(imgId != null) {
                        Uri file = Uri.fromFile(new File(imgId));
                        // 파이어베이스 저장소에 선택한 파일의 이름으로 이미지 저장 경로 생성
                        imgRf = storageRf.child("Profile/" + file.getLastPathSegment());
                        UploadTask uploadTask = imgRf.putFile(file);    // 파이어 베이스 저장소에 이미지 저장

                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            //저장소에 업로드가 실패했을 경우
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(ProfileModifyActivity.this, "사진 업로드 실패", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            //저장소에 업로드가 성공했을 경우
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(ProfileModifyActivity.this, "사진 업로드 성공", Toast.LENGTH_SHORT).show();
                                //파이어베이스 데이터베이스에 저장
                                uploadProfile();
                                //프로필 수정 > 마이페이지 이동
                                finish();
                            }
                        });
                    }
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case R.id.modifyBtn_help:
                    Modify_help helpdialog = new Modify_help(view.getContext());
                    ViewGroup.LayoutParams params = helpdialog.getWindow().getAttributes();
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    helpdialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
                    helpdialog.setCancelable(true);
                    helpdialog.show();
                    break;
                case R.id.modifyIv_profile:
                    Permission();
                    break;
            }
        }
    };

    //파베 저장소 사진의 액세스토큰 및 데이터 데베 등록
    public void uploadProfile(){
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference dbreference = db.getReference("User");
        if(imgId != null) {
            final String[] path = imgId.split("/");
            storageRf.child("Profile/" + path[path.length - 1]).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onSuccess(Uri uri) {
                    loginuser.setImgUrl(uri.toString());    //파이어베이스 저장소에 저장한 사진의 액세스 토큰 주소
                    dbreference.child(firebaseUser.getUid()).setValue(loginuser);
                }
            });
        }
        else{
            dbreference.child(firebaseUser.getUid()).setValue(loginuser);
        }

    }

    //권한설정 _ 안드로이드스튜디오개발가이드 + 구글링
    public void Permission(){
        for(String permission : permission_list){
            //권한 확인 여부
            int access = ContextCompat.checkSelfPermission(this, permission);
            //권한 x
            if(access == PackageManager.PERMISSION_DENIED){
                //권한 설정 확인 창이 뜸!!
                //requestCode는 아래의 onRequestPermissionsResult 함수 실행의 매개변수!
                requestPermissions(permission_list,0);
            }
            //권한 o
            else{
                //해당 페이지로 이동
                Intent intent = new Intent(this, NewsGalleryActivity.class);
                intent.putExtra("request", R.integer.profileModifyRequestcode);
                startActivityForResult(intent, 1020);
            }
        }
    }

    //권한 요청 후 어떻게 관리할 것인지에 대한 함수 _ 개발가이드에 자세히 나와있음
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 0)
        {
            for(int i=0; i<grantResults.length; i++)
            {
                //사용자가 허용했을 경우!
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    //페이지 이동
                    Intent intent = new Intent(this, NewsGalleryActivity.class);
                    startActivity(intent);

                }
                //사용자가 거부했을 경우!
                else {
                    Toast.makeText(this,"저장소 권한을 설정하세요.",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}