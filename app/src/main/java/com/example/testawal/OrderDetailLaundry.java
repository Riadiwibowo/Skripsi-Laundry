package com.example.testawal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.appwidget.AppWidgetHost;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.util.Calendar;
import java.util.Locale;

public class OrderDetailLaundry extends AppCompatActivity {

    TextView Id, namaUser, noTelp, category, service, price, tanggalpickup, jampickup, alamatpickup;
    DatabaseReference databaseReference, databaseReferenceT;
    StorageReference storageReference;
    FirebaseUser fUser;
    String userId, trId;
    LinearLayout layoutPickup;
    Button btnCancel, btnAccept, btnPickup;
    Space space, space1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_laundry);

        getSupportActionBar().setTitle("Transaction Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReferenceT = FirebaseDatabase.getInstance().getReference("Transactions");
        storageReference = FirebaseStorage.getInstance().getReference();

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = fUser.getUid();

        Id = findViewById(R.id.Id);

        //laundry properties
        namaUser = findViewById(R.id.namaUser);
        noTelp = findViewById(R.id.noTelp);

        //order properties
        category = findViewById(R.id.category);
        service = findViewById(R.id.service);
        price = findViewById(R.id.price);

        //pickup properties
        tanggalpickup = findViewById(R.id.tanggalPickup);
        jampickup = findViewById(R.id.jamPickup);
        alamatpickup = findViewById(R.id.alamatpickup);

        //button properties
        btnCancel = findViewById(R.id.btnCancel);
        btnAccept = findViewById(R.id.btnAccept);
        btnPickup = findViewById(R.id.btnPickup);
        space = findViewById(R.id.space);
        space1 = findViewById(R.id.space1);

        Bundle b = getIntent().getExtras();
        String orderid = (String) b.get("orderid");
        String namaUser1 = (String) b.get("namaUser1");

        Id.setText(orderid);

        databaseReferenceT.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("id").getValue().toString().equals(orderid)) {
                        namaUser.setText("Nama Laundry: " + dataSnapshot.child("namaUser").getValue().toString());
                        category.setText("Category: " + dataSnapshot.child("category").getValue().toString());
                        service.setText("Services: " + dataSnapshot.child("services").getValue().toString());
                        price.setText("Harga: " + dataSnapshot.child("harga").getValue().toString());
                        tanggalpickup.setText("Tanggal pickup: " + dataSnapshot.child("Tanggal pickup").getValue().toString());
                        jampickup.setText("Jam pickup: " + dataSnapshot.child("Jam pickup").getValue().toString());
                        alamatpickup.setText("Alamat pickup: " + dataSnapshot.child("address").getValue().toString());
                        if (dataSnapshot.child("isPickup").getValue().toString().equals("No")) {
                            layoutPickup.setVisibility(View.GONE);
                            btnPickup.setVisibility(View.GONE);
                            space1.setVisibility(View.GONE);
                            if (dataSnapshot.child("status").getValue().toString().equals("1")) {
                                btnAccept.setText("Done");
                            }
                            if (dataSnapshot.child("status").getValue().toString().equals("2") || dataSnapshot.child("status").getValue().toString().equals("3")) {
                                btnCancel.setVisibility(View.GONE);
                                btnAccept.setVisibility(View.GONE);
                                space.setVisibility(View.GONE);
                            }
                        }
                        else if (dataSnapshot.child("isPickup").getValue().toString().equals("Yes")) {
                            if (dataSnapshot.child("status").getValue().toString().equals("0")) {
                                btnPickup.setVisibility(View.GONE);
                                space1.setVisibility(View.GONE);
                            }
                            if (dataSnapshot.child("status").getValue().toString().equals("1")) {
                                btnAccept.setVisibility(View.GONE);
                                space.setVisibility(View.GONE);
                            }
                            if (dataSnapshot.child("status").getValue().toString().equals("4")) {
                                btnCancel.setVisibility(View.GONE);
                                btnPickup.setVisibility(View.GONE);
                                space.setVisibility(View.GONE);
                                space1.setVisibility(View.GONE);
                                btnAccept.setText("Done");
                            }
                            if (dataSnapshot.child("status").getValue().toString().equals("2") || dataSnapshot.child("status").getValue().toString().equals("3")) {
                                btnCancel.setVisibility(View.GONE);
                                btnAccept.setVisibility(View.GONE);
                                btnPickup.setVisibility(View.GONE);
                                space.setVisibility(View.GONE);
                                space1.setVisibility(View.GONE);
                            }
                        }
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                databaseReferenceT.child(orderid).child("status").setValue("3");
                                startActivity(new Intent(OrderDetailLaundry.this, LaundryTransaction.class));
                            }
                        });
                        btnAccept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (btnAccept.getText().toString().equals("ACCEPT") || btnAccept.getText().toString().equals("Accept")) {
                                    databaseReferenceT.child(orderid).child("status").setValue("1");
                                    startActivity(new Intent(OrderDetailLaundry.this, LaundryTransaction.class));
                                }
                                else if (btnAccept.getText().toString().equals("DONE") || btnAccept.getText().toString().equals("Done")) {
                                    databaseReferenceT.child(orderid).child("status").setValue("2");
                                    startActivity(new Intent(OrderDetailLaundry.this, LaundryTransaction.class));
                                }
                            }
                        });
                        btnPickup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                databaseReferenceT.child(orderid).child("status").setValue("4");
                                startActivity(new Intent(OrderDetailLaundry.this, LaundryTransaction.class));
                            }
                        });
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
                    if (dataSnapshot.child("nama").getValue().toString().equals(namaUser1)) {
                        noTelp.setText("No. Telp: " + dataSnapshot.child("phone").getValue().toString());
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