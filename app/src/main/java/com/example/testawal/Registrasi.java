package com.example.testawal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registrasi extends AppCompatActivity {

    EditText editrgsfullName, editrgsEmail, editrgsTelp, editrgsPassword, editrgsConfirm;
    Button btnRegister;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        editrgsfullName = findViewById(R.id.editrgsfullName);
        editrgsEmail = findViewById(R.id.editrgsEmail);
        editrgsTelp = findViewById(R.id.editrgsTelp);
        editrgsPassword = findViewById(R.id.editrgsPassword);
        editrgsConfirm = findViewById(R.id.editrgsConfirm);
        btnRegister = findViewById(R.id.btnRegister);
        DB = new DBHelper(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = editrgsfullName.getText().toString();
                String email = editrgsEmail.getText().toString();
                String phone = editrgsTelp.getText().toString();
                String pass = editrgsPassword.getText().toString();
                String confpass = editrgsConfirm.getText().toString();

                if(TextUtils.isEmpty(fname) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(confpass))
                    Toast.makeText(Registrasi.this, "All fields required", Toast.LENGTH_SHORT).show();
                else {
                    if(pass.equals(confpass)) {
                        Boolean checkmail = DB.checkemail(email);
                        if(checkmail==false) {
                            Boolean insert = DB.insertData(fname, email, phone, pass);
                            if(insert==true) {
                                Toast.makeText(Registrasi.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Registrasi.this, MainActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(Registrasi.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(Registrasi.this, "User already exists", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(Registrasi.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
}