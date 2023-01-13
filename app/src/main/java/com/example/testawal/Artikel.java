package com.example.testawal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class Artikel extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    ConstraintLayout parentLayout;

    String s1[];
    int image[] = {R.drawable.artikelsatu, R.drawable.artikeldua, R.drawable.artikeltiga, R.drawable.artikelsatu, R.drawable.artikeldua};

    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artikel);

        getSupportActionBar().setTitle("Artikel");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv = findViewById(R.id.recyclerArtikel);
        parentLayout = findViewById(R.id.parentLayout);

        s1 = getResources().getStringArray(R.array.judul_artikel);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        final Drawable black = ContextCompat.getDrawable(this, R.color.black);
        final Drawable bluex = ContextCompat.getDrawable(this, R.color.bluex);
        final Drawable lightbluex = ContextCompat.getDrawable(this, R.color.lightbluex);
        final Drawable backgroundmaindark = ContextCompat.getDrawable(this, R.drawable.belakangmaindark);

        if (sharedPreferences.getBoolean("dark_mode", true)) {
            parentLayout.setBackground(black);
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