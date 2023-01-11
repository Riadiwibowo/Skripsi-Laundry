package com.example.testawal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.appwidget.AppWidgetHost;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

public class OrderDetailNew extends AppCompatActivity {

    TextView Id, namaLaundry, noTelp, alamat, category, service, hargaPickup, price, tanggalpickup, jampickup, alamatpickup, editTanggal, editJam;
    private int jam, menit;
    private int tanggal, bulan, tahun;
    DatabaseReference databaseReference, databaseReferenceT;
    StorageReference storageReference;
    FirebaseUser fUser;
    String userId, trId;
    Spinner rescheduleSpinner;
    LinearLayout layoutPickup, layoutReschedule, layoutHargaPickup;
    Button btnCancel, btnSave, btnAccept;
    Space space;

    //daniel
    TextView status;
    TextView jumlahbaju, jumlahsepatu, jumlahothers;
    String categoryDetail, servicesDetail;
    LinearLayout detailBaju, detailSepatu, detailOthers, bodyPickupTop;
    TextView subTotalBaju, subTotalSepatu, subTotalOthers;
    Integer subCalcSatuan=0, subCalcKiloan=0, subCalcSepatu=0;
    ImageView lastLine;

    //popupcancel
    LinearLayout alasan1, alasan2, alasan3, alasan4;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_new);

        getSupportActionBar().setTitle("Transaction Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReferenceT = FirebaseDatabase.getInstance().getReference("Transactions");
        storageReference = FirebaseStorage.getInstance().getReference();

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = fUser.getUid();

        Id = findViewById(R.id.Id);

        //daniel properties UI new
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


        //laundry properties
        namaLaundry = findViewById(R.id.namaLaundry);
        noTelp = findViewById(R.id.noTelp);
        alamat = findViewById(R.id.alamat);

        //order properties
//        category = findViewById(R.id.category);
        service = findViewById(R.id.service);
        layoutHargaPickup = findViewById(R.id.layoutHargaPickup);
        hargaPickup = findViewById(R.id.hargaPickup);
        price = findViewById(R.id.price);

        //pickup properties
        tanggalpickup = findViewById(R.id.tanggalPickup);
        jampickup = findViewById(R.id.jamPickup);
        alamatpickup = findViewById(R.id.alamatpickup);

        //reschedule properties
        bodyPickupTop = findViewById(R.id.bodyPickupTop);
        rescheduleSpinner = findViewById(R.id.rescheduleSpinner);
        String[] items = new String[]{"Tidak", "Ya"};
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

        //popupcancel properties
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.popupcancelorder);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alasan1 = dialog.findViewById(R.id.alasan1);
        alasan2 = dialog.findViewById(R.id.alasan2);
        alasan3 = dialog.findViewById(R.id.alasan3);
        alasan4 = dialog.findViewById(R.id.alasan4);

        Bundle b = getIntent().getExtras();
        String orderid = (String) b.get("orderid");
        String namaLaundry1 = (String) b.get("namaLaundry1");

        Id.setText(orderid);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("nama").getValue().toString().equals(namaLaundry1)) {
                        noTelp.setText(dataSnapshot.child("phone").getValue().toString());
                        if (dataSnapshot.child("Harga").child("Pickup").exists()) {
                            hargaPickup.setText(dataSnapshot.child("Harga").child("Pickup").getValue().toString());
                        }
                        else {
                            layoutHargaPickup.setVisibility(View.GONE);
                        }
                        if (dataSnapshot.child("address").exists()) {
                            alamat.setText(dataSnapshot.child("address").getValue().toString());
                        }
                        else {
                            alamat.setText("-");
                        }
                        if (dataSnapshot.child("Harga").child("Satuan").exists()){
                            subCalcSatuan = Integer.valueOf(dataSnapshot.child("Harga").child("Satuan").getValue().toString());
                        }
                        if (dataSnapshot.child("Harga").child("Kiloan").exists()){
                            subCalcKiloan = Integer.valueOf(dataSnapshot.child("Harga").child("Kiloan").getValue().toString());
                        }
                        if (dataSnapshot.child("Harga").child("Sepatu").exists()){
                            subCalcSepatu = Integer.valueOf(dataSnapshot.child("Harga").child("Sepatu").getValue().toString());
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
                        namaLaundry.setText(dataSnapshot.child("namaLaundry").getValue().toString());
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
//                            detailOthers.setVisibility(View.GONE);
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
//                                detailOthers.setVisibility(View.GONE);
                                detailSepatu.setVisibility(View.VISIBLE);
                                if (dataSnapshot.child("services").getValue().toString().contains("pair") || dataSnapshot.child("services").getValue().toString().contains("Pair")){
                                    jumlahsepatu.setText(dataSnapshot.child("pair").getValue().toString() + " pair(s)");
                                    if (subCalcSepatu!=0){
                                        subTotalSepatu.setText(String.valueOf(subCalcSepatu*Integer.valueOf(dataSnapshot.child("pair").getValue().toString())));
                                    }
                                }
                            }
                            if (categoryDetail.contains("others") || categoryDetail.contains("Others")){
//                                detailOthers.setVisibility(View.VISIBLE);
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
//                            detailOthers.setVisibility(View.GONE);
                            detailBaju.setVisibility(View.GONE);
                            if (dataSnapshot.child("services").getValue().toString().contains("pair") || dataSnapshot.child("services").getValue().toString().contains("Pair")){
                                jumlahsepatu.setText(dataSnapshot.child("pair").getValue().toString() + " pair(s)");
                                if (subCalcSepatu!=0){
                                    subTotalSepatu.setText(String.valueOf(subCalcSepatu*Integer.valueOf(dataSnapshot.child("pair").getValue().toString())));
                                }
                            }
                            if (categoryDetail.contains("others") || categoryDetail.contains("Others")){
//                                detailOthers.setVisibility(View.VISIBLE);
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
//                                detailOthers.setVisibility(View.VISIBLE);
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
                        }else{
                            tanggalpickup.setText("-");
                        }
                        if (dataSnapshot.child("Jam pickup").exists()){
                            jampickup.setText(dataSnapshot.child("Jam pickup").getValue().toString());
                        }else{
                            jampickup.setText("-");
                        }
                        if (dataSnapshot.child("address").exists()){
                            alamatpickup.setText(dataSnapshot.child("address").getValue().toString());
                        }else{
                            alamatpickup.setText("-");
                        }
                        if (dataSnapshot.child("isPickup").getValue().toString().equals("No")) {
                            bodyPickupTop.setVisibility(View.GONE);
                            layoutPickup.setVisibility(View.GONE);
                            layoutReschedule.setVisibility(View.GONE);
                            btnSave.setVisibility(View.GONE);
                            space.setVisibility(View.GONE);
                            lastLine.setVisibility(View.GONE);
                            if (dataSnapshot.child("status").getValue().toString().equals("3") || dataSnapshot.child("status").getValue().toString().equals("4") || dataSnapshot.child("status").getValue().toString().equals("5")) {
                                btnCancel.setVisibility(View.GONE);
                            }
                        }
                        else if (dataSnapshot.child("isPickup").getValue().toString().equals("Yes")) {
                            if (dataSnapshot.child("status").getValue().toString().equals("2") || dataSnapshot.child("status").getValue().toString().equals("3") || dataSnapshot.child("status").getValue().toString().equals("4") || dataSnapshot.child("status").getValue().toString().equals("5")) {
                                bodyPickupTop.setVisibility(View.GONE);
                                layoutReschedule.setVisibility(View.GONE);
                                btnCancel.setVisibility(View.GONE);
                                btnSave.setVisibility(View.GONE);
                                space.setVisibility(View.GONE);
                            }
                            rescheduleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    if (rescheduleSpinner.getItemAtPosition(i).toString().equals("Ya")){
                                        if (dataSnapshot.child("status").getValue().toString().equals("0") || dataSnapshot.child("status").getValue().toString().equals("1")) {
                                            layoutReschedule.setVisibility(View.VISIBLE);
                                            btnCancel.setVisibility(View.VISIBLE);
                                            btnSave.setVisibility(View.VISIBLE);
                                            space.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    else if (rescheduleSpinner.getItemAtPosition(i).toString().equals("Tidak")){
                                        layoutReschedule.setVisibility(View.GONE);
                                        btnSave.setVisibility(View.GONE);
                                        space.setVisibility(View.GONE);
                                        if (dataSnapshot.child("status").getValue().toString().equals("0")) {
                                            btnCancel.setVisibility(View.VISIBLE);
                                        }
                                        if (dataSnapshot.child("status").getValue().toString().equals("1")) {
                                            btnCancel.setVisibility(View.VISIBLE);
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
                                dialog.show();
                                alasan1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        databaseReferenceT.child(orderid).child("alasan").setValue("Salah tempat laundry");
                                        databaseReferenceT.child(orderid).child("status").setValue("5");
                                        startActivity(new Intent(OrderDetailNew.this, HomeActivity.class));
                                    }
                                });
                                alasan2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        databaseReferenceT.child(orderid).child("alasan").setValue("Salah memilih servis laundry");
                                        databaseReferenceT.child(orderid).child("status").setValue("5");
                                        startActivity(new Intent(OrderDetailNew.this, HomeActivity.class));
                                    }
                                });
                                alasan3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        databaseReferenceT.child(orderid).child("alasan").setValue("Salah input berat/satuan/pasang");
                                        databaseReferenceT.child(orderid).child("status").setValue("5");
                                        startActivity(new Intent(OrderDetailNew.this, HomeActivity.class));
                                    }
                                });
                                if (dataSnapshot.child("isPickup").getValue().toString().equals("No")) {
                                    alasan4.setVisibility(View.GONE);
                                }
                                else if (dataSnapshot.child("isPickup").getValue().toString().equals("Yes")) {
                                    alasan4.setVisibility(View.VISIBLE);
                                    alasan4.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            databaseReferenceT.child(orderid).child("alasan").setValue("Salah detail pickup");
                                            databaseReferenceT.child(orderid).child("status").setValue("5");
                                            startActivity(new Intent(OrderDetailNew.this, HomeActivity.class));
                                        }
                                    });
                                }
                            }
                        });
                        btnSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (editTanggal.getText().toString().equals("Tanggal?") || editJam.getText().toString().equals("Jam?")) {
                                    Toast.makeText(OrderDetailNew.this, "Harus input data reschedule", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                else {
                                    databaseReferenceT.child(orderid).child("Tanggal pickup").setValue(editTanggal.getText().toString());
                                    databaseReferenceT.child(orderid).child("Jam pickup").setValue(editJam.getText().toString());
                                    startActivity(new Intent(OrderDetailNew.this, HomeActivity.class));
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


        editJam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                jam = calendar.get(Calendar.HOUR_OF_DAY);
                menit = calendar.get(Calendar.MINUTE);

                TimePickerDialog timedialog;
                timedialog = new TimePickerDialog(OrderDetailNew.this, new TimePickerDialog.OnTimeSetListener() {
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
                datedialog = new DatePickerDialog(OrderDetailNew.this, new DatePickerDialog.OnDateSetListener() {
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