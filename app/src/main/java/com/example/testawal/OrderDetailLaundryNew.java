package com.example.testawal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.appwidget.AppWidgetHost;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
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

public class OrderDetailLaundryNew extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    TextView Id, namaUser, noTelp, category, service, hargaPickup, price, tanggalpickup, jampickup, alamatpickup, editTanggal, editJam;
    private int jam, menit;
    private int tanggal, bulan, tahun;
    DatabaseReference databaseReference, databaseReferenceT;
    StorageReference storageReference;
    FirebaseUser fUser;
    String userId, trId;
    Spinner rescheduleSpinner;
    LinearLayout layoutPickup, layoutReschedule, layoutHargaPickup, footerLayout;
    Button btnCancel, btnAccept, btnPickup;
    Space space, space1;

    //daniel
    TextView status;
    TextView jumlahbaju, jumlahsepatu, jumlahothers;
    String categoryDetail, servicesDetail;
    LinearLayout detailBaju, detailSepatu, detailOthers, bodyPickupTop;
    TextView subTotalBaju, subTotalSepatu, subTotalOthers;
    Integer subCalcSatuan=0, subCalcKiloan=0, subCalcSepatu=0;
    ImageView lastLine;

    LinearLayout parentLayout;
    TextView titleId, titleService, headerInfo, titleNamaUser, titleNoTelp, headerDetail, titleBaju, titleSepatu, rp1, rp2,
    rp3, titlePickup, titleTotalHarga, rp4, linearPickup, titleTanggal, titleAlamatPickup, titleJam;
    TextView td1, td2, td3, td4, td5, td6, td7;

    //popupcancel
    ImageView lineblack;
    LinearLayout alasan1, alasan2;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_laundry_new);

        getSupportActionBar().setTitle("Transaction Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReferenceT = FirebaseDatabase.getInstance().getReference("Transactions");
        storageReference = FirebaseStorage.getInstance().getReference();

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = fUser.getUid();

        Id = findViewById(R.id.Id);

        //region daniel properties UI new
        status = findViewById(R.id.Status);
        detailBaju = findViewById(R.id.detailBaju);
        detailSepatu = findViewById(R.id.detailSepatu);
        lastLine = findViewById(R.id.lastLine);
//        detailOthers = findViewById(R.id.detailOthers);
        jumlahbaju = findViewById(R.id.jumlahbaju);
        jumlahsepatu = findViewById(R.id.jumlahsepatu);
//        jumlahothers = findViewById(R.id.jumlahothers);
        subTotalBaju = findViewById(R.id.subTotalBaju);
        subTotalSepatu = findViewById(R.id.subTotalSepatu);
//        subTotalOthers = findViewById(R.id.subTotalOthers);
        //endregion

        //laundry properties
        namaUser = findViewById(R.id.namaUser);
        noTelp = findViewById(R.id.noTelp);

        //order properties
//        category = findViewById(R.id.category);
        service = findViewById(R.id.service);
        hargaPickup = findViewById(R.id.hargaPickup);
        layoutHargaPickup = findViewById(R.id.layoutHargaPickup);
        price = findViewById(R.id.price);

        //pickup properties
        tanggalpickup = findViewById(R.id.tanggalPickup);
        jampickup = findViewById(R.id.jamPickup);
        alamatpickup = findViewById(R.id.alamatpickup);
        layoutPickup = findViewById(R.id.layoutPickup);

        //button properties
        btnCancel = findViewById(R.id.btnCancel);
        btnAccept = findViewById(R.id.btnAccept);
        btnPickup = findViewById(R.id.btnPickup);
        space = findViewById(R.id.space);
        space1 = findViewById(R.id.space1);

        //popupcancel properties
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.popupcancelorderlaundry);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alasan1 = dialog.findViewById(R.id.alasan1);
        alasan2 = dialog.findViewById(R.id.alasan2);
        lineblack = dialog.findViewById(R.id.lineblack);

        Bundle b = getIntent().getExtras();
        String orderid = (String) b.get("orderid");
        String namaLaundry1 = (String) b.get("namaLaundry1");
        String namaUser1 = (String) b.get("namaUser1");

        Id.setText(orderid);

        parentLayout = findViewById(R.id.parentLayout);
        footerLayout = findViewById(R.id.footerLayout);
        titleId = findViewById(R.id.titleId);
        titleService = findViewById(R.id.titleService);
        headerInfo = findViewById(R.id.headerInfo);
        titleNamaUser = findViewById(R.id.titleNamaUser);
        titleNoTelp = findViewById(R.id.titleNoTelp);
        td1 = findViewById(R.id.td1);
        td2 = findViewById(R.id.td2);
        td3 = findViewById(R.id.td3);
        td4 = findViewById(R.id.td4);
        td5 = findViewById(R.id.td5);
        td6 = findViewById(R.id.td6);
        td7 = findViewById(R.id.td7);
        headerDetail = findViewById(R.id.headerDetail);
        titleBaju = findViewById(R.id.titleBaju);
        titleSepatu = findViewById(R.id.titleSepatu);
        titlePickup = findViewById(R.id.titlePickup);
        titleTotalHarga = findViewById(R.id.titleTotalHarga);
        rp1 = findViewById(R.id.rp1);
        rp2 = findViewById(R.id.rp2);
        rp3 = findViewById(R.id.rp3);
        rp4 = findViewById(R.id.rp4);
        linearPickup = findViewById(R.id.linearPickup);
        titleTanggal = findViewById(R.id.titleTanggal);
        titleJam = findViewById(R.id.titleJam);
        titleAlamatPickup = findViewById(R.id.titleAlamatPickup);

        //region dark mode
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final int black = ContextCompat.getColor(this, R.color.black);
        final int white = ContextCompat.getColor(this, R.color.white);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#083444"));

        if (sharedPreferences.getBoolean("dark_mode", true)) {
            parentLayout.setBackgroundColor(black);
            footerLayout.setBackgroundColor(black);
            titleId.setTextColor(white);
            td1.setTextColor(white);
            Id.setTextColor(white);
            titleService.setTextColor(white);
            td2.setTextColor(white);
            service.setTextColor(white);
            headerInfo.setTextColor(white);
            titleNamaUser.setTextColor(white);
            td3.setTextColor(white);
            namaUser.setTextColor(white);
            titleNoTelp.setTextColor(white);
            td4.setTextColor(white);
            noTelp.setTextColor(white);
            headerDetail.setTextColor(white);
            titleBaju.setTextColor(black);
            rp1.setTextColor(black);
            subTotalBaju.setTextColor(black);
            titleSepatu.setTextColor(black);
            rp2.setTextColor(black);
            subTotalSepatu.setTextColor(black);
            titlePickup.setTextColor(white);
            rp3.setTextColor(white);
            hargaPickup.setTextColor(white);
            titleTotalHarga.setTextColor(white);
            rp4.setTextColor(white);
            price.setTextColor(white);
            linearPickup.setTextColor(white);
            td5.setTextColor(white);
            td6.setTextColor(white);
            td7.setTextColor(white);
            titleAlamatPickup.setTextColor(white);
            titleTanggal.setTextColor(white);
            titleJam.setTextColor(white);
            tanggalpickup.setTextColor(white);
            jampickup.setTextColor(white);
            alamatpickup.setTextColor(white);
            getSupportActionBar().setBackgroundDrawable(colorDrawable);
        }
        //endregion

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("nama").getValue().toString().equals(namaUser1)) {
                        noTelp.setText(dataSnapshot.child("phone").getValue().toString());
                    }
                    if (dataSnapshot.child("nama").getValue().toString().equals(namaLaundry1)) {
                        noTelp.setText(dataSnapshot.child("phone").getValue().toString());
                        if (dataSnapshot.child("harga").child("satuan").exists()){
                            subCalcSatuan = Integer.valueOf(dataSnapshot.child("harga").child("satuan").getValue().toString());
                        }
                        if (dataSnapshot.child("harga").child("kiloan").exists()){
                            subCalcKiloan = Integer.valueOf(dataSnapshot.child("harga").child("kiloan").getValue().toString());
                        }
                        if (dataSnapshot.child("harga").child("sepatu").exists()){
                            subCalcSepatu = Integer.valueOf(dataSnapshot.child("harga").child("sepatu").getValue().toString());
                        }
                        if (dataSnapshot.child("harga").child("pickup").exists()) {
                            hargaPickup.setText(dataSnapshot.child("harga").child("pickup").getValue().toString());
                        }
                        else {
                            layoutHargaPickup.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReferenceT.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("id").getValue().toString().equals(orderid)) {
                        namaUser.setText(dataSnapshot.child("namaUser").getValue().toString());
//                        category.setText("Category: " + dataSnapshot.child("category").getValue().toString());
//                        service.setText("Services: " + dataSnapshot.child("services").getValue().toString());
                        if (dataSnapshot.child("status").getValue().toString().equals("0")) {
                            status.setText("Awaiting");
                            status.setTextColor(getResources().getColor(R.color.blue));
                        }
                        if (dataSnapshot.child("status").getValue().toString().equals("1")) {
                            status.setText("Accepted");
                            status.setTextColor(getResources().getColor(R.color.blue));
                        }
                        if (dataSnapshot.child("status").getValue().toString().equals("2")) {
                            status.setText("On Pickup");
                            status.setTextColor(getResources().getColor(R.color.orange));
                        }
                        if (dataSnapshot.child("status").getValue().toString().equals("3")) {
                            status.setText("On Process");
                            status.setTextColor(getResources().getColor(R.color.orange));
                        }
                        if (dataSnapshot.child("status").getValue().toString().equals("4")) {
                            status.setText("Done");
                            status.setTextColor(getResources().getColor(R.color.green));
                        }
                        if (dataSnapshot.child("status").getValue().toString().equals("5")) {
                            status.setText("Cancelled");
                            status.setTextColor(getResources().getColor(R.color.red));
                        }
                        price.setText(dataSnapshot.child("harga").getValue().toString());
                        service.setText(dataSnapshot.child("services").getValue().toString());
                        categoryDetail = dataSnapshot.child("category").getValue().toString();
                        servicesDetail = dataSnapshot.child("services").getValue().toString();
                        if (categoryDetail.contains("Baju") || categoryDetail.contains("Baju")){
                            detailSepatu.setVisibility(View.GONE);
                            if (dataSnapshot.child("services").getValue().toString().contains("Satuan") || dataSnapshot.child("services").getValue().toString().contains("satuan")){
                                jumlahbaju.setText(dataSnapshot.child("jumlah").getValue().toString() + " pcs");
                                if (subCalcSatuan!=0){
                                    subTotalBaju.setText(String.valueOf(subCalcSatuan*Integer.valueOf(dataSnapshot.child("jumlah").getValue().toString())));
                                }
                            }
                            if (dataSnapshot.child("services").getValue().toString().contains("Kiloan") || dataSnapshot.child("services").getValue().toString().contains("kiloan")){
                                jumlahbaju.setText(dataSnapshot.child("berat").getValue().toString() + " kg");
                                if (subCalcKiloan!=0){
                                    subTotalBaju.setText(String.valueOf(subCalcKiloan*Integer.valueOf(dataSnapshot.child("berat").getValue().toString())));
                                }
                            }
                            if (categoryDetail.contains("sepatu") || categoryDetail.contains("Sepatu")){
                                detailSepatu.setVisibility(View.VISIBLE);
                                if (dataSnapshot.child("services").getValue().toString().contains("pair") || dataSnapshot.child("services").getValue().toString().contains("Pair")){
                                    jumlahsepatu.setText(dataSnapshot.child("pair").getValue().toString() + " pair(s)");
                                    if (subCalcSepatu!=0){
                                        subTotalSepatu.setText(String.valueOf(subCalcSepatu*Integer.valueOf(dataSnapshot.child("pair").getValue().toString())));
                                    }
                                }
                            }
                            if (categoryDetail.contains("others") || categoryDetail.contains("Others")){
                                if (dataSnapshot.child("services").getValue().toString().contains("Satuan") || dataSnapshot.child("services").getValue().toString().contains("satuan")){
                                    jumlahbaju.setText(dataSnapshot.child("jumlah").getValue().toString() + " pcs");
                                    if (subCalcSatuan!=0){
                                        subTotalBaju.setText(String.valueOf(subCalcSatuan*Integer.valueOf(dataSnapshot.child("jumlah").getValue().toString())));
                                    }
                                }
                                if (dataSnapshot.child("services").getValue().toString().contains("Kiloan") || dataSnapshot.child("services").getValue().toString().contains("kiloan")){
                                    jumlahbaju.setText(dataSnapshot.child("berat").getValue().toString() + " kg");
                                    if (subCalcKiloan!=0){
                                        subTotalBaju.setText(String.valueOf(subCalcKiloan*Integer.valueOf(dataSnapshot.child("berat").getValue().toString())));
                                    }
                                }
                            }
                        }
                        else if (categoryDetail.contains("sepatu") || categoryDetail.contains("Sepatu")){
                            detailBaju.setVisibility(View.GONE);
                            if (dataSnapshot.child("services").getValue().toString().contains("pair") || dataSnapshot.child("services").getValue().toString().contains("Pair")){
                                jumlahsepatu.setText(dataSnapshot.child("pair").getValue().toString() + " pair(s)");
                                if (subCalcSepatu!=0){
                                    subTotalSepatu.setText(String.valueOf(subCalcSepatu*Integer.valueOf(dataSnapshot.child("pair").getValue().toString())));
                                }
                            }
                            if (categoryDetail.contains("others") || categoryDetail.contains("Others")){
                                detailBaju.setVisibility(View.VISIBLE);
                                if (dataSnapshot.child("services").getValue().toString().contains("Satuan") || dataSnapshot.child("services").getValue().toString().contains("satuan")){
                                    jumlahbaju.setText(dataSnapshot.child("jumlah").getValue().toString() + " pcs");
                                    if (subCalcSatuan!=0){
                                        subTotalBaju.setText(String.valueOf(subCalcSatuan*Integer.valueOf(dataSnapshot.child("jumlah").getValue().toString())));
                                    }
                                }
                                if (dataSnapshot.child("services").getValue().toString().contains("Kiloan") || dataSnapshot.child("services").getValue().toString().contains("kiloan")){
                                    jumlahbaju.setText(dataSnapshot.child("berat").getValue().toString() + " kg");
                                    if (subCalcKiloan!=0){
                                        subTotalBaju.setText(String.valueOf(subCalcKiloan*Integer.valueOf(dataSnapshot.child("berat").getValue().toString())));
                                    }
                                }
                            }
                        }
                        else if (categoryDetail.contains("others") || categoryDetail.contains("Others")){
                                detailSepatu.setVisibility(View.GONE);
                            if (dataSnapshot.child("services").getValue().toString().contains("Satuan") || dataSnapshot.child("services").getValue().toString().contains("satuan")){
                                jumlahbaju.setText(dataSnapshot.child("jumlah").getValue().toString() + " pcs");
                                if (subCalcSatuan!=0){
                                    subTotalBaju.setText(String.valueOf(subCalcSatuan*Integer.valueOf(dataSnapshot.child("jumlah").getValue().toString())));
                                }
                            }
                            if (dataSnapshot.child("services").getValue().toString().contains("Kiloan") || dataSnapshot.child("services").getValue().toString().contains("kiloan")){
                                jumlahbaju.setText(dataSnapshot.child("berat").getValue().toString() + " kg");
                                if (subCalcKiloan!=0){
                                    subTotalBaju.setText(String.valueOf(subCalcKiloan*Integer.valueOf(dataSnapshot.child("berat").getValue().toString())));
                                }
                            }
                        }


                        if (dataSnapshot.child("Tanggal pickup").exists()){
                            tanggalpickup.setText(dataSnapshot.child("Tanggal pickup").getValue().toString());
                        }
                        if (dataSnapshot.child("Jam pickup").exists()){
                            jampickup.setText(dataSnapshot.child("Jam pickup").getValue().toString());
                        }
                        if (dataSnapshot.child("address").exists()){
                            alamatpickup.setText(dataSnapshot.child("address").getValue().toString());
                        }
                        if (dataSnapshot.child("isPickup").getValue().toString().equals("No")) {
                            layoutPickup.setVisibility(View.GONE);
                            btnPickup.setVisibility(View.GONE);
                            space1.setVisibility(View.GONE);
                            lastLine.setVisibility(View.GONE);
                            layoutHargaPickup.setVisibility(View.GONE);
                            if (dataSnapshot.child("status").getValue().toString().equals("1")) {
                                btnAccept.setText("On Process");
                            }
                            if (dataSnapshot.child("status").getValue().toString().equals("3")) {
                                btnCancel.setVisibility(View.GONE);
                                btnAccept.setText("Done");
                                space.setVisibility(View.GONE);
                            }
                            if (dataSnapshot.child("status").getValue().toString().equals("4") || dataSnapshot.child("status").getValue().toString().equals("5")) {
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
                                btnPickup.setVisibility(View.VISIBLE);
                                space.setVisibility(View.GONE);
                                btnAccept.setVisibility(View.GONE);
                            }
                            if (dataSnapshot.child("status").getValue().toString().equals("2")) {
                                btnCancel.setVisibility(View.GONE);
                                space.setVisibility(View.GONE);
                                space1.setVisibility(View.GONE);
                                btnPickup.setVisibility(View.GONE);
                                btnAccept.setText("On Process");
                            }
                            if (dataSnapshot.child("status").getValue().toString().equals("3")) {
                                btnCancel.setVisibility(View.GONE);
                                btnAccept.setText("Done");
                                btnPickup.setVisibility(View.GONE);
                                space.setVisibility(View.GONE);
                                space1.setVisibility(View.GONE);
                            }
                            if (dataSnapshot.child("status").getValue().toString().equals("4") || dataSnapshot.child("status").getValue().toString().equals("5")) {
                                btnCancel.setVisibility(View.GONE);
                                btnPickup.setVisibility(View.GONE);
                                btnAccept.setVisibility(View.GONE);
                                space.setVisibility(View.GONE);
                                space1.setVisibility(View.GONE);
                            }
                        }
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.show();
                                alasan1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        databaseReferenceT.child(orderid).child("alasan").setValue("Pesanan melebihi batas maksimum");
                                        databaseReferenceT.child(orderid).child("status").setValue("5");
                                        startActivity(new Intent(OrderDetailLaundryNew.this, LaundryMain.class));
                                    }
                                });
                                if (dataSnapshot.child("isPickup").getValue().toString().equals("No")) {
                                    alasan2.setVisibility(View.GONE);
                                }
                                else if (dataSnapshot.child("isPickup").getValue().toString().equals("Yes")) {
                                    alasan2.setVisibility(View.VISIBLE);
                                    lineblack.setVisibility(View.VISIBLE);
                                    alasan2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            databaseReferenceT.child(orderid).child("alasan").setValue("Jarak pickup tidak sesuai");
                                            databaseReferenceT.child(orderid).child("status").setValue("5");
                                            startActivity(new Intent(OrderDetailLaundryNew.this, LaundryMain.class));
                                        }
                                    });
                                }
                            }
                        });
                        btnAccept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (btnAccept.getText().toString().equals("ACCEPT") || btnAccept.getText().toString().equals("Accept")) {
                                    databaseReferenceT.child(orderid).child("status").setValue("1");
                                    startActivity(new Intent(OrderDetailLaundryNew.this, LaundryTransaction.class));
                                }
                                else if (btnAccept.getText().toString().equals("On Process") || btnAccept.getText().toString().equals("ON PROCESS")) {
                                    databaseReferenceT.child(orderid).child("status").setValue("3");
                                    startActivity(new Intent(OrderDetailLaundryNew.this, LaundryTransaction.class));
                                }
                                else if (btnAccept.getText().toString().equals("DONE") || btnAccept.getText().toString().equals("Done")) {
                                    databaseReferenceT.child(orderid).child("status").setValue("4");
                                    startActivity(new Intent(OrderDetailLaundryNew.this, LaundryTransaction.class));
                                }
                            }
                        });
                        btnPickup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                databaseReferenceT.child(orderid).child("status").setValue("2");
                                startActivity(new Intent(OrderDetailLaundryNew.this, LaundryTransaction.class));
                            }
                        });
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