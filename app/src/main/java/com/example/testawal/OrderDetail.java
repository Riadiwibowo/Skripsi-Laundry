package com.example.testawal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.appwidget.AppWidgetHost;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class OrderDetail extends AppCompatActivity {

    TextView Id, namaLaundry, noTelp, alamat, category, service, price, tanggal, jam;
    DatabaseReference databaseReference, databaseReferenceT;
    StorageReference storageReference;
    FirebaseUser fUser;
    String userId, trId;
    LinearLayout linearPickup, linearReschedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        getSupportActionBar().setTitle("Transaction Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReferenceT = FirebaseDatabase.getInstance().getReference("Transactions");
        storageReference = FirebaseStorage.getInstance().getReference();

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = fUser.getUid();

        Id = findViewById(R.id.Id);
        namaLaundry = findViewById(R.id.namaLaundry);
        noTelp = findViewById(R.id.noTelp);
        alamat = findViewById(R.id.alamat);
        category = findViewById(R.id.category);
        service = findViewById(R.id.service);
        price = findViewById(R.id.price);
        tanggal = findViewById(R.id.tanggal);
        jam = findViewById(R.id.jam);

        Bundle b = getIntent().getExtras();
        String orderid = (String) b.get("orderid");
        String namaLaundry1 = (String) b.get("namaLaundry1");
        String alamatLaundry1 = (String) b.get("alamatLaundry1");

        Id.setText(orderid);

        databaseReferenceT.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("id").getValue().toString().equals(orderid)) {
                        namaLaundry.setText("Nama Laundry: " + dataSnapshot.child("namaLaundry").getValue().toString());
                        category.setText("Category: " + dataSnapshot.child("category").getValue().toString());
                        service.setText("Services: " + dataSnapshot.child("services").getValue().toString());
//                        price.setText(dataSnapshot.child("price").getValue().toString());
//                        tanggal.setText(dataSnapshot.child("tanggal").getValue().toString());
//                        jam.setText(dataSnapshot.child("jam").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("nama").getValue().toString().equals(namaLaundry1)) {
                           noTelp.setText("No. Telp: " + dataSnapshot.child("phone").getValue().toString());
                           if (dataSnapshot.child("address").exists()) {
                               alamat.setText("Alamat: " + dataSnapshot.child("address").getValue().toString());
                           }
                           else {
                               alamat.setText("-");
                           }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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