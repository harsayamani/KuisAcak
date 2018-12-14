package com.example.harsayamani.kuisacak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button loginPemain;
    Button loginPembuatSoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        loginPemain = findViewById(R.id.btn_pemain);
        loginPemain.setOnClickListener(this);
        loginPembuatSoal = findViewById(R.id.btn_pembuat_soal);
        loginPembuatSoal.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_pemain:
                startActivity(new Intent(MainActivity.this, Login2Activity.class));
                break;
            case R.id.btn_pembuat_soal:
                startActivity(new Intent(MainActivity.this, MaterialSlide2.class));
                break;
        }
    }
}
