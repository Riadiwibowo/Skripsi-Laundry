package com.example.testawal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.text.LineBreaker;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ArtikelDua extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    LinearLayout parentLayout;

    TextView txtartikel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artikel_dua);

        getSupportActionBar().setTitle("Artikel");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        parentLayout = findViewById(R.id.parentLayout);
        txtartikel = findViewById(R.id.txtartikel);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        final int black = ContextCompat.getColor(this, R.color.black);
        final int white = ContextCompat.getColor(this, R.color.white);
        final Drawable backgroundeditlaundrydark = ContextCompat.getDrawable(this, R.drawable.backgroundeditlaundrydark);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#083444"));

        if (sharedPreferences.getBoolean("dark_mode", true)) {
            parentLayout.setBackgroundColor(black);
            txtartikel.setBackground(backgroundeditlaundrydark);
            txtartikel.setTextColor(white);
            getSupportActionBar().setBackgroundDrawable(colorDrawable);
        }

        txtartikel.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
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