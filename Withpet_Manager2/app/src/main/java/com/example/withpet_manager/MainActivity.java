package com.example.withpet_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView; // 바텀 네비게이션 뷰
    private FragmentManager fm;
    private FragmentTransaction ft;
    private ClientcenterFrag clientcenterFrag;
    private DeclarationlistFrag declarationlistFrag;
    private UserboardFrag userboardFrag;
    private UserfeedFrag userfeedFrag;
    private UserinfoFrag userinfoFrag;
    private UserdetailinfoFrag userdetailinfoFrag;

    private String uid = "asd";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNV);

        clientcenterFrag = new ClientcenterFrag();
        declarationlistFrag = new DeclarationlistFrag();
        userinfoFrag = new UserinfoFrag();
        userfeedFrag = new UserfeedFrag();
        userboardFrag = new UserboardFrag();
        userdetailinfoFrag = new UserdetailinfoFrag();

        Intent intent = getIntent();
        int frag =intent.getIntExtra("frag",0);
        uid = intent.getStringExtra("uid");
        setFrag(frag);


    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.userinfo:
                        setFrag(0);
                        break;
                    case R.id.userfeed:
                        setFrag(1);
                        break;
                    case R.id.userboard:
                        setFrag(2);
                        break;
                    case R.id.declarationlist:
                        setFrag(3);
                        break;
                    case R.id.clientcenter:
                        setFrag(4);
                        break;
                }
                return true;
            }
        });
    }

    private void setFrag(int n){
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        Fragment fragment = null;
        switch (n){
            case 0:
                fragment = userinfoFrag;
                break;
            case 1:
                fragment = userfeedFrag;
                break;
            case 2:
                fragment = userboardFrag;
                break;
            case 3:
                fragment = declarationlistFrag;
                break;
            case 4:
                fragment = clientcenterFrag;
                break;
            case 5:
                Bundle bundle = new Bundle();
                bundle.putString("uid",uid);
                userdetailinfoFrag.setArguments(bundle);
                fragment = userdetailinfoFrag;

                break;
            default:
                return;

        }
        ft.replace(R.id.main,fragment);
        ft.commit();
    }

}