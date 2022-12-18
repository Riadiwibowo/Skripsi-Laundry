package com.example.testawal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import javax.xml.transform.sax.SAXResult;

public class MainActivity extends AppCompatActivity {

    EditText txtEmail, txtPassword;
    Button btnLogin;
    TextView registerNow;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    FirebaseUser fUser;
    String role, uid;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Login");

        mAuth = FirebaseAuth.getInstance();
        txtEmail = findViewById(R.id.editEmail);
        txtPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        registerNow = findViewById(R.id.registernow);
        progressBar = findViewById(R.id.progressBarLogin);
        progressBar.setVisibility(View.INVISIBLE);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
//        Toast.makeText(MainActivity.this, databaseReference.toString(), Toast.LENGTH_SHORT).show();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                databaseReference.child("loginv2").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        String input1 = txtEmail.getText().toString();
//                        String input2 = txtPassword.getText().toString();
//                        String usernames = input1.replace(".","");
//                        if (dataSnapshot.child("member").child(usernames).exists()) {
//                            if (dataSnapshot.child("member").child(usernames).child("password").getValue(String.class).equals(input2)) {
//                                Toast.makeText(MainActivity.this, "akun user ada", Toast.LENGTH_SHORT).show();
//                                if(dataSnapshot.child("member").child(usernames).exists()) {
//                                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
//                                }
//                            } else {
//                                Toast.makeText(MainActivity.this, "Kata sandi salah", Toast.LENGTH_SHORT).show();
//                            }
//                        } else if (dataSnapshot.child("laundry").child(usernames).exists()) {
//                            if (dataSnapshot.child("laundry").child(usernames).child("password").getValue(String.class).equals(input2)) {
//                                Toast.makeText(MainActivity.this, "akun laundry ada", Toast.LENGTH_SHORT).show();
//                                if(dataSnapshot.child("laundry").child(usernames).exists()) {
//                                    startActivity(new Intent(MainActivity.this, LaundryHome.class));
//                                }
//                            } else {
//                                Toast.makeText(MainActivity.this, "Kata sandi salah", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Toast.makeText(MainActivity.this, "Data belum terdaftar", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//                databaseReference.child("loginv2").child("laundry").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        String input1 = txtEmail.getText().toString();
//                        String input2 = txtPassword.getText().toString();
//                        String usernames = input1.replace(".","");
//                        if (dataSnapshot.child(usernames).exists()) {
//                            if (dataSnapshot.child(usernames).child("password").getValue(String.class).equals(input2)) {
//                                Toast.makeText(MainActivity.this, "akun ada", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
//                            } else {
//                                Toast.makeText(MainActivity.this, "Kata sandi salah", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Toast.makeText(MainActivity.this, "Data belum terdaftar", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
                usersLogin();
            }
        });

        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Registrasi.class));
            }
        });
    }

    private void usersLogin(){
        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();


        //region validasi input
        if(email.isEmpty()){
            txtEmail.setError("Email harus diisi");
            txtEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtEmail.setError("Format email salah");
            txtEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            txtPassword.setError("Password harus diisi");
            txtPassword.requestFocus();
            return;
        }
        //endregion
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //region getUserData
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnap : snapshot.getChildren()){
                                if(dataSnap.child("email").getValue().equals(email)) {
//                                    Toast.makeText(MainActivity.this, "email = " + email, Toast.LENGTH_SHORT).show();
                                    role = dataSnap.child("role").getValue(String.class);
//                                    Toast.makeText(MainActivity.this, "role = " + role, Toast.LENGTH_SHORT).show();
                                    if(role.equals("user")){
                                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                    }else if(role.equals("laundry")){
                                        startActivity(new Intent(MainActivity.this, LaundryMain.class));
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    //endregion
                }else{
                    Toast.makeText(MainActivity.this, "การเข้าสู่ระบบล้มเหลว", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}