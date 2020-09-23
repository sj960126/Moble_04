package com.withpet.walk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.withpet.*;
import com.withpet.main.*;

import java.util.ArrayList;

public class Walk_Adapter extends RecyclerView.Adapter<Walk_Adapter.CustomViewholder>{

    private  ArrayList<Walk_boardUpload> arrayList;
    private Context context;
    private Activity activity;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private String walkboard_title;
    private String walkboard_content;
    int reply_nb = 0;
    int board_nb = 0;
    double centerPoint;
    public Walk_Adapter(ArrayList<Walk_boardUpload> arrayList,Context context,Activity activity){
        this.arrayList =arrayList;
        this.context = context;
        this.activity = activity;

    }
    @NonNull
    @Override
    public CustomViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_walk_custom, parent, false);
        CustomViewholder holder = new CustomViewholder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewholder holder, int position) {


    //    Toast.makeText(this.context, ""+arrayList.get(position).getWalkboard_title(), Toast.LENGTH_SHORT).show();
      /*  database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("walk-board").child(Integer.toString(arrayList.get(position).getWalkboard_nb()));
*/
        holder.tv_title.setText(arrayList.get(position).getWalkboard_title());
        holder.tv_contents.setText(arrayList.get(position).getWalkboard_content());

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }
//recyclerview 산책 메인
    public class CustomViewholder extends RecyclerView.ViewHolder {
      //  ImageView iv_profile;
        TextView tv_title;
        TextView tv_contents;

        public CustomViewholder(@NonNull final View itemView) {
            super(itemView);
            this.tv_title = itemView.findViewById(R.id.walk_title);
            this.tv_contents = itemView.findViewById(R.id.walk_content);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int pos = getAdapterPosition();

                    database = FirebaseDatabase.getInstance();
                    databaseReference = database.getReference("walk-reply").child(Integer.toString(arrayList.get(pos).getWalkboard_nb()));
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot ds:snapshot.getChildren()){
                                Walk_ReplyUpload tmp = ds.getValue(Walk_ReplyUpload.class);
                                reply_nb = tmp.getReply_nb()+1;



                                //댓글 번호 내부 저장소에 저장
                                SharedPreferences sharedPreferences = activity.getSharedPreferences("sFile",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("reply_nb",reply_nb);
                                editor.putInt("board_nb", arrayList.get(getAdapterPosition()).getWalkboard_nb()); //이거부터 해라 게시글 번호 내부 저장소에 저장
                                editor.commit();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    board_nb = arrayList.get(pos).getWalkboard_nb();

                    Log.i("nb", ""+board_nb);
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("frag",5); // 작성한 글 frag로 가기위해 intent값 전달
                    intent.putExtra("board_nb",board_nb);
                    intent.putExtra("centerLat", Point_Lat(pos));
                    intent.putExtra("centerLong", Point_Long(pos));
                    Log.i("유럽가봤니?",""+Point_Lat(pos));
                    context.startActivity(intent);


                    if (pos != RecyclerView.NO_POSITION){

                    }
                }
            });
        }

        //게시판 tmap 위도 경도 중간값 전달
        private double Point_Lat(int pos){
            double Lat = arrayList.get(pos).getLat0()+arrayList.get(pos).getLat1()+arrayList.get(pos).getLat2()+ arrayList.get(pos).getLat3();

            if(arrayList.get(pos).getLong3() == 0.0 && arrayList.get(pos).getLong2() != 0.0 ){
                centerPoint = (Lat)/3;
            }else if (arrayList.get(pos).getLong2() == 0.0){
                centerPoint = (Lat)/2;
            }else{
                centerPoint = (Lat)/4;
            }

            return centerPoint;
        }
        private double Point_Long(int pos){
        double Long = arrayList.get(pos).getLong0() + arrayList.get(pos).getLong1() + arrayList.get(pos).getLong2()+ arrayList.get(pos).getLong3();

        if(arrayList.get(pos).getLong3() == 0 && arrayList.get(pos).getLong2() != 0.0 ){
            centerPoint = (Long)/3;
        }else if (arrayList.get(pos).getLong2() == 0){
            centerPoint = (Long)/2;
        }else{
            centerPoint = (Long)/4;
        }

        return centerPoint;
    }

    }
}
