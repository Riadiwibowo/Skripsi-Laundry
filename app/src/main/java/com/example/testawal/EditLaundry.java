package com.example.testawal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
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

public class EditLaundry extends AppCompatActivity {

    //region properties
    private Button btnAdd;
    EditText txtDesc, txtName, txtPhone;
    CheckBox services1, services2, services3, services4, category1, category2, category3;
    ProgressBar progressBar;
    private ImageView imageView, imageProfile;
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
        setContentView(R.layout.activity_edit_laundry);

        getSupportActionBar().setTitle("Laundry Profile Setting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        databaseReference = FirebaseDatabase.getInstance().getReference().child("Image");
        btnAdd = findViewById(R.id.buttonAdd);
        progressBar = findViewById(R.id.progressBar);
        imageProfile = findViewById(R.id.laundryProfileImage);
        imageView = findViewById(R.id.laundryImageAdd);
        progressBar.setVisibility(View.INVISIBLE);
        txtDesc = findViewById(R.id.txtLaunDescription);
        txtName = findViewById(R.id.txtLaunName);
        txtPhone = findViewById(R.id.txtLaunPhone);
        services1 = findViewById(R.id.services1);
        services2 = findViewById(R.id.services2);
        services3 = findViewById(R.id.services3);
        services4 = findViewById(R.id.services4);
        category1 = findViewById(R.id.category1);
        category2 = findViewById(R.id.category2);
        category3 = findViewById(R.id.category3);

        //cek current user (untuk add image melalui reference current user)
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = fUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

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
                if(!txtPhone.getText().toString().trim().isEmpty() && (txtName.getText().toString().trim().equals("") || txtName.getText().toString().trim().isEmpty())
                        && (txtDesc.getText().toString().trim().equals("") || txtDesc.getText().toString().trim().isEmpty())) {
                    insertPhone();
                }
                else if(!txtName.getText().toString().trim().isEmpty() && (txtPhone.getText().toString().trim().equals("") || txtPhone.getText().toString().trim().isEmpty())
                        && (txtDesc.getText().toString().trim().equals("") || txtDesc.getText().toString().trim().isEmpty())) {
                    insertName();
                }
                else if(!txtDesc.getText().toString().trim().isEmpty() && (txtPhone.getText().toString().trim().equals("") || txtPhone.getText().toString().trim().isEmpty())
                        && (txtName.getText().toString().trim().equals("") || txtName.getText().toString().trim().isEmpty())) {
                    insertDescription();
                }
                else if(!txtDesc.getText().toString().trim().isEmpty() && !txtPhone.getText().toString().trim().isEmpty()
                        && (txtName.getText().toString().trim().equals("") || txtName.getText().toString().trim().isEmpty())) {
                    insertPhone();
                    insertDescription();
                }
                else if(!txtDesc.getText().toString().trim().isEmpty() && !txtName.getText().toString().trim().isEmpty()
                        && (txtPhone.getText().toString().trim().equals("") || txtPhone.getText().toString().trim().isEmpty())) {
                    insertName();
                    insertDescription();
                }
                else if(!txtPhone.getText().toString().trim().isEmpty() && !txtName.getText().toString().trim().isEmpty()
                        && (txtDesc.getText().toString().trim().equals("") || txtDesc.getText().toString().trim().isEmpty())) {
                    insertName();
                    insertPhone();
                }
                else if(!txtName.getText().toString().trim().isEmpty() && !txtPhone.getText().toString().trim().isEmpty() && !txtDesc.getText().toString().trim().isEmpty()) {
                    insertName();
                    insertPhone();
                    insertDescription();
                }
                else{
                    Toast.makeText(EditLaundry.this, "Salah satu harus diisi", Toast.LENGTH_SHORT).show();
                }

                if(services1.isChecked() || services2.isChecked() || services3.isChecked() || services4.isChecked()) {
//                    if(services1.isChecked()){
//                        databaseReference.child(userId).child("services").setValue("Kilat");
//                    }
//                    else if(services2.isChecked()) {
//                        databaseReference.child(userId).child("services").setValue("Pickup");
//                    }
                    if(services3.isChecked()) {
                        databaseReference.child(userId).child("services").setValue("Satuan");
                    }
                    else if(services4.isChecked()) {
                        databaseReference.child(userId).child("services").setValue("Kiloan");
                    }
                    if(services3.isChecked() && services4.isChecked()) {
                        databaseReference.child(userId).child("services").setValue("Satuan; Kiloan");
                    }
                    if(services1.isChecked() && services3.isChecked()){
                        databaseReference.child(userId).child("services").setValue("Kilat; Satuan");
                    }
                    if(services1.isChecked() && services4.isChecked()){
                        databaseReference.child(userId).child("services").setValue("Kilat; Kiloan");
                    }
                    if(services1.isChecked() && services3.isChecked() && services4.isChecked()){
                        databaseReference.child(userId).child("services").setValue("Kilat; Satuan; Kiloan");
                    }
                    if(services2.isChecked() && services3.isChecked()) {
                        databaseReference.child(userId).child("services").setValue("Pickup; Satuan");
                    }
                    if(services2.isChecked() && services4.isChecked()) {
                        databaseReference.child(userId).child("services").setValue("Pickup; Kiloan");
                    }
                    if(services2.isChecked() && services3.isChecked() && services4.isChecked()) {
                        databaseReference.child(userId).child("services").setValue("Pickup; Satuan; Kiloan");
                    }
                    if(services1.isChecked() && services2.isChecked() && services3.isChecked()) {
                        databaseReference.child(userId).child("services").setValue("Kilat; Pickup; Satuan");
                    }
                    if(services1.isChecked() && services2.isChecked() && services4.isChecked()) {
                        databaseReference.child(userId).child("services").setValue("Kilat; Pickup; Kiloan");
                    }
                    if(services1.isChecked() && services2.isChecked() && services3.isChecked() && services4.isChecked()) {
                        databaseReference.child(userId).child("services").setValue("Kilat; Pickup; Satuan; Kiloan");
                    }

//                    if(services1.isChecked() && services4.isChecked()) {
//                        databaseReference.child(userId).child("services").setValue("Kilat; Kiloan");
//                    }
//                    if(services2.isChecked() && services3.isChecked()) {
//                        databaseReference.child(userId).child("services").setValue("Pickup; Satuan");
//                    }
//                    if(services2.isChecked() && services4.isChecked()) {
//                        databaseReference.child(userId).child("services").setValue("Pickup; Kiloan");
//                    }
//                    if(services3.isChecked() && services4.isChecked()) {
//                        databaseReference.child(userId).child("services").setValue("Satuan; Kiloan");
//                    }
//                    if(services1.isChecked() && services2.isChecked() && services3.isChecked()) {
//                        databaseReference.child(userId).child("services").setValue("Kilat; Pickup; Satuan");
//                    }
//                    if(services1.isChecked() && services2.isChecked() && services4.isChecked()) {
//                        databaseReference.child(userId).child("services").setValue("Kilat; Pickup; Kiloan");
//                    }
//                    if(services1.isChecked() && services3.isChecked() && services4.isChecked()) {
//                        databaseReference.child(userId).child("services").setValue("Kilat; Satuan; Kiloan");
//                    }
//                    if(services2.isChecked() && services3.isChecked() && services4.isChecked()) {
//                        databaseReference.child(userId).child("services").setValue("Pickup; Satuan; Kiloan");
//                    }
                }
                else {
                    Toast.makeText(EditLaundry.this, "Minimal memilih antara satuan atau kiloan", Toast.LENGTH_SHORT).show();
                }

                if(category1.isChecked() || category2.isChecked() || category3.isChecked()) {
                    if(category1.isChecked()) {
                        databaseReference.child(userId).child("category").setValue("Baju");
                    }
                    else if(category2.isChecked()) {
                        databaseReference.child(userId).child("category").setValue("Sepatu");
                    }
                    else if(category3.isChecked()) {
                        databaseReference.child(userId).child("category").setValue("Lain-lain");
                    }
                    if(category1.isChecked() && category2.isChecked()) {
                        databaseReference.child(userId).child("category").setValue("Baju; Sepatu");
                    }
                    else if(category1.isChecked() && category3.isChecked()) {
                        databaseReference.child(userId).child("category").setValue("Baju; Lain-lain");
                    }
                    else if(category2.isChecked() && category3.isChecked()) {
                        databaseReference.child(userId).child("category").setValue("Sepatu; Lain-lain");
                    }
                    if(category1.isChecked() && category2.isChecked() && category3.isChecked()) {
                        databaseReference.child(userId).child("category").setValue("Baju; Sepatu; Lain-lain");
                    }
                }
            }
        });

        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user != null){
                    String laundryName = user.nama;
                    String role = user.role;
                    String images = user.imageUrl;
                    Toast.makeText(EditLaundry.this, "Hi " + laundryName + " role " + role, Toast.LENGTH_SHORT).show();
                    if(user.imageUrl!=null && !user.imageUrl.equals("")){
                        Glide.with(EditLaundry.this).load(user.getImageUrl()).into(imageProfile);
                    }else if (user.imageUrl.equals("")){
                        String uri = "@drawable/ic_profile_icon";
                        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                        Drawable res = getResources().getDrawable(imageResource);
                        imageProfile.setImageDrawable(res);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditLaundry.this, "error get current user", Toast.LENGTH_SHORT).show();
            }
        });

        //4
        /*btnDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertDescription();
            }
        });*/
    }

    //region imageUrl
    //2
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2 && resultCode==RESULT_OK && data!=null){
            imageUrl = data.getData();
            imageProfile.setImageURI(imageUrl);
        }
        Toast.makeText(EditLaundry.this, "foto di home", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(EditLaundry.this, "Sukses upload foto", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(EditLaundry.this, "error get current user", Toast.LENGTH_SHORT).show();
                            }
                        });
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
                Toast.makeText(EditLaundry.this, "gagal upload", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime. getExtensionFromMimeType(cr.getType(uri));
    }
    //endregion

    private void insertDescription() {
        String description = txtDesc.getText().toString().trim();

        //region validasi input
//        if(description.isEmpty()){
//            txtDesc.setError("Deskripsi harus diisi");
//            txtDesc.requestFocus();
//            return;
//        }
        //endregion

        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child(userId).child("description").setValue(description.toString());
                Toast.makeText(EditLaundry.this, "Description Inserted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditLaundry.this, "error get current user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void insertName() {
        String name = txtName.getText().toString().trim();

        //region validasi input
//        if(name.isEmpty()){
//            txtName.setError("Nama harus diisi");
//            txtName.requestFocus();
//            return;
//        }
        //endregion

        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child(userId).child("nama").setValue(name.toString());
                Toast.makeText(EditLaundry.this, "Name Inserted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditLaundry.this, "error get current user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void insertPhone() {
        String phone = txtPhone.getText().toString().trim();

        //region validasi input
//        if(phone.isEmpty()){
//            txtPhone.setError("No. Telp harus diisi");
//            txtPhone.requestFocus();
//            return;
//        }
        //endregion

        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child(userId).child("phone").setValue(phone.toString());
                Toast.makeText(EditLaundry.this, "Phone Inserted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditLaundry.this, "error get current user", Toast.LENGTH_SHORT).show();
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