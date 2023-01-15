package com.example.testawal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingActivity extends PreferenceActivity {

    String userId, role;
    FirebaseUser fUser;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        fUser = FirebaseAuth.getInstance().getCurrentUser();
//        userId = fUser.getUid();
//        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//
//        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                User user = snapshot.getValue(User.class);
//                if(user != null){
//                    role = user.role;
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        final int black = ContextCompat.getColor(this, R.color.black);

        if (sharedPreferences.getBoolean("dark_mode", true)) {
            getListView().setBackgroundColor(black);
            setTheme(R.style.TEXTWhite);
        }

        getFragmentManager().beginTransaction().replace(android.R.id.content, new Mypreferences()).commit();
    }

    public static class Mypreferences extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        String userId, role;
        FirebaseUser fUser;
        DatabaseReference databaseReference;

        SharedPreferences sharedPreferences;
        SwitchPreference darkmode;
        SharedPreferences.OnSharedPreferenceChangeListener listener;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settingxml);

            fUser = FirebaseAuth.getInstance().getCurrentUser();
            userId = fUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if(user != null){
                        role = user.role;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            darkmode = (SwitchPreference) findPreference("dark_mode");

            darkmode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    boolean isChecked = (Boolean) newValue;

                    if (isChecked) {
                        DarkModeisOn();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (role.equals("laundry")){
                                    Intent intent = new Intent(getActivity(), LaundryMain.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }else if (role.equals("user")){
                                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            }
                        },1000);
                    } else if (!isChecked) {
                        DarkModeisOff();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (role.equals("laundry")){
                                    Intent intent = new Intent(getActivity(), LaundryMain.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }else if (role.equals("user")){
                                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            }
                        },1000);
                    }
                    return true;
                }
            });
        }

        public void DarkModeisOff() {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.apply();
        }

        public void DarkModeisOn() {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("dark_mode", true);
            editor.apply();

        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        }
    }

}
