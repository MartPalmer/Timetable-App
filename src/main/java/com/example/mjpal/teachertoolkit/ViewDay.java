package com.example.mjpal.teachertoolkit;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

public class ViewDay extends AppCompatActivity implements View.OnClickListener {

    TextView titleText;
    String day;
    String week;
    myDBHandler dbHandler;
    ttDB[] day_results = new ttDB[6];
    TextView p1lesson_label;
    TextView p2lesson_label;
    TextView p3lesson_label;
    TextView p4lesson_label;
    TextView p5lesson_label;
    TextView p6lesson_label;

    Button p1Button;
    Button p2Button;
    Button p3Button;
    Button p4Button;
    Button p5Button;
    Button p6Button;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(ViewDay.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.navigation_settings:
                    intent = new Intent(ViewDay.this, ChangeSettings.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.navigation_reminders:
                    intent = new Intent(ViewDay.this, ImportantReminders.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.navigation_timetable:
                    intent = new Intent(ViewDay.this, SelectDay.class);
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
        setContentView(R.layout.activity_view_day);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent intent = getIntent();
        week = intent.getStringExtra("week");
        day = intent.getStringExtra("day");
        String hwkSetString = "";

        dbHandler = new myDBHandler(this,null, null, 1);

        p1Button = findViewById(R.id.p1Edit);
        p1Button.setOnClickListener(this);
        p2Button = findViewById(R.id.p2Edit);
        p2Button.setOnClickListener(this);
        p3Button = findViewById(R.id.p3Edit);
        p3Button.setOnClickListener(this);
        p4Button = findViewById(R.id.p4Edit);
        p4Button.setOnClickListener(this);
        p5Button = findViewById(R.id.p5Edit);
        p5Button.setOnClickListener(this);
        p6Button = findViewById(R.id.p6Edit);
        p6Button.setOnClickListener(this);

        day_results = dbHandler.getLesson(week,day);

        p1lesson_label = (TextView) findViewById(R.id.p1lesson);
        if (day_results[0].get_hwkset().equalsIgnoreCase("HWK Set")) { hwkSetString = " (HWK Set)"; } else { hwkSetString = ""; }
        p1lesson_label.setText(day_results[0].get_period() + " - " + day_results[0].get_lesson() + " - " + day_results[0].get_room() + hwkSetString);
        setColour(p1lesson_label, day_results[0].get_lesson());

        if (day_results[1].get_hwkset().equalsIgnoreCase("HWK Set")) { hwkSetString = " (HWK Set)"; } else { hwkSetString = ""; }
        p2lesson_label = (TextView) findViewById(R.id.p2lesson);
        p2lesson_label.setText(day_results[1].get_period() + " - " + day_results[1].get_lesson() + " - " + day_results[1].get_room()+ hwkSetString);
        setColour(p2lesson_label, day_results[1].get_lesson());

        if (day_results[2].get_hwkset().equalsIgnoreCase("HWK Set")) { hwkSetString = " (HWK Set)"; } else { hwkSetString = ""; }
        p3lesson_label = (TextView) findViewById(R.id.p3lesson);
        p3lesson_label.setText(day_results[2].get_period() + " - " + day_results[2].get_lesson() + " - " + day_results[2].get_room()+ hwkSetString);
        setColour(p3lesson_label, day_results[2].get_lesson());

        if (day_results[3].get_hwkset().equalsIgnoreCase("HWK Set")) { hwkSetString = " (HWK Set)"; } else { hwkSetString = ""; }
        p4lesson_label = (TextView) findViewById(R.id.p4lesson);
        p4lesson_label.setText(day_results[3].get_period() + " - " + day_results[3].get_lesson() + " - " + day_results[3].get_room() + hwkSetString);
        setColour(p4lesson_label, day_results[3].get_lesson());

        if (day_results[4].get_hwkset().equalsIgnoreCase("HWK Set")) { hwkSetString = " (HWK Set)"; } else { hwkSetString = ""; }
        p5lesson_label = (TextView) findViewById(R.id.p5lesson);
        p5lesson_label.setText(day_results[4].get_period() + " - " + day_results[4].get_lesson() + " - " + day_results[4].get_room()+ hwkSetString);
        setColour(p5lesson_label, day_results[4].get_lesson());

        if (day_results[5].get_hwkset().equalsIgnoreCase("HWK Set")) { hwkSetString = " (HWK Set)"; } else { hwkSetString = ""; }
        p6lesson_label = (TextView) findViewById(R.id.p6lesson);
        p6lesson_label.setText(day_results[5].get_period() + " - " + day_results[5].get_lesson() + " - " + day_results[5].get_room() + hwkSetString);
        setColour(p6lesson_label, day_results[5].get_lesson());

    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(ViewDay.this, EnterData.class);

        switch (v.getId()) {
            case R.id.p1Edit:
                intent.putExtra("week", day_results[0].get_week());
                intent.putExtra("day", day_results[0].get_day());
                intent.putExtra("period", day_results[0].get_period());
                intent.putExtra("lesson", day_results[0].get_lesson());
                intent.putExtra("room", day_results[0].get_room());
                intent.putExtra("hwkset", day_results[0].get_hwkset());
                break;
            case R.id.p2Edit:
                intent.putExtra("week", day_results[1].get_week());
                intent.putExtra("day", day_results[1].get_day());
                intent.putExtra("period", day_results[1].get_period());
                intent.putExtra("lesson", day_results[1].get_lesson());
                intent.putExtra("room", day_results[1].get_room());
                intent.putExtra("hwkset", day_results[1].get_hwkset());
                break;
            case R.id.p3Edit:
                intent.putExtra("week", day_results[2].get_week());
                intent.putExtra("day", day_results[2].get_day());
                intent.putExtra("period", day_results[2].get_period());
                intent.putExtra("lesson", day_results[2].get_lesson());
                intent.putExtra("room", day_results[2].get_room());
                intent.putExtra("hwkset", day_results[2].get_hwkset());
                break;
            case R.id.p4Edit:
                intent.putExtra("week", day_results[3].get_week());
                intent.putExtra("day", day_results[3].get_day());
                intent.putExtra("period", day_results[3].get_period());
                intent.putExtra("lesson", day_results[3].get_lesson());
                intent.putExtra("room", day_results[3].get_room());
                intent.putExtra("hwkset", day_results[3].get_hwkset());
                break;
            case R.id.p5Edit:
                intent.putExtra("week", day_results[4].get_week());
                intent.putExtra("day", day_results[4].get_day());
                intent.putExtra("period", day_results[4].get_period());
                intent.putExtra("lesson", day_results[4].get_lesson());
                intent.putExtra("room", day_results[4].get_room());
                intent.putExtra("hwkset", day_results[4].get_hwkset());
                break;
            case R.id.p6Edit:
                intent.putExtra("week", day_results[5].get_week());
                intent.putExtra("day", day_results[5].get_day());
                intent.putExtra("period", day_results[5].get_period());
                intent.putExtra("lesson", day_results[5].get_lesson());
                intent.putExtra("room", day_results[5].get_room());
                intent.putExtra("hwkset", day_results[5].get_hwkset());
                break;
        }

        startActivity(intent);
        finish();
    }

    public void setColour(TextView t, String lesson) {

        String colour = "";
        Drawable box = getResources().getDrawable(R.drawable.rounded_rect);

        myDBHandler dbHandler = new myDBHandler(this, null, null,1);
        colour = dbHandler.getColourForLesson(lesson);

        switch (colour) {
            case "Blue":
                t.setTextColor(getResources().getColor(R.color.textWhite));

                box.setColorFilter(getResources().getColor(R.color.ttblue), PorterDuff.Mode.ADD);
                //t.setBackgroundColor(getResources().getColor(R.color.ttblue));

                break;
            case "Yellow":
                t.setTextColor(getResources().getColor(R.color.textBlack));
                box.setColorFilter(getResources().getColor(R.color.ttyellow), PorterDuff.Mode.ADD);

                //t.setBackgroundColor(getResources().getColor(R.color.ttyellow));
                break;
            case "Brown":
                t.setTextColor(getResources().getColor(R.color.textWhite));
                box.setColorFilter(getResources().getColor(R.color.ttbrown), PorterDuff.Mode.ADD);
                //t.setBackgroundColor(getResources().getColor(R.color.ttbrown));
                break;
            case "Turquiose":
                t.setTextColor(getResources().getColor(R.color.textBlack));
                box.setColorFilter(getResources().getColor(R.color.ttturquoise), PorterDuff.Mode.ADD);
                //t.setBackgroundColor(getResources().getColor(R.color.ttturquoise));
                break;
            case "Light Yellow":
                t.setTextColor(getResources().getColor(R.color.textBlack));
                box.setColorFilter(getResources().getColor(R.color.ttlightyellow), PorterDuff.Mode.ADD);
                //t.setBackgroundColor(getResources().getColor(R.color.ttlightyellow));
                break;
            case "Beige":
                t.setTextColor(getResources().getColor(R.color.textWhite));
                //t.setBackgroundColor(getResources().getColor(R.color.ttbeige));
                box.setColorFilter(getResources().getColor(R.color.ttbeige), PorterDuff.Mode.ADD);
                break;
            case "Green":
                t.setTextColor(getResources().getColor(R.color.textWhite));
                //t.setBackgroundColor(getResources().getColor(R.color.ttgreen));
                box.setColorFilter(getResources().getColor(R.color.ttgreen), PorterDuff.Mode.ADD);
                break;
            case "Light Green":
                t.setTextColor(getResources().getColor(R.color.textBlack));
                //t.setBackgroundColor(getResources().getColor(R.color.ttlightgreen));
                box.setColorFilter(getResources().getColor(R.color.ttlightgreen), PorterDuff.Mode.ADD);
                break;
            case "Purple":
                t.setTextColor(getResources().getColor(R.color.textWhite));
                //t.setBackgroundColor(getResources().getColor(R.color.ttpurple));
                box.setColorFilter(getResources().getColor(R.color.ttpurple), PorterDuff.Mode.ADD);
                break;
            case "Violet":
                t.setTextColor(getResources().getColor(R.color.textBlack));
                //t.setBackgroundColor(getResources().getColor(R.color.ttvoilet));
                box.setColorFilter(getResources().getColor(R.color.ttvoilet), PorterDuff.Mode.ADD);
                break;
            case "Grey":
                t.setTextColor(getResources().getColor(R.color.textWhite));
                //t.setBackgroundColor(getResources().getColor(R.color.ttgrey));
                box.setColorFilter(getResources().getColor(R.color.ttgrey), PorterDuff.Mode.ADD);
                break;
            case "Mustard":
                t.setTextColor(getResources().getColor(R.color.textBlack));
                //t.setBackgroundColor(getResources().getColor(R.color.ttmustard));
                box.setColorFilter(getResources().getColor(R.color.ttmustard), PorterDuff.Mode.ADD);
                break;
            case "Dark Beige":
                t.setTextColor(getResources().getColor(R.color.textBlack));
                //t.setBackgroundColor(getResources().getColor(R.color.ttdarkbeige));
                box.setColorFilter(getResources().getColor(R.color.ttdarkbeige), PorterDuff.Mode.ADD);
                break;
            case "Light Blue":
                t.setTextColor(getResources().getColor(R.color.textBlack));
                //t.setBackgroundColor(getResources().getColor(R.color.ttlightblue));
                box.setColorFilter(getResources().getColor(R.color.ttlightblue), PorterDuff.Mode.ADD);
                break;
            case "Pink":
                t.setTextColor(getResources().getColor(R.color.textBlack));
                //t.setBackgroundColor(getResources().getColor(R.color.ttpink));
                box.setColorFilter(getResources().getColor(R.color.ttpink), PorterDuff.Mode.ADD);
                break;
            case "Orange":
                t.setTextColor(getResources().getColor(R.color.textBlack));
                //t.setBackgroundColor(getResources().getColor(R.color.ttorange));
                box.setColorFilter(getResources().getColor(R.color.ttorange), PorterDuff.Mode.ADD);
                break;
            case "Dark Orange":
                t.setTextColor(getResources().getColor(R.color.textWhite));
                //t.setBackgroundColor(getResources().getColor(R.color.ttdarkorange));
                box.setColorFilter(getResources().getColor(R.color.ttdarkorange), PorterDuff.Mode.ADD);
                break;
            case "Red":
                t.setTextColor(getResources().getColor(R.color.textWhite));
                //t.setBackgroundColor(getResources().getColor(R.color.ttred));
                box.setColorFilter(getResources().getColor(R.color.ttred), PorterDuff.Mode.ADD);
                break;

        }
        t.setBackground(box);
    }

}
