package com.example.testawal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.LayoutTransition;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

    Button btnOrder;
    EditText editTanggal,  editJam;
    TextView txtNamaLaundry, txtNamaUser;
    String userId, trId, laundryId, namaUser;
    ArrayList<User> users;
    FirebaseUser fUser;
    DatabaseReference databaseReference, databaseReferenceT;
    private FirebaseAuth mAuth;

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

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_order_process);
//
//        Bundle b = getIntent().getExtras();
//        String nama1 = (String) b.get("nama1");
//
//
//        txtNamaUser = findViewById(R.id.txtNamaUser);
//        txtNamaLaundry = findViewById(R.id.txtNamaLaundry);
//        editTanggal = findViewById(R.id.editTanggal);
//        editJam = findViewById(R.id.editJam);
//        btnOrder = findViewById(R.id.btnOrder);
//
//        mAuth = FirebaseAuth.getInstance();
//
//        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
//        databaseReferenceT = FirebaseDatabase.getInstance().getReference("Transactions");
//        fUser = FirebaseAuth.getInstance().getCurrentUser();
//        userId = fUser.getUid();
//
//        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                User user = snapshot.getValue(User.class);
//                if(user != null){
//                    txtNamaUser.setText("Nama pengguna: " + user.nama);
//                    txtNamaLaundry.setText("Nama Laundry: " + nama1);
//                    namaUser = user.nama;
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        editJam.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Calendar calendar = Calendar.getInstance();
//                jam = calendar.get(Calendar.HOUR_OF_DAY);
//                menit = calendar.get(Calendar.MINUTE);
//
//                TimePickerDialog timedialog;
//                timedialog = new TimePickerDialog(OrderProcess.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
//                        jam = hour;
//                        menit = minute;
//
//                        if (jam <= 12) {
//                            editJam.setText(String.format(Locale.getDefault(), "%d:%d am", jam, menit));
//                        }
//                        else {
//                            editJam.setText(String.format(Locale.getDefault(), "%d:%d pm", jam, menit));
//                        }
//                    }
//                }, jam, menit, true);
//                timedialog.show();
//            }
//        });
//
//        editTanggal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Calendar calendar = Calendar.getInstance();
//                tanggal = calendar.get(Calendar.DAY_OF_MONTH);
//                bulan = calendar.get(Calendar.MONTH);
//                tahun = calendar.get(Calendar.YEAR);
//
//                DatePickerDialog datedialog;
//                datedialog = new DatePickerDialog(OrderProcess.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker datePicker, int date, int month, int year) {
//                        tanggal = date;
//                        bulan = month;
//                        tahun = year;
//
//                        editTanggal.setText(tahun + "/" + bulan + "/" + tanggal);
//                    }
//                }, tahun, bulan, tanggal);
//                datedialog.show();
//            }
//        });
//
//        btnOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                trId = databaseReferenceT.push().getKey();
//                databaseReferenceT.child(trId).child("harga").setValue("25000");
//                databaseReferenceT.child(trId).child("services").setValue("cuci kilat");
//                databaseReferenceT.child(trId).child("namaLaundry").setValue(nama1);
//                databaseReferenceT.child(trId).child("namaUser").setValue(namaUser);
//                databaseReference.child(userId).child("transactions").child(trId).setValue(trId);
//                Toast.makeText(OrderProcess.this, "id generated = " + trId, Toast.LENGTH_LONG).show();
//
////              dbReference refer ke semua userId, trId selalu unique
//                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                            if(dataSnapshot.child("nama").getValue().toString().equals(nama1)){
//                                laundryId = dataSnapshot.getKey();
//                                databaseReference.child(laundryId).child("transaction").child(trId).setValue(trId);
//                                break;
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        });
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_process);

        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReferenceT = FirebaseDatabase.getInstance().getReference("Transactions");
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = fUser.getUid();

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
        kilatKiloan = findViewById(R.id.kilatKiloan);
        kilatPair = findViewById(R.id.kilatPair);
        regulerSatuan = findViewById(R.id.regulerSatuan);
        regulerKiloan = findViewById(R.id.regulerKiloan);
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
        //endregion

    }

    //region method expand
    public void expand(View view) {
        int v = (radioGrp.getVisibility() == View.GONE)?View.VISIBLE: View.GONE;
        int w = (radioGrpPair.getVisibility() == View.GONE)?View.VISIBLE: View.GONE;
        if (radioGrp.getVisibility()==View.VISIBLE){
            inputKg.setVisibility(View.GONE);
            radioGrp.clearCheck();
            check = 0;
        }
        if (radioGrpPair.getVisibility()==View.VISIBLE){
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
            inputKg1.setVisibility(View.GONE);
            radioGrp1.clearCheck();
            check1 = 0;
        }
        if (radioGrpPair1.getVisibility()==View.VISIBLE){
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
            inputKg.setVisibility(x);
            TransitionManager.beginDelayedTransition(layoutKilat, new AutoTransition());
            check+=1;
        }
    }

    public void checkButton1(View view) {
        if (check1==0){
            int x = (inputKg1.getVisibility() == View.GONE)?View.VISIBLE: View.GONE;
            inputKg1.setVisibility(x);
            TransitionManager.beginDelayedTransition(layoutReguler, new AutoTransition());
            check1+=1;
        }
    }

    public void checkButtonPair1(View view) {
        if (checkPair1==0){
            int x = (inputPair1.getVisibility() == View.GONE)?View.VISIBLE: View.GONE;
            inputPair1.setVisibility(x);
            TransitionManager.beginDelayedTransition(layoutReguler, new AutoTransition());
            checkPair1+=1;
        }
    }

    public void checkButtonPair(View view) {
        if (checkPair==0){
            int x = (inputPair.getVisibility() == View.GONE)?View.VISIBLE: View.GONE;
            inputPair.setVisibility(x);
            TransitionManager.beginDelayedTransition(layoutReguler, new AutoTransition());
            checkPair+=1;
        }
    }
    //endregion

    //method checkSepatu
    public void checkSepatu(View view) {
        kilatPair.setEnabled(false);
        regulerPair.setEnabled(false);
        if (catSepatu.isChecked()==true){
            kilatPair.setEnabled(true);
            regulerPair.setEnabled(true);
        }
    }

}


