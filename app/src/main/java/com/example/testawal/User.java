package com.example.testawal;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public User(){}

    public User(String nama, String email, String password, String role, String imageUrl) {
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.role = role;
        this.imageUrl = imageUrl;
    }

    public String nama;
    public String email;
    public String password;
    public String role;
    public String imageUrl = "";

    public String getNama() {
        return nama;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() { return role; }

    public String getImageUrl() {
        return imageUrl;
    }

    public String setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return imageUrl;
    }
}
