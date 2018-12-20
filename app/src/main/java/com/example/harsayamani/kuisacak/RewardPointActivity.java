package com.example.harsayamani.kuisacak;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class RewardPointActivity extends AppCompatActivity {

    TextView rewardPoint;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ArrayList<DataSoal> dataSoals;
    private FirebaseAuth auth;
    ProgressDialog progressDialog;
    String getUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_point);
        getSupportActionBar().setTitle("Reward Point");
        auth = FirebaseAuth.getInstance();
        rewardPoint = findViewById(R.id.reward_point);
        onState();
        getData();
    }

    private void onState() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(RewardPointActivity.this, LoginActivity.class));
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
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSoals = new ArrayList<>();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            DataSoal soal = snapshot.getValue(DataSoal.class);
                            if(auth.getCurrentUser().getDisplayName().equals(soal.getAuthor())){
                                dataSoals.add(soal);
                            }
                        }
                        int point = 0;
                        for (int i=0; i<dataSoals.size(); i++){
                            if (dataSoals.get(i).getAuthor().equals(auth.getCurrentUser().getDisplayName())){
                                point = (i+1)*100;
                            }
                        }
                        rewardPoint.setText(String.valueOf(point));

                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Daftar soal gagal dimuat", Toast.LENGTH_SHORT).show();
                        Log.e("Daftar Soal", databaseError.getDetails()+" "+databaseError.getMessage());
                    }
                });
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RewardPointActivity.this, DashboardPembuatSoal.class));
    }
}
