package com.withpet.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private int reply_nb = 0;
    private boolean mypagemenuclick = false;    // 마이페이지로 갈 때 메뉴를 통해 간 것인지, 프로필을 통해 간 것인지 판단하는 변수
    private Bundle mypageBundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //화면전환시 아래에서 위로 올라오는 애니메이션 제거
        overridePendingTransition(0, 0);

        Intent intent = getIntent();
        int frag = intent.getIntExtra("frag",0);
        board_nb = intent.getIntExtra("board_nb",0);
        reply_nb = intent.getIntExtra("reply_nb",0);


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
        mypageBundle = new Bundle();
        if(frag == 3){
            mypageBundle.clear();
            TransUser tuser = (TransUser)intent.getSerializableExtra("userinfo");
            mypageBundle.putSerializable("userinfo",tuser);
            mypageBundle.putString("from", "proflie");
            mypagemenuclick = false;
        }
        setFrag(frag);
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
                        setFrag(0);
                        break;
                    case R.id.menu_health:
                        setFrag(1);
                        break;
                    case R.id.menu_walk:
                        setFrag(2);
                        break;
                    case R.id.menu_mypage:
                        mypagemenuclick = true;
                        setFrag(3);
                        break;
                    case R.id.menu_iot:
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
                //내용추가
                if(mypagemenuclick){
                    mypageBundle.clear();
                    mypageBundle.putString("from", "menu");
                    //mypageBundle.putSe
                    Log.i("실행확인", "메뉴클릭됨");
                }
                mypageFrag.setArguments(mypageBundle);
                ft.detach(mypageFrag).attach(mypageFrag);
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
