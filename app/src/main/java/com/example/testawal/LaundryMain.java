package com.example.testawal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LaundryMain extends AppCompatActivity {

    CardView profileSetting, laundryTransaction;
    TextView laundryName, txtNo, txtYes;
    private DatabaseReference databaseReference;
    //storageReference to store image data
    private FirebaseUser fUser;
    private String userId;
    Dialog dialog;

    //region dark properties
    ConstraintLayout parentLayout, constraintLayout;
    SharedPreferences sharedPreferences;
    ImageView imageProfile, imageTransaksi;
    TextView teksProfile, teksTransaksi;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry_main);

        getSupportActionBar().setTitle("Laundry Home");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        parentLayout = findViewById(R.id.parentLayout);
        constraintLayout = findViewById(R.id.constraintLayout);
        laundryTransaction = findViewById(R.id.laundryTransaction);
        profileSetting = findViewById(R.id.laundryProfileSet);
        teksProfile = findViewById(R.id.teksProfile);
        teksTransaksi = findViewById(R.id.teksTransaksi);
        imageProfile = findViewById(R.id.imageProfile);
        imageTransaksi = findViewById(R.id.imageTransaksi);

        final int black = ContextCompat.getColor(this, R.color.black);
        final int white = ContextCompat.getColor(this, R.color.white);
        final int greyimage = ContextCompat.getColor(this, R.color.greyimage);
        final Drawable backgroundTopDark = ContextCompat.getDrawable(this, R.drawable.belakangmaindark);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#083444"));

        if (sharedPreferences.getBoolean("dark_mode", true)) {
            parentLayout.setBackgroundColor(black);
            constraintLayout.setBackground(backgroundTopDark);
            laundryTransaction.setCardBackgroundColor(greyimage);
            profileSetting.setCardBackgroundColor(greyimage);
            DrawableCompat.setTint(imageProfile.getDrawable(),ContextCompat.getColor(getApplicationContext(), R.color.white));
            DrawableCompat.setTint(imageTransaksi.getDrawable(),ContextCompat.getColor(getApplicationContext(), R.color.white));
            teksProfile.setTextColor(white);
            teksTransaksi.setTextColor(white);
            getSupportActionBar().setBackgroundDrawable(colorDrawable);
        }

        laundryName = (TextView) findViewById(R.id.txtTop);

        //cek current user (untuk add image melalui reference current user)
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = fUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user != null){
                    laundryName.setText("Hello, " + user.nama + "!!");
                    String laundryName = user.nama;
                    String role = user.role;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LaundryMain.this, "error get current user", Toast.LENGTH_SHORT).show();
            }
        });

        profileSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LaundryMain.this, ProfileLaundry.class));
            }
        });

        laundryTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LaundryMain.this, LaundryTransaction.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflates = getMenuInflater();
        inflates.inflate(R.menu.logout_laundry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemLogout:
                dialog = new Dialog(this);
                dialog.setContentView(R.layout.popuplogout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                txtNo = dialog.findViewById(R.id.txtNo);
                txtYes = dialog.findViewById(R.id.txtYes);
                dialog.show();
                txtNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                txtYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        logout();
                    }
                });
                return true;
            case R.id.itemSetting:
                startActivity(new Intent(LaundryMain.this, SettingActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(LaundryMain.this, MainActivity.class));
    }
}