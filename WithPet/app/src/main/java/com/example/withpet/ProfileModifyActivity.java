package com.example.withpet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileModifyActivity extends AppCompatActivity {
    String shape;
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_modify);
        Spinner spinner = findViewById(R.id.modifySp_shape);
        findViewById(R.id.modifyBtn_Ok).setOnClickListener(onclickListener);
        findViewById(R.id.modifyBtn_cancel).setOnClickListener(onclickListener);
        findViewById(R.id.modifyBtn_help).setOnClickListener(onclickListener);

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
            }
        }
    };
    // 입력 정보를 판단(입력 정보 중 공백을 판단)해서 ProfileInfo 객체를 반환하는 메소드
    public ProfileInfo checkProfileInfo(String username, String shape, String meal){
        ProfileInfo pinfo;
        String tmpshape = (shape.equals("종류를 선택하세요.")) ? "" : shape;
        int tmpmeal = (meal.equals("")) ? -1 : Integer.parseInt(meal);
        pinfo = new ProfileInfo(username, tmpshape, tmpmeal);
        return  pinfo;
    }

}