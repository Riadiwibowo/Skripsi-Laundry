package com.example.testawal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
    String userId, transactionid, namalaundry;
    ArrayList<User> users;
    FirebaseUser fUser;
    DatabaseReference databaseReference;

    private FirebaseAuth mAuth;

    private int jam, menit;
    private int tanggal, bulan, tahun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_process);

        Bundle b = getIntent().getExtras();
        String nama1 = (String) b.get("nama1");

        txtNamaUser = findViewById(R.id.txtNamaUser);
        txtNamaLaundry = findViewById(R.id.txtNamaLaundry);
        editTanggal = findViewById(R.id.editTanggal);
        editJam = findViewById(R.id.editJam);
        btnOrder = findViewById(R.id.btnOrder);

        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = fUser.getUid();

        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user != null){
                    txtNamaUser.setText("Nama pengguna: " + user.nama);
                    txtNamaLaundry.setText("Nama Laundry: " + nama1);
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
                        bulan = month;
                        tahun = year;

                        editTanggal.setText(tahun + "/" + bulan + "/" + tanggal);
                    }
                }, tahun, bulan, tanggal);
                datedialog.show();
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(userId).child("transaction").push().child("harga").setValue("15000");
                // Ambil transactionid (key)
                databaseReference.child(userId).child("transaction").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            if (dataSnapshot.child("harga").getValue().toString().equals("15000")) {
                                transactionid = dataSnapshot.getKey();
                                break;
                            }
                            else {
                                break;
                            }
                        }
                        Toast.makeText(OrderProcess.this, transactionid, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//                transactionid = databaseReference.child(userId).child("transaction").getKey();
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    //fetch data
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            if(dataSnapshot.child("nama").getValue().toString().equals(nama1)){
                                namalaundry = dataSnapshot.getKey();
                                databaseReference.child(namalaundry).child("transaction").child(transactionid).child("harga").setValue("15000");
                                break;
                            }else{
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Toast.makeText(OrderProcess.this, "DATA MASUK", Toast.LENGTH_SHORT).show();
            }
        });
    }
}