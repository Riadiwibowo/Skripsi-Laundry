package com.example.testawal;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.text.LineBreaker;
import android.os.Bundle;
import android.widget.TextView;

public class ArtikelEmpat extends AppCompatActivity {

    TextView txtartikel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artikel_empat);

        getSupportActionBar().setTitle("Artikel");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtartikel = findViewById(R.id.txtartikel);

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