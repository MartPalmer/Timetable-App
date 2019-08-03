package com.example.mjpal.teachertoolkit;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ImportantReminders extends AppCompatActivity {

    private EditText users_reminder;
    private TextView reminder_date;
    private TextView reminder_warning;
    private ListView remindersList;
    private Calendar cal;
    private DatePickerDialog.OnDateSetListener mReminderDateSetListener;
    private myDBHandler dbhandler;
    private SimpleDateFormat sdf;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(ImportantReminders.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.navigation_settings:
                    intent = new Intent(ImportantReminders.this, ChangeSettings.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.navigation_reminders:
                    intent= new Intent(ImportantReminders.this, ImportantReminders.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.navigation_timetable:
                    intent = new Intent(ImportantReminders.this, SelectDay.class);
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
        setContentView(R.layout.activity_important_reminders);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        users_reminder = (EditText) findViewById(R.id.ET_reminder);
        reminder_date = (TextView) findViewById(R.id.TV_reminderDate);
        reminder_warning = (TextView) findViewById(R.id.TV_reminderWarning);
        remindersList = (ListView) findViewById(R.id.LV_Reminders);
        reminder_warning.setVisibility(View.GONE);

        cal = Calendar.getInstance();

        dbhandler = new myDBHandler(this, null, null, 1);

        sdf = new SimpleDateFormat("dd/MM/yyyy");

        reminder_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);

                DatePickerDialog d = new DatePickerDialog(ImportantReminders.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mReminderDateSetListener, year, month, day);
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                d.show();
            }
        });

        mReminderDateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datepicker, int year, int month, int day){
                String new_start_date = Integer.toString(day) + "/" + Integer.toString(month+1) + "/" + Integer.toString(year);
                reminder_date.setText(new_start_date);
            }
        };

        setList();
    }

    public void setList() {
        final String[][] reminders = dbhandler.getReminders("ImportantReminders", "");
        String[] newReminders = new String[reminders.length];

        for (int reminderListCounter = 0; reminderListCounter < reminders.length; reminderListCounter++ ) {
            newReminders[reminderListCounter] = reminders[reminderListCounter][0] + " - " + reminders[reminderListCounter][1];
        }

        ArrayAdapter reminderAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, newReminders);

        remindersList.setAdapter(reminderAdapter);
        remindersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                users_reminder.setText(reminders[position][0]);
                reminder_date.setText(reminders[position][1]);
            }
        });

    }

    public boolean checkTextBoxes () {
        if (users_reminder.getText().toString().equalsIgnoreCase("") ||
                users_reminder.getText().toString().equalsIgnoreCase("Enter Reminder")) {
            reminder_warning.setVisibility(View.VISIBLE);
            reminder_warning.setText("No reminder entered");
            return false;
        }
        else {
            if (reminder_date.getText().toString().equalsIgnoreCase("") ||
                    reminder_date.getText().toString().equalsIgnoreCase("Tap To Enter Date For Reminder")) {
                reminder_warning.setVisibility(View.VISIBLE);
                reminder_warning.setText("No date entered");
                return false;
            }
        }
        return true;
    }

    public void addReminder(View view) {
            if (checkTextBoxes()) {
                dbhandler.addReminder(users_reminder.getText().toString(), reminder_date.getText().toString());
                reminder_warning.setVisibility(View.VISIBLE);
                reminder_warning.setText("Reminder added!");

            }
        backToDash();
    }

    public void cancelReminder(View view) {
        backToDash();
    }

    public void deleteReminder(View view) {
        if (checkTextBoxes()) {
            dbhandler.deleteReminder(users_reminder.getText().toString(), reminder_date.getText().toString());
            reminder_warning.setVisibility(View.VISIBLE);
            reminder_warning.setText("Reminder deleted!");

        }

        backToDash();
    }

    public void backToDash() {
        Intent intent = new Intent(ImportantReminders.this, Dashboard.class);
        startActivity(intent);
        finish();
    }
}
