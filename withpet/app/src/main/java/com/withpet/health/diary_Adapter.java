package com.withpet.health;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.withpet.*;
import com.withpet.main.MainActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class diary_Adapter extends RecyclerView.Adapter<diary_Adapter.CustomViewholder> {

    private  ArrayList<diary> arrayList; //객체 class
    private  ArrayList<String> arraykeyList; //추가
    private Context context; // 선택한 액티비티에 컨텍스트가 필요할떄 사용한다
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private  String date;

    //추가
    public diary_Adapter(ArrayList<diary> arrayList,Context context, ArrayList<String> keylist){
        this.arrayList =arrayList;
        this.context = context;
        arraykeyList = keylist;
    }

    @NonNull
    @Override //실제 리스트뷰가 어뎁터에 연결될때 최초로만들어냄 리사이클 뷰의 행을 표시한다
    public CustomViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_view, parent, false);
        diary_Adapter.CustomViewholder holder = new diary_Adapter.CustomViewholder(view);
        return holder;
    }

    @Override//실제 매칭 이미지 뷰와 텍스트 뷰를 설정한다
    public void onBindViewHolder(@NonNull final CustomViewholder holder, final int position) {
        holder.time.setText(arrayList.get(position).getTime());
        holder.kind.setText(arrayList.get(position).getKind());
        holder.eat.setText(arrayList.get(position).getEat());
        holder.brand.setText(arrayList.get(position).getBrand());
        holder.itemView.setTag(R.integer.diarykey, arraykeyList.get(position)); //아이템 뷰  태그를 단다 포지션  마지막값 선택됨

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("삭제").setMessage("입력 정보를 다시  확인해 주세요.");
                builder.setPositiveButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase database =FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동
                        String key = (String) view.getTag(R.integer.diarykey);
                        date = ((diaryActivity)context).getDate();

                        DatabaseReference databaseReference = database.getReference("Diary").child(firebaseUser.getUid()).child(date).child(key);
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                snapshot.getRef().removeValue();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return false;
            }
        });

    }

    @Override //리사이클 뷰의 행 갯수를 리턴한다
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0); //arraylist사이즈가 널이면 0이다
    }

    //
    public class CustomViewholder extends RecyclerView.ViewHolder {
        TextView kind;
        TextView time;
        TextView eat;
        TextView brand;

        public CustomViewholder(@NonNull final View itemView) {
            super(itemView);
            this.time = itemView.findViewById(R.id.time);
            this.kind = itemView.findViewById(R.id.kind);
            this.eat = itemView.findViewById(R.id.eat);
            this.brand = itemView.findViewById(R.id.brand);

        }
    }

}
