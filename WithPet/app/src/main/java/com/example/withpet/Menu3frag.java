package com.example.withpet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Menu3frag extends Fragment {

    private View rootview;
    private ArrayList<Walk_news> walkfeed = new ArrayList<Walk_news>();
    private Walk_Adapter adpater;
    private FloatingActionButton walkFab_write;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_walk,container,false);
        adpater  = new Walk_Adapter(getActivity(), android.R.layout.simple_list_item_1, walkfeed);


        walkFab_write = (FloatingActionButton) rootview.findViewById(R.id.fab);
        walkFab_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Walk_boardwrite.class);
                startActivity(intent);

            }
        });


        ListView listVN = rootview.findViewById(R.id.list);
        listVN.setAdapter(adpater);
        retrieveUsers();// ListView

        listVN.setOnItemClickListener(listener);

        return rootview;
    }


    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //제목 값 가져올수 있다.
            Toast.makeText(view.getContext(), "뭐가 나올까 : "+walkfeed.get(i).getTitle(), Toast.LENGTH_SHORT).show(); // list 안에 값 갖고온다. 휴
      //      Intent intent  = new Intent(view.getContext(), test.class);
     //       startActivity(intent);
        }
    };

    //=======================
    //  LIST VIEW adapter
    //=======================
    private void retrieveUsers(){
        for(int i =0; i < 10; i++){
            walkfeed.add(new Walk_news("제목 : " + i, "장소 : " + i, R.mipmap.dog));
        }
    }

    public class Walk_Adapter extends ArrayAdapter {
        Context mContext;

        public Walk_Adapter(@NonNull Context context, int resource, @NonNull ArrayList<Walk_news> objects) {
            super(context, resource, objects);
            mContext = context;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            View view = null;
            if(convertView == null){
                view = View.inflate(mContext, R.layout.activity_walk_custom, null);
            }else{
                view = convertView;
            }
            Walk_news news = walkfeed.get(position);
            TextView title = (TextView)view.findViewById(R.id.walk_contents);
            TextView content = (TextView)view.findViewById(R.id.walk_content);
            ImageView img = (ImageView)view.findViewById(R.id.imageView);

            title.setText(news.getTitle());
            content.setText(news.getContext());
            img.setImageResource(news.getImgUri());

            /*LinearLayout click = (LinearLayout)convertView.findViewById(R.id.walk_list);
            click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "dasd", Toast.LENGTH_SHORT).show();
                }
            });*/
            return view;
        }
    }
}
