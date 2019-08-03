package com.example.mjpal.teachertoolkit;

import android.app.DatePickerDialog;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;

import org.w3c.dom.Text;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.example.mjpal.teachertoolkit.myDBHandler;

public class Dashboard extends AppCompatActivity {

    private TextView date_textview;
    private TextView next_available_day;
    private TextView summary;
    private ListView reminderList;

    private String dayOfWeek;
    private String week;
    ttDB[] day_results = new ttDB[6];
    private Calendar currentTime;
    private Calendar firstDayOfWeek;
    private java.util.Date date;
    private SimpleDateFormat sdf;
    private SimpleDateFormat dbSDF;

    private TextView p1_dashboard;
    private TextView p2_dashboard;
    private TextView p3_dashboard;
    private TextView p4_dashboard;
    private TextView p5_dashboard;
    private TextView p6_dashboard;

    private myDBHandler dbHandler;

    public String[] weeks;

    private Button searchDateButton;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(Dashboard.this, Dashboard.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_settings:
                    intent = new Intent(Dashboard.this, ChangeSettings.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_reminders:
                    intent = new Intent(Dashboard.this, ImportantReminders.class);
                    startActivity(intent);
                    return true;

                case R.id.navigation_timetable:
                    intent = new Intent(Dashboard.this, SelectDay.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        date = new java.util.Date();

        date_textview = (TextView) findViewById(R.id.date_textview);
        next_available_day = (TextView) findViewById(R.id.next_lesson_textview);
        p1_dashboard = (TextView) findViewById(R.id.p1_dashboard);
        p2_dashboard = (TextView) findViewById(R.id.p2_dashboard);
        p3_dashboard = (TextView) findViewById(R.id.p3_dashboard);
        p4_dashboard = (TextView) findViewById(R.id.p4_dashboard);
        p5_dashboard = (TextView) findViewById(R.id.p5_dashboard);
        p6_dashboard = (TextView) findViewById(R.id.p6_dashboard);

        reminderList = (ListView) findViewById(R.id.LV_Reminders);

        sdf = new SimpleDateFormat("dd/MM/yyyy");
        dbSDF = new SimpleDateFormat("yyyy/MM/dd");

        summary = (TextView) findViewById(R.id.t1);

        dbHandler = new myDBHandler(this,null, null, 1);

        currentTime = Calendar.getInstance();
        firstDayOfWeek = Calendar.getInstance();

        currentTime.setTime(date);
        resetTime();

        int day = currentTime.get(Calendar.DAY_OF_WEEK);

        termWeek weekBeginning = dbHandler.findStartWeek(currentTime.getTime());
        week = weekBeginning.getWeekAorB();

        //summary.setText(sdf.format(weekBeginning.getWeek_beg()));

        firstDayOfWeek.setTime(weekBeginning.getWeek_beg());


        date_textview.setText(sdf.format(date));
        dayOfWeek = getDayOfWeek(day);

        outputLessons();

        searchDateButton = findViewById(R.id.search_date_button);
        searchDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = currentTime.get(Calendar.YEAR);
                int month = currentTime.get(Calendar.MONTH);
                int day = currentTime.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog d = new DatePickerDialog(Dashboard.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                d.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datepicker, int year, int month, int day){
                Date d = new Date(year-1900, month, day);
                currentTime.setTime(d);
                updateLesson();
                setList();
            }
        };

        setList();
    }

    public void setList() {
        final String[][] reminders = dbHandler.getReminders("Dashboard", sdf.format(date));
        String[] newReminders = new String[reminders.length];

        for (int reminderListCounter = 0; reminderListCounter < reminders.length; reminderListCounter++ ) {
            newReminders[reminderListCounter] = reminders[reminderListCounter][0] + " - " + reminders[reminderListCounter][1];
        }

        ArrayAdapter reminderAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, newReminders);

        reminderList.setAdapter(reminderAdapter);


    }

