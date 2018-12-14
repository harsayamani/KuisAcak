package com.example.harsayamani.kuisacak;

public class DataSoal {
    private String soal;
    private String opsiA;
    private String opsiB;
    private String opsiC;
    private String opsiBenar;
    private String author;



    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSoal() {
        return soal;
    }

    public void setSoal(String soal) {
        this.soal = soal;
    }

    public String getOpsiA() {
        return opsiA;
    }

    public void setOpsiA(String opsiA) {
        this.opsiA = opsiA;
    }

    public String getOpsiB() {
        return opsiB;
    }

    public void setOpsiB(String opsiB) {
        this.opsiB = opsiB;
    }

    public String getOpsiC() {
        return opsiC;
    }

    public void setOpsiC(String opsiC) {
        this.opsiC = opsiC;
    }


    public String getOpsiBenar() {
        return opsiBenar;
    }

    public void setOpsiBenar(String opsiBenar) {
        this.opsiBenar = opsiBenar;
    }

    public DataSoal(){}

    public DataSoal(String soal, String opsiA, String opsiB, String opsiC, String opsiBenar, String author) {
        this.soal = soal;
        this.opsiA = opsiA;
        this.opsiB = opsiB;
        this.opsiC = opsiC;
        this.opsiBenar = opsiBenar;
        this.author = author;
    }
}
