package com.example.testawal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class LaundryHome extends AppCompatActivity {

    //region properties
    private Button btnAdd, btnShow;
    ProgressBar progressBar;
    private ImageView imageView;
    private DatabaseReference databaseReference;
    //storageReference to store image data
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private Uri imageUrl;
    private FirebaseUser fUser;
    private String userId;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry_home);

        getSupportActionBar().setTitle("Home Page");

//        databaseReference = FirebaseDatabase.getInstance().getReference().child("Image");
        btnAdd = findViewById(R.id.buttonAdd);
        btnShow = findViewById(R.id.buttonShow);
        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.laundryImageAdd);
        progressBar.setVisibility(View.INVISIBLE);

        //1
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myGallery = new Intent();
                myGallery.setAction(Intent.ACTION_GET_CONTENT);
                myGallery.setType("image/*");
                startActivityForResult(myGallery, 2);
            }
        });

        //3
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUrl != null){
                    uploadFirebase(imageUrl);
                }else{
                    Toast.makeText(LaundryHome.this, "Select a photo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //cek current user (untuk add image melalui reference current user)
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = fUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");


        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user != null){
                    String laundryName = user.nama;
                    String role = user.role;
                    Toast.makeText(LaundryHome.this, "Hi " + laundryName + " role " + role, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LaundryHome.this, "error get current user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //region imageUrl
    //2
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2 && resultCode==RESULT_OK && data!=null){
            imageUrl = data.getData();
            imageView.setImageURI(imageUrl);
        }
        Toast.makeText(LaundryHome.this, "foto di home", Toast.LENGTH_SHORT).show();
    }

    //4
    private void uploadFirebase(Uri uri){
        StorageReference filepath = reference.child(System.currentTimeMillis()+"."+getFileExtension(uri));
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user = snapshot.getValue(User.class);
                                if(user != null){
                                    databaseReference.child(userId).child("imageUrl").setValue(uri.toString());
                                    Toast.makeText(LaundryHome.this, "Sukses upload foto", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(LaundryHome.this, "error get current user", Toast.LENGTH_SHORT).show();
                            }
                        });

                        //insert data
//                        UrlLaundryImage uli = new UrlLaundryImage(uri.toString());
//                        String uliId = databaseReference.push().getKey();
//                        Toast.makeText(LaundryHome.this, "ulId = " + uliId, Toast.LENGTH_LONG).show();
//                        databaseReference.child(uliId).setValue(uli);
//                        Toast.makeText(LaundryHome.this, "Sukses upload foto", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(LaundryHome.this, "gagal upload", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime. getExtensionFromMimeType(cr.getType(uri));
    }
    //endregion

    //region menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflates = getMenuInflater();
        inflates.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemProfile:
                logout();
                Toast.makeText(this, "profile laundry clicked", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(LaundryHome.this, MainActivity.class));
    }
}