    public String getDayOfWeek(int dayNum){
        String dow = "";

        switch (dayNum) {
            case 1:
                dow = "Sunday";
                break;
            case 2:
                dow = "Monday";
                break;
            case 3:
                dow = "Tuesday";
                break;
            case 4:
                dow = "Wednesday";
                break;
            case 5:
                dow = "Thursday";
                break;
            case 6:
                dow = "Friday";
                break;
            case 7:
                dow = "Saturday";
                break;
        }

        return dow;
    }

    public CharSequence setLessonStringStyle(int periodNum) {
        CharSequence finalLesson1String;
        SpannableString[] lesson1data = new SpannableString[2];
        lesson1data[0] = new SpannableString(day_results[periodNum].get_lesson() + "\n");

        if (day_results[periodNum].get_hwkset().equalsIgnoreCase("HWK Set")) {
            lesson1data[1] = new SpannableString("(" + day_results[periodNum].get_room() + " - " + day_results[periodNum].get_hwkset() + ")");
        }
        else {
            lesson1data[1] = new SpannableString("(" + day_results[periodNum].get_room() + ")");
        }
        lesson1data[1].setSpan(new RelativeSizeSpan(0.75f), 0, lesson1data[1].length(), 0);
        return finalLesson1String = TextUtils.concat(lesson1data[0], lesson1data[1]);
    }

    public void outputLessons() {

        Date date2 = currentTime.getTime();
        CharSequence lessonFinalString;

        day_results = dbHandler.getLesson(week, dayOfWeek);

        if (week.compareToIgnoreCase("n") == 0) {
            next_available_day.setText(String.format(dayOfWeek + " (Schools out!) - " + sdf.format(date2)));
        }
        else if (week.compareToIgnoreCase("h") == 0) {
            next_available_day.setText(String.format(dayOfWeek + " (Holiday) - " + sdf.format(date2)));
        }
        else if (dayOfWeek.equals("Saturday") || dayOfWeek.equals("Sunday")) {
            next_available_day.setText(String.format(dayOfWeek + " (No Lessons Today) - " + sdf.format(date2)));
        }
        else {
            next_available_day.setText(dayOfWeek + " (" + week + ") - " + sdf.format(date2));
        }

        lessonFinalString = setLessonStringStyle(0);
        p1_dashboard.setText(lessonFinalString);
        setColour(p1_dashboard, day_results[0].get_lesson());

        lessonFinalString = setLessonStringStyle(1);
        p2_dashboard.setText(lessonFinalString);
        setColour(p2_dashboard, day_results[1].get_lesson());

        lessonFinalString = setLessonStringStyle(2);
        p3_dashboard.setText(lessonFinalString);
        setColour(p3_dashboard, day_results[2].get_lesson());

        lessonFinalString = setLessonStringStyle(3);
        p4_dashboard.setText(lessonFinalString);
        setColour(p4_dashboard, day_results[3].get_lesson());

        lessonFinalString = setLessonStringStyle(4);
        p5_dashboard.setText(lessonFinalString);
        setColour(p5_dashboard, day_results[4].get_lesson());

        if (day_results[5].get_hwkset().equalsIgnoreCase("HWK Set")) {
            p6_dashboard.setText("Intervention - " + day_results[5].get_lesson() + " - " + day_results[5].get_room() + " - " + day_results[5].get_hwkset());
        }
        else {
            p6_dashboard.setText("Intervention - " + day_results[5].get_lesson() + " - " + day_results[5].get_room());
        }
        setColour(p6_dashboard, day_results[5].get_lesson());

    }

