package com.example.testawal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class Setting extends AppCompatActivity {

    private View parentView;
    private SwitchMaterial themeSwitch;
    private TextView theme, title;
    private UserSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        settings = (UserSettings) getApplication();

        initWidgets();
        loadSharedPreferences();
        initSwitchListener();
    }

    private void loadSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(UserSettings.PREFERENCES, MODE_PRIVATE);
        String theme = sharedPreferences.getString(UserSettings.CUSTOM_THEME, UserSettings.LIGHT_THEME);
        settings.setCustomTheme(theme);
    }

    private void initSwitchListener() {
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean choosen) {
                if (choosen){
                    settings.setCustomTheme(UserSettings.DARK_THEME);
                }else{
                    settings.setCustomTheme(UserSettings.LIGHT_THEME);
                }
                SharedPreferences.Editor editor = getSharedPreferences(UserSettings.PREFERENCES, MODE_PRIVATE).edit();
                editor.putString(UserSettings.CUSTOM_THEME, settings.getCustomTheme());
                editor.apply();
                updateView();
            }
        });
    }

    private void updateView(){
        final int black = ContextCompat.getColor(this, R.color.black);
        final int white = ContextCompat.getColor(this, R.color.white);
        if(settings.getCustomTheme().equals(UserSettings.DARK_THEME)){
            title.setTextColor(white);
            theme.setTextColor(white);
            theme.setText("Dark");
            parentView.setBackgroundColor(black);
            themeSwitch.setChecked(true);
        }else{
            title.setTextColor(black);
            theme.setTextColor(black);
            theme.setText("White");
            parentView.setBackgroundColor(white);
            themeSwitch.setChecked(false);
        }
    }

    private void initWidgets(){
        theme = findViewById(R.id.theme);
        title = findViewById(R.id.title);
        themeSwitch = findViewById(R.id.switches);
        parentView = findViewById(R.id.parentSetting);
    }
}