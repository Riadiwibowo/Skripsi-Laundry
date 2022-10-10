package com.example.testawal;

public class Laundry {

    String namaLaundry, deskripsiLaundry, password;

    public Laundry() {
    }

    public Laundry(String namaLaundry, String password, String deskripsiLaundry) {
        this.namaLaundry = namaLaundry;
        this.password = password;
        this.deskripsiLaundry = deskripsiLaundry;
    }

    public String getPassword() {
        return password;
    }

    public String getNamaLaundry() {
        return namaLaundry;
    }

    public String getDeskripsiLaundry() {
        return deskripsiLaundry;
    }
}
