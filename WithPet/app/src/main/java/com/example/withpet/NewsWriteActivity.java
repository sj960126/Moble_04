package com.example.withpet;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NewsWriteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_write);

        final int clo = 3;

        recyclerView = (RecyclerView) findViewById(R.id.mainwGv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, clo));

        mAdapter = new GalleryAdapter(getImagesPath(this));
        recyclerView.setAdapter(mAdapter);
    }

    @NonNull
    public static ArrayList<String> getImagesPath(Activity activity){
        Uri uri;
        ArrayList<String> list = new ArrayList<String>();
        Cursor cursor;
        int column_data, column_name;
        String pathImg = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

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
