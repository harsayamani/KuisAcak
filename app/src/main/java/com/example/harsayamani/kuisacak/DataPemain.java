package com.example.harsayamani.kuisacak;

public class DataPemain {
    private String namaPemain;
    private int scorePemain;
    private int expPoint;

    public String getNamaPemain() {
        return namaPemain;
    }

    public int getScorePemain() {
        return scorePemain;
    }

    public int getExpPoint() {
        return expPoint;
    }

    public DataPemain(){};

    public DataPemain(String namaPemain, int scorePemain, int expPoint) {
        this.namaPemain = namaPemain;
        this.scorePemain = scorePemain;
        this.expPoint = expPoint;
    }
}
