package com.withpet.walk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.withpet.R;

import org.w3c.dom.Text;

public class Walk_Report extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference reportReference;
    private TextView reportedtitle;
    private TextView walkreport_title;
    private TextView walkreport_content;
    private Button reportBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk__report);

        reportedtitle = findViewById(R.id.walkreport_walktitle);
        walkreport_title = findViewById(R.id.walkreport_title);
        walkreport_content = findViewById(R.id.walkreport_content);
        reportBtn = findViewById(R.id.WalkreportBtn);

        Intent intent = new Intent();
        intent = getIntent();
        final int board_nb = intent.getIntExtra("board_nb",0);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("walk-board").child(Integer.toString(board_nb));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Walk_boardUpload walk_boardUpload = snapshot.getValue(Walk_boardUpload.class);
                reportedtitle.setText(walk_boardUpload.getWalkboard_title());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Walk_ReportUpload walk_reportUpload = new Walk_ReportUpload(walkreport_title.getText().toString(), walkreport_content.getText().toString(),board_nb,user.getUid());
                reportReference = firebaseDatabase.getReference("walk-report").child(Integer.toString(board_nb)).child(user.getUid());
                reportReference.setValue(walk_reportUpload);


            }
        });
  }
}