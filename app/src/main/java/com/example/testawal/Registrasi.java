package com.example.testawal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registrasi extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    LinearLayout parentLayout;
    FrameLayout frameLayout;

    private EditText editrgsfullName, editrgsEmail, editrgsTelp, editrgsPassword, editrgsConfirm;
    private Button btnRegister;
    private DatabaseReference databaseReference;
    private Spinner spinner;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        getSupportActionBar().setTitle("Registrasi");

        //region spinner
        Spinner dropdown = findViewById(R.id.fidgetSpinner);
        //create a list of items for the spinner.
        String[] items = new String[]{"user", "laundry"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        //endregion

        editrgsfullName = findViewById(R.id.editrgsfullName);
        editrgsEmail = findViewById(R.id.editrgsEmail);
        editrgsTelp = findViewById(R.id.editrgsTelp);
        editrgsPassword = findViewById(R.id.editrgsPassword);
        editrgsConfirm = findViewById(R.id.editrgsConfirm);
        btnRegister = findViewById(R.id.btnRegister);
        spinner = findViewById(R.id.fidgetSpinner);
        parentLayout = findViewById(R.id.parentLayout);
        frameLayout = findViewById(R.id.frameLayout);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        final Drawable belakangregistrasidark = ContextCompat.getDrawable(this, R.drawable.belakangregistrasidark);
        final int bluex = ContextCompat.getColor(this, R.color.bluex);
        final int lightbluex = ContextCompat.getColor(this, R.color.lightbluex);
        final int white = ContextCompat.getColor(this, R.color.white);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#083444"));

        if (sharedPreferences.getBoolean("dark_mode", true)) {
            parentLayout.setBackgroundColor(bluex);
            frameLayout.setBackground(belakangregistrasidark);
            btnRegister.setBackgroundColor(lightbluex);
            btnRegister.setTextColor(white);
            getSupportActionBar().setBackgroundDrawable(colorDrawable);
        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String fname = editrgsfullName.getText().toString();
//                String email = editrgsEmail.getText().toString();
//                String phone = editrgsTelp.getText().toString();
//                String pass = editrgsPassword.getText().toString();
//                String confpass = editrgsConfirm.getText().toString();
//                String role = spinner.getSelectedItem().toString();
                insertDataUser();
//                if(role == "user"){
//                    insertDataUser();
//                }else if(role == "laundry"){
//                    insertDataLaundry();
//                }
            }
        });
    }

    private void insertDataUser() {
//        String name = editrgsfullName.getText().toString();
//        String email = editrgsEmail.getText().toString();
//        String pass = editrgsPassword.getText().toString();
//        String emails = email.replace(".","");
//
//        User users = new User(name, emails, pass);
//        databaseReference.child("member").child(emails).setValue(users);
//        Toast.makeText(Registrasi.this, "sukses insert data user", Toast.LENGTH_SHORT).show();
        String email = editrgsEmail.getText().toString().trim();
        String password = editrgsPassword.getText().toString().trim();
        String cpassword = editrgsConfirm.getText().toString().trim();
        String name = editrgsfullName.getText().toString().trim();
        String role = spinner.getSelectedItem().toString();
        String phone = editrgsTelp.getText().toString().trim();

        //region validasi input
        if(email.isEmpty()){
            editrgsEmail.setError("Email harus diisi");
            editrgsEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editrgsEmail.setError("Format email salah");
            editrgsEmail.requestFocus();
            return;
        }
        if(name.isEmpty()){
            editrgsfullName.setError("Nama harus diisi");
            editrgsfullName.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editrgsPassword.setError("Password harus diisi");
            editrgsPassword.requestFocus();
            return;
        }
        if(cpassword.isEmpty()){
            editrgsConfirm.setError("Konfirmasi password harus diisi");
            editrgsConfirm.requestFocus();
            return;
        }
        if(!cpassword.isEmpty()){
            if(!cpassword.equals(password)){
                editrgsConfirm.setError("Input password tidak sama");
                editrgsConfirm.requestFocus();
                return;
            }
        }
        //endregion

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(name, email, password, role, phone, null, null, null, null, null, null, null, null, null);
                    FirebaseDatabase.getInstance().getReference().child("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Registrasi.this, "sukses insert", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }else{
                                Toast.makeText(Registrasi.this, "gagal insert", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                    startActivity(new Intent(Registrasi.this, MainActivity.class));
                }else{
                    Toast.makeText(Registrasi.this, "gagal proses", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

//    private void insertDataLaundry() {
//        String name = editrgsfullName.getText().toString();
//        String email = editrgsEmail.getText().toString();
//        String pass = editrgsPassword.getText().toString();
//        String emails = email.replace(".","");
//
//        Laundry laundries = new Laundry(name, pass, null);
//        databaseReference.child("laundry").child(emails).setValue(laundries);
//        Toast.makeText(Registrasi.this, "sukses insert data laundry", Toast.LENGTH_SHORT).show();
//    }

}