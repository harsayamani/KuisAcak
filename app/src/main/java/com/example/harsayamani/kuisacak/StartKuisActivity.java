package com.example.harsayamani.kuisacak;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class StartKuisActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    TextView tvUser, jumlahSoal;
    Button btnPlay;
    int kelipatan;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start_kuis);
        auth = FirebaseAuth.getInstance();
        tvUser = findViewById(R.id.nama_user);
        jumlahSoal = findViewById(R.id.tv10Soal);
        btnPlay = findViewById(R.id.btn_play);
        btnPlay.setOnClickListener(this);
        onAuthState();
        viewJumlahSoal();
    }

    private void onAuthState() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(StartKuisActivity.this, Login2Activity.class));
                    finish();
                }else {
                    String getUserName = firebaseAuth.getCurrentUser().getDisplayName();
                    Uri resID = firebaseAuth.getCurrentUser().getPhotoUrl();
                    tvUser.setText(getUserName);
                    Picasso.with(getApplicationContext()).load(resID)
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher)
                            .transform(new PicassoCircleTransformation())
                            .into((ImageView)findViewById(R.id.foto_user));
                }
            }
        };
    }

    @SuppressLint("SetTextI18n")
    private void viewJumlahSoal() {
        Intent intent = getIntent();
        kelipatan = intent.getIntExtra("kelipatan", 0);
        jumlahSoal.setText(String.valueOf(kelipatan*10)+" Soal");
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(StartKuisActivity.this, FormKuisActivity.class);
        i.putExtra("kelipatan", kelipatan*10);
        startActivity(i);
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
