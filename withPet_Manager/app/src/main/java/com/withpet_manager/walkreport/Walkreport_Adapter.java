package com.withpet_manager.walkreport;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.withpet_manager.R;
import com.withpet_manager.main.MainActivity;

import java.util.ArrayList;

public class Walkreport_Adapter extends RecyclerView.Adapter<Walkreport_Adapter.CustomViewholder> {

    private  ArrayList<Walkreport_getset> arrayList;
    private Context context;

    public Walkreport_Adapter(ArrayList<Walkreport_getset> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }
    @NonNull
    @Override
    public CustomViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_walkitem,parent,false);
       Walkreport_Adapter.CustomViewholder holder = new Walkreport_Adapter.CustomViewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewholder holder, int position) {
        holder.report_title.setText(arrayList.get(position).getReport_title());
        holder.report_content.setText(arrayList.get(position).getReport_content());
    }

    @Override
    public int getItemCount()  {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class  CustomViewholder extends RecyclerView.ViewHolder{
        TextView report_title;
        TextView report_content;
        public CustomViewholder(@NonNull final View itemView) {
            super(itemView);

            this.report_title = itemView.findViewById(R.id.report_title);
            this.report_content = itemView.findViewById(R.id.report_content);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.putExtra("frag",5);
                    context.startActivity(intent);
                 //   arrayList.get(getAdapterPosition()).getReport_boardnb();
                 //   arrayList.get(getAdapterPosition()).getReporter();

                }
            });
        }
    }
}
