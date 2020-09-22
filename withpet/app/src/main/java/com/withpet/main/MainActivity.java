package com.withpet.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.withpet.walk.*;
import com.withpet.*;

//Main
public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView; // 바텀 네비게이션 뷰
    private FragmentManager fm;
    private FragmentTransaction ft;
    private HomeFrag homeFrag;
    private HealthFrag healthFrag;
    private WalkFrag walkFrag;
    private MypageFrag mypageFrag;
    private IotFrag iotFrag;
    private Walk_boarddetailFrag walkBoarddetail;
    private int board_nb = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //화면전환시 아래에서 위로 올라오는 애니메이션 제거
        overridePendingTransition(0, 0);

        Intent intent = getIntent();
        int frag = intent.getIntExtra("frag",0);
        board_nb = intent.getIntExtra("board_nb",0);


        //하단메뉴바 초기화
        bottomNavigationView =findViewById(R.id.bottomNV);

        //각 메뉴페이지 초기화
        homeFrag = new HomeFrag();
        healthFrag = new HealthFrag();
        walkFrag = new WalkFrag();
        mypageFrag = new MypageFrag();
        iotFrag = new IotFrag();
        walkBoarddetail = new Walk_boarddetailFrag();

        Bundle bundle = new Bundle();
        bundle.putInt("board_nb",board_nb);
        walkBoarddetail.setArguments(bundle);
        setFrag(frag);
        if(frag == R.integer.mypagefrag){

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //하단바 클릭 이벤트 : 해당 메뉴 페이지로 이동
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_home:
                        Toast.makeText(MainActivity.this, "홈", Toast.LENGTH_SHORT).show();
                        setFrag(0);
                        break;
                    case R.id.menu_health:
                        Toast.makeText(MainActivity.this, "건강", Toast.LENGTH_SHORT).show();
                        setFrag(1);
                        break;
                    case R.id.menu_walk:
                        Toast.makeText(MainActivity.this, "산책", Toast.LENGTH_SHORT).show();
                        setFrag(2);
                        break;
                    case R.id.menu_mypage:
                        Toast.makeText(MainActivity.this, "내정보", Toast.LENGTH_SHORT).show();
                        setFrag(3);
                        break;
                    case R.id.menu_iot:
                        Toast.makeText(MainActivity.this, "IOT", Toast.LENGTH_SHORT).show();
                        setFrag(4);
                        break;
                }
                return true;
            }
        });
    }

    //각 메뉴의 레이아웃 화면 설정
    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        Fragment fragment = null;
        switch (n) {
            case 0:
                fragment = homeFrag;
                break;
            case 1:
                fragment = healthFrag;
                break;
            case 2:
                fragment = walkFrag;
                break;
            case 3:
                fragment = mypageFrag;
                break;
            case 4:
                fragment = iotFrag;
                break;
            case 5:
                fragment = walkBoarddetail;
                break;
            default:
                return;
        }
        ft.replace(R.id.main, fragment);
        ft.commit();
    }
}