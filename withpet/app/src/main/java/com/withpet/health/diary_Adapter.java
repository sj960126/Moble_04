package com.withpet.health;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.withpet.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class diary_Adapter extends RecyclerView.Adapter<diary_Adapter.CustomViewholder> {

    private  ArrayList<diary> arrayList; //객체 class
    private Context context; // 선택한 액티비티에 컨텍스트가 필요할떄 사용한다


    public diary_Adapter(ArrayList<diary> arrayList,Context context){
        this.arrayList =arrayList;
        this.context = context;
    }

    @NonNull
    @Override //실제 리스트뷰가 어뎁터에 연결될때 최초로만들어냄
    public CustomViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_view, parent, false);
        diary_Adapter.CustomViewholder holder = new diary_Adapter.CustomViewholder(view);
        return holder;
    }

    @Override//실제 매칭
    public void onBindViewHolder(@NonNull CustomViewholder holder, int position) {

        holder.time.setText(arrayList.get(position).getTime());
        holder.kind.setText(arrayList.get(position).getKind());
        holder.eat.setText(arrayList.get(position).getEat());
        holder.brand.setText(arrayList.get(position).getBrand());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0); //arraylist사이즈가 널이면 0이다
    }

    //
    public class CustomViewholder extends RecyclerView.ViewHolder {
        TextView kind;
        TextView time;
        TextView eat;
        TextView brand;

        public CustomViewholder(@NonNull View itemView) {
            super(itemView);
            this.time = itemView.findViewById(R.id.time);
            this.kind = itemView.findViewById(R.id.kind);
            this.eat = itemView.findViewById(R.id.eat);
            this.brand = itemView.findViewById(R.id.brand);
        }
    }

}
