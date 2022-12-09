package com.example.testawal;

public class Transaction {

    public Transaction(){}

    public Transaction(String namaUser, String namaLaundry, String tanggal, String jam) {
        this.namaUser = namaUser;
        this.namaLaundry = namaLaundry;
        this.tanggal = tanggal;
        this.jam = jam;
    }

    public String namaUser;
    public String namaLaundry;
    public String tanggal;
    public String jam;

    public String getNamaUser() {
        return namaUser;
    }

    public String getNamaLaundry() {
        return namaLaundry;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getJam() {
        return jam;
    }
}
