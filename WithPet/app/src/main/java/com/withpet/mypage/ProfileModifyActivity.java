package com.withpet.mypage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.withpet.*;
import com.withpet.newsfeed.*;

public class ProfileModifyActivity extends AppCompatActivity {
    String shape;
    ArrayAdapter adapter;
    private String[] permission_list = {Manifest.permission.READ_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_modify);
        Spinner spinner = findViewById(R.id.modifySp_shape);
        findViewById(R.id.modifyBtn_Ok).setOnClickListener(onclickListener);
        findViewById(R.id.modifyBtn_cancel).setOnClickListener(onclickListener);
        findViewById(R.id.modifyBtn_help).setOnClickListener(onclickListener);
        findViewById(R.id.modifyIv_profile).setOnClickListener(onclickListener);

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
                    ProfileInfo pinfo = checkProfileInfo(username, shape, meal);
                    Log.i("username:", ""+pinfo.getUsername());
                    Log.i("shape:", pinfo.getShape());
                    Log.i("meal:", ""+pinfo.getMeal());
                    intent.putExtra("profileinfo", pinfo);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Intent mintent = data;
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            ImageView iv = findViewById(R.id.modifyIv_profile);
            // 갤러리에서 선택한 이미지 가져오기
            Glide.with(this).load(mintent.getStringExtra("imgId")).override(800).into(iv);
        }
    }
    // 입력 정보를 판단(입력 정보 중 공백을 판단)해서 ProfileInfo 객체를 반환하는 메소드
    public ProfileInfo checkProfileInfo(String username, String shape, String meal){
        ProfileInfo pinfo;
        String tmpshape = (shape.equals("종류를 선택하세요.")) ? "" : shape;
        int tmpmeal = (meal.equals("")) ? -1 : Integer.parseInt(meal);
        pinfo = new ProfileInfo(username, tmpshape, tmpmeal);
        return  pinfo;
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