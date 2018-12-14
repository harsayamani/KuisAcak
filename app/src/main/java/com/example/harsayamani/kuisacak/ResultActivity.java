package com.example.harsayamani.kuisacak;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvScore, tvExpPoint;
    ImageView imageView1, imageView2, imageView3;
    Button btnScoreBoard, btnShare;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    FirebaseDatabase database;
    DatabaseReference getReference;
    String getUserName;
    RelativeLayout viewResult;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_result);
        imageView1 = findViewById(R.id.bintang1);
        imageView2 = findViewById(R.id.bintang2);
        imageView3 = findViewById(R.id.bintang3);
        tvScore = findViewById(R.id.tv_score_akhir);
        tvExpPoint = findViewById(R.id.tv_exp);
        viewResult = findViewById(R.id.view_result);
        btnShare = findViewById(R.id.btn_share);
        btnShare.setOnClickListener(this);
        btnScoreBoard = findViewById(R.id.btn_lihat_peringkat);
        btnScoreBoard.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        getReference = database.getReference();

        onAuthState();
        setExpPoint();
        setScore();
        getStar();
        getDataPemain();

    }

    private void onAuthState() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(ResultActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
    }

    @SuppressLint("SetTextI18n")
    private void setExpPoint() {
        tvExpPoint.setText("Exp. Point Anda : "+getExpoint());
    }

    @SuppressLint("SetTextI18n")
    private void setScore() {
        tvScore.setText("Score Anda : "+getScore());
    }

    private void getStar() {
        if (getScore()>=250 && getScore()<=332){
            imageView1.setImageResource(R.drawable.icon_favorite_setengah);
            imageView2.setImageResource(R.drawable.icon_favorite_kosong);
            imageView3.setImageResource(R.drawable.icon_favorite_kosong);
        }else if (getScore()>=333 && getScore()<=499){
            imageView1.setImageResource(R.drawable.icon_favorite_full);
            imageView2.setImageResource(R.drawable.icon_favorite_kosong);
            imageView3.setImageResource(R.drawable.icon_favorite_kosong);
        }else if (getScore()>=500 && getScore()<=665){
            imageView1.setImageResource(R.drawable.icon_favorite_full);
            imageView2.setImageResource(R.drawable.icon_favorite_setengah);
            imageView3.setImageResource(R.drawable.icon_favorite_kosong);
        }else if (getScore()>666 && getScore()<=749){
            imageView1.setImageResource(R.drawable.icon_favorite_full);
            imageView2.setImageResource(R.drawable.icon_favorite_full);
            imageView3.setImageResource(R.drawable.icon_favorite_kosong);
        }else if (getScore()>750 && getScore()<=998){
            imageView1.setImageResource(R.drawable.icon_favorite_full);
            imageView2.setImageResource(R.drawable.icon_favorite_full);
            imageView3.setImageResource(R.drawable.icon_favorite_setengah);
        }else if (getScore()>=999){
            imageView1.setImageResource(R.drawable.icon_favorite_full);
            imageView2.setImageResource(R.drawable.icon_favorite_full);
            imageView3.setImageResource(R.drawable.icon_favorite_full);
        }else{
            imageView1.setImageResource(R.drawable.icon_favorite_kosong);
            imageView2.setImageResource(R.drawable.icon_favorite_kosong);
            imageView3.setImageResource(R.drawable.icon_favorite_kosong);
        }
    }

    private int getExpoint(){
        int exp = 0;
        exp = exp +100;
        return exp;
    }

    private String getDisplayName(){
        getUserName = auth.getCurrentUser().getDisplayName();
        return getUserName;
    }

    private int getScore(){
        Intent intent = getIntent();
        return intent.getIntExtra("score", 0);
    }

    private void getDataPemain(){ ;
        getReference.child("Pemain")
                .push().setValue(new DataPemain(getDisplayName(), getScore(), getExpoint()))
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ResultActivity.this, DashboardPemain.class));
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
            case R.id.btn_lihat_peringkat:
                startActivity(new Intent(ResultActivity.this, LeaderBoardActivity.class));
                break;
            case R.id.btn_share:
                viewResult.setDrawingCacheEnabled(true);
                viewResult.buildDrawingCache();
                Bitmap bitmap = viewResult.getDrawingCache();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/png");
                intent.putExtra(Intent.EXTRA_STREAM, saveImage(bitmap));
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(intent,"Bagikan dengan"));
                break;
        }
    }

    private Uri saveImage(Bitmap image) {
        //TODO - Should be processed in another thread
        File imagesFolder = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            imagesFolder.mkdirs();
            File file = new File(imagesFolder, "shared_image.png");

            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(this, "com.mydomain.fileprovider", file);

        } catch (IOException e) {
            Log.d("Error", "IOException while trying to write file for sharing: " + e.getMessage());
        }
        return uri;
    }
}
