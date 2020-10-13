package com.withpet_manager.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.withpet_manager.R;
import com.withpet_manager.userinfo.Userinfo_getset;
import com.withpet_manager.walkreport.Walk_boardUpload;

import org.w3c.dom.Text;

public class WalkreportdetailFrag extends Fragment {

    private View view;
    private TextView report_er;
    private TextView report_title;
    private TextView report_content;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference_user;
    private Button delete_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_walkreportdetail_frag, container,false);

        report_er = view.findViewById(R.id.walk_reporter);
        report_title = view.findViewById(R.id.walkreport_title);
        report_content = view.findViewById(R.id.walkreport_content);
        delete_btn = view.findViewById(R.id.delete_btn);

        Bundle data = getArguments();
        final String reporter = data.getString("reporter");
        final int board_nb = data.getInt("board_nb");
        Toast.makeText(view.getContext(), ""+board_nb, Toast.LENGTH_SHORT).show();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("walk-board").child(Integer.toString(board_nb));

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Walk_boardUpload walk_boardUpload = snapshot.getValue(Walk_boardUpload.class);

                report_title.setText(walk_boardUpload.getWalkboard_title());
                report_content.setText(walk_boardUpload.getWalkboard_content());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        firebaseDatabase.getReference("User").child(reporter).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                /*Userinfo_getset userinfo_getset = snapshot.getValue(Userinfo_getset.class);
                reporter.setText(userinfo_getset.getName());*/
                report_er.setText((CharSequence) snapshot.child("name").getValue());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseDatabase.getReference("walk-board").child(Integer.toString(board_nb)).removeValue();
                firebaseDatabase.getReference("walk-reply").child(Integer.toString(board_nb)).removeValue();
            }
        });



        return view;
    }
}