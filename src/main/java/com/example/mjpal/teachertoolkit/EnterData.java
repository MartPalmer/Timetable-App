package com.example.mjpal.teachertoolkit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.view.View;

import org.w3c.dom.Text;


public class EnterData extends AppCompatActivity {

    String week;
    String day;
    String period;
    String lesson;
    String room;
    String hwkset;


    TextView dbText;
    myDBHandler dbhandler;
    Spinner groupSpinner;
    Spinner roomSpinner;
    Switch hwksetSwitch;
    TextView dayTitle;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(EnterData.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.navigation_settings:
                    intent = new Intent(EnterData.this, ChangeSettings.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.navigation_reminders:
                    intent = new Intent(EnterData.this, ImportantReminders.class);
                    startActivity(intent);
                    finish();
                    return true;

                case R.id.navigation_timetable:
                    intent = new Intent(EnterData.this, SelectDay.class);
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
        setContentView(R.layout.activity_enter_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent intent = getIntent();
        week = intent.getStringExtra("week");
        day = intent.getStringExtra("day");
        period = intent.getStringExtra("period");
        lesson = intent.getStringExtra("lesson");
        room = intent.getStringExtra("room");
        hwkset = intent.getStringExtra("hwkset");

        dbhandler = new myDBHandler(this, null, null, 1);

        int selectedGroupIndex = 0;
        groupSpinner = (Spinner) findViewById(R.id.group_spinner);
        String[][] groupsArray = dbhandler.getGroupData();
        String[] groupDropDownData = new String[groupsArray.length];
        for (int counter = 0; counter < groupDropDownData.length; counter++) {
            groupDropDownData[counter] = groupsArray[counter][0];
            if (groupDropDownData[counter].equals(lesson)) {
                selectedGroupIndex = counter;
            }
        }

        ArrayAdapter<String> groups_adapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, groupDropDownData);
        groups_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        groupSpinner.setAdapter(groups_adapter);
        groupSpinner.setSelection(selectedGroupIndex);

        int selectedRoomIndex = 0;
        roomSpinner = (Spinner) findViewById(R.id.room_spinner);
        String[] roomsArray = dbhandler.getRoomData();
        ArrayAdapter<String> room_adapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, roomsArray);
        room_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for (int room_counter = 0; room_counter < roomsArray.length; room_counter++) {
            if (roomsArray[room_counter].equalsIgnoreCase(room)) {
                selectedRoomIndex = room_counter;
            }
        }

        roomSpinner.setAdapter(room_adapter);
        roomSpinner.setSelection(selectedRoomIndex);

        hwksetSwitch = (Switch) findViewById(R.id.SW_hwkset);
        if (hwkset.equalsIgnoreCase("HWK Set")) {
            hwksetSwitch.setChecked(true);

        }

        dayTitle = (TextView) findViewById(R.id.day_title);
        dayTitle.setText(day + "(" + week + ") - P" + period);

        dbText = (TextView) findViewById(R.id.dbText);
        dbText.setText("Waiting to update or delete...");


    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }

    public void updateLesson(View v){
        lesson = groupSpinner.getSelectedItem().toString();
        room = roomSpinner.getSelectedItem().toString();
        //room = room_textbox.getText().toString();

        Boolean hwksetSwitchState = hwksetSwitch.isChecked();
        String hwksetString = new String();

        if (hwksetSwitchState) {
            hwksetString = "HWK Set";
        }
        else {
            hwksetString = "NO HWK SET";
        }

        ttDB ttdb = new ttDB(week, day, period, lesson, room, hwksetString);
        dbhandler.updateLesson(ttdb);

        dbText.setText("Lesson updated");

        backToDayPage(v);
    }

    public void resetLesson(View v){
        lesson = "FREE";
        room = "N/A";
        hwkset = "NO HWK SET";

        ttDB ttdb = new ttDB(week, day, period, lesson, room, hwkset);
        dbhandler.updateLesson(ttdb);

        dbText.setText("Lesson Removed");

        backToDayPage(v);
    }

    public void backToDayPage(View v) {
        Intent backToDay = new Intent(EnterData.this, ViewDay.class);
        backToDay.putExtra("day", day);
        backToDay.putExtra("week", week);
        startActivity(backToDay);
        finish();
    }



}
