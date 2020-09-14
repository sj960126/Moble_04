package com.withpet.newsfeed;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.withpet.*;

import java.util.ArrayList;

public class NewsGalleryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private Button btnNextPage;
    private ImageView ivDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_gallery);

        //화면전환시 아래에서 위로 올라오는 애니메이션 제거
        overridePendingTransition(0, 0);

        //상단 next 버튼 이미지 설정
        btnNextPage = (Button) findViewById(R.id.mainwBtn_next);
        Intent intent = getIntent();
        int requestcode = intent.getIntExtra("request", -1);

        // 게시글 작성 버튼 클릭 > 갤러리 선택 액티비티 실행
        if(requestcode == R.integer.newsRequestcode){
            btnNextPage.setBackgroundResource(R.drawable.iconnext);
            btnNextPage.setTag(requestcode);
        }
        // 프로필 수정에서 갤러리 사진 선택 액티비티 실행시
        else if(requestcode == R.integer.profileModifyRequestcode){
            btnNextPage.setBackgroundResource(R.drawable.iconcheck);
            btnNextPage.setTag(requestcode);
        }

        //선택한 이미지 보는 imageView 디폴트 사진 설정
        ivDefault = (ImageView) findViewById(R.id.mainwIv_choice);
        ivDefault.setImageResource(R.drawable.dog);

        //Gridview 행의 수
        final int clo = 4;

        //recyclerView(갤러리 미리보기) 설정
        recyclerView = (RecyclerView) findViewById(R.id.mainwGv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, clo));

        //recyclerView(갤러리 미리보기) 어댑터 연결
        mAdapter = new GalleryAdapter(this,getImagesPath(this));
        recyclerView.setAdapter(mAdapter);
    }

    //권한설정 후 사용자의 갤러리에서 이미지 데이터 가져오는 함수(거의 국룰)
    @NonNull
    public static ArrayList<String> getImagesPath(Activity activity){
        Uri uri;
        ArrayList<String> list = new ArrayList<String>();
        Cursor cursor;
        int column_data, column_name;
        String pathImg = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection ={MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = activity.getContentResolver().query(uri, projection, null, null, null);
        column_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while(cursor.moveToNext()){
            pathImg = cursor.getString(column_data);
            list.add(pathImg);
        }
        return list;
    }
}
