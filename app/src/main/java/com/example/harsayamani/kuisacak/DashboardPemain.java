package com.example.harsayamani.kuisacak;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.view.MaterialIntroView;

public class DashboardPemain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    TextView namaAkun, displayName, tvLevel;
    CardView leaderboard;
    Button playKuis;
    int soalKelipatan = 0;
    ImageView imgHelp;
    SeekBar seekBar;
    ArrayList<DataPemain> dataPemainArrayList;
    int expPoint = 0;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_pemain);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        namaAkun = findViewById(R.id.nama_pemain);
        displayName = findViewById(R.id.display_user_dashboard);
        tvLevel = findViewById(R.id.level_user);
        auth = FirebaseAuth.getInstance();
        seekBar = findViewById(R.id.seekBar);
        imgHelp = findViewById(R.id.image_help);
        imgHelp.setOnClickListener(this);
        playKuis = findViewById(R.id.soal_pilihan_ganda);
        playKuis.setOnClickListener(this);
        leaderboard = findViewById(R.id.btn_leaderboard);
        leaderboard.setOnClickListener(this);
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        seekBar.setOnSeekBarChangeListener(this);
        navigationDrawer();
        onState();
        levelPemain();
    }

    private void onState() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(DashboardPemain.this, Login2Activity.class));
                    finish();
                }else {
                    String getUserName = firebaseAuth.getCurrentUser().getDisplayName();
                    namaAkun.setText(getUserName);
                    displayName.setText(getUserName);
                    Uri resID = firebaseAuth.getCurrentUser().getPhotoUrl();
                    Picasso.with(getApplicationContext()).load(resID)
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher)
                            .transform(new PicassoCircleTransformation())
                            .into((ImageView)findViewById(R.id.akun_foto));
                }
            }
        };
    }

    private void navigationDrawer() {
        drawerLayout = findViewById(R.id.fitur_utama);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);
        navigationView = findViewById(R.id.navigasi);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.dashboard:
                Intent intent = new Intent(DashboardPemain.this, DashboardPemain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
            case R.id.daftar_skor:
                startActivity(new Intent(DashboardPemain.this, LeaderBoardActivity.class));
                break;
            case R.id.exp_point:
                startActivity(new Intent(DashboardPemain.this, ExpPointActivity.class));
                break;
            case R.id.log_out:
                logOut();
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.fitur_utama);
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        return false;
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

    private void logOut() {
        auth.signOut();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.soal_pilihan_ganda:
                Intent intent = new Intent(DashboardPemain.this, StartKuisActivity.class);
                intent.putExtra("kelipatan", soalKelipatan);
                startActivity(intent);
                break;
            case R.id.btn_leaderboard:
                startActivity(new Intent(DashboardPemain.this, LeaderBoardActivity.class));
                break;
            case R.id.image_help:
                Intent intent1 = new Intent(DashboardPemain.this, MaterialSlide.class);
                startActivity(intent1);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(DashboardPemain.this, MainActivity.class));
    }

    public void levelPemain(){
        final ProgressDialog progressDialog = new ProgressDialog(DashboardPemain.this);
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setIndeterminate(true);
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

                String userName = auth.getCurrentUser().getDisplayName();
                int point;

                for (int i=0; i<dataPemainArrayList.size(); i++){
                    if(dataPemainArrayList.get(i).getNamaPemain().equals(userName)){
                        point = dataPemainArrayList.get(i).getExpPoint();
                        expPoint = expPoint + point;
                    }else {
                        continue;
                    }
                }
                if(expPoint>= 0 && expPoint<=3000){
                    soalKelipatan = 1;
                    seekBar.setMax(3000);
                    seekBar.setProgress(expPoint);
                    tvLevel.setText("Level 1");
                }else if (expPoint>=3001 && expPoint<=6000){
                    soalKelipatan = 2;
                    seekBar.setMax(6000-3000);
                    seekBar.setProgress(expPoint-3000);
                    tvLevel.setText("Level 2");
                }else if (expPoint>=6001 && expPoint<=9000){
                    soalKelipatan = 3;
                    seekBar.setMax(9000-6000);
                    seekBar.setProgress(expPoint-6000);
                    tvLevel.setText("Level 3");
                }else if (expPoint>=9001 && expPoint<=12000){
                    seekBar.setMax(12000-9000);
                    seekBar.setProgress(expPoint-9000);
                    soalKelipatan = 4;
                    tvLevel.setText("Level 4");
                }else if (expPoint>=12001 && expPoint<=15000){
                    soalKelipatan = 5;
                    seekBar.setMax(15000-12000);
                    seekBar.setProgress(expPoint-12000);
                    tvLevel.setText("Level 5");
                }else if (expPoint>=15001 && expPoint<=18000){
                    soalKelipatan = 6;
                    seekBar.setMax(18000-15000);
                    seekBar.setProgress(expPoint-15000);
                    tvLevel.setText("Level 6");
                }else if (expPoint>=18001 && expPoint<=21000){
                    soalKelipatan = 7;
                    seekBar.setMax(21000-18000);
                    seekBar.setProgress(expPoint-18000);
                    tvLevel.setText("Level 7");
                }else if (expPoint>=21001 && expPoint<=24000){
                    soalKelipatan = 8;
                    seekBar.setMax(24000-21000);
                    seekBar.setProgress(expPoint-21000);
                    tvLevel.setText("Level 8");
                }else if (expPoint>=24001 && expPoint<=27000){
                    soalKelipatan = 9;
                    seekBar.setMax(27000-24000);
                    seekBar.setProgress(expPoint-24000);
                    tvLevel.setText("Level 9");
                }else if (expPoint>=27001 && expPoint<=30000){
                    soalKelipatan = 10;
                    seekBar.setMax(30000-27000);
                    seekBar.setProgress(expPoint-27000);
                    tvLevel.setText("Level 10");
                }
                progressDialog.dismiss();

                new MaterialIntroView.Builder(DashboardPemain.this)
                        .enableDotAnimation(true)
                        .enableIcon(false)
                        .setFocusGravity(FocusGravity.CENTER)
                        .setFocusType(Focus.MINIMUM)
                        .setDelayMillis(500)
                        .setShape(ShapeType.CIRCLE)
                        .enableFadeAnimation(true)
                        .performClick(true)
                        .setInfoText("Klik Disini!")
                        .setIdempotent(true)
                        .setTarget(imgHelp)
                        .setUsageId("intro_card")
                        .show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
