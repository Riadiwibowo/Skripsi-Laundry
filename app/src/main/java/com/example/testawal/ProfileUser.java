package com.example.testawal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileUser extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    TextView textView, Nama, Email, Telp;
    private DatabaseReference databaseReference;
    private FirebaseUser fUser;
    private String userId;
    Button btnEdit;

    ConstraintLayout parentLayout, constraintNama, constraintEmail, constraintPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        getSupportActionBar().setTitle("Profil User");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView = findViewById(R.id.textView);
        Nama = (TextView) findViewById(R.id.Nama);
        Email = (TextView) findViewById(R.id.Email);
        Telp = (TextView) findViewById(R.id.Phone);
        btnEdit = findViewById(R.id.btnEdit);
        parentLayout = findViewById(R.id.parentLayout);
        constraintNama = findViewById(R.id.constraintNama);
        constraintEmail = findViewById(R.id.constraintEmail);
        constraintPhone = findViewById(R.id.constraintPhone);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = fUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        final int black = ContextCompat.getColor(this, R.color.black);
        final int white = ContextCompat.getColor(this, R.color.white);
        final int lightbluex = ContextCompat.getColor(this, R.color.lightbluex);
        final Drawable constraintprofile = ContextCompat.getDrawable(this, R.drawable.belakangdetailuserdark);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#083444"));

        if (sharedPreferences.getBoolean("dark_mode", true)) {
            parentLayout.setBackgroundColor(black);
            constraintNama.setBackground(constraintprofile);
            constraintEmail.setBackground(constraintprofile);
            constraintPhone.setBackground(constraintprofile);
            btnEdit.setTextColor(white);
            btnEdit.setBackgroundColor(lightbluex);
            textView.setTextColor(white);
            getSupportActionBar().setBackgroundDrawable(colorDrawable);
        }

        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user != null){
                    Nama.setText(user.nama);
                    Email.setText(user.email);
                    Telp.setText(user.phone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileUser.this, "error get current user", Toast.LENGTH_SHORT).show();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileUser.this, EditUser.class));
            }
        });
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