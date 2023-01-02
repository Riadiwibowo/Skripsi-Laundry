package com.example.testawal;

public class Transaction {

    public Transaction(){}

    public Transaction(String namaUser, String namaLaundry, String tanggal, String jam, String harga, String imageUrl, String status, String id, String alamat) {
        this.namaUser = namaUser;
        this.namaLaundry = namaLaundry;
        this.tanggal = tanggal;
        this.jam = jam;
        this.harga = harga;
        this.imageUrl = imageUrl;
        this.status = status;
        this.id = id;
        this.alamat = alamat;
    }

    public String namaUser;
    public String namaLaundry;
    public String tanggal;
    public String jam;
    public String harga;
    public String imageUrl = "";
    public String status;
    public String id;
    public String alamat;

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

    public String getHarga() {
        return harga;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public String getAlamat() {
        return alamat;
    }
}