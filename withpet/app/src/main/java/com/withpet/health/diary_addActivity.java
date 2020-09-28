package com.withpet.health;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.withpet.*;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class diary_addActivity extends AppCompatActivity {

    private RadioGroup rg;
    private RadioButton rb1;
    private RadioButton rb2;
    private EditText eat;
    private EditText brand;
    private String kind;
    private String time;
    private Button submit;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_add);
        brand =findViewById(R.id.input_brand);
        eat =findViewById(R.id.input_eat);
        rg = findViewById(R.id.radiogroup);
        submit = findViewById(R.id.diaryBtn_submit);
        context =this;
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.eat,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter)
        ;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (l==1){
                    time ="아침";
                }else if(i==2){
                    time ="점심";
                }else if(i==3){
                    time ="저녁";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButton:
                        kind = "식사";
                        break;
                    case R.id.radioButton2:
                        kind = "간식";
                        break;
                }
            }
        });
        try {
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String day = getIntent().getStringExtra("day");
                    if (kind==null||eat.getText().equals(" ")||time==null||brand.getText().equals(" ")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("입력 정보 부족").setMessage("입력 정보를 다시 확인해 주세요.");
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                    else{
                        diary Diary = new diary(kind, eat.getText().toString(), time, brand.getText().toString());
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = database.getReference("Diary").child("song").child(day);
                        databaseReference.push().setValue(Diary);
                        finish();
                    }
                }
            });
        }catch (Exception e){
            Log.i("예외",e.toString());
        }

    }
}