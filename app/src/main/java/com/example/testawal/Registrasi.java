package com.example.testawal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registrasi extends AppCompatActivity {

    EditText editrgsfullName, editrgsEmail, editrgsTelp, editrgsPassword, editrgsConfirm;
    Button btnRegister;
    DatabaseReference databaseReference;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        //spinner
        Spinner dropdown = findViewById(R.id.fidgetSpinner);
//create a list of items for the spinner.
        String[] items = new String[]{"user", "laundry"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        editrgsfullName = findViewById(R.id.editrgsfullName);
        editrgsEmail = findViewById(R.id.editrgsEmail);
        editrgsTelp = findViewById(R.id.editrgsTelp);
        editrgsPassword = findViewById(R.id.editrgsPassword);
        editrgsConfirm = findViewById(R.id.editrgsConfirm);
        btnRegister = findViewById(R.id.btnRegister);
        spinner = findViewById(R.id.fidgetSpinner);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("loginv2");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = editrgsfullName.getText().toString();
                String email = editrgsEmail.getText().toString();
                String phone = editrgsTelp.getText().toString();
                String pass = editrgsPassword.getText().toString();
                String confpass = editrgsConfirm.getText().toString();
                String role = spinner.getSelectedItem().toString();

                if(role == "user"){
                    insertDataUser();
                }else if(role == "laundry"){
                    insertDataLaundry();
                }
            }
        });
    }

    private void insertDataUser() {
        String name = editrgsfullName.getText().toString();
        String email = editrgsEmail.getText().toString();
        String pass = editrgsPassword.getText().toString();
        String emails = email.replace(".","");

        User users = new User(name, emails, pass);
        databaseReference.child("member").child(emails).setValue(users);
        Toast.makeText(Registrasi.this, "sukses insert data user", Toast.LENGTH_SHORT).show();
    }

    private void insertDataLaundry() {
        String name = editrgsfullName.getText().toString();
        String email = editrgsEmail.getText().toString();
        String pass = editrgsPassword.getText().toString();
        String emails = email.replace(".","");

        Laundry laundries = new Laundry(name, pass, null);
        databaseReference.child("laundry").child(emails).setValue(laundries);
        Toast.makeText(Registrasi.this, "sukses insert data laundry", Toast.LENGTH_SHORT).show();
    }

}