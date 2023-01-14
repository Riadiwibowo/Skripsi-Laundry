package com.example.testawal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class ProfileLaundry extends AppCompatActivity {

    TextView Nama, Telp, Desc;
    ImageView imageLaundry;
    private DatabaseReference databaseReference;
    private FirebaseUser fUser;
    private String userId;
    Button btnEdit;

    //region dark properties
    SharedPreferences sharedPreferences;
    ConstraintLayout parentLayout, constraintDesc, constraintNama, constraintPhone;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_laundry);

        getSupportActionBar().setTitle("Laundry Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageLaundry = findViewById(R.id.imageLaundry);
        Nama = (TextView) findViewById(R.id.Nama);
        Telp = (TextView) findViewById(R.id.Telp);
        Desc = (TextView) findViewById(R.id.Desc);
        btnEdit = findViewById(R.id.btnEdit);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = fUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        parentLayout = findViewById(R.id.parentLayout);
        constraintDesc = findViewById(R.id.constraintDesc);
        constraintNama = findViewById(R.id.constraintNama);
        constraintPhone = findViewById(R.id.constraintPhone);

        final int black = ContextCompat.getColor(this, R.color.black);
        final int white = ContextCompat.getColor(this, R.color.white);
        final int lightbluex = ContextCompat.getColor(this, R.color.lightbluex);
        final Drawable constraintprofile = ContextCompat.getDrawable(this, R.drawable.belakangdetailuserdark);

        if (sharedPreferences.getBoolean("dark_mode", true)) {
            parentLayout.setBackgroundColor(black);
            constraintNama.setBackground(constraintprofile);
            constraintDesc.setBackground(constraintprofile);
            constraintPhone.setBackground(constraintprofile);
            constraintPhone.setMinHeight(270);
            constraintNama.setMinHeight(270);
            btnEdit.setTextColor(white);
            btnEdit.setBackgroundColor(lightbluex);
        }

        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user != null){
                    if(user.imageUrl!=null && !user.imageUrl.equals("")){
                        Glide.with(ProfileLaundry.this).load(user.getImageUrl()).into(imageLaundry);
                    }else if (user.imageUrl.equals("")) {
                        String uri = "@drawable/ic_profile_icon";
                        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                        Drawable res = getResources().getDrawable(imageResource);
                        imageLaundry.setImageDrawable(res);
                    }
                    Nama.setText(user.nama);
                    Telp.setText(user.phone);
                    Desc.setText(user.description);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileLaundry.this, "error get current user", Toast.LENGTH_SHORT).show();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileLaundry.this, EditLaundry.class));
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