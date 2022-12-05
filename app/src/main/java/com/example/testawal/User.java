package com.example.testawal;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public User(){}

    public User(String nama, String email, String password, String role, String phone, String description, String imageUrl, String services, String category) {
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.description = description;
        this.imageUrl = imageUrl;
        this.services = services;
        this.category = category;
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

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
