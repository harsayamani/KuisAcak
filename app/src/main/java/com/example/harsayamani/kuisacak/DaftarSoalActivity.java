package com.example.harsayamani.kuisacak;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class DaftarSoalActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ArrayList<DataSoal> dataSoals;
    private FirebaseAuth auth;
    ProgressDialog progressDialog;
    String getUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_soal);
        recyclerView = findViewById(R.id.datalist);
        getSupportActionBar().setTitle("Daftar Soal");
        auth = FirebaseAuth.getInstance();
        onState();
        dataRecyclerView();
        getData();
    }

    private void onState() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(DaftarSoalActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
    }

    private void getData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mohon tunggu sebentar...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        getUserName = auth.getCurrentUser().getDisplayName();
        reference.child("Pembuat Soal").child("Soal")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSoals = new ArrayList<>();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            DataSoal soal = snapshot.getValue(DataSoal.class);
                            if(auth.getCurrentUser().getDisplayName().equals(soal.getAuthor())){
                                dataSoals.add(soal);
                            }
                        }
                        adapter = new RecyclerViewAdapter1(dataSoals, DaftarSoalActivity.this);
                        recyclerView.setAdapter(adapter);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Daftar soal gagal dimuat", Toast.LENGTH_SHORT).show();
                        Log.e("Daftar Soal", databaseError.getDetails()+" "+databaseError.getMessage());
                    }
                });
    }

    private void dataRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener != null){
            auth.removeAuthStateListener(authStateListener);
        }
    }
}
