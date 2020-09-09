package com.withpet.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.withpet.walk.*;
import com.withpet.*;

//Main
public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView; // 바텀 네비게이션 뷰
    FragmentManager fm;
    FragmentTransaction ft;
    home_frag homeFrag;
    health_frag healthFrag;
    walk_frag walkFrag;
    mypage_frag mypageFrag;
    iot_frag iotFrag;
    Walk_boarddetail walkBoarddetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //화면전환시 아래에서 위로 올라오는 애니메이션 제거
        overridePendingTransition(0, 0);

        Intent intent = getIntent();
        int frag = intent.getIntExtra("frag",0);

        //bottomNavigationView 클릭시 이벤트
        bottomNavigationView =findViewById(R.id.bottomNV);
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

        homeFrag = new home_frag();
        healthFrag = new health_frag();
        walkFrag = new walk_frag();
        mypageFrag = new mypage_frag();
        iotFrag = new iot_frag();
        walkBoarddetail = new Walk_boarddetail();

        setFrag(frag);

    }

    //각 메뉴의 레이아웃 화면 설정
    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n) {
            case 0:
                ft.replace(R.id.main, homeFrag);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main, healthFrag);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main, walkFrag);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.main, mypageFrag);
                ft.commit();
                break;
            case 4:
                ft.replace(R.id.main, iotFrag);
                ft.commit();
                break;
            case 5:
                ft.replace(R.id.main, walkBoarddetail);
                ft.commit();
                break;
        }
    }

}