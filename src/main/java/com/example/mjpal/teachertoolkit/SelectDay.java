package com.example.mjpal.teachertoolkit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

public class SelectDay extends AppCompatActivity {

    private ToggleButton weekA;
    private ToggleButton weekB;

    private ToggleButton Monday;
    private ToggleButton Tuesday;
    private ToggleButton Wednesday;
    private ToggleButton Thursday;
    private ToggleButton Friday;

    private String selectedWeek;
    private String selectedDay;

    private Button submitButton;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(SelectDay.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.navigation_settings:
                    intent = new Intent(SelectDay.this, ChangeSettings.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.navigation_reminders:
                    intent = new Intent(SelectDay.this, ImportantReminders.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.navigation_timetable:
                    intent = new Intent(SelectDay.this, SelectDay.class);
                    startActivity(intent);
                    finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_day);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        weekA = (ToggleButton) findViewById(R.id.BTN_WeekA);
        weekA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weekB.setChecked(false);
                selectedWeek  = "A";
            }
        });

        weekB = (ToggleButton) findViewById(R.id.BTN_WeekB);
        weekB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weekA.setChecked(false);
                selectedWeek  = "B";
            }
        });

        Monday = (ToggleButton) findViewById(R.id.BTN_Monday);
        Monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tuesday.setChecked(false);
                Wednesday.setChecked(false);
                Thursday.setChecked(false);
                Friday.setChecked(false);
                selectedDay  = "Monday";
            }
        });

        Tuesday = (ToggleButton) findViewById(R.id.BTN_Tuesday);
        Tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Monday.setChecked(false);
                Wednesday.setChecked(false);
                Thursday.setChecked(false);
                Friday.setChecked(false);
                selectedDay  = "Tuesday";
            }
        });

        Wednesday = (ToggleButton) findViewById(R.id.BTN_Wednesday);
        Wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Monday.setChecked(false);
                Tuesday.setChecked(false);
                Thursday.setChecked(false);
                Friday.setChecked(false);
                selectedDay  = "Wednesday";
            }
        });

        Thursday = (ToggleButton) findViewById(R.id.BTN_Thursday);
        Thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Monday.setChecked(false);
                Tuesday.setChecked(false);
                Wednesday.setChecked(false);
                Friday.setChecked(false);
                selectedDay  = "Thursday";
            }
        });

        Friday = (ToggleButton) findViewById(R.id.BTN_Friday);
        Friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Monday.setChecked(false);
                Tuesday.setChecked(false);
                Wednesday.setChecked(false);
                Thursday.setChecked(false);
                selectedDay  = "Friday";
            }
        });

        selectedWeek  = "A";
        selectedDay = "Monday";

        weekA.setChecked(true);
        Monday.setChecked(true);

        submitButton = (Button) findViewById(R.id.BTN_ConfirmSelectDay);
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectDay.this, ViewDay.class);
                intent.putExtra("week", selectedWeek);
                intent.putExtra("day", selectedDay);

                startActivity(intent);
                finish();
            }

        }
        );

    }
}
