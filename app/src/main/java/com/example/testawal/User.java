package com.example.testawal;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public User(){}

    public User(String nama, String email, String password, String role, String phone, String description, String imageUrl, String services, String category, String address,
                String kiloan, String satuan, String sepatu, String pickup) {
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.description = description;
        this.imageUrl = imageUrl;
        this.services = services;
        this.category = category;
        this.address = address;
        this.kiloan = kiloan;
        this.satuan = satuan;
        this.pickup = pickup;
        this.sepatu = sepatu;
    }

    public String nama;
    public String email;
    public String password;
    public String role;
    public String phone;
    public String description= "";
    public String imageUrl= "";
    public String services;
    public String category;
    public String address;
    public String kiloan;
    public String satuan;
    public String sepatu;
    public String pickup;

    public String getNama() {
        return nama;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getPhone() {
        return phone;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getServices() {
        return services;
    }

    public String getCategory() {
        return category;
    }

    public String getAddress() {
        return address;
    }

    public String getKiloan() {
        return kiloan;
    }

    public String getSatuan() {
        return satuan;
    }

    public String getSepatu() {
        return sepatu;
    }

    public String getPickup() {
        return pickup;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
