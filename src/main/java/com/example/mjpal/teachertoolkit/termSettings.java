package com.example.mjpal.teachertoolkit;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class termSettings extends AppCompatActivity {

    private TextView holiday_start;
    private TextView holiday_end;
    private TextView warning;
    private ListView allHolidaysList;
    private myDBHandler dbhandler;
    private DatePickerDialog.OnDateSetListener mHolStartDateSetListener;
    private DatePickerDialog.OnDateSetListener mHolEndDateSetListener;
    private Date date;
    private Calendar startCal;
    private Calendar endCal;
    private String new_start_date;
    private String new_end_date;
    private SimpleDateFormat sdf;
    private Date actualYearStart;
    private Date actualYearEnd;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(termSettings.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.navigation_settings:
                    intent = new Intent(termSettings.this, ChangeSettings.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.navigation_reminders:
                    intent = new Intent(termSettings.this, ImportantReminders.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.navigation_timetable:
                    intent = new Intent(termSettings.this, SelectDay.class);
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

        setContentView(R.layout.activity_term_settings);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        dbhandler = new myDBHandler(this, null, null, 1);
        sdf = new SimpleDateFormat("dd/MM/yyyy");

        fillStartAndEndDates();
        actualYearStart = dbhandler.stringToDate(holiday_start.getText().toString(), "dd/MM/yyyy");
        actualYearEnd = dbhandler.stringToDate(holiday_end.getText().toString(), "dd/MM/yyyy");

        startCal = Calendar.getInstance();

        warning = (TextView) findViewById(R.id.TV_termWarning);
        warning.setVisibility(View.GONE);

        holiday_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date startBoxDate = dbhandler.stringToDate(holiday_start.getText().toString(), "dd/MM/yyyy");
                startCal.setTime(startBoxDate);

                int day = startCal.get(Calendar.DAY_OF_MONTH);
                int month = startCal.get(Calendar.MONTH);
                int year = startCal.get(Calendar.YEAR);

                DatePickerDialog d = new DatePickerDialog(termSettings.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mHolStartDateSetListener, year, month, day);
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                d.show();
            }
        });

        mHolStartDateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datepicker, int year, int month, int day){
                new_start_date = Integer.toString(day) + "/" + Integer.toString(month+1) + "/" + Integer.toString(year);
                holiday_start.setText(new_start_date);
            }
        };

        endCal = Calendar.getInstance();

        holiday_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date endBoxDate = dbhandler.stringToDate(holiday_start.getText().toString(), "dd/MM/yyyy");
                endCal.setTime(endBoxDate);

                int day = endCal.get(Calendar.DAY_OF_MONTH);
                int month = endCal.get(Calendar.MONTH);
                int year = endCal.get(Calendar.YEAR);

                DatePickerDialog d = new DatePickerDialog(termSettings.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mHolEndDateSetListener, year, month, day);
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                d.show();
            }
        });


        mHolEndDateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datepicker, int year, int month, int day){
                new_end_date = Integer.toString(day) + "/" + Integer.toString(month+1) + "/" + Integer.toString(year);
                holiday_end.setText(new_end_date);
            }
        };

        allHolidaysList = (ListView) findViewById(R.id.LV_Holidays);
        getAllHolidays();
    }

    public void getAllHolidays() {
        termWeek[] allWeeks = dbhandler.getAllTermStrings();
        final ArrayList<Holidays> dataForListView = new ArrayList<Holidays>();

        Calendar pointer = Calendar.getInstance();

        Date startHolidayPointer = new java.util.Date();
        Date endHolidayPointer = new java.util.Date();

        int holidayCounter = 0;
        boolean holidayFound = false;
        boolean holidayCompleted = false;

        int counter = 0;
        Holidays tempHolilday;

        while (counter < allWeeks.length){
            if (holidayFound == false) {
                //Check for start date to holiday
                if (counter != 0) {
                    if (allWeeks[counter].getMon().equalsIgnoreCase("H") && allWeeks[counter - 1].getFri().equalsIgnoreCase("Y")) {
                        holidayCounter++;
                        pointer.setTime(allWeeks[counter].getWeek_beg());
                        startHolidayPointer = pointer.getTime();
                        holidayFound = true;
                    }
                } else {
                    if (allWeeks[counter].getMon().equalsIgnoreCase("H")) {
                        holidayCounter++;
                        pointer.setTime(allWeeks[counter].getWeek_beg());
                        startHolidayPointer = pointer.getTime();
                        holidayFound = true;
                    }
                }

                if (allWeeks[counter].getTue().equalsIgnoreCase("H") && allWeeks[counter].getMon().equalsIgnoreCase("Y")) {
                    holidayCounter++;
                    pointer.setTime(allWeeks[counter].getWeek_beg());
                    pointer.add(Calendar.DATE, 1);
                    startHolidayPointer = pointer.getTime();
                    holidayFound = true;
                } else if (allWeeks[counter].getWed().equalsIgnoreCase("H") && allWeeks[counter].getTue().equalsIgnoreCase("Y")) {
                    holidayCounter++;
                    pointer.setTime(allWeeks[counter].getWeek_beg());
                    pointer.add(Calendar.DATE, 2);
                    startHolidayPointer = pointer.getTime();
                    holidayFound = true;
                } else if (allWeeks[counter].getThur().equalsIgnoreCase("H") && allWeeks[counter].getWed().equalsIgnoreCase("Y")) {
                    holidayCounter++;
                    pointer.setTime(allWeeks[counter].getWeek_beg());
                    pointer.add(Calendar.DATE, 3);
                    startHolidayPointer = pointer.getTime();
                    holidayFound = true;
                } else if (allWeeks[counter].getFri().equalsIgnoreCase("H") && allWeeks[counter].getThur().equalsIgnoreCase("Y")) {
                    holidayCounter++;
                    pointer.setTime(allWeeks[counter].getWeek_beg());
                    pointer.add(Calendar.DATE, 4);
                    startHolidayPointer = pointer.getTime();
                    holidayFound = true;
                }


            }
            if (holidayFound == true) {
                //Check for the end date of the holiday
                if (allWeeks[counter].getMon().equalsIgnoreCase("H") && (allWeeks[counter].getTue().equalsIgnoreCase("Y") || allWeeks[counter].getTue().equalsIgnoreCase("N"))) {
                    pointer.setTime(allWeeks[counter].getWeek_beg());
                    endHolidayPointer = pointer.getTime();
                    holidayFound = false;
                    holidayCompleted = true;
                } else if (allWeeks[counter].getTue().equalsIgnoreCase("H") && (allWeeks[counter].getWed().equalsIgnoreCase("Y") || allWeeks[counter].getWed().equalsIgnoreCase("N"))) {
                    pointer.setTime(allWeeks[counter].getWeek_beg());
                    pointer.add(Calendar.DATE, 1);
                    endHolidayPointer = pointer.getTime();
                    holidayFound = false;
                    holidayCompleted = true;

                } else if (allWeeks[counter].getWed().equalsIgnoreCase("H") && (allWeeks[counter].getThur().equalsIgnoreCase("Y") || allWeeks[counter].getThur().equalsIgnoreCase("N"))) {
                    pointer.setTime(allWeeks[counter].getWeek_beg());
                    pointer.add(Calendar.DATE, 2);
                    endHolidayPointer = pointer.getTime();
                    holidayFound = false;
                    holidayCompleted = true;
                } else if (allWeeks[counter].getThur().equalsIgnoreCase("H") && (allWeeks[counter].getFri().equalsIgnoreCase("Y") || allWeeks[counter].getFri().equalsIgnoreCase("N"))) {
                    pointer.setTime(allWeeks[counter].getWeek_beg());
                    pointer.add(Calendar.DATE, 3);
                    endHolidayPointer = pointer.getTime();
                    holidayFound = false;
                    holidayCompleted = true;
                }
                else {
                    if (counter == allWeeks.length - 1) {
                        //if its the final week then automatically set it to the Friday
                        pointer.setTime(allWeeks[counter].getWeek_beg());
                        pointer.add(Calendar.DATE, 4);
                        endHolidayPointer = pointer.getTime();
                        holidayFound = false;
                        holidayCompleted = true;
                    }
                    else {
                        int nextWeek = counter + 1;
                        if (allWeeks[counter].getFri().equalsIgnoreCase("H") && (allWeeks[nextWeek].getMon().equalsIgnoreCase("Y") || allWeeks[nextWeek].getFri().equalsIgnoreCase("N"))) {
                                pointer.setTime(allWeeks[counter].getWeek_beg());
                                pointer.add(Calendar.DATE, 4);
                                endHolidayPointer = pointer.getTime();
                                holidayFound = false;
                                holidayCompleted = true;
                        }
                    }
                }
            }

            if (holidayCompleted == true) {
                //do something
                dataForListView.add(new Holidays(startHolidayPointer, endHolidayPointer, holidayCounter));
                holidayCompleted = false;
            }
            counter++;

            }

        String[] dataArray = new String[holidayCounter];
        for (int counter2 = 0; counter2 < holidayCounter; counter2++) {
            dataArray[counter2] = dataForListView.get(counter2).getHolidayID() + " - " + sdf.format(dataForListView.get(counter2).getStartDate()) + " to " + sdf.format(dataForListView.get(counter2).getEndDate());
        }



        ArrayAdapter holidayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, dataArray);

        allHolidaysList.setAdapter(holidayAdapter); allHolidaysList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                holiday_start.setText(sdf.format(dataForListView.get(position).getStartDate()));
                holiday_end.setText(sdf.format(dataForListView.get(position).getEndDate()));
            }
        });

    }

    public void fillStartAndEndDates() {
        holiday_start = (TextView) findViewById(R.id.hols_start_date);
        String startDateString = dbhandler.getStartOrEndDate("start");
        holiday_start.setText(startDateString);

        holiday_end = (TextView) findViewById(R.id.hols_finish_date);
        String endDateString = dbhandler.getStartOrEndDate("end");
        holiday_end.setText(endDateString);
    }

    public boolean checkDates(String tempDateString) {
        Date tempDate = dbhandler.stringToDate(tempDateString, "dd/MM/yyyy");

        if (tempDate.after(actualYearStart) && tempDate.before(actualYearEnd)) {
            warning.setText("Date ok!");
            warning.setVisibility(View.GONE);
            return true;
        }
        else {
            warning.setText("Check the dates all within the school year");
            warning.setVisibility(View.VISIBLE);
            return false;
        }
    }

    public void updateTerm(View v) {
        String s = holiday_start.getText().toString();
        String e = holiday_end.getText().toString();

        if (checkDates(s) && checkDates(e)) {
            dbhandler.updateTerm(s, e);
            Intent intent = new Intent(termSettings.this, Dashboard.class);
            startActivity(intent);
            finish();
        }
    }

    public void deleteHoliday(View v) {
        String s = holiday_start.getText().toString();
        String e = holiday_end.getText().toString();

        if (checkDates(s) && checkDates(e)) {
            dbhandler.deleteTerm(s, e);
            Intent intent = new Intent(termSettings.this, Dashboard.class);
            startActivity(intent);
            finish();
        }
    }

}
