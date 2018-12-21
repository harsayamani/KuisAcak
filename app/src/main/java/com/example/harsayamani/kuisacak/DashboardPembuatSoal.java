package com.example.harsayamani.kuisacak;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import static android.text.TextUtils.isEmpty;

public class DashboardPembuatSoal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    EditText soal_kuis, opsi_A, opsi_B, opsi_C, opsi_benar;
    Button submit;
    TextView namaAkun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_pembuat_soal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationDrawer();
        soal_kuis = findViewById(R.id.soal);
        opsi_A = findViewById(R.id.opsiA);
        opsi_B = findViewById(R.id.opsiB);
        opsi_C = findViewById(R.id.opsiC);
        opsi_benar = findViewById(R.id.opsiBenar);
        submit = findViewById(R.id.submit_soal);
        submit.setOnClickListener(this);
        namaAkun = findViewById(R.id.nama);
        auth =  FirebaseAuth.getInstance();

        onAuthState();

    }

    private void onAuthState() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(DashboardPembuatSoal.this, LoginActivity.class));
                    finish();
                }else {
                    String getUserName = firebaseAuth.getCurrentUser().getDisplayName();
                    namaAkun.setText(getUserName);
                    Uri resID = firebaseAuth.getCurrentUser().getPhotoUrl();
                    Picasso.with(getApplicationContext()).load(resID)
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher)
                            .transform(new PicassoCircleTransformation())
                            .into((ImageView)findViewById(R.id.foto_akun));
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
                Intent intent = new Intent(this, DashboardPembuatSoal.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
            case R.id.daftar_soal:
                startActivity(new Intent(this, DaftarSoalActivity.class));
                break;
            case R.id.reward:
                startActivity(new Intent(this, RewardPointActivity.class));
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

    private void logOut() {
        auth.signOut();
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_soal:
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference getReference;

                String getSoal = soal_kuis.getText().toString();
                String getOpsiA = opsi_A.getText().toString();
                String getOpsiB = opsi_B.getText().toString();
                String getOpsiC = opsi_C.getText().toString();
                String getOpsiBenar = opsi_benar.getText().toString();
                String getAuthor = auth.getCurrentUser().getDisplayName();

                getReference = database.getReference();

                if(isEmpty(getSoal)){
                    soal_kuis.setError("Inputan tidak boleh kosong");
                }else if(isEmpty(getOpsiA)){
                    opsi_A.setError("Inputan tidak boleh kosong");
                }else  if(isEmpty(getOpsiB)){
                    opsi_B.setError("Inputan tidak boleh kosong");
                }else if(isEmpty(getOpsiC)){
                    opsi_C.setError("Inputan tidak boleh kosong");
                }else if(isEmpty(getOpsiBenar)){
                    opsi_benar.setError("Wajib diisi");
                }else {
                    final ProgressDialog progressDialog = new ProgressDialog(DashboardPembuatSoal.this);
                    progressDialog.setMessage("Soal sedang diinputkan, mohon tungu...");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    getReference.child("Pembuat Soal").child("Soal")
                            .push().setValue(new DataSoal(getSoal, getOpsiA, getOpsiB, getOpsiC, getOpsiBenar, getAuthor))
                            .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    soal_kuis.setText("");
                                    opsi_A.setText("");
                                    opsi_B.setText("");
                                    opsi_C.setText("");
                                    opsi_benar.setText("");
                                    progressDialog.dismiss();
                                    Snackbar.make(findViewById(R.id.submit_soal), "Soal berhasil ditambahkan", Snackbar.LENGTH_SHORT).show();
                                }
                            });
                break;
        }
    }

    }
}
