package com.example.testawal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderProcess extends AppCompatActivity {

    TextView txtNamaLaundry, txtNamaUser;
    String userId;
    ArrayList<User> users;
    FirebaseUser fUser;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_process);

        Bundle b = getIntent().getExtras();
        String nama1 = (String) b.get("nama1");

        txtNamaUser = findViewById(R.id.txtNamaUser);
        txtNamaLaundry = findViewById(R.id.txtNamaLaundry);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = fUser.getUid();

        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user != null){
                    txtNamaUser.setText("Hello, " + user.nama + ".\nWelcome to our laundry apps!");
                    txtNamaLaundry.setText("Nama Laundry: " + nama1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}