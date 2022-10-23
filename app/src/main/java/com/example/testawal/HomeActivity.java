package com.example.testawal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
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

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    ImageSlider imageSlider;
    ViewFlipper viewFlipper;

    //recyclerView
    RecyclerView rv;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    MyAdapter myAdapter;
    ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setTitle("Home Page");

        drawerLayout = findViewById(R.id.home_layout);
        navigationView = findViewById(R.id.nav_home);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item1: {
                        Toast.makeText(HomeActivity.this, "item1", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.item2: {
                        Toast.makeText(HomeActivity.this, "item2", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                return false;
            }
        });

//        sliderViewFlipper
//        int images[] = {R.drawable.twitter, R.drawable.google, R.drawable.facebook};
//        viewFlipper = findViewById(R.id.vFlipper);
//        for(int i=0;i<images.length;i++){
//            flipperChanges(images[i]);
//        }
//        for(int imageIdx:images){
//            flipperChanges(imageIdx);
//        }

        //slider
        imageSlider = findViewById(R.id.imgSlider);
        ArrayList<SlideModel> img = new ArrayList<>();
        img.add(new SlideModel(R.drawable.twitter, null));
        img.add(new SlideModel(R.drawable.facebook, null));
        img.add(new SlideModel(R.drawable.google, null));

        imageSlider.setImageList(img, ScaleTypes.CENTER_CROP);

        //region recyclerView
        rv = findViewById(R.id.recyclerLaundry);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();
        rv.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        users = new ArrayList<>();
        myAdapter = new MyAdapter(HomeActivity.this, users);
        rv.setAdapter(myAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            //fetch data
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (dataSnapshot.child("role").getValue().equals("laundry")) {
                        User user1 = dataSnapshot.getValue(User.class);
                        users.add(user1);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflates = getMenuInflater();
        inflates.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemProfile:
                logout();
                Toast.makeText(this, "profile clicked", Toast.LENGTH_SHORT).show();
                return true;
        }
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    //slider
    public void flipperChanges(int img){
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(img);
        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(2000);
        viewFlipper.setAutoStart(true);
        //animation
        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(HomeActivity.this, MainActivity.class));
    }

}