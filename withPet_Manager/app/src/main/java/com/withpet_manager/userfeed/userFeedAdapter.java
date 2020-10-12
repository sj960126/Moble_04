package com.withpet_manager.userfeed;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import com.withpet_manager.*;
import com.withpet_manager.main.MainActivity;

public class userFeedAdapter  extends RecyclerView.Adapter<userFeedAdapter.userFeedViewHolder> {

    private ArrayList<Report> reports;
    private Context context; //선택한 activity action 내용

    public userFeedAdapter(ArrayList<Report> reports, Context context){
        this.reports = reports;
        this.context = context;
    }

    @NonNull
    @Override
    public userFeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userfeed_item, parent, false);
        userFeedViewHolder holder = new userFeedViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull userFeedViewHolder holder, int position) {

        holder.tvCategory.setText(reports.get(position).getCategory());
        holder.tvTitle.setText(reports.get(position).getTitle());
        //날짜
        String[] date = reports.get(position).getDate().split("_");
        holder.tvDate.setText(date[0]);
        holder.tvContext.setText(reports.get(position).getContext());
        holder.tvUid.setText(reports.get(position).getUid());
    }

    @Override
    public int getItemCount() {
        return (reports != null ? reports.size():0);
    }

    public class userFeedViewHolder extends RecyclerView.ViewHolder {
        //위젯
        TextView tvCategory, tvTitle, tvDate, tvContext, tvUid;

        public userFeedViewHolder(@NonNull View itemView) {
            super(itemView);
            //정의
            tvCategory = itemView.findViewById(R.id.feedTv_category);
            tvTitle = itemView.findViewById(R.id.feedTv_title);
            tvDate = itemView.findViewById(R.id.feedTv_date);
            tvContext = itemView.findViewById(R.id.feedTv_context);
            tvUid = itemView.findViewById(R.id.feedTv_uid);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, feedDetailActivity.class);
                    intent.putExtra("boardName", reports.get(getAdapterPosition()).getFeedName());
                    intent.putExtra("category", reports.get(getAdapterPosition()).getCategory());
                    context.startActivity(intent);
                }
            });
        }
    }
}
