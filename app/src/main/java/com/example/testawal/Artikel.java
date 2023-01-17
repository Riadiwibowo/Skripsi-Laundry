package com.example.testawal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class Artikel extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    ConstraintLayout parentLayout;

    String s1[];
    int image[] = {R.drawable.artikelsatu, R.drawable.artikeldua, R.drawable.artikeltiga, R.drawable.artikelempat, R.drawable.artikellima};

    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artikel);

        getSupportActionBar().setTitle("Daftar Artikel");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv = findViewById(R.id.recyclerArtikel);
        parentLayout = findViewById(R.id.parentLayout);

        s1 = getResources().getStringArray(R.array.judul_artikel);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        final int black = ContextCompat.getColor(this, R.color.black);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#083444"));

        if (sharedPreferences.getBoolean("dark_mode", true)) {
            parentLayout.setBackgroundColor(black);
            getSupportActionBar().setBackgroundDrawable(colorDrawable);
        }

        MyAdapterArtikel myAdapter = new MyAdapterArtikel(this, s1, image);

        rv.setAdapter(myAdapter);
        rv.setLayoutManager(new LinearLayoutManager(Artikel.this));
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}