package com.example.harsayamani.kuisacak;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExpPointActivity extends AppCompatActivity {

    FirebaseAuth auth;
    TextView tvPoint;
    ArrayList<DataPemain> dataPemainArrayList;
    ArrayList<DataRiwayat> dataRiwayatArrayList;
    DatabaseKuisAcak databaseKuisAcak;
    FirebaseAuth.AuthStateListener authStateListener;
    private RecyclerView recyclerView;
    int expPoint = 0;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp_point);
        getSupportActionBar().setTitle("Exp Point");
        recyclerView = findViewById(R.id.recycle_riwayat);
        tvPoint = findViewById(R.id.tvPoint);
        databaseKuisAcak = new DatabaseKuisAcak(getBaseContext());
        dataRiwayatArrayList = new ArrayList<>();
        onAuthState();
        auth = FirebaseAuth.getInstance();
        getPoint();
        dataRecycleView();
    }

    private void onAuthState() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(ExpPointActivity.this, Login2Activity.class));
                    finish();
                }else {
                    user = firebaseAuth.getCurrentUser().getDisplayName();
                }
            }
        };
    }

    private void showRiwayat(){
        SQLiteDatabase db = databaseKuisAcak.getReadableDatabase();
        String getUserName = auth.getCurrentUser().getDisplayName();
        Cursor cursor = db.rawQuery("select * from tb_riwayat where nama = '"+getUserName+"'", null);
        cursor.moveToLast();
        for(int i=0; i<cursor.getCount(); i++){
            cursor.moveToPosition(i);
            DataRiwayat dataRiwayat = new DataRiwayat(i+1, cursor.getString(1), cursor.getInt(2), cursor.getInt(3));
            dataRiwayatArrayList.add(dataRiwayat);
        }
        RecyclerView.Adapter adapter = new RecyclerViewAdapter3(dataRiwayatArrayList, ExpPointActivity.this);
        recyclerView.setAdapter(adapter);
    }

    private void toSQLite(){
        databaseKuisAcak.deleteAllRiwayat();
        for (int i=0; i<dataPemainArrayList.size(); i++){
            String nama = dataPemainArrayList.get(i).getNamaPemain();
            int score = dataPemainArrayList.get(i).getScorePemain();
            int exp = dataPemainArrayList.get(i).getExpPoint();
            databaseKuisAcak.insertDataRiwayat(nama, score, exp);
        }
    }

    private void dataRecycleView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    private void getPoint() {
        final ProgressDialog progressDialog = new ProgressDialog(ExpPointActivity.this);
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Pemain").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataPemainArrayList = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    DataPemain dataPemain = snapshot.getValue(DataPemain.class);
                    dataPemainArrayList.add(dataPemain);
                }

                toSQLite();
                showRiwayat();
                String userName = auth.getCurrentUser().getDisplayName();
                int point;
                for (int i=0; i<dataPemainArrayList.size(); i++){
                    if(dataPemainArrayList.get(i).getNamaPemain().equals(userName)){
                        point = dataPemainArrayList.get(i).getExpPoint();
                        expPoint = expPoint + point;
                    }
                }
                tvPoint.setText(String.valueOf(expPoint)+"xp");
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        startActivity(new Intent(ExpPointActivity.this, DashboardPemain.class));
    }
}
