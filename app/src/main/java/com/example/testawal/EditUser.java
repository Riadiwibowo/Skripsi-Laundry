package com.example.testawal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditUser extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    ConstraintLayout parentLayout;

    //region properties
    private Button btnAdd;
    EditText txtName, txtPhone;
    private DatabaseReference databaseReference;
    //storageReference to store image data
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private FirebaseUser fUser;
    private String userId;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        getSupportActionBar().setTitle("Edit Profil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtName = findViewById(R.id.txtUserNama);
        txtPhone = findViewById(R.id.txtUserPhone);
        btnAdd = findViewById(R.id.buttonAdd);
        parentLayout = findViewById(R.id.parentLayout);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        final int black = ContextCompat.getColor(this, R.color.black);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#083444"));

        if (sharedPreferences.getBoolean("dark_mode", true)) {
            parentLayout.setBackgroundColor(black);
            getSupportActionBar().setBackgroundDrawable(colorDrawable);
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txtPhone.getText().toString().trim().isEmpty() && (txtName.getText().toString().trim().equals("") || txtName.getText().toString().trim().isEmpty())) {
                    insertPhone();
//                    startActivity(new Intent(EditUser.this, ProfileUser.class));
                }
                else if(!txtName.getText().toString().trim().isEmpty() && (txtPhone.getText().toString().trim().equals("") || txtPhone.getText().toString().trim().isEmpty())) {
                    insertName();
                    startActivity(new Intent(EditUser.this, ProfileUser.class));
                }
                else if(!txtName.getText().toString().trim().isEmpty() && !txtPhone.getText().toString().trim().isEmpty()) {
                    insertName();
                    insertPhone();
                    startActivity(new Intent(EditUser.this, ProfileUser.class));
                }
                else{
                    Toast.makeText(EditUser.this, "Tidak ada perubahan data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //cek current user (untuk add image melalui reference current user)
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = fUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
    }


    private void insertName() {
        String name = txtName.getText().toString().trim();
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child(userId).child("nama").setValue(name.toString());
                Toast.makeText(EditUser.this, "Berhasil ubah nama", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditUser.this, "error get current user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void insertPhone() {
        String phone = txtPhone.getText().toString().trim();
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child(userId).child("phone").setValue(phone.toString());
                Toast.makeText(EditUser.this, "Berhasil ubah nomor telepon", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditUser.this, "error get current user", Toast.LENGTH_SHORT).show();
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