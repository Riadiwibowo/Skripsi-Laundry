package com.example.testawal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.LayoutTransition;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OrderProcess extends AppCompatActivity {

    //region properties
    TextView editTanggal,  editJam;
    TextView txtNamaLaundry, txtNamaUser;
    String userId, trId, laundryId, namaUser;
    ArrayList<User> users;
    FirebaseUser fUser;
    DatabaseReference databaseReference, databaseReferenceT;
    private FirebaseAuth mAuth;
    private Uri url;

    private int jam, menit;
    private int tanggal, bulan, tahun;

    CheckBox catBaju, catSepatu, catOthers;

    CardView cardReg, cardKil;
    RadioButton regulerSatuan, regulerKiloan, kilatSatuan, kilatKiloan, regulerPair, kilatPair;
    RadioGroup radioGrpKilatPair, radioGrpRegulerPair;

    EditText inputKg, inputKg1, inputPair1, inputPair;
    LinearLayout layoutKilat, layoutReguler;
    RadioGroup radioGrp, radioGrp1, radioGrpPair, radioGrpPair1;
    int check=0, check1=0, checkPair=0, checkPair1=0;

    LinearLayout headerPickup, bodyPickup, bodyPickupBottom, bodyPickupTop;
    CardView cardPickup;
    Spinner dropdown;

    LinearLayout layoutAlamat;
    EditText editAlamat;

    int calckilat=0, calcreguler=0, calcpickup=0, temptotal=0;
    String hargaSatuan, hargaKiloan, hargaPair, hargaPickup;
    TextView harga, txtHarga;

    Button btnHarga, btnSubmit;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_process);

        getSupportActionBar().setTitle("Order Form");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //region expand properties
        layoutKilat = findViewById(R.id.layoutKilat);
        layoutKilat.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        layoutReguler = findViewById(R.id.layoutReguler);
        layoutReguler.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        //kilat
        inputKg = findViewById(R.id.inputBerat);
        radioGrp = findViewById(R.id.radioGroupKilat);
        radioGrpPair = findViewById(R.id.radioGroupKilatPair);
        inputPair = findViewById(R.id.inputPair);
        //reguler
        inputKg1 = findViewById(R.id.inputBerat1);
        radioGrp1 = findViewById(R.id.radioGroupReguler);
        radioGrpPair1 = findViewById(R.id.radioGroupRegulerPair);
        inputPair1 = findViewById(R.id.inputPair1);

        harga = findViewById(R.id.harga);
        txtHarga = findViewById(R.id.txtHarga);
        //endregion

        //region input kg/pair condition
        inputKg.addTextChangedListener(inputKgWatcher);
        inputKg1.addTextChangedListener(inputKgWatcher);
        inputPair.addTextChangedListener(inputKgWatcher);
        inputPair1.addTextChangedListener(inputKgWatcher);
        //endregion

        //region spinner
        dropdown = findViewById(R.id.pickupSpinner);
        String[] items = new String[]{"No", "Yes"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        //endregion

        //region editTanggal dan editJam
        editTanggal = findViewById(R.id.editTanggal);
        editJam = findViewById(R.id.editJam);

        editJam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                jam = calendar.get(Calendar.HOUR_OF_DAY);
                menit = calendar.get(Calendar.MINUTE);

                TimePickerDialog timedialog;
                timedialog = new TimePickerDialog(OrderProcess.this, new TimePickerDialog.OnTimeSetListener() {
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
                datedialog = new DatePickerDialog(OrderProcess.this, new DatePickerDialog.OnDateSetListener() {
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
        //endregion

        //region firebase properties
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReferenceT = FirebaseDatabase.getInstance().getReference("Transactions");
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = fUser.getUid();
        //endregion

        //region get current laundry
        Bundle b = getIntent().getExtras();
        String namaLaundry = (String) b.get("namaLaundry");
        txtNamaLaundry = findViewById(R.id.txtNamaLaundry);
        txtNamaLaundry.setText(namaLaundry);
        //endregion

        //region get category
        catBaju = findViewById(R.id.categoryBaju);
        catSepatu = findViewById(R.id.categorySepatu);
        catOthers = findViewById(R.id.categoryOthers);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.child("nama").getValue().toString().equals(namaLaundry)){
                        if(dataSnapshot.child("category").getValue().toString().contains("Baju")){
                            catBaju.setVisibility(View.VISIBLE);
                            catSepatu.setVisibility(View.GONE);
                            catOthers.setVisibility(View.GONE);
                            if(dataSnapshot.child("category").getValue().toString().contains("Sepatu")) {
                                catSepatu.setVisibility(View.VISIBLE);
                            }
                            if(dataSnapshot.child("category").getValue().toString().contains("Others")) {
                                catOthers.setVisibility(View.VISIBLE);
                            }
                        }
                        else if(dataSnapshot.child("category").getValue().toString().contains("Sepatu")){
                            catBaju.setVisibility(View.GONE);
                            catSepatu.setVisibility(View.VISIBLE);
                            catOthers.setVisibility(View.GONE);
                            if(dataSnapshot.child("category").getValue().toString().contains("Others")) {
                                catOthers.setVisibility(View.VISIBLE);
                            }
                        }
                        else if(dataSnapshot.child("category").getValue().toString().contains("Others")){
                            catBaju.setVisibility(View.GONE);
                            catSepatu.setVisibility(View.GONE);
                            catOthers.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //endregion

        //region get services reguler/kilat
        cardKil = findViewById(R.id.cardKilat);
        cardReg = findViewById(R.id.cardReguler);
        kilatSatuan = findViewById(R.id.kilatSatuan);
        kilatSatuan.setEnabled(false);
        kilatKiloan = findViewById(R.id.kilatKiloan);
        kilatKiloan.setEnabled(false);
        kilatPair = findViewById(R.id.kilatPair);
        regulerSatuan = findViewById(R.id.regulerSatuan);
        regulerSatuan.setEnabled(false);
        regulerKiloan = findViewById(R.id.regulerKiloan);
        regulerKiloan.setEnabled(false);
        regulerPair = findViewById(R.id.regulerPair);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("nama").getValue().toString().equals(namaLaundry)) {
                        if (dataSnapshot.child("services").getValue().toString().contains("Kilat")){
                            cardKil.setVisibility(View.VISIBLE);
                            cardReg.setVisibility(View.VISIBLE);
                            kilatSatuan.setVisibility(View.GONE);
                            kilatKiloan.setVisibility(View.GONE);
                            kilatPair.setVisibility(View.GONE);
                            regulerSatuan.setVisibility(View.GONE);
                            regulerKiloan.setVisibility(View.GONE);
                            regulerPair.setVisibility(View.GONE);
                            if (dataSnapshot.child("services").getValue().toString().contains("Satuan")){
                                kilatSatuan.setVisibility(View.VISIBLE);
                                regulerSatuan.setVisibility(View.VISIBLE);
                            }
                            if (dataSnapshot.child("services").getValue().toString().contains("Kiloan")){
                                kilatKiloan.setVisibility(View.VISIBLE);
                                regulerKiloan.setVisibility(View.VISIBLE);
                            }
                            if (dataSnapshot.child("category").getValue().toString().contains("Sepatu")){
                                kilatPair.setVisibility(View.VISIBLE);
                                regulerPair.setVisibility(View.VISIBLE);
                                kilatPair.setEnabled(false);
                                regulerPair.setEnabled(false);
                                if (dataSnapshot.child("category").getValue().toString().equals("Sepatu")){
                                    kilatPair.setVisibility(View.VISIBLE);
                                    regulerPair.setVisibility(View.VISIBLE);
                                    kilatPair.setEnabled(false);
                                    regulerPair.setEnabled(false);
                                    kilatSatuan.setVisibility(View.GONE);
                                    kilatKiloan.setVisibility(View.GONE);
                                    regulerKiloan.setVisibility(View.GONE);
                                    regulerSatuan.setVisibility(View.GONE);
                                }
                            }
                        }else{
                            cardReg.setVisibility(View.VISIBLE);
                            cardKil.setVisibility(View.GONE);
                            regulerSatuan.setVisibility(View.GONE);
                            regulerKiloan.setVisibility(View.GONE);
                            if (dataSnapshot.child("services").getValue().toString().contains("Satuan")){
                                regulerSatuan.setVisibility(View.VISIBLE);
                            }
                            if (dataSnapshot.child("services").getValue().toString().contains("Kiloan")){
                                regulerKiloan.setVisibility(View.VISIBLE);
                            }
                            if (dataSnapshot.child("category").getValue().toString().contains("Sepatu")){
                                regulerPair.setVisibility(View.VISIBLE);
                                regulerPair.setEnabled(false);
                                if (dataSnapshot.child("category").getValue().toString().equals("Sepatu")){
                                    regulerPair.setVisibility(View.VISIBLE);
                                    regulerPair.setEnabled(false);
                                    regulerKiloan.setVisibility(View.GONE);
                                    regulerSatuan.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //endregion

        //region get pickup
        headerPickup = findViewById(R.id.headerPickup);
        bodyPickup = findViewById(R.id.bodyPickup);
        bodyPickupTop = findViewById(R.id.bodyPickupTop);
        bodyPickupBottom = findViewById(R.id.bodyPickupBottom);
        cardPickup = findViewById(R.id.cardPickup);
        layoutAlamat = findViewById(R.id.layoutAlamat);
        editAlamat = findViewById(R.id.editAlamat);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("nama").getValue().toString().equals(namaLaundry)) {
                        if (dataSnapshot.child("services").getValue().toString().contains("Pickup")){
                            headerPickup.setVisibility(View.VISIBLE);
                            cardPickup.setVisibility(View.VISIBLE);
                            bodyPickupTop.setVisibility(View.VISIBLE);
                            bodyPickupBottom.setVisibility(View.GONE);
                        }
                        else {
                            headerPickup.setVisibility(View.GONE);
                            cardPickup.setVisibility(View.GONE);
                        }
                        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if (dropdown.getItemAtPosition(i).toString().equals("Yes")){
                                    TransitionManager.beginDelayedTransition(bodyPickupBottom, new AutoTransition());
                                    TransitionManager.beginDelayedTransition(cardPickup, new AutoTransition());
                                    bodyPickupBottom.setVisibility(View.VISIBLE);
                                    layoutAlamat.setVisibility(View.VISIBLE);
                                }
                                else{
                                    TransitionManager.beginDelayedTransition(bodyPickupBottom, new AutoTransition());
                                    TransitionManager.beginDelayedTransition(cardPickup, new AutoTransition());
                                    bodyPickupBottom.setVisibility(View.GONE);
                                    layoutAlamat.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //endregion

        for (int i=0;i<1;i++){
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user != null){
                    namaUser = user.nama;
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
                    if (dataSnapshot.child("nama").getValue().toString().equals(namaLaundry)) {
                        if (dataSnapshot.child("Harga").child("Kiloan").exists()) {
                            hargaKiloan = dataSnapshot.child("Harga").child("Kiloan").getValue().toString();
                        }
                        else {
                            hargaKiloan = "0";
                        }
                        if (dataSnapshot.child("Harga").child("Satuan").exists()) {
                            hargaSatuan = dataSnapshot.child("Harga").child("Satuan").getValue().toString();
                        }
                        else {
                            hargaSatuan = "0";
                        }
                        if (dataSnapshot.child("Harga").child("Sepatu").exists()) {
                            hargaPair = dataSnapshot.child("Harga").child("Sepatu").getValue().toString();
                        }
                        else {
                            hargaPair = "0";
                        }
                        if (dataSnapshot.child("Harga").child("Pickup").exists()) {
                            hargaPickup = dataSnapshot.child("Harga").child("Pickup").getValue().toString();
                        }
                        else {
                            hargaPickup = "0";
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //region show harga
        btnHarga = findViewById(R.id.buttonHarga);
        btnHarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (regulerSatuan.isChecked()) {
                    if (!inputKg1.getText().toString().trim().isEmpty()) {
                        calcreguler += Integer.valueOf(inputKg1.getText().toString()) * Integer.valueOf(hargaSatuan);
                    }
                }
                if (regulerKiloan.isChecked()) {
                    if (!inputKg1.getText().toString().trim().isEmpty()) {
                        calcreguler += Integer.valueOf(inputKg1.getText().toString()) * Integer.valueOf(hargaKiloan);
                    }
                }
                if (regulerPair.isChecked()) {
                    if (!inputPair1.getText().toString().trim().isEmpty()) {
                        calcreguler += Integer.valueOf(inputPair1.getText().toString()) * Integer.valueOf(hargaPair);
                    }
                }
                if (kilatSatuan.isChecked()) {
                    if (!inputKg.getText().toString().trim().isEmpty()) {
                        calckilat += Integer.valueOf(inputKg.getText().toString()) * Integer.valueOf(hargaSatuan) * 1.2;
                    }
                }
                if (kilatKiloan.isChecked()) {
                    if (!inputKg.getText().toString().trim().isEmpty()) {
                        calckilat += Integer.valueOf(inputKg.getText().toString()) * Integer.valueOf(hargaKiloan) * 1.2;
                    }
                }
                if (kilatPair.isChecked()) {
                    if (!inputPair.getText().toString().trim().isEmpty()) {
                        calckilat += Integer.valueOf(inputPair.getText().toString()) * Integer.valueOf(hargaPair) * 1.2;
                    }
                }
                if (dropdown.getSelectedItem().toString().equals("Yes")){
                    calcpickup = Integer.valueOf(hargaPickup);
                }
                temptotal = calcreguler+calckilat+calcpickup;
                txtHarga.setText(String.valueOf(temptotal));
                calcreguler = 0;
                calckilat = 0;
            }
        });
        //endregion

        //region input data transaksi
        btnSubmit = findViewById(R.id.buttonSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trId = databaseReferenceT.push().getKey();

                //region order process without pickup
                if (catBaju.isChecked() || catSepatu.isChecked() || catOthers.isChecked()) {
                    if (catBaju.isChecked()) {
                        if ((regulerSatuan.isChecked() || regulerKiloan.isChecked()) && (kilatSatuan.isChecked() || kilatKiloan.isChecked())) {
                            Toast.makeText(OrderProcess.this, "Pilih hanya satu servis untuk Baju!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if (!regulerSatuan.isChecked() && !regulerKiloan.isChecked() && !kilatSatuan.isChecked() && !kilatKiloan.isChecked()) {
                            Toast.makeText(OrderProcess.this, "Pilih salah satu servis untuk Baju!", Toast.LENGTH_SHORT).show();
                            return;
                        }//else cek inputKg dan inputKg1
                        else {
                            if (!inputKg1.getText().toString().trim().isEmpty()){
                                if (regulerSatuan.isChecked()){
//                                    Toast.makeText(OrderProcess.this, "insert Reguler; Satuan", Toast.LENGTH_SHORT).show();
                                    databaseReferenceT.child(trId).child("services").setValue("Reguler, satuan");
                                    databaseReferenceT.child(trId).child("jumlah").setValue(inputKg1.getText().toString().trim());
                                }
                                else if (regulerKiloan.isChecked()){
//                                    Toast.makeText(OrderProcess.this, "insert Reguler; Kiloan", Toast.LENGTH_SHORT).show();
                                    databaseReferenceT.child(trId).child("services").setValue("Reguler, kiloan");
                                    databaseReferenceT.child(trId).child("berat").setValue(inputKg1.getText().toString().trim());
                                }
                            }
                            if (!inputKg.getText().toString().trim().isEmpty()){
                                if (kilatSatuan.isChecked()){
//                                    Toast.makeText(OrderProcess.this, "insert Kilat; Satuan", Toast.LENGTH_SHORT).show();
                                    databaseReferenceT.child(trId).child("services").setValue("Kilat, satuan");
                                    databaseReferenceT.child(trId).child("jumlah").setValue(inputKg.getText().toString().trim());

                                }
                                else if (kilatKiloan.isChecked()){
//                                    Toast.makeText(OrderProcess.this, "insert Kilat; Kiloan", Toast.LENGTH_SHORT).show();
                                    databaseReferenceT.child(trId).child("services").setValue("Kilat, kiloan");
                                    databaseReferenceT.child(trId).child("berat").setValue(inputKg.getText().toString().trim());
                                }
                            }
                            databaseReferenceT.child(trId).child("category").setValue("Baju");
//                            Toast.makeText(OrderProcess.this, "insert Baju", Toast.LENGTH_SHORT).show();

                            if (catSepatu.isChecked()) {
                                if (!regulerPair.isChecked() && !kilatPair.isChecked()) {
                                    Toast.makeText(OrderProcess.this, "Pilih hanya satu servis untuk Pair!", Toast.LENGTH_SHORT).show();
                                    return;
                                }//else cek inputPair dan inputPair1
                                else {
                                    if (!inputPair1.getText().toString().trim().isEmpty()){
                                        if (regulerPair.isChecked()){
//                                            Toast.makeText(OrderProcess.this, "insert Pair = " + inputPair1.getText(), Toast.LENGTH_SHORT).show();
                                            databaseReferenceT.child(trId).child("services").setValue("Reguler, pair");
                                            databaseReferenceT.child(trId).child("pair").setValue(inputPair1.getText().toString().trim());
                                        }
                                    }
                                    if (!inputPair.getText().toString().trim().isEmpty()){
                                        if (kilatPair.isChecked()){
//                                            Toast.makeText(OrderProcess.this, "insert Pair = " + inputPair.getText(), Toast.LENGTH_SHORT).show();
                                            databaseReferenceT.child(trId).child("services").setValue("Kilat, pair");
                                            databaseReferenceT.child(trId).child("pair").setValue(inputPair.getText().toString().trim());
                                        }
                                    }
//                                    Toast.makeText(OrderProcess.this, "insert Baju; Sepatu", Toast.LENGTH_SHORT).show();
                                    databaseReferenceT.child(trId).child("category").setValue("Baju, sepatu");
                                }
                            }

                            if (catOthers.isChecked()) {
                                //else cek inputKg dan inputKg1
//                                Toast.makeText(OrderProcess.this, "insert Baju; Others", Toast.LENGTH_SHORT).show();
                                databaseReferenceT.child(trId).child("category").setValue("Baju, others");
                            }

                            if (catSepatu.isChecked() && catOthers.isChecked()){
                                if (!regulerPair.isChecked() && !kilatPair.isChecked()) {
                                    Toast.makeText(OrderProcess.this, "Pilih hanya satu servis untuk Pair!", Toast.LENGTH_SHORT).show();
                                    return;
                                }//else cek inputPair dan inputPair1
                                else {
                                    if (!inputPair1.getText().toString().trim().isEmpty()){
                                        if (regulerPair.isChecked()){
//                                            Toast.makeText(OrderProcess.this, "insert Pair = " + inputPair1.getText(), Toast.LENGTH_SHORT).show();
                                            databaseReferenceT.child(trId).child("services").setValue("Reguler, pair");
                                            databaseReferenceT.child(trId).child("pair").setValue(inputPair1.getText().toString().trim());
                                        }
                                    }
                                    if (!inputPair.getText().toString().trim().isEmpty()){
                                        if (kilatPair.isChecked()){
//                                            Toast.makeText(OrderProcess.this, "insert Pair = " + inputPair.getText(), Toast.LENGTH_SHORT).show();
                                            databaseReferenceT.child(trId).child("services").setValue("Kilat, pair");
                                            databaseReferenceT.child(trId).child("pair").setValue(inputPair.getText().toString().trim());
                                        }
                                    }
//                                    Toast.makeText(OrderProcess.this, "insert Baju; Sepatu; Others", Toast.LENGTH_SHORT).show();
                                    databaseReferenceT.child(trId).child("category").setValue("Baju, sepatu, others");
                                }
                            }
//                            return;
                            databaseReferenceT.child(trId).child("isPickup").setValue("No");
                            databaseReferenceT.child(trId).child("status").setValue("0");
                            databaseReferenceT.child(trId).child("id").setValue(trId);
                            databaseReferenceT.child(trId).child("harga").setValue(txtHarga.getText().toString());
                        }
                    }
                    else if (catSepatu.isChecked()) {
                        if (!regulerPair.isChecked() && !kilatPair.isChecked()) {
                            Toast.makeText(OrderProcess.this, "Pilih hanya satu servis untuk Pair!", Toast.LENGTH_SHORT).show();
                            return;
                        }//else cek inputPair dan inputPair1
                        else {
                            if (!inputPair1.getText().toString().trim().isEmpty()){
                                if (regulerPair.isChecked()){
//                                    Toast.makeText(OrderProcess.this, "insert Reguler", Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(OrderProcess.this, "insert Pair = " + inputPair1.getText(), Toast.LENGTH_SHORT).show();
                                    databaseReferenceT.child(trId).child("services").setValue("Reguler, pair");
                                    databaseReferenceT.child(trId).child("pair").setValue(inputPair1.getText().toString().trim());
                                }
                            }
                            if (!inputPair.getText().toString().trim().isEmpty()){
                                if (kilatPair.isChecked()){
//                                    Toast.makeText(OrderProcess.this, "insert Kilat", Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(OrderProcess.this, "insert Pair = " + inputPair.getText(), Toast.LENGTH_SHORT).show();
                                    databaseReferenceT.child(trId).child("services").setValue("Kilat, pair");
                                    databaseReferenceT.child(trId).child("pair").setValue(inputPair.getText().toString().trim());
                                }
                            }
                            databaseReferenceT.child(trId).child("category").setValue("Sepatu");
//                            Toast.makeText(OrderProcess.this, "insert Sepatu", Toast.LENGTH_SHORT).show();

                            if (catOthers.isChecked()) {
                                if (!regulerSatuan.isChecked() && !regulerKiloan.isChecked() && !kilatSatuan.isChecked() && !kilatKiloan.isChecked()) {
                                    Toast.makeText(OrderProcess.this, "Pilih salah satu servis untuk Others!", Toast.LENGTH_SHORT).show();
                                    return;
                                }//else cek inputKg dan inputKg1
                                else {
                                    if (!inputKg1.getText().toString().trim().isEmpty()) {
                                        if (regulerSatuan.isChecked()) {
//                                            Toast.makeText(OrderProcess.this, "insert Reguler; Satuan", Toast.LENGTH_SHORT).show();
                                            databaseReferenceT.child(trId).child("services").setValue("Reguler, satuan");
                                            databaseReferenceT.child(trId).child("jumlah").setValue(inputKg1.getText().toString().trim());
                                        } else if (regulerKiloan.isChecked()) {
//                                            Toast.makeText(OrderProcess.this, "insert Reguler; Kiloan", Toast.LENGTH_SHORT).show();
                                            databaseReferenceT.child(trId).child("services").setValue("Reguler, kiloan");
                                            databaseReferenceT.child(trId).child("berat").setValue(inputKg1.getText().toString().trim());
                                        }
                                    }
                                    if (!inputKg.getText().toString().trim().isEmpty()) {
                                        if (kilatSatuan.isChecked()) {
//                                            Toast.makeText(OrderProcess.this, "insert Kilat; Satuan", Toast.LENGTH_SHORT).show();
                                            databaseReferenceT.child(trId).child("services").setValue("Kilat, satuan");
                                            databaseReferenceT.child(trId).child("jumlah").setValue(inputKg.getText().toString().trim());
                                        } else if (kilatKiloan.isChecked()) {
//                                            Toast.makeText(OrderProcess.this, "insert Kilat; Kiloan", Toast.LENGTH_SHORT).show();
                                            databaseReferenceT.child(trId).child("services").setValue("Kilat, kiloan");
                                            databaseReferenceT.child(trId).child("berat").setValue(inputKg.getText().toString().trim());
                                        }
                                    }
                                    databaseReferenceT.child(trId).child("category").setValue("Sepatu, others");
//                                    Toast.makeText(OrderProcess.this, "insert Sepatu; Others", Toast.LENGTH_SHORT).show();
                                }
                            }
//                            return;
                            databaseReferenceT.child(trId).child("isPickup").setValue("No");
                            databaseReferenceT.child(trId).child("status").setValue("0");
                            databaseReferenceT.child(trId).child("id").setValue(trId);
                            databaseReferenceT.child(trId).child("harga").setValue(txtHarga.getText().toString());
                        }
                    }
                    else if (catOthers.isChecked()) {
                        if (!regulerSatuan.isChecked() && !regulerKiloan.isChecked() && !kilatSatuan.isChecked() && !kilatKiloan.isChecked()) {
                            Toast.makeText(OrderProcess.this, "Pilih salah satu servis untuk Others!", Toast.LENGTH_SHORT).show();
                            return;
                        }//else cek inputKg dan inputKg1
                        else {
                            if (!inputKg1.getText().toString().trim().isEmpty()) {
                                if (regulerSatuan.isChecked()) {
//                                    Toast.makeText(OrderProcess.this, "insert Reguler; Satuan", Toast.LENGTH_SHORT).show();
                                    databaseReferenceT.child(trId).child("services").setValue("Reguler, satuan");
                                    databaseReferenceT.child(trId).child("jumlah").setValue(inputKg1.getText().toString().trim());
                                } else if (regulerKiloan.isChecked()) {
//                                    Toast.makeText(OrderProcess.this, "insert Reguler; Kiloan", Toast.LENGTH_SHORT).show();
                                    databaseReferenceT.child(trId).child("services").setValue("Reguler, kiloan");
                                    databaseReferenceT.child(trId).child("berat").setValue(inputKg1.getText().toString().trim());
                                }
                            }
                            if (!inputKg.getText().toString().trim().isEmpty()) {
                                if (kilatSatuan.isChecked()) {
//                                    Toast.makeText(OrderProcess.this, "insert Kilat; Satuan", Toast.LENGTH_SHORT).show();
                                    databaseReferenceT.child(trId).child("services").setValue("Kilat, satuan");
                                    databaseReferenceT.child(trId).child("jumlah").setValue(inputKg.getText().toString().trim());
                                } else if (kilatKiloan.isChecked()) {
//                                    Toast.makeText(OrderProcess.this, "insert Kilat; Kiloan", Toast.LENGTH_SHORT).show();
                                    databaseReferenceT.child(trId).child("services").setValue("Kilat, kiloan");
                                    databaseReferenceT.child(trId).child("berat").setValue(inputKg.getText().toString().trim());
                                }
                            }
                            databaseReferenceT.child(trId).child("category").setValue("Others");
//                            Toast.makeText(OrderProcess.this, "insert Others", Toast.LENGTH_SHORT).show();
                        }
//                        return;
                        databaseReferenceT.child(trId).child("isPickup").setValue("No");
                        databaseReferenceT.child(trId).child("status").setValue("0");
                        databaseReferenceT.child(trId).child("id").setValue(trId);
                        databaseReferenceT.child(trId).child("harga").setValue(txtHarga.getText().toString());
                    }
                }
                else {
                    Toast.makeText(OrderProcess.this, "Harus memilih minimal 1 kategori", Toast.LENGTH_SHORT).show();
                    if (!catBaju.isChecked()) {
                        catBaju.requestFocus();
                        return;
                    }
                    if (!catSepatu.isChecked()) {
                        catSepatu.requestFocus();
                        return;
                    }
                    if (!catOthers.isChecked()) {
                        catOthers.requestFocus();
                        return;
                    }
                }
                //endregion

                //region order process with pickup
                if (dropdown.getSelectedItem().toString().equals("Yes")){
                    databaseReferenceT.child(trId).child("isPickup").setValue("Yes");
                    databaseReferenceT.child(trId).child("Tanggal pickup").setValue(editTanggal.getText().toString());
                    databaseReferenceT.child(trId).child("Jam pickup").setValue(editJam.getText().toString());
                    databaseReferenceT.child(trId).child("address").setValue(editAlamat.getText().toString());
                }
                //endregion

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            User user = dataSnapshot.getValue(User.class);
                            if(namaLaundry.equals(user.nama)){
                                url = Uri.parse(user.imageUrl);
                                databaseReferenceT.child(trId).child("imageUrl").setValue(url.toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                databaseReferenceT.child(trId).child("namaLaundry").setValue(namaLaundry);
                databaseReferenceT.child(trId).child("namaUser").setValue(namaUser);
                databaseReference.child(userId).child("transactions").child(trId).setValue(trId);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            if(dataSnapshot.child("nama").getValue().toString().equals(namaLaundry)){
                                laundryId = dataSnapshot.getKey();
                                databaseReference.child(laundryId).child("transactions").child(trId).setValue(trId);
                                Toast.makeText(OrderProcess.this, "Transaksi sukses, silahkan menunggu respon Laundry", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        //endregion
    }

    private  TextWatcher inputKgWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String inputKgReguler = inputKg1.getText().toString().trim();
            String inputKgKilat = inputKg.getText().toString().trim();
            String inputPairKilat = inputPair.getText().toString().trim();
            String inputPairReguler = inputPair1.getText().toString().trim();
            inputPair.setEnabled(inputPairReguler.isEmpty());
            inputPair1.setEnabled(inputPairKilat.isEmpty());
            inputKg.setEnabled(inputKgReguler.isEmpty());
            inputKg1.setEnabled(inputKgKilat.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    //region method expand
    public void expand(View view) {
        int v = (radioGrp.getVisibility() == View.GONE)?View.VISIBLE: View.GONE;
        int w = (radioGrpPair.getVisibility() == View.GONE)?View.VISIBLE: View.GONE;
        if (radioGrp.getVisibility()==View.VISIBLE){
            if (catBaju.isChecked() || catOthers.isChecked()){
                regulerSatuan.setEnabled(true);
                regulerKiloan.setEnabled(true);
            }
            if (catSepatu.isChecked()) {
                regulerPair.setEnabled(true);
            }
            inputKg.setText("");
            inputKg.setVisibility(View.GONE);
            radioGrp.clearCheck();
            check = 0;
        }
        if (radioGrpPair.getVisibility()==View.VISIBLE){
            if (catSepatu.isChecked()){
                regulerPair.setEnabled(true);
            }
            if (catBaju.isChecked() || catOthers.isChecked()){
                regulerSatuan.setEnabled(true);
                regulerKiloan.setEnabled(true);
            }
            inputPair.setText("");
            inputPair.setVisibility(View.GONE);
            radioGrpPair.clearCheck();
            checkPair = 0;
        }

        TransitionManager.beginDelayedTransition(layoutKilat, new AutoTransition());
        radioGrp.setVisibility(v);
        radioGrpPair.setVisibility(w);
    }

    public void expand1(View view) {
        int v = (radioGrp1.getVisibility() == View.GONE)?View.VISIBLE: View.GONE;
        int w = (radioGrpPair1.getVisibility() == View.GONE)?View.VISIBLE: View.GONE;
        if (radioGrp1.getVisibility()==View.VISIBLE){
            if (catBaju.isChecked() || catOthers.isChecked()){
                kilatSatuan.setEnabled(true);
                kilatKiloan.setEnabled(true);
            }
            if (catSepatu.isChecked()) {
                kilatPair.setEnabled(true);
            }
            inputKg1.setText("");
            inputKg1.setVisibility(View.GONE);
            radioGrp1.clearCheck();
            check1 = 0;
        }

        if (radioGrpPair1.getVisibility()==View.VISIBLE){
            if (catSepatu.isChecked()){
                kilatPair.setEnabled(true);
            }
            if (catBaju.isChecked() || catOthers.isChecked()){
                kilatSatuan.setEnabled(true);
                kilatKiloan.setEnabled(true);
            }
            inputPair1.setText("");
            inputPair1.setVisibility(View.GONE);
            radioGrpPair1.clearCheck();
            checkPair1 = 0;
        }

        TransitionManager.beginDelayedTransition(layoutReguler, new AutoTransition());
        radioGrp1.setVisibility(v);
        radioGrpPair1.setVisibility(w);
    }

    public void checkButton(View view) {
        if (check==0){
            int x = (inputKg.getVisibility() == View.GONE)?View.VISIBLE: View.GONE;
            if (kilatKiloan.isChecked()) {
                inputKg.setHint("masukkan perkiraan berat (kg)");
            }
            if (kilatSatuan.isChecked()) {
                inputKg.setHint("masukkan jumlah dalam satuan");
            }
            inputKg.setVisibility(x);
            regulerSatuan.setEnabled(false);
            regulerPair.setEnabled(false);
            regulerKiloan.setEnabled(false);
            TransitionManager.beginDelayedTransition(layoutKilat, new AutoTransition());
            check+=1;
        }
    }

    public void checkButton1(View view) {
        if (check1==0){
            int x = (inputKg1.getVisibility() == View.GONE)?View.VISIBLE: View.GONE;
            if (regulerKiloan.isChecked()) {
                inputKg1.setHint("Input estimated kg here");
            }
            if (regulerSatuan.isChecked()) {
                inputKg1.setHint("Input estimated pcs here");
            }
            inputKg1.setVisibility(x);
            kilatSatuan.setEnabled(false);
            kilatKiloan.setEnabled(false);
            kilatPair.setEnabled(false);
            TransitionManager.beginDelayedTransition(layoutReguler, new AutoTransition());
            check1+=1;
        }
    }

    public void checkButtonPair1(View view) {
        if (checkPair1==0){
            int x = (inputPair1.getVisibility() == View.GONE)?View.VISIBLE: View.GONE;
            inputPair1.setVisibility(x);
            kilatSatuan.setEnabled(false);
            kilatKiloan.setEnabled(false);
            kilatPair.setEnabled(false);
            TransitionManager.beginDelayedTransition(layoutReguler, new AutoTransition());
            checkPair1+=1;
        }
    }

    public void checkButtonPair(View view) {
        if (checkPair==0){
            int x = (inputPair.getVisibility() == View.GONE)?View.VISIBLE: View.GONE;
            inputPair.setVisibility(x);
            regulerSatuan.setEnabled(false);
            regulerPair.setEnabled(false);
            regulerKiloan.setEnabled(false);
            TransitionManager.beginDelayedTransition(layoutReguler, new AutoTransition());
            checkPair+=1;
        }
    }
    //endregion

    //region method checkSepatu
    public void checkSepatu(View view) {
        kilatPair.setEnabled(false);
        regulerPair.setEnabled(false);
        if (catSepatu.isChecked()==true){
            kilatPair.setEnabled(true);
            regulerPair.setEnabled(true);
            if (regulerPair.isChecked() || regulerSatuan.isChecked() || regulerKiloan.isChecked()) {
                kilatPair.setEnabled(false);
            }
            if (kilatPair.isChecked() || kilatSatuan.isChecked() || kilatKiloan.isChecked()) {
                regulerPair.setEnabled(false);
            }
        } else if (catSepatu.isChecked()==false) {
            if (radioGrpPair.getVisibility() == View.VISIBLE) {
                inputPair.setText("");
                inputPair.setVisibility(View.GONE);
                radioGrpPair.clearCheck();
                checkPair = 0;
            }
            if (radioGrpPair1.getVisibility() == View.VISIBLE) {
                inputPair1.setText("");
                inputPair1.setVisibility(View.GONE);
                radioGrpPair1.clearCheck();
                checkPair1 = 0;
            }
            kilatPair.setEnabled(false);
            regulerPair.setEnabled(false);
        }
    }
    //endregion

    //region method checkBaju dan checkOthers
    public void checkRadioBtn(View view) {
        regulerKiloan.setEnabled(false);
        regulerSatuan.setEnabled(false);
        kilatSatuan.setEnabled(false);
        kilatKiloan.setEnabled(false);
        if (catBaju.isChecked()==true || catOthers.isChecked()==true){
            regulerKiloan.setEnabled(true);
            regulerSatuan.setEnabled(true);
            kilatSatuan.setEnabled(true);
            kilatKiloan.setEnabled(true);
            if (regulerSatuan.isChecked() || regulerKiloan.isChecked() || regulerPair.isChecked()){
                kilatSatuan.setEnabled(false);
                kilatKiloan.setEnabled(false);
            }
            if (kilatSatuan.isChecked() || kilatKiloan.isChecked() || kilatPair.isChecked()){
                regulerKiloan.setEnabled(false);
                regulerSatuan.setEnabled(false);
            }
//            regulerKiloan.setEnabled(true);
//            regulerSatuan.setEnabled(true);
//            kilatSatuan.setEnabled(true);
//            kilatKiloan.setEnabled(true);
        } else if (catBaju.isChecked()==false && catOthers.isChecked()==false){
            if (radioGrp.getVisibility()==View.VISIBLE){
                inputKg.setText("");
                inputKg.setVisibility(View.GONE);
                radioGrp.clearCheck();
                check = 0;
            }
            if (radioGrp1.getVisibility()==View.VISIBLE){
                inputKg1.setText("");
                inputKg1.setVisibility(View.GONE);
                radioGrp1.clearCheck();
                check1 = 0;
            }
            regulerKiloan.setEnabled(false);
            regulerSatuan.setEnabled(false);
            kilatSatuan.setEnabled(false);
            kilatKiloan.setEnabled(false);
        }
    }
    //endregion

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