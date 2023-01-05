package com.example.testawal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.appwidget.AppWidgetHost;
import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Spinner;
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

public class OrderDetail extends AppCompatActivity {

    TextView Id, namaLaundry, noTelp, alamat, category, service, price, tanggalpickup, jampickup, alamatpickup, editTanggal, editJam;
    private int jam, menit;
    private int tanggal, bulan, tahun;
    DatabaseReference databaseReference, databaseReferenceT;
    StorageReference storageReference;
    FirebaseUser fUser;
    String userId, trId;
    Spinner rescheduleSpinner;
    LinearLayout layoutPickup, layoutReschedule;
    Button btnCancel, btnSave, btnAccept;
    Space space;

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

        //laundry properties
        namaLaundry = findViewById(R.id.namaLaundry);
        noTelp = findViewById(R.id.noTelp);
        alamat = findViewById(R.id.alamat);

        //order properties
        category = findViewById(R.id.category);
        service = findViewById(R.id.service);
        price = findViewById(R.id.price);

        //pickup properties
        tanggalpickup = findViewById(R.id.tanggalPickup);
        jampickup = findViewById(R.id.jamPickup);
        alamatpickup = findViewById(R.id.alamatpickup);

        //reschedule properties
        rescheduleSpinner = findViewById(R.id.rescheduleSpinner);
        String[] items = new String[]{"Yes", "No"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        rescheduleSpinner.setAdapter(adapter);
        editTanggal = findViewById(R.id.editTanggal);
        editJam = findViewById(R.id.editJam);
        layoutPickup = findViewById(R.id.layoutPickup);
        layoutReschedule = findViewById(R.id.layoutReschedule);

        //button properties
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);
        space = findViewById(R.id.space);

        Bundle b = getIntent().getExtras();
        String orderid = (String) b.get("orderid");
        String namaLaundry1 = (String) b.get("namaLaundry1");

        Id.setText(orderid);

