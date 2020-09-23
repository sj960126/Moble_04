package com.withpet.walk;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;
import com.withpet.*;

import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

public class Walk_boarddetailFrag extends Fragment {
    private View view;
    private int board_nb;
    private String title ="title";
    private String content = "content";
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference_reply;
    private DatabaseReference databaseReference_replyview;
    private FirebaseDatabase database;
    private TextView title_tv;
    private TextView content_tv;
    private double spot[][] = new double[100][2];
    private TMapView tMapView;
    private final String APK ="l7xxfa281c47f54b4b8d866946553f981932";
    private LinearLayout tmap;
    private TextView replyTv;
    private TextView nameTv;
    private TextView walkreplyTv_add;
    private Button replyaddBtn;
    int line_nb;
    int repeat;
    int reply_nb;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Walk_ReplyUpload> arrayList;
    private TextView nicknameTv;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_walk_boarddetail,container,false);
        tmap = view.findViewById(R.id.detail_tmap);
        replyTv = view.findViewById(R.id.walkreplyTv_add);
        replyaddBtn =view.findViewById(R.id.walkreplyBtn_add);
        database = FirebaseDatabase.getInstance();

        tMapView = new TMapView(getContext());
        tMapView.setSKTMapApiKey(APK);
        tmap.addView(tMapView);

        recyclerView = view.findViewById(R.id.walkrv_reply);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        walkreplyTv_add = view.findViewById(R.id.walkreplyTv_add);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            bundle = getArguments();
            board_nb = bundle.getInt("board_nb");
            title = bundle.getString("board_title");
            content = bundle.getString("board_content");
        }

        databaseReference_replyview = database.getReference("walk-reply").child(Integer.toString(board_nb));
        //recyclerview adapter 바로 업데이트
        databaseReference_replyview.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot datasnapshot, @Nullable String previousChildName) {

                Walk_ReplyUpload walk_replyUpload = datasnapshot.getValue(Walk_ReplyUpload.class);
                arrayList.add(walk_replyUpload);


                adapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        adapter = new Walk_ReplyAdapter(arrayList, this.getContext());
        recyclerView.setAdapter(adapter);


        //댓글 작성 버튼 firebase 업로드
        replyaddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                databaseReference_reply = database.getReference("walk-reply").child(Integer.toString(board_nb)).child(Integer.toString(reply_nb));

                Walk_ReplyUpload walk_replyUpload = new Walk_ReplyUpload(board_nb,replyTv.getText().toString().trim(),user.getUid(),reply_nb);
                databaseReference_reply.setValue(walk_replyUpload);
                reply_nb++;
                walkreplyTv_add.setText("");
                adapter.notifyDataSetChanged();
            }
        });


        // 클릭한 게시물 번호에 관련된 firebase 제목 내용 갖고 오기
        databaseReference = database.getReference("walk-board").child(Integer.toString(board_nb));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Walk_boardUpload tmp = dataSnapshot.getValue(Walk_boardUpload.class);

                    title = tmp.getWalkboard_title();
                    content = tmp.getWalkboard_content();

                    spot[0][0] = tmp.getLat0();
                    spot[0][1] = tmp.getLong0();
                    spot[1][0] = tmp.getLat1();
                    spot[1][1] = tmp.getLong1();
                    spot[2][0] = tmp.getLat2();
                    spot[2][1] = tmp.getLong2();
                    spot[3][0] = tmp.getLat3();
                    spot[3][1] = tmp.getLong3();

                    title_tv = (TextView) view.findViewById(R.id.walkTv_title);
                    content_tv = (TextView) view.findViewById(R.id.walkTv_content);
                    nicknameTv = view.findViewById(R.id.walk_nickname);

                    //SharedPreferences 내부저장소
                    SharedPreferences preferences = getContext().getSharedPreferences(tmp.getUid(), Context.MODE_PRIVATE);
                    String nickName = preferences.getString("nickName", "host");
                    SharedPreferences sf = getActivity().getSharedPreferences("sFile",Context.MODE_PRIVATE);
                    reply_nb = sf.getInt("reply_nb",0);

                    title_tv.setText(title);
                    content_tv.setText(content);
                    nicknameTv.setText(nickName);

                    if(spot[3][0] == 0.0)repeat=2;
                    else if(spot[2][0] == 0.0)repeat=1;
                    else repeat = 3;
                    for(int i =0; i<repeat; i++) {
                        path(spot[i][0], spot[i][1], spot[i+1][0], spot[i+1][1]);
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        return view;
    }

    //Tmap 좌표 받아와 경로 찍기
    public void path(final double a, final double b, final double c, final double d){

        new Thread(){
            @Override
            public void run(){
                try{

                    TMapPoint start_point = new TMapPoint(a,b);
                    TMapPoint end_point = new TMapPoint(c,d);

                    TMapPolyLine tMapPolyLine = new TMapData().findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, start_point, end_point);
                    tMapPolyLine.setLineColor(Color.BLUE);
                    tMapPolyLine.setLineWidth(2);
                    tMapView.addTMapPolyLine("Line"+line_nb, tMapPolyLine);
                    line_nb++;
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }
}