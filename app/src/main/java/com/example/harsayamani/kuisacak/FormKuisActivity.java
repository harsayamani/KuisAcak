package com.example.harsayamani.kuisacak;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FormKuisActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    TextView tvTimer, tvSoal, tvScore;
    Button btnOpsi1, btnOpsi2, btnOpsi3, btnOpsi4;
    private FirebaseAuth auth ;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    Random random = new Random();
    String getOpsiA;
    String getOpsiB;
    String getOpsiC;
    String getOpsiBenar;
    int score = 0;
    int count = 1;
    LayoutInflater inflater;
    View dialog_view;
    int kelipatan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_form_kuis);

        tvScore = findViewById(R.id.tv_score);
        tvSoal = findViewById(R.id.tv_soal);
        tvTimer = findViewById(R.id.tv_timer);
        btnOpsi1 = findViewById(R.id.opsi1);
        btnOpsi1.setOnClickListener(this);
        btnOpsi2 = findViewById(R.id.opsi2);
        btnOpsi2.setOnClickListener(this);
        btnOpsi3 = findViewById(R.id.opsi3);
        btnOpsi3.setOnClickListener(this);
        btnOpsi4 = findViewById(R.id.opsi4);
        btnOpsi4.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        inflater = getLayoutInflater();
        scoreView();
        generateSoal();
    }

    private void scoreView() {
        tvScore.setText("Score : "+score);
    }

    private void timerRun() {
        TimerClass timerClass = new TimerClass(10000, 1000);
        timerClass.start();
    }

    public class TimerClass extends CountDownTimer{

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        TimerClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            @SuppressLint("DefaultLocale") String timer = String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

            tvTimer.setText(timer);
        }

        @SuppressLint("InflateParams")
        @Override
        public void onFinish() {
            Intent i = getIntent();
            kelipatan = i.getIntExtra("kelipatan",0);
            dialog_view = inflater.inflate(R.layout.desain_dialog_waktu, null);
            AlertDialog.Builder alert = new AlertDialog.Builder(FormKuisActivity.this);
            alert.setView(dialog_view);
            alert.setCancelable(false);
            alert.setPositiveButton("Lanjut", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    score = score + 0;
                    Intent intent = new Intent(FormKuisActivity.this, FormKuis2Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("score", score);
                    intent.putExtra("count", count++);
                    intent.putExtra("kelipatan", kelipatan);
                    startActivity(intent);
                }
            });
            alert.show();
        }
    }

    private void generateSoal() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sedang memuat soal...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        reference.child("Pembuat Soal").child("Soal").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<DataSoal> dataSoalArrayList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    DataSoal soalPemain = snapshot.getValue(DataSoal.class);
                    dataSoalArrayList.add(soalPemain);
                }

                int indexGenerator;
                for(int i=0; i<dataSoalArrayList.size(); i++){
                    indexGenerator = random.nextInt(dataSoalArrayList.size());
                    tvSoal.setText(dataSoalArrayList.get(indexGenerator).getSoal());
                    getOpsiA = dataSoalArrayList.get(indexGenerator).getOpsiA();
                    getOpsiB = dataSoalArrayList.get(indexGenerator).getOpsiB();
                    getOpsiC = dataSoalArrayList.get(indexGenerator).getOpsiC();
                    getOpsiBenar = dataSoalArrayList.get(indexGenerator).getOpsiBenar();
                }

                btnOpsi1.setText(getOpsiB);
                btnOpsi2.setText(getOpsiC);
                btnOpsi3.setText(getOpsiBenar);
                btnOpsi4.setText(getOpsiA);

                progressDialog.dismiss();
                timerRun();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Soal gagal dimuat", Toast.LENGTH_SHORT).show();
                Log.e("Daftar Soal", databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });
    }


    @Override
    public void onBackPressed() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Apakah anda ingin keluar dari kuis?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(FormKuisActivity.this, DashboardPemain.class));
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.setCancelable(true);
                    }
                });

        alertDialog.show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        RelativeLayout onConnectionFail = findViewById(R.id.connection_failed);
        onConnectionFail.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.opsi1:
                dialogSalah();
                break;
            case R.id.opsi2:
                dialogSalah();
                break;
            case R.id.opsi3:
                dialogBenar();
                break;
            case R.id.opsi4:
                dialogSalah();
                break;
        }
    }

    @SuppressLint("InflateParams")
    private void dialogSalah(){
        Intent i = getIntent();
        kelipatan = i.getIntExtra("kelipatan",0);
        dialog_view = inflater.inflate(R.layout.desain_jawaban_salah,null);
        AlertDialog.Builder alert = new AlertDialog.Builder(FormKuisActivity.this);
        alert.setView(dialog_view);
        alert.setCancelable(false);
        alert.setPositiveButton("Lanjut", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                score = score+0;
                count = count+1;
                Intent intent = new Intent(FormKuisActivity.this, FormKuis2Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("score", score);
                intent.putExtra("count", count);
                intent.putExtra("kelipatan", kelipatan);
                startActivity(intent);
            }
        });
        alert.show();
    }

    @SuppressLint("InflateParams")
    private void dialogBenar(){
        Intent i = getIntent();
        kelipatan = i.getIntExtra("kelipatan",0);
        dialog_view = inflater.inflate(R.layout.desain_jawaban_benar,null);
        AlertDialog.Builder alert = new AlertDialog.Builder(FormKuisActivity.this);
        alert.setView(dialog_view);
        alert.setCancelable(false);
        alert.setPositiveButton("Lanjut", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                score = score+100;
                count = count + 1;
                Intent intent = new Intent(FormKuisActivity.this, FormKuis2Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("score", score);
                intent.putExtra("count", count);
                intent.putExtra("kelipatan", kelipatan);
                startActivity(intent);
            }
        }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        startActivity(new Intent(FormKuisActivity.this, DashboardPemain.class));
    }
}
