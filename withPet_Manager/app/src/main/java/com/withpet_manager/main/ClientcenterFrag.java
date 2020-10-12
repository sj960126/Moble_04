package com.withpet_manager.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.withpet_manager.Client.Client;
import com.withpet_manager.Client.ClinetAdapter;
import com.withpet_manager.R;

import java.util.ArrayList;

public class ClientcenterFrag extends Fragment {
    private  View view;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ArrayList<Client> arrayList;
    private Client client;
    private RecyclerView.Adapter adapter;
    private ArrayList<String> arraylistKey;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_clientcenter_frag, container,false);
        recyclerView = view.findViewById(R.id.clientcenter_rc);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<Client>();
        arraylistKey = new ArrayList<String>();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        arrayList.clear();
        arraylistKey.clear();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("SC");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    client = new Client();
                    client=dataSnapshot.getValue(Client.class);
                    if (client.getFeedName().equals("x")){
                        arrayList.add(client);
                        arraylistKey.add(dataSnapshot.getKey());
                    }
                }
                adapter.notifyDataSetChanged();

                Log.i("확인"," "+arrayList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new ClinetAdapter(arrayList,arraylistKey,getContext());
        recyclerView.setAdapter(adapter);
    }
}