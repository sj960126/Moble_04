package com.example.withpet;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyPageNoticeAdapter extends RecyclerView.Adapter<MyPageNoticeAdapter.MyPageNoticeViewHolder> {

    private ArrayList<News> myfeed;
    private Context context; //선택한 activity action 내용
    private  int count = 0;       // 파이어베이스에서 데이터를 가져올 때 3개씩 가져오기위한 변수
    private  int share = 0;       // ArrayList 사이즈를 3으로 나눠 나온 몫의 횟수만큼 사용하기 위한 변수
    private  boolean draw = false;  // 파이어베이스에 있는 내용을 다 출력했는지를 저장하는 변수

    //생성자
    public MyPageNoticeAdapter(ArrayList<News> myfeed, Context context) {
        this.myfeed = myfeed;
        this.context = context;
    }


    //리스트뷰아이템 생성 실행되는 곳
    @NonNull
    @Override
    public MyPageNoticeAdapter.MyPageNoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypagenotice_item, parent, false);
        MyPageNoticeAdapter.MyPageNoticeViewHolder holder = new MyPageNoticeAdapter.MyPageNoticeViewHolder(view);
        Log.i("my view count : ", ""+456456);
        return holder;
    }

    //매칭
    //이미지 서버에서 이미지 불러오기
    //코드내용 줄여보기
    @Override
    public void onBindViewHolder(@NonNull MyPageNoticeAdapter.MyPageNoticeViewHolder holder, int position) {
        /* onBindViewHolder은 화면에 보여질 수 있을 만큼 실행 됨
           position은 화면에 보여지는 수만큼 연결한 ArrayList 항목을 가르킴
           ex) 화면에 출력되는 내용이 4개면 onBindViewHolder은 4번 실행되고 position은 0,1,2,3이 된다.

        */
        Log.i("position:",""+position);
        if (!draw) {
            Log.i("url",""+myfeed.get(position).getImgUrl());
            Log.i("size:",""+myfeed.size());

            // 게시물 내용 3개씩 끊어서 출력하기 위한  if문
            // ex) 파이어베이스에 저장된 데이터가 5개면 한 번은 3개를 그리고 한 번은 2개를 그리기 위함
            if(share < (myfeed.size()/3) ){
                int index = 0;
                for(; count< (position+1)*3 ; count++){
                    Log.i("count1:",""+count);
                    Log.i("iter:",""+((position+1)*3));
                    Log.i("index:",""+index);
                    Glide.with(holder.itemView)
                            .load(myfeed.get(count).getImgUrl())
                            //.placeholder(R.drawable.dog)
                            .into(holder.img[index]);
                    holder.img[index].setTag(count);
                    holder.img[index].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(context, "게시자명 : "+ myfeed.get((int)view.getTag()).getId(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    index++;
                }
                share++;
            }
            else{
                for(int i = 0; i < (myfeed.size()%3) ; i++){
                    Log.i("count2:",""+count);
                    Glide.with(holder.itemView)
                            .load(myfeed.get(count).getImgUrl())
                            //.placeholder(R.drawable.dog)
                            .into(holder.img[i]);
                    holder.img[i].setTag(count++);
                    holder.img[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(context, "게시자명 : "+ myfeed.get((int)view.getTag()).getId(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                draw = true;
            }
        }
    }

    @Override
    public int getItemCount() {
        return (myfeed != null ? myfeed.size():0);
    }

    //viewHolder = listItem
    public class MyPageNoticeViewHolder extends RecyclerView.ViewHolder {
        //listitem
        ImageView[] img = new ImageView[3];

        public MyPageNoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            // 리소스 객체로 줄일 수 있음 같은그림 찾기 소스파일 볼 것
            img[0] = itemView.findViewById(R.id.mypageNoticeIv_item1);
            img[1] = itemView.findViewById(R.id.mypageNoticeIv_item2);
            img[2] = itemView.findViewById(R.id.mypageNoticeIv_item3);
        }
    }
}
