package com.example.mjpal.teachertoolkit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ChangeSettings extends AppCompatActivity implements View.OnClickListener {

    private myDBHandler dbHandler;
    private Button add_groups_button;
    private Button add_rooms_button;
    private Button reset_DB_Button;
    private Button term_button;
    private Button settings_button;

    private TextView appTextView;
    private TextView dbTextView;

    //private TextView mTextMessage;

    private Date yearStartDate;
    private Date yearEndDate;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(ChangeSettings.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.navigation_settings:
                    intent = new Intent(ChangeSettings.this, ChangeSettings.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.navigation_reminders:
                    intent = new Intent(ChangeSettings.this, ImportantReminders.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_timetable:
                    intent = new Intent(ChangeSettings.this, SelectDay.class);
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
        setContentView(R.layout.content_change_settings);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        dbHandler = new myDBHandler(this, null, null, 1);
        yearStartDate = stringToDate("03/09/2018", "dd/MM/yyyy");
        yearEndDate = stringToDate("19/07/2019", "dd/MM/yyyy");

        appTextView = (TextView) findViewById(R.id.TV_AppVersionNumber);
        dbTextView = (TextView) findViewById(R.id.TV_DBVersionNumber);

        String appVersionString = dbHandler.getAppAndDBVersion("app");
        String dbVersionNumber = dbHandler.getAppAndDBVersion("db");

        appTextView.setText("App Version Number: " + appVersionString);
        dbTextView.setText("DB Version Number: " + dbVersionNumber);

        add_groups_button = findViewById(R.id.BTN_addGroups);
        add_groups_button.setOnClickListener(this);

        add_rooms_button = findViewById(R.id.BTN_addRooms);
        add_rooms_button.setOnClickListener(this);

        reset_DB_Button = findViewById(R.id.BTN_ResetDB);
        reset_DB_Button.setOnClickListener(this);

        term_button = findViewById(R.id.BTN_addHoliday);
        term_button.setOnClickListener(this);

        settings_button = findViewById(R.id.BTN_UpdateYearDates);
        settings_button.setOnClickListener(this);
    }

    public void add_frees() {
        String new_week = new String("");
        String new_day = new String("");
        String new_period;

        dbHandler.clearTimetableFromDB();

        for (int weekCounter = 1; weekCounter <= 2; weekCounter++) {
            for (int day_counter = 1; day_counter<= 5; day_counter++) {
                for (int period_counter = 1; period_counter <= 6; period_counter++) {
                    if (weekCounter == 1) {
                        new_week = "A";
                    } else {
                        new_week = "B";
                    }

                    switch (day_counter) {
                        case 1:
                            new_day = "Monday";
                            break;
                        case 2:
                            new_day = "Tuesday";
                            break;
                        case 3:
                            new_day = "Wednesday";
                            break;
                        case 4:
                            new_day = "Thursday";
                            break;
                        case 5:
                            new_day = "Friday";
                            break;

                    }

                    new_period = new Integer(period_counter).toString();

                    ttDB new_lesson = new ttDB(new_week, new_day, new_period, "FREE", "N/A", "NO HWK SET");
                    dbHandler.addLesson(new_lesson);
                }
            }
        }

    }

    public void resetWeeks() {
        int counter = 0;
        Date newDate = firstDayOfWeek(yearStartDate);

        while (yearEndDate.after(newDate)){
            counter += 1;
            newDate = minusOrAddDayFromDate(newDate, 7);
        }

        termWeek[] tw = new termWeek[counter];

        counter = 0;
        newDate = firstDayOfWeek(yearStartDate);

        while (yearEndDate.after(newDate)){

            //Works out whether its week A or B if its even its a week B
            String weekAorB = "";
            if (counter % 2 == 0){ weekAorB = "A"; }
            else { weekAorB = "B"; }

            String[] weekData = checkWeek(newDate);
            tw[counter] = new termWeek(newDate, weekData, weekAorB);
            dbHandler.addTermData(tw[counter]); //Add term data to the database

            //Move on 7 days
            newDate = minusOrAddDayFromDate(newDate, 7);

            //update counter to get array number
            counter += 1;

        }
    }

    public void setupDefaultRoomsAndGroups() {
        dbHandler.addRoom("N/A");
        dbHandler.addGroup("FREE", "Blue");
    }

    public String[] checkWeek(Date holidayStartDate){
        String[] data = new String[5];
        int new_counter = 0;
        Date tempDate = holidayStartDate;

        while (new_counter < 5){

            Date new_start_date = minusOrAddDayFromDate(yearStartDate, -1);
            Date new_end_date = minusOrAddDayFromDate(yearEndDate, 1);

            if(new_start_date.before(tempDate) && new_end_date.after(tempDate)){
                data[new_counter] = "Y";
            }
            else {
                data[new_counter] = "N";
            }

            tempDate = minusOrAddDayFromDate(tempDate, 1);
            new_counter += 1;
        }

        return data;
    }

    public Date stringToDate(String aDate, String aFormat) {
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat sdf = new SimpleDateFormat(aFormat);
        Date stringDate = sdf.parse(aDate, pos);

        return stringDate;
    }

    public Date minusOrAddDayFromDate(Date d, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DAY_OF_WEEK, amount);

        Date newDate = c.getTime();
        return newDate;
    }

    public Date firstDayOfWeek(Date d) {
        Date start_of_week;

        Calendar c3 = Calendar.getInstance();
        c3.setTime(d);

        int day_num = c3.get(Calendar.DAY_OF_WEEK);

        switch (day_num) {
            case 1:
                c3.add(Calendar.DAY_OF_WEEK, +1);
                start_of_week = c3.getTime();
                break;
            case 2:
                start_of_week = c3.getTime();
                break;
            case 3:
                c3.add(Calendar.DAY_OF_WEEK, -1);
                start_of_week = c3.getTime();
                break;
            case 4:
                c3.add(Calendar.DAY_OF_WEEK, -2);
                start_of_week = c3.getTime();
                break;
            case 5:
                c3.add(Calendar.DAY_OF_WEEK, -3);
                start_of_week = c3.getTime();
                break;
            case 6:
                c3.add(Calendar.DAY_OF_WEEK, -4);
                start_of_week = c3.getTime();
                break;
            case 7:
                c3.add(Calendar.DAY_OF_WEEK, -5);
                start_of_week = c3.getTime();
                break;

            default:
                c3.add(Calendar.DAY_OF_WEEK, -50);
                start_of_week = c3.getTime();
                break;
        }


        return start_of_week;
    }

    public void setYearStartAndEndDates() {
        String defaultStartDateString = "03/09/2018";
        String defaultEndDateString = "19/07/2019";
        dbHandler.updateStartAndEndDate(defaultStartDateString, defaultEndDateString);
    }

    public void resetDB() {
        dbHandler.dropAllTables();
        dbHandler.createNewTables();

        setupDefaultRoomsAndGroups();
        add_frees();
        resetWeeks();

        setYearStartAndEndDates();


    }

    @Override
    public void onClick(View v) {
        String selected;
        Intent intent;
        switch (v.getId()) {
            case R.id.BTN_addGroups:
                selected = "groups";
                intent = new Intent(ChangeSettings.this, AddRoomGroups.class);
                intent.putExtra("choice", selected);
                startActivity(intent);
                finish();
                break;
            case R.id.BTN_addRooms:
                selected = "rooms";
                intent = new Intent(ChangeSettings.this, AddRoomGroups.class);
                intent.putExtra("choice", selected);
                startActivity(intent);
                finish();
                break;

            case R.id.BTN_ResetDB:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to reset the database. This will wipe all of your data and it will not be recoverable.")
                        .setTitle("Reset Database")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // CONFIRM
                                resetDB();
                                Intent intent = new Intent(ChangeSettings.this, Dashboard.class);
                                startActivity(intent);
                                finish();
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
                break;

            case R.id.BTN_UpdateYearDates:
                intent = new Intent(ChangeSettings.this, UpdateSettings.class);
                startActivity(intent);
                finish();
                break;

            case R.id.BTN_addHoliday:
            intent = new Intent(ChangeSettings.this, termSettings.class);
            startActivity(intent);
            finish();
            break;

        }

    }



}
