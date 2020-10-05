package com.withpet.newsfeed;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.withpet.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

//ReplyAdapter function
public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {

    private ArrayList<Reply> reply;
    private Context context; //선택한 activity action 내용
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    //생성자
    public ReplyAdapter(ArrayList<Reply> reply, Context context) {
        this.reply = reply;
        this.context = context;
    }

    @NonNull
    @Override
    public ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_replyitem, parent, false);
        ReplyViewHolder holder = new ReplyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyViewHolder holder, int position) {
        //Glide.with(holder.itemView).load(myfeed.get(position).getImgUrl()).circleCrop().into(holder.loginUserImg);

        //각 게시글의 닉네임, 프로필이미지
        SharedPreferences preferences = context.getSharedPreferences(reply.get(position).getUid(), Context.MODE_PRIVATE);
        String nickName = preferences.getString("nickName", "host");
        String feedImg = preferences.getString("img","");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        holder.name.setText(nickName);
        holder.tvcontext.setText(reply.get(position).getContext());
        Glide.with(holder.itemView).load(feedImg).circleCrop().into(holder.img);
    }

    @Override
    public int getItemCount() {
        return (reply != null ? reply.size():0);
    }

    public class ReplyViewHolder extends RecyclerView.ViewHolder {
        TextView name, tvcontext;
        CircleImageView img;

    public ReplyViewHolder(@NonNull View itemView) {
        super(itemView);

        this.name = itemView.findViewById(R.id.replyTv_nickname);
        //img = itemView.findViewById(R.id.mainImage);
        this.tvcontext = itemView.findViewById(R.id.replyTv_re);
        this.img = itemView.findViewById(R.id.replyCiv_re);

        img.setImageResource(R.drawable.userdefault);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = getAdapterPosition();
                if(pos != RecyclerView.NO_POSITION){
/*                    String ang = (tvcontext.getText()).toString();
                    Toast.makeText(context, ""+ reply.get(pos).getBoardName(), Toast.LENGTH_SHORT).show();*/
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    if((reply.get(pos).getUid()).equals(firebaseUser.getUid())){
                        final List<String> ListItems = new ArrayList<>();
                        ListItems.add("댓글 수정");
                        ListItems.add("댓글 삭제");
                        final CharSequence[] items =  ListItems.toArray(new String[ ListItems.size()]);

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("댓글 설정");
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String selectedText = items[which].toString();
                                DatabaseReference databaseReference = firebaseDatabase.getReference("Reply");
                                if(selectedText.equals("댓글 수정")){
                                    Intent intent = new Intent(context, FeedWriteActivity.class);
                                    intent.putExtra("replyBoardName", reply.get(pos).getBoardName());
                                    intent.putExtra("replyName", reply.get(pos).getReplyName());
                                    intent.putExtra("replyUid", reply.get(pos).getUid());
                                    intent.putExtra("replyContext", reply.get(pos).getContext());
                                    intent.putExtra("replyDate", reply.get(pos).getDate());
                                    context.startActivity(intent);
                                }
                                else if(selectedText.equals("댓글 삭제")){
                                    databaseReference.child(reply.get(pos).getBoardName()).child(reply.get(pos).getReplyName()).removeValue();
                                    Toast.makeText(context, "성공적으로 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                        builder.show();
                    }
                    else {
                        Toast.makeText(context, "본인이 작성한 댓글이 아닙니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

}
}
