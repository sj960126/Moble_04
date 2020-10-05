package com.withpet.main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.withpet.R;
import com.withpet.newsfeed.*;

import java.util.ArrayList;

public class ScAdapter extends RecyclerView.Adapter<ScAdapter.ScViewHolder>{

    private ArrayList<Report> sc;
    private Context context; //선택한 activity action 내용

    public ScAdapter(ArrayList<Report> sc, Context context){
        this.sc = sc;
        this.context = context;
    }

    @NonNull
    @Override
    public ScViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sclist_scitem, parent, false);
        ScAdapter.ScViewHolder holder = new ScAdapter.ScViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ScViewHolder holder, int position) {
        holder.tvTitle.setText(sc.get(position).getTitle());
        holder.tvContext.setText(sc.get(position).getContext());
        holder.tvDate.setText(sc.get(position).getDate());
        holder.tvCategory.setText(sc.get(position).getCategory());

        if((sc.get(position).getFeedName()).equals("x")){
            holder.tvAnswer.setText("빠른 시일 내에 확인하도록 하겠습니다. 죄송합니다.");
            holder.tvState.setText("[ 접수 ]");
        }else{
            holder.tvAnswer.setText(sc.get(position).getFeedName());
            holder.tvState.setText("[ 답변 ]");
        }
    }

    @Override
    public int getItemCount() {
        return (sc != null ? sc.size():0);
    }

    public class ScViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDate, tvContext, tvAnswer, tvState, tvCategory;

        public ScViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvState = itemView.findViewById(R.id.sclistTv_state);
            this.tvTitle = itemView.findViewById(R.id.sclistTv_title);
            this.tvDate = itemView.findViewById(R.id.sclistTv_date);
            this.tvContext = itemView.findViewById(R.id.sclistTv_context);
            this.tvAnswer = itemView.findViewById(R.id.sclistTv_answer);
            this.tvCategory = itemView.findViewById(R.id.sclistTv_category);
        }
    }
}
