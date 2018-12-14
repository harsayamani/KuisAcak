package com.example.harsayamani.kuisacak;

public class DataLeaderboard {
    private int nomor;
    private String nama;
    private int score;

    public int getNomor() {
        return nomor;
    }

    public String getNama() {
        return nama;
    }

    public int getScore() {
        return score;
    }

    public DataLeaderboard(int nomor, String nama, int score) {
        this.nomor = nomor;
        this.nama = nama;
        this.score = score;
    }
}
