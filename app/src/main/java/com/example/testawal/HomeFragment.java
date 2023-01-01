package com.example.testawal;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ActionTypes;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.interfaces.TouchListener;
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

public class HomeFragment extends Fragment {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    ImageSlider imageSlider;
    ViewFlipper viewFlipper;
    TextView txtArtikel, txtSeeAll, txtNamaUser, txtNo, txtYes;
    ImageView img1, img2, img3, img4;
    FirebaseUser fUser;
    String userId;
    //recyclerView
    RecyclerView rv;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    MyAdapter myAdapter;
    ArrayList<User> users;
    Integer idx=0;
    Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Home Page");

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        txtNamaUser = view.findViewById(R.id.txtNamaUser);
        txtArtikel = view.findViewById(R.id.txtArtikel);
        txtSeeAll = view.findViewById(R.id.txtSeeAll);
        img1 = view.findViewById(R.id.img1);
        img2 = view.findViewById(R.id.img2);
        img3 = view.findViewById(R.id.img3);
        img4 = view.findViewById(R.id.img4);
        drawerLayout = view.findViewById(R.id.home_layout);
        navigationView = view.findViewById(R.id.nav_home);
        toggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        //region slider
        imageSlider = view.findViewById(R.id.imgSlider);
        ArrayList<SlideModel> img = new ArrayList<>();
        img.add(new SlideModel("https://images.unsplash.com/photo-1587563871167-1ee9c731aefb?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=831&q=80", "Cara Cuci Sepatu dengan Benar", null));
        img.add(new SlideModel("https://images.unsplash.com/photo-1530558215369-ba361d8734f8?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=874&q=80", "Bingung Cara Setrika Pakaian?", null));
        img.add(new SlideModel("https://images.unsplash.com/photo-1469329989238-48310798c014?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=873&q=80", "6 Keuntungan Menggunakan Jasa Laundry", null));

        imageSlider.setImageList(img, ScaleTypes.CENTER_CROP);

        //region artikel clicked
        imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {
                if (img.get(i).getTitle().toString().equals("Cara Cuci Sepatu dengan Benar")) {
                    startActivity(new Intent(getActivity(), ArtikelSatu.class));
                }
                if (img.get(i).getTitle().toString().equals("Bingung Cara Setrika Pakaian?")) {
                    startActivity(new Intent(getActivity(), ArtikelDua.class));
                }
                if (img.get(i).getTitle().toString().equals("6 Keuntungan Menggunakan Jasa Laundry")) {
                    startActivity(new Intent(getActivity(), ArtikelTiga.class));
                }
            }
        });

        txtArtikel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Pindah ke halaman artikel", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), Artikel.class));
            }
        });
        //endregion

        //endregion

        //region recyclerView
        rv = view.findViewById(R.id.recyclerLaundry);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        users = new ArrayList<>();
        myAdapter = new MyAdapter(getActivity(), users);
        rv.setAdapter(myAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            //fetch data
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(idx<3){
                        if (dataSnapshot.child("role").getValue().equals("laundry")) {
                            User user1 = dataSnapshot.getValue(User.class);
                            users.add(user1);
                            idx++;
                        }
                    }else{
                        break;
                    }

//                    String info = dataSnapshot.getValue().toString();
//                    Toast.makeText(HomeActivity.this, info, Toast.LENGTH_LONG).show();
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //endregion

        //region set user name
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = fUser.getUid();

        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user != null){
                    txtNamaUser.setText("Hello, " + user.nama + ".\nWelcome to our laundry apps!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ServiceKilat.class));
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Pickup.class));
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Satuan.class));
            }
        });

        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Kiloan.class));
            }
        });
        //endregion

        txtSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), HomeLaundryList.class));
            }
        });

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