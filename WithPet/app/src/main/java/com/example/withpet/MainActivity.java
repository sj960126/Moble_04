package com.example.withpet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

//Main
public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView; // 바텀 네비게이션 뷰
    FragmentManager fm;
    FragmentTransaction ft;
    Menu1frag m1;
    Menu2frag m2;
    Menu3frag m3;
    Menu4frag m4;
    Menu5frag m5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bottomNavigationView 클릭시 이벤트
        bottomNavigationView =findViewById(R.id.bottomNV);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu1:
                        Toast.makeText(MainActivity.this, "홈", Toast.LENGTH_SHORT).show();
                        setFrag(0);
                        break;
                    case R.id.menu2:
                        Toast.makeText(MainActivity.this, "건강", Toast.LENGTH_SHORT).show();
                        setFrag(1);
                        break;
                    case R.id.menu3:
                        Toast.makeText(MainActivity.this, "산책", Toast.LENGTH_SHORT).show();
                        setFrag(2);
                        break;
                    case R.id.menu4:
                        Toast.makeText(MainActivity.this, "내정보", Toast.LENGTH_SHORT).show();
                        setFrag(3);
                        break;
                    case R.id.menu5:
                        Toast.makeText(MainActivity.this, "IOT", Toast.LENGTH_SHORT).show();
                        setFrag(4);
                        break;
                }
                return true;
            }
        });

        m1 = new Menu1frag();
        m2 = new Menu2frag();
        m3 = new Menu3frag();
        m4 = new Menu4frag();
        m5 = new Menu5frag();

        Intent intent = getIntent();
        int frag = intent.getIntExtra("frag",0);
        setFrag(frag);

    }

    //각 메뉴의 레이아웃 화면 설정
    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n) {
            case 0:
                ft.replace(R.id.main, m1);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main, m2);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main, m3);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.main, m4);
                ft.commit();
                break;
            case 4:
                ft.replace(R.id.main, m5);
                ft.commit();
                break;
        }
    }

}