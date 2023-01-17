package com.example.testawal;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

public class TransactionFragment extends Fragment {

    SharedPreferences sharedPreferences;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    //recyclerView
    RecyclerView rv;
    DatabaseReference databaseReference, databaseReferenceT;
    StorageReference storageReference;
    MyAdapterTransaction myAdapter;
    ArrayList<Transaction> transactions;
    FirebaseUser fUser;
    String userId;
    Dialog dialog;
    TextView txtNo, txtYes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Daftar Transaksi");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = fUser.getUid();

        drawerLayout = view.findViewById(R.id.home_layout);
        navigationView = view.findViewById(R.id.nav_home);
        toggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());

        final int black = ContextCompat.getColor(container.getContext(), R.color.black);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#083444"));

        if (sharedPreferences.getBoolean("dark_mode", true)) {
            drawerLayout.setBackgroundColor(black);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(colorDrawable);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.catBaju:
                        startActivity(new Intent(getActivity(), CategoryBaju.class));
                        Toast.makeText(getActivity(), "Category Baju", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.catSepatu:
                        startActivity(new Intent(getActivity(), CategorySepatu.class));
                        Toast.makeText(getActivity(), "Category Sepatu", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.catOther:
                        startActivity(new Intent(getActivity(), CategoryLain.class));
                        Toast.makeText(getActivity(), "Category Others", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menuAbout:
                        startActivity(new Intent(getActivity(), AboutUs.class));
                        break;
                }
                return false;
            }
        });

        //region recyclerView
        rv = view.findViewById(R.id.recyclerTransactions);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReferenceT = FirebaseDatabase.getInstance().getReference("Transactions");
        storageReference = FirebaseStorage.getInstance().getReference();
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        transactions = new ArrayList<>();
        myAdapter = new MyAdapterTransaction(getActivity(), transactions);
        rv.setAdapter(myAdapter);

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
//                                    Toast.makeText(getActivity(), "trId = " + dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
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

        //endregion

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemLogout:
                dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.popuplogout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                txtNo = dialog.findViewById(R.id.txtNo);
                txtYes = dialog.findViewById(R.id.txtYes);
                dialog.show();
                txtNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                txtYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        logout();
                    }
                });
                return true;
            case R.id.itemProfile:
                startActivity(new Intent(getActivity(), ProfileUser.class));
                return true;
            case R.id.itemSetting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                return true;
        }
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            Toast.makeText(getActivity(), "backpressed", Toast.LENGTH_SHORT).show();
        }
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), MainActivity.class));
    }
}