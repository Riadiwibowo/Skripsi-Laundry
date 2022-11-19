package com.example.testawal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
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
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

//    DrawerLayout drawerLayout;
//    NavigationView navigationView;
//    ActionBarDrawerToggle toggle;
//    ImageSlider imageSlider;
//    ViewFlipper viewFlipper;
//    TextView txtSeeAll, txtNamaUser;
//    FirebaseUser fUser;
//    String userId;
//    //recyclerView
//    RecyclerView rv;
//    DatabaseReference databaseReference;
//    StorageReference storageReference;
//    MyAdapter myAdapter;
//    ArrayList<User> users;
//    Integer idx=0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
//
//        getSupportActionBar().setTitle("Home Page");
//
//        txtNamaUser = findViewById(R.id.txtNamaUser);
//        txtSeeAll = findViewById(R.id.txtSeeAll);
//        drawerLayout = findViewById(R.id.home_layout);
//        navigationView = findViewById(R.id.nav_home);
//        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.catBaju:
//                        Toast.makeText(HomeActivity.this, "Category Baju", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.catSepatu:
//                        Toast.makeText(HomeActivity.this, "Category Sepatu", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.catOther:
//                        Toast.makeText(HomeActivity.this, "Category Others", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.menuAbout:
//                        startActivity(new Intent(HomeActivity.this, AboutUs.class));
//                        break;
//                }
//                return false;
//            }
//        });
//
////        sliderViewFlipper
////        int images[] = {R.drawable.twitter, R.drawable.google, R.drawable.facebook};
////        viewFlipper = findViewById(R.id.vFlipper);
////        for(int i=0;i<images.length;i++){
////            flipperChanges(images[i]);
////        }
////        for(int imageIdx:images){
////            flipperChanges(imageIdx);
////        }
//
//        //region slider
//        imageSlider = findViewById(R.id.imgSlider);
//        ArrayList<SlideModel> img = new ArrayList<>();
//        img.add(new SlideModel("https://images.unsplash.com/photo-1587563871167-1ee9c731aefb?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=831&q=80", "Cara cuci sepatu dengan benar", null));
//        img.add(new SlideModel("https://images.unsplash.com/photo-1530558215369-ba361d8734f8?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=874&q=80", "Bingung cara setrika pakaian?", null));
//        img.add(new SlideModel("https://images.unsplash.com/photo-1469329989238-48310798c014?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=873&q=80", "Cuci mandiri jadi lebih mudah, asalkan dengan...", null));
//
//        imageSlider.setImageList(img, ScaleTypes.CENTER_CROP);
//        //endregion
//
//        //region recyclerView
//        rv = findViewById(R.id.recyclerLaundry);
//        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
//        storageReference = FirebaseStorage.getInstance().getReference();
//        rv.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
//        users = new ArrayList<>();
//        myAdapter = new MyAdapter(HomeActivity.this, users);
//        rv.setAdapter(myAdapter);
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            //fetch data
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    if(idx<3){
//                        if (dataSnapshot.child("role").getValue().equals("laundry")) {
//                            User user1 = dataSnapshot.getValue(User.class);
//                            users.add(user1);
//                            idx++;
//                        }
//                    }else{
//                        break;
//                    }
//
////                    String info = dataSnapshot.getValue().toString();
////                    Toast.makeText(HomeActivity.this, info, Toast.LENGTH_LONG).show();
//                }
//                myAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        //endregion
//
//        //region set user name
//        fUser = FirebaseAuth.getInstance().getCurrentUser();
//        userId = fUser.getUid();
//
//        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
//               @Override
//               public void onDataChange(@NonNull DataSnapshot snapshot) {
//                   User user = snapshot.getValue(User.class);
//                   if(user != null){
//                       txtNamaUser.setText("Hello, " + user.nama + ".\nWelcome to our laundry apps!");
//                   }
//               }
//
//               @Override
//               public void onCancelled(@NonNull DatabaseError error) {
//
//               }
//        });
//
//        //endregion
//
//        txtSeeAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(HomeActivity.this, HomeLaundryList.class));
//            }
//        });
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflates = getMenuInflater();
//        inflates.inflate(R.menu.home_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.itemLogout:
//                logout();
//                Toast.makeText(this, "logout processed", Toast.LENGTH_SHORT).show();
//                return true;
//        }
//        if(toggle.onOptionsItemSelected(item)){
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout.closeDrawer(GravityCompat.START);
//        }else{
//            super.onBackPressed();
//        }
//    }
//
//    //slider
////    public void flipperChanges(int img){
////        ImageView imageView = new ImageView(this);
////        imageView.setBackgroundResource(img);
////        viewFlipper.addView(imageView);
////        viewFlipper.setFlipInterval(2000);
////        viewFlipper.setAutoStart(true);
////        //animation
////        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
////        viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);
////    }
//
//    public void logout(){
//        FirebaseAuth.getInstance().signOut();
//        startActivity(new Intent(HomeActivity.this, MainActivity.class));
//    }

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loadFragment(new HomeFragment());

        bottomNavigationView = findViewById(R.id.nav_bottom);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment frgmnt = null;

                switch (item.getItemId()) {
                    case R.id.bottomMenuHome:
                        frgmnt = new HomeFragment();
                        break;
                    case R.id.bottomMenuTransaction:
                        frgmnt = new TransactionFragment();
                        break;
                }

                loadFragment(frgmnt);

                return true;
            }
        });
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}