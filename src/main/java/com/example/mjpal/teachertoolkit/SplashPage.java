package com.example.mjpal.teachertoolkit;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;

import java.util.Calendar;
import java.util.Date;

public class SplashPage extends AppCompatActivity {


    private Date defaultStartDate;
    private Date defaultEndDate;
    private String defaultStartDateString;
    private String defaultEndDateString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_page);

        TextView loaded_data = (TextView) findViewById(R.id.loading_data_textview);
        loaded_data.setText("Loading database");

        SQLiteDatabase db;
        try {
            db = SQLiteDatabase.openDatabase("/data/data/com.example.mjpal.teachertoolkit/databases/tt.db",null, SQLiteDatabase.OPEN_READWRITE);
            loaded_data.setText("Database Found - Checking Tables");
            myDBHandler dbhandler = new myDBHandler(this,null, null, 1);
            int groupCount = dbhandler.checkGroupTableLength();
            if (groupCount == 0){ dbhandler.addGroup("FREE", "Blue"); }
            int roomCount = dbhandler.checkRoomTableLength();
            if (roomCount == 0){ dbhandler.addRoom("N/A"); }

            Intent intent = new Intent(SplashPage.this, Dashboard.class);
            startActivity(intent);
            finish();
        }
        catch (SQLiteException sqlExcepion) {
            //loaded_data.setText("Database missing");
            myDBHandler dbhandler = new myDBHandler(this,null, null, 1);

            //*******************************************************************
            //Sets the default rooms and groups in the database
            //*******************************************************************

            dbhandler.addRoom("N/A");
            dbhandler.addGroup("FREE", "Blue");

             //*******************************************************************
            //Creates a free period for every lesson in the week and adds it to the db
            //*******************************************************************

            String new_week = new String("");
            String new_day = new String("");
            String new_period;

            dbhandler.clearTimetableFromDB();
            for (int w = 1; w <= 2; w++) {
                for (int d = 1; d<= 5; d++) {
                    for (int p = 1; p <= 6; p++) {
                        if (w == 1) { new_week = "A"; }
                        else { new_week = "B"; }

                        switch (d) {
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

                        new_period = new Integer(p).toString();

                        ttDB tt_lesson = new ttDB(new_week, new_day, new_period, "FREE", "N/A", "NO HWK SET");
                        dbhandler.addLesson(tt_lesson);
                    }
                }
            }

            loaded_data.setText("Database created");

            //*******************************************************************
            //Works out all the weeks throughout the year from the default start and end date
            //*******************************************************************

            defaultStartDateString = "03/09/2018";
            defaultEndDateString = "19/07/2019";

            dbhandler.updateStartAndEndDate(defaultStartDateString, defaultEndDateString);

            defaultStartDate = dbhandler.stringToDate(defaultStartDateString, "dd/MM/yyyy");
            defaultEndDate = dbhandler.stringToDate(defaultEndDateString, "dd/MM/yyyy");

            int counter = 0;
            Date newDate = firstDayOfWeek(defaultStartDate);

            while (defaultEndDate.after(newDate)){
                counter += 1;
                newDate = minusOrAddDayFromDate(newDate, 7);
            }

            termWeek[] tempTermWeek = new termWeek[counter];

            counter = 0;
            newDate = firstDayOfWeek(defaultStartDate);

            while (defaultEndDate.after(newDate)){

                //Works out whether its week A or B
                String weekAorB = "";
                if (counter % 2 == 0){
                    weekAorB = "A";
                }
                else {
                    weekAorB = "B";
                }

                String[] weekData = checkWeek(newDate);
                tempTermWeek[counter] = new termWeek(newDate, weekData, weekAorB);
                dbhandler.addTermData(tempTermWeek[counter]); //Add term data to the database

                //Move on 7 days
                newDate = minusOrAddDayFromDate(newDate, 7);

                //update counter to get array number
                counter += 1;

            }


        }

    }

    public String[] checkWeek(Date sd){
        String[] data = new String[5];
        int new_counter = 0;
        Date tempDate = sd;

        while (new_counter < 5){

            Date new_start_date = minusOrAddDayFromDate(defaultStartDate, -1);
            Date new_end_date = minusOrAddDayFromDate(defaultEndDate, 1);

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

    public void goToSettings(View view) {
        Intent intent = new Intent(SplashPage.this, ChangeSettings.class);
        startActivity(intent);
        finish();
    }
}
