package com.example.testawal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Kiloan extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    //region properties recyclerView
    RecyclerView rv;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    MyAdapter myAdapter;
    ArrayList<User> users;
    ConstraintLayout parentLayout;
    //endregion

    //region properties search
    ListView listLaundry;
    AutoCompleteTextView txtSearch;

    SearchView searchView;
    //endregion

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText popupName;
    private Button btnPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup);

        getSupportActionBar().setTitle("Laundry Kiloan List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //region recyclerView
        parentLayout = findViewById(R.id.parentLayout);
        rv = findViewById(R.id.recyclerLaundry);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();
        rv.setLayoutManager(new LinearLayoutManager(Kiloan.this));
        users = new ArrayList<>();
        myAdapter = new MyAdapter(Kiloan.this, users);
        rv.setAdapter(myAdapter);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        final int bluex = ContextCompat.getColor(this, R.color.bluex);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#083444"));

        if (sharedPreferences.getBoolean("dark_mode", true)) {
            parentLayout.setBackgroundColor(bluex);
            getSupportActionBar().setBackgroundDrawable(colorDrawable);
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            //fetch data
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (dataSnapshot.child("role").getValue().equals("laundry") && dataSnapshot.child("services").getValue() != null &&
                            dataSnapshot.child("services").getValue().toString().contains("Kiloan")) {
                        User user2 = dataSnapshot.getValue(User.class);
                        users.add(user2);
                    }
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //endregion

        //region search
        searchView = findViewById(R.id.searchView);
        searchView.onActionViewExpanded();
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filter(s);
                return true;
            }
        });
        //endregion
    }

    private void filter(String s) {
        ArrayList<User> searchList = new ArrayList<>();
        for (User obj : users){
            if(obj.getNama().toLowerCase().contains(s.toLowerCase())){
                searchList.add(obj);
            }
        }
        MyAdapter adapterSearch = new MyAdapter(Kiloan.this, searchList);
        rv.setAdapter(adapterSearch);
    }

    /*public void createNewContactDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View laundryPopupView = getLayoutInflater().inflate(R.layout.popup, null);
        popupName = laundryPopupView.findViewById(R.id.namePopup);
        btnPopup = laundryPopupView.findViewById(R.id.btnPopup);

        dialogBuilder.setView(laundryPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
    }*/

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

