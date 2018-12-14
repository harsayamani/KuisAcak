package com.example.harsayamani.kuisacak;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class LeaderBoardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<DataPemain> dataPemains;
    private ArrayList<DataLeaderboard> listLeaderBoard;
    private FirebaseAuth.AuthStateListener authStateListener;
    ProgressDialog progressDialog;
    int score;
    String nama;
    DatabaseLeader databaseLeader;
    SQLiteDatabase readData;
    FirebaseAuth auth;
    String user = " ";
    TextView peringkatSaya, peringkatTinggi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        auth = FirebaseAuth.getInstance();
        getSupportActionBar().setTitle("Leaderboard Global");
        recyclerView = findViewById(R.id.dataleader);
        peringkatSaya = findViewById(R.id.tv_peringkat_saya);
        peringkatTinggi = findViewById(R.id.tv_peringkat_tertinggi);
        databaseLeader = new DatabaseLeader(getBaseContext());
        onAuthState();
        listLeaderBoard = new ArrayList<>();
        dataRecycleView();
        getData();
    }

    private void onAuthState() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(LeaderBoardActivity.this, Login2Activity.class));
                    finish();
                }else {
                    user = firebaseAuth.getCurrentUser().getDisplayName();
                }
            }
        };

    }

    private void dataRecycleView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    private void showData() {
        readData = databaseLeader.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = readData.rawQuery("select * from tb_leaderboard order by score desc",null);
        cursor.moveToFirst();
        for(int i=0; i<cursor.getCount(); i++){
//            if (i==10){
//                break;
//            }
            cursor.moveToPosition(i);
            DataLeaderboard dataLeaderboard = new DataLeaderboard(i+1, cursor.getString(1), cursor.getInt(2));
            listLeaderBoard.add(dataLeaderboard);
        }
        RecyclerView.Adapter adapter = new RecyclerViewAdapter2(listLeaderBoard, LeaderBoardActivity.this);
        recyclerView.setAdapter(adapter);
        showPeringkat();
    }

    private void showPeringkat(){
        for(int i=0; i<listLeaderBoard.size(); i++){
            if (listLeaderBoard.get(i).getNomor()==1){
                peringkatTinggi.setText(listLeaderBoard.get(i).getNama());
            }
            if (auth.getCurrentUser().getDisplayName().equals(listLeaderBoard.get(i).getNama())){
                peringkatSaya.setText(String.valueOf(listLeaderBoard.get(i).getNomor()));
                break;
            }
        }
    }

    private void toSQLite(){
        databaseLeader.deleteAll();
        for (int i=0; i<dataPemains.size(); i++){
            nama = dataPemains.get(i).getNamaPemain();
            score = dataPemains.get(i).getScorePemain();
            databaseLeader.insertData(nama, score);
        }
    }

    private void getData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sedang memuat...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        reference.child("Pemain").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataPemains = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    DataPemain dp = snapshot.getValue(DataPemain.class);
                    dataPemains.add(dp);
                }
                toSQLite();
                showData();
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
    public void onBackPressed() {
        startActivity(new Intent(LeaderBoardActivity.this, DashboardPemain.class));
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
