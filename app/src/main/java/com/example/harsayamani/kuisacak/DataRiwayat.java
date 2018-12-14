package com.example.harsayamani.kuisacak;

public class DataRiwayat {
    private int nomor;
    private int score;
    private int exp;
    private String nama;

    public int getNomor() {
        return nomor;
    }

    public int getScore() {
        return score;
    }

    public int getExp() {
        return exp;
    }

    public String getNama() {
        return nama;
    }

    public DataRiwayat(int nomor, String nama, int score, int exp) {
        this.nomor = nomor;
        this.score = score;
        this.exp = exp;
        this.nama = nama;
    }
}
