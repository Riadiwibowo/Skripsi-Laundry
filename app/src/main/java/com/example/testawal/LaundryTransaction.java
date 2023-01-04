package com.example.testawal;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class LaundryTransaction extends AppCompatActivity {
    //recyclerView
    RecyclerView rv;
    DatabaseReference databaseReference, databaseReferenceT;
    StorageReference storageReference;
    MyAdapterTransactionLaundry myAdapter;
    ArrayList<Transaction> transactions;
    FirebaseUser fUser;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry_transaction);

        getSupportActionBar().setTitle("Laundry Transactions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv = findViewById(R.id.recyclerTransactions);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReferenceT = FirebaseDatabase.getInstance().getReference("Transactions");
        storageReference = FirebaseStorage.getInstance().getReference();
        rv.setLayoutManager(new LinearLayoutManager(LaundryTransaction.this));
        transactions = new ArrayList<>();
        myAdapter = new MyAdapterTransactionLaundry(LaundryTransaction.this, transactions);
        rv.setAdapter(myAdapter);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = fUser.getUid();

        databaseReference.child(userId).child("transactions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Toast.makeText(getActivity(), "snapshot = " + dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();

                    databaseReferenceT.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot trSnapshot : snapshot.getChildren()) {
                                if(trSnapshot.getKey().equals(dataSnapshot.getKey())){
                                    Toast.makeText(LaundryTransaction.this, "trId = " + dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                                    Transaction transaction1 = trSnapshot.getValue(Transaction.class);
                                    transactions.add(transaction1);
                                }
                            }
                            myAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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