package com.example.testawal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class Artikel extends AppCompatActivity {

    String s1[];
    int image[] = {R.drawable.artikelsatu, R.drawable.artikeldua, R.drawable.artikeltiga, R.drawable.artikelsatu, R.drawable.artikeldua};

    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artikel);

        rv= findViewById(R.id.recyclerArtikel);

        s1 = getResources().getStringArray(R.array.judul_artikel);

        MyAdapterArtikel myAdapter = new MyAdapterArtikel(this, s1, image);

        rv.setAdapter(myAdapter);
        rv.setLayoutManager(new LinearLayoutManager(Artikel.this));
    }
}