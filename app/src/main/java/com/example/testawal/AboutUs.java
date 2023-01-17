package com.example.testawal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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

public class AboutUs extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    TextView namaAplikasi;
    LinearLayout parentLayoutAboutUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        getSupportActionBar().setTitle("About Us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        namaAplikasi = findViewById(R.id.namaAplikasi);

        namaAplikasi.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        parentLayoutAboutUs = findViewById(R.id.parentLayoutAboutUs);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        final int bluex = ContextCompat.getColor(this, R.color.bluex);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#083444"));

        if (sharedPreferences.getBoolean("dark_mode", true)) {
            parentLayoutAboutUs.setBackgroundColor(bluex);
            getSupportActionBar().setBackgroundDrawable(colorDrawable);
        }
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
