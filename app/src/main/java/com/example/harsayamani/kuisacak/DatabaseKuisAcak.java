package com.example.harsayamani.kuisacak;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseKuisAcak extends SQLiteOpenHelper {
    DatabaseKuisAcak(@Nullable Context context) {
        super(context, "leaderboard.tb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String leaderBoard = "create table tb_leaderboard(id integer primary key autoincrement, " +
                "namaPemain text, " +
                "score integer)";
        db.execSQL(leaderBoard);

        String riwayat = "create table tb_riwayat(id integer primary key autoincrement, " +
                "nama text, " +
                "score integer, " +
                "exp integer)";
        db.execSQL(riwayat);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tb_leaderboard ");
        db.execSQL("DROP TABLE IF EXISTS tb_riwayat");
        onCreate(db);
    }


    public void insertDataRiwayat(String nama, int score, int exp){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nama", nama);
        contentValues.put("score", score);
        contentValues.put("exp", exp);
        db.insert("tb_riwayat", "id", contentValues);
    }

    void insertData(String nama, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("namaPemain", nama);
        contentValues.put("score", score);
        db.insert("tb_leaderboard","id", contentValues);
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from tb_leaderboard");
        db.close();
    }

    public void deleteAllRiwayat(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from tb_riwayat");
        db.close();
    }
}
