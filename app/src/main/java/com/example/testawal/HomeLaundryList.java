package com.example.testawal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class HomeLaundryList extends AppCompatActivity {

    //region properties recyclerView
    RecyclerView rv;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    MyAdapter myAdapter;
    ArrayList<User> users;
    CardView parentLayout;
    //endregion

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText popupName;
    private Button btnPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_laundry_list);

        getSupportActionBar().setTitle("Laundry List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //region recyclerView
        parentLayout = findViewById(R.id.parentLayout);
        rv = findViewById(R.id.recyclerLaundry);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();
        rv.setLayoutManager(new LinearLayoutManager(HomeLaundryList.this));
        users = new ArrayList<>();
        myAdapter = new MyAdapter(HomeLaundryList.this, users);
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
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //endregion
    }

    public void createNewContactDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View laundryPopupView = getLayoutInflater().inflate(R.layout.popup, null);
        popupName = laundryPopupView.findViewById(R.id.namePopup);
        btnPopup = laundryPopupView.findViewById(R.id.btnPopup);

        dialogBuilder.setView(laundryPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
    }

}