    public void setColour(TextView t, String lesson) {

        String colour = "";

        colour = dbHandler.getColourForLesson(lesson);

        switch (colour) {
            case "Blue":
                t.setTextColor(getResources().getColor(R.color.textWhite));
                t.setBackgroundColor(getResources().getColor(R.color.ttblue));
                break;
            case "Yellow":
                t.setTextColor(getResources().getColor(R.color.textBlack));
                t.setBackgroundColor(getResources().getColor(R.color.ttyellow));
                break;

            case "Brown":
                t.setTextColor(getResources().getColor(R.color.textWhite));
                t.setBackgroundColor(getResources().getColor(R.color.ttbrown));
                break;
            case "Turquiose":
                t.setTextColor(getResources().getColor(R.color.textBlack));
                t.setBackgroundColor(getResources().getColor(R.color.ttturquoise));
                break;
            case "Light Yellow":
                t.setTextColor(getResources().getColor(R.color.textBlack));
                t.setBackgroundColor(getResources().getColor(R.color.ttlightyellow));
                break;
            case "Beige":
                t.setTextColor(getResources().getColor(R.color.textWhite));
                t.setBackgroundColor(getResources().getColor(R.color.ttbeige));
                break;
            case "Green":
                t.setTextColor(getResources().getColor(R.color.textWhite));
                t.setBackgroundColor(getResources().getColor(R.color.ttgreen));
                break;
            case "Light Green":
                t.setTextColor(getResources().getColor(R.color.textBlack));
                t.setBackgroundColor(getResources().getColor(R.color.ttlightgreen));
                break;
            case "Purple":
                t.setTextColor(getResources().getColor(R.color.textWhite));
                t.setBackgroundColor(getResources().getColor(R.color.ttpurple));
                break;
            case "Violet":
                t.setTextColor(getResources().getColor(R.color.textBlack));
                t.setBackgroundColor(getResources().getColor(R.color.ttvoilet));
                break;
            case "Grey":
                t.setTextColor(getResources().getColor(R.color.textWhite));
                t.setBackgroundColor(getResources().getColor(R.color.ttgrey));
                break;
            case "Mustard":
                t.setTextColor(getResources().getColor(R.color.textBlack));
                t.setBackgroundColor(getResources().getColor(R.color.ttmustard));
                break;
            case "Dark Beige":
                t.setTextColor(getResources().getColor(R.color.textBlack));
                t.setBackgroundColor(getResources().getColor(R.color.ttdarkbeige));
                break;
            case "Light Blue":
                t.setTextColor(getResources().getColor(R.color.textBlack));
                t.setBackgroundColor(getResources().getColor(R.color.ttlightblue));
                break;
            case "Pink":
                t.setTextColor(getResources().getColor(R.color.textBlack));
                t.setBackgroundColor(getResources().getColor(R.color.ttpink));
                break;
            case "Orange":
                t.setTextColor(getResources().getColor(R.color.textBlack));
                t.setBackgroundColor(getResources().getColor(R.color.ttorange));
                break;
            case "Dark Orange":
                t.setTextColor(getResources().getColor(R.color.textWhite));
                t.setBackgroundColor(getResources().getColor(R.color.ttdarkorange));
                break;
            case "Red":
                t.setTextColor(getResources().getColor(R.color.textWhite));
                t.setBackgroundColor(getResources().getColor(R.color.ttred));
                break;
        }
    }

    public void changeWeek(View v) {
        if (week.equals("A")) {
            week = "B";
        }
        else {
            week = "A";
        }

        outputLessons();

    }

    public void changeDay(View v) {
        switch (v.getId()) {
            case R.id.next_day_button:
                currentTime.add(Calendar.DATE, 1);
                //summary.setText(sdf.format(currentTime.getTime()));
                break;
            case R.id.previous_day_button:
                currentTime.add(Calendar.DATE, -1);
                //summary.setText(sdf.format(currentTime.getTime()));
                break;
            case R.id.today_button:
                Date new_date = new java.util.Date();
                currentTime.setTime(new_date);
                //summary.setText(sdf.format(currentTime.getTime()));
                resetTime();
                break;
        }

        updateLesson();
    }

    public void updateLesson(){
        int day = currentTime.get(Calendar.DAY_OF_WEEK);
        termWeek new_wk = dbHandler.findStartWeek(currentTime.getTime());
        week = new_wk.getWeekAorB();

        dayOfWeek = getDayOfWeek(day);
        outputLessons();
    }

    public void resetTime() {
        currentTime.set(Calendar.MILLISECOND, 0);
        currentTime.set(Calendar.SECOND, 0);
        currentTime.set(Calendar.MINUTE, 0);
        currentTime.set(Calendar.HOUR, 0);
        currentTime.set(Calendar.HOUR_OF_DAY, 0);
        currentTime.set(Calendar.DST_OFFSET, 0);
    }


}