        databaseReferenceT.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("id").getValue().toString().equals(orderid)) {
                        namaLaundry.setText("Nama Laundry: " + dataSnapshot.child("namaLaundry").getValue().toString());
                        category.setText("Category: " + dataSnapshot.child("category").getValue().toString());
                        service.setText("Services: " + dataSnapshot.child("services").getValue().toString());
                        price.setText("Harga: " + dataSnapshot.child("harga").getValue().toString());
                        if (dataSnapshot.child("Tanggal pickup").exists()){
                            tanggalpickup.setText("Tanggal pickup: " + dataSnapshot.child("Tanggal pickup").getValue().toString());
                        }
                        if (dataSnapshot.child("Jam pickup").exists()){
                            jampickup.setText("Jam pickup: " + dataSnapshot.child("Jam pickup").getValue().toString());
                        }
                        if (dataSnapshot.child("address").exists()){
                            alamatpickup.setText("Alamat pickup: " + dataSnapshot.child("address").getValue().toString());
                        }
                        if (dataSnapshot.child("isPickup").getValue().toString().equals("No")) {
                            layoutPickup.setVisibility(View.GONE);
                            layoutReschedule.setVisibility(View.GONE);
                            btnSave.setVisibility(View.GONE);
                            space.setVisibility(View.GONE);
                            if (!dataSnapshot.child("status").getValue().toString().equals("0")) {
                                btnCancel.setVisibility(View.GONE);
                            }
                        }

                        //4=on pickup 2=done
                        else if (dataSnapshot.child("isPickup").getValue().toString().equals("Yes")) {
                            rescheduleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    if (rescheduleSpinner.getItemAtPosition(i).toString().equals("Yes")){
                                        layoutReschedule.setVisibility(View.VISIBLE);
                                        if (dataSnapshot.child("status").getValue().toString().equals("0")) {
                                            btnCancel.setVisibility(View.VISIBLE);
                                            btnSave.setVisibility(View.VISIBLE);
                                            space.setVisibility(View.VISIBLE);
                                        }
                                        if (dataSnapshot.child("status").getValue().toString().equals("1")) {
                                            btnCancel.setVisibility(View.GONE);
                                            space.setVisibility(View.GONE);
                                        }
                                        if (dataSnapshot.child("status").getValue().toString().equals("2") || dataSnapshot.child("status").getValue().toString().equals("3") || dataSnapshot.child("status").getValue().toString().equals("4")) {
                                            layoutReschedule.setVisibility(View.GONE);
                                            btnCancel.setVisibility(View.GONE);
                                            btnSave.setVisibility(View.GONE);
                                            space.setVisibility(View.GONE);
                                        }
                                    }
                                    else{
                                        layoutReschedule.setVisibility(View.GONE);
                                        btnSave.setVisibility(View.GONE);
                                        space.setVisibility(View.GONE);
                                        if (dataSnapshot.child("status").getValue().toString().equals("0")) {
                                            btnCancel.setVisibility(View.VISIBLE);
                                        }
                                        if (dataSnapshot.child("status").getValue().toString().equals("1") || dataSnapshot.child("status").getValue().toString().equals("2") || dataSnapshot.child("status").getValue().toString().equals("3") || dataSnapshot.child("status").getValue().toString().equals("4")) {
                                            btnCancel.setVisibility(View.GONE);
                                        }
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
//                            if (rescheduleSpinner.getSelectedItem().toString().equals("No")) {
//                                layoutReschedule.setVisibility(View.GONE);
//                                btnSave.setVisibility(View.GONE);
//                                space.setVisibility(View.GONE);
//                                if (dataSnapshot.child("status").getValue().toString().equals("0")) {
//                                    btnCancel.setVisibility(View.VISIBLE);
//                                }
//                                if (dataSnapshot.child("status").getValue().toString().equals("1") || dataSnapshot.child("status").getValue().toString().equals("2") || dataSnapshot.child("status").getValue().toString().equals("3") || dataSnapshot.child("status").getValue().toString().equals("4")) {
//                                    btnCancel.setVisibility(View.GONE);
//                                }
//                            }
//                            else if (rescheduleSpinner.getSelectedItem().toString().equals("Yes")) {
//                                layoutReschedule.setVisibility(View.VISIBLE);
//                                if (dataSnapshot.child("status").getValue().toString().equals("0")) {
//                                    btnCancel.setVisibility(View.VISIBLE);
//                                    btnSave.setVisibility(View.VISIBLE);
//                                }
//                                if (dataSnapshot.child("status").getValue().toString().equals("1")) {
//                                    btnCancel.setVisibility(View.GONE);
//                                    space.setVisibility(View.GONE);
//                                }
//                                if (dataSnapshot.child("status").getValue().toString().equals("2") || dataSnapshot.child("status").getValue().toString().equals("3") || dataSnapshot.child("status").getValue().toString().equals("4")) {
//                                    layoutReschedule.setVisibility(View.GONE);
//                                    btnCancel.setVisibility(View.GONE);
//                                    btnSave.setVisibility(View.GONE);
//                                    space.setVisibility(View.GONE);
//                                }
//                            }

                        }
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                databaseReferenceT.child(orderid).child("status").setValue("3");
                                startActivity(new Intent(OrderDetail.this, HomeActivity.class));
                            }
                        });
                        btnSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (editTanggal.getText().toString().equals("Tanggal?") || editJam.getText().toString().equals("Jam?")) {
                                    Toast.makeText(OrderDetail.this, "Harus input data reschedule", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                else {
                                    databaseReferenceT.child(orderid).child("Tanggal pickup").setValue(editTanggal.getText().toString());
                                    databaseReferenceT.child(orderid).child("Jam pickup").setValue(editJam.getText().toString());
                                    startActivity(new Intent(OrderDetail.this, HomeActivity.class));
                                }
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
                    if (dataSnapshot.child("nama").getValue().toString().equals(namaLaundry1)) {
                           noTelp.setText("No. Telp: " + dataSnapshot.child("phone").getValue().toString());
                           if (dataSnapshot.child("address").exists()) {
                               alamat.setText("Alamat: " + dataSnapshot.child("address").getValue().toString());
                           }
                           else {
                               alamat.setText("Alamat: -");
                           }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        editJam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                jam = calendar.get(Calendar.HOUR_OF_DAY);
                menit = calendar.get(Calendar.MINUTE);

                TimePickerDialog timedialog;
                timedialog = new TimePickerDialog(OrderDetail.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        jam = hour;
                        menit = minute;

                        if (jam <= 12) {
                            editJam.setText(String.format(Locale.getDefault(), "%d:%d am", jam, menit));
                        }
                        else {
                            editJam.setText(String.format(Locale.getDefault(), "%d:%d pm", jam, menit));
                        }
                    }
                }, jam, menit, true);
                timedialog.show();
            }
        });

        editTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                tanggal = calendar.get(Calendar.DAY_OF_MONTH);
                bulan = calendar.get(Calendar.MONTH);
                tahun = calendar.get(Calendar.YEAR);

                DatePickerDialog datedialog;
                datedialog = new DatePickerDialog(OrderDetail.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int date, int month, int year) {
                        tanggal = date;
                        bulan = month+1;
                        tahun = year;

                        editTanggal.setText(tahun + "/" + bulan + "/" + tanggal);
                    }
                }, tahun, bulan, tanggal);
                datedialog.show();
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