package com.example.mjpal.teachertoolkit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.EditText;

public class AddRoomGroups extends AppCompatActivity {
    private myDBHandler groupDBHandler;
    private TextView warning;
    private EditText enteredItem;
    private String choice;
    private Spinner colourChoice;
    private ListView listOfData;
    private String[][] data;
    private String[] newRoomData;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(AddRoomGroups.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.navigation_settings:
                    intent = new Intent(AddRoomGroups.this, ChangeSettings.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.navigation_reminders:
                    intent = new Intent(AddRoomGroups.this, ImportantReminders.class);
                    startActivity(intent);
                    finish();
                    return true;

                case R.id.navigation_timetable:
                    intent = new Intent(AddRoomGroups.this, SelectDay.class);
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
        setContentView(R.layout.activity_add_room_groups);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent intent = getIntent();
        choice = intent.getStringExtra("choice");

        groupDBHandler = new myDBHandler(this, null, null,1);

        warning = (TextView) findViewById(R.id.TV_warning);
        enteredItem = (EditText) findViewById(R.id.GroupInfo);

        listOfData = (ListView) findViewById(R.id.LV_data);

        if (choice.equalsIgnoreCase("groups")) {
            enteredItem.setHint("Enter Group Details");
        }
        else {
        enteredItem.setHint("Enter Room Details");
        }

        warning.setText("");
        warning.setVisibility(View.GONE);

        colourChoice = (Spinner) findViewById(R.id.colour_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.colour_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colourChoice.setAdapter(adapter);

        if (choice.equals("rooms")) {
            colourChoice.setVisibility(View.GONE);
        }

        generateListView();

    }

    public void addGroupToDB(View v){


        switch (choice) {
            case "groups":
                if (!enteredItem.getText().toString().equalsIgnoreCase("")) {
                    groupDBHandler.addGroup(enteredItem.getText().toString(), colourChoice.getSelectedItem().toString());
                    warning.setVisibility(View.GONE);
                }
                else {
                    warning.setText("No group entered");
                    warning.setVisibility(View.VISIBLE);
                }
                break;
            case "rooms":
                if (!enteredItem.getText().toString().equalsIgnoreCase("")) {
                    groupDBHandler.addRoom(enteredItem.getText().toString());
                    warning.setVisibility(View.GONE);
                }
                else {
                    warning.setText("No room entered");
                    warning.setVisibility(View.VISIBLE);
                }
                break;
        }

        generateListView();
    }

    public void editGroupInDB(View v){
        switch (choice) {
            case "groups":
                if (!enteredItem.getText().equals("")) {
                    groupDBHandler.editGroup(enteredItem.getText().toString(), colourChoice.getSelectedItem().toString());
                    warning.setVisibility(View.GONE);
                }
                else {
                    warning.setVisibility(View.VISIBLE);
                    warning.setText("No Group entered");
                }
                break;
            case "rooms":
                //Not needed
                warning.setText("Cannot edit rooms - please delete or add");
                warning.setVisibility(View.VISIBLE);
                break;
        }

        generateListView();


    }

    public void deleteGroupFromDB(View v) {
        switch (choice) {
            case "groups":
                if (!enteredItem.getText().toString().equalsIgnoreCase("free")) {
                    groupDBHandler.deleteGroup(enteredItem.getText().toString());
                    warning.setVisibility(View.GONE);
                    groupDBHandler.resetLesson(enteredItem.getText().toString());
                }
                else {
                    warning.setVisibility(View.VISIBLE);
                    warning.setText("Cannot delete group - FREE");
                }

                break;
            case "rooms":
                if (!enteredItem.getText().toString().equalsIgnoreCase("n/a")) {
                    groupDBHandler.deleteRoom(enteredItem.getText().toString());
                    warning.setVisibility(View.GONE);
                    groupDBHandler.resetGroup(enteredItem.getText().toString());

                }
                else {
                    warning.setVisibility(View.VISIBLE);
                    warning.setText("Cannnot delete group - N/A");
                }
                break;
        }
        generateListView();
    }


    public void generateListView(){
        switch (choice) {
            case "groups":
                data = groupDBHandler.getGroupData();
                String[] newData = new String[data.length];

                for (int counter = 0; counter < data.length; counter++ ) {
                    newData[counter] = data[counter][0] + " - " + data[counter][1];
                }

                ArrayAdapter groupRoomAdapter = new ArrayAdapter(this,
                        android.R.layout.simple_list_item_1, newData);

                listOfData.setAdapter(groupRoomAdapter);
                listOfData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        enteredItem.setText(data[position][0]);
                        colourChoice.setSelection(((ArrayAdapter<String>)colourChoice.getAdapter()).getPosition(data[position][1]));
                    }
                });
                break;

            case "rooms":
                newRoomData = groupDBHandler.getRoomData();
                ArrayAdapter roomAdapter = new ArrayAdapter(this,
                        android.R.layout.simple_list_item_1, newRoomData);

                listOfData.setAdapter(roomAdapter);listOfData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    enteredItem.setText(newRoomData[position]);
                }
            });
                break;
        }



    enteredItem.setText("");
    }


}
