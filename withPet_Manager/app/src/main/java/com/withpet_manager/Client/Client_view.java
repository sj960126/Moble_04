package com.withpet_manager.Client;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.withpet_manager.*;
public class Client_view extends AppCompatActivity {
    private TextView category;
    private TextView title;
    private EditText context;
    private Button button;
    private String key;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_view);
        category = findViewById(R.id.clinet_category);
        title = findViewById(R.id.client_title);
        context = findViewById(R.id.context);
        button = findViewById(R.id.client_submit);

        Intent intent = getIntent();
        final String input_category = intent.getStringExtra("category");
        final String input_title = intent.getStringExtra("title");
        final String input_context = intent.getStringExtra("context");
        final String input_date = intent.getStringExtra("date");
        final String input_uid = intent.getStringExtra("uid");
        key = intent.getStringExtra("key");
        category.setText(input_category);
        title.setText(input_title);
        context.setHint(input_context);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String change_context = context.getText().toString();
                Client client = new Client(input_category,change_context,input_date,"o",input_title,input_uid);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference("SC").child(key);
                databaseReference.setValue(client);
                finish();
            }
        });

    }



}