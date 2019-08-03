package com.example.mjpal.teachertoolkit;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

public class UpdateSettings extends AppCompatActivity {

    private TextView yearStartDate;
    private TextView yearEndDate;
    private TextView warning;
    private myDBHandler dbhandler;
    private Calendar startCal;
    private Calendar endCal;
    private DatePickerDialog.OnDateSetListener mYearStartDateSetListener;
    private DatePickerDialog.OnDateSetListener mYearEndDateSetListener;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(UpdateSettings.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.navigation_settings:
                    intent = new Intent(UpdateSettings.this, ChangeSettings.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.navigation_reminders:
                    intent = new Intent(UpdateSettings.this, ImportantReminders.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.navigation_timetable:
                    intent = new Intent(UpdateSettings.this, SelectDay.class);
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
        setContentView(R.layout.activity_update_settings);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        dbhandler = new myDBHandler(this,null, null,1);
        yearStartDate = (TextView) findViewById(R.id.TV_YearStartDateData);
        yearEndDate = (TextView) findViewById(R.id.TV_YearEndDateData);
        warning = (TextView) findViewById(R.id.TV_SettingsWarning);
        warning.setVisibility(View.GONE);


        //Set up so that a date picker opens when the START date text view is clicked.
        String startDateString = dbhandler.getStartOrEndDate("start");
        yearStartDate.setText(startDateString);

        startCal = Calendar.getInstance();
        endCal = Calendar.getInstance();

        yearStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date startBoxDate = dbhandler.stringToDate(yearStartDate.getText().toString(), "dd/MM/yyyy");
                startCal.setTime(startBoxDate);

                int day = startCal.get(Calendar.DAY_OF_MONTH);
                int month = startCal.get(Calendar.MONTH);
                int year = startCal.get(Calendar.YEAR);

                DatePickerDialog d = new DatePickerDialog(UpdateSettings.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mYearStartDateSetListener, year, month, day);
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                d.show();
            }
        });

        mYearStartDateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datepicker, int year, int month, int day){
                String new_start_date = Integer.toString(day) + "/" + Integer.toString(month+1) + "/" + Integer.toString(year);
                yearStartDate.setText(new_start_date);
            }
        };


        //Set up so that a date picker opens when the END date text view is clicked.
        String endDateString = dbhandler.getStartOrEndDate("end");
        yearEndDate.setText(endDateString);

        yearEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date endBoxDate = dbhandler.stringToDate(yearEndDate.getText().toString(), "dd/MM/yyyy");
                endCal.setTime(endBoxDate);

                int day = endCal.get(Calendar.DAY_OF_MONTH);
                int month = endCal.get(Calendar.MONTH);
                int year = endCal.get(Calendar.YEAR);

                DatePickerDialog d = new DatePickerDialog(UpdateSettings.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mYearEndDateSetListener, year, month, day);
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                d.show();
            }
        });

        mYearEndDateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datepicker, int year, int month, int day){
                String new_end_date = Integer.toString(day) + "/" + Integer.toString(month+1) + "/" + Integer.toString(year);
                yearEndDate.setText(new_end_date);
            }
        };


    }

    public void updateNewSettings(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to reset the start and end dates of the school year. This will wipe all of the holidays you have already entered.")
                .setTitle("Reset Year")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // CONFIRM
                        String newStartDate = yearStartDate.getText().toString();
                        String newEndDate = yearEndDate.getText().toString();
                        if (newStartDate.equalsIgnoreCase("No date in database") || newEndDate.equalsIgnoreCase("No date in database")) {
                            warning.setVisibility(View.VISIBLE);
                            warning.setText("Error - Invalid start or end date");
                        }
                        else {
                            if (dbhandler.checkStartIsBeforeEndDate(newStartDate, newEndDate, "dd/MM/yyyy")) {
                                dbhandler.updateStartAndEndDate(newStartDate, newEndDate);
                                dbhandler.updateSchoolYearWeeks(newStartDate, newEndDate, "dd/MM/yyyy");
                                warning.setVisibility(View.GONE);

                                Intent intent = new Intent(UpdateSettings.this, Dashboard.class);
                                startActivity(intent);
                                finish();

                            }
                            else {
                                warning.setVisibility(View.VISIBLE);
                                warning.setText("Error - Start date after the end date");
                            }
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // CANCEL
                        //Don't want to do anything if they click cancel
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();


    }
}
