package com.example.testawal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import javax.xml.transform.sax.SAXResult;

public class MainActivity extends AppCompatActivity {

    EditText txtEmail, txtPassword;
    Button btnLogin;
    TextView registernow;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtEmail = findViewById(R.id.editEmail);
        txtPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        registernow = findViewById(R.id.registernow);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        Toast.makeText(MainActivity.this, databaseReference.toString(), Toast.LENGTH_SHORT).show();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("loginv2").child("member").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String input1 = txtEmail.getText().toString();
                        String input2 = txtPassword.getText().toString();
                        String usernames = input1.replace(".","");
                        if (dataSnapshot.child(usernames).exists()) {
                            if (dataSnapshot.child(usernames).child("password").getValue(String.class).equals(input2)) {
                                Toast.makeText(MainActivity.this, "akun ada", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            } else {
                                Toast.makeText(MainActivity.this, "Kata sandi salah", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Data belum terdaftar", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                databaseReference.child("loginv2").child("laundry").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String input1 = txtEmail.getText().toString();
                        String input2 = txtPassword.getText().toString();
                        String usernames = input1.replace(".","");
                        if (dataSnapshot.child(usernames).exists()) {
                            if (dataSnapshot.child(usernames).child("password").getValue(String.class).equals(input2)) {
                                Toast.makeText(MainActivity.this, "akun ada", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            } else {
                                Toast.makeText(MainActivity.this, "Kata sandi salah", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Data belum terdaftar", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        registernow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Registrasi.class));
            }
        });

    }
}