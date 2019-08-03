package com.example.mjpal.teachertoolkit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class myDBHandler extends SQLiteOpenHelper {

    private SimpleDateFormat sdf;

    private static final int DATABASE_VERSION = 1;
    private static final String APP_VERSION = "1.0";
    private static final String DATABASE_NAME = "tt.db";
    public static final String TABLE_TT = "tt_table";


    //Variables for table Lesson
    public static final String COLUMN_WEEK = "_week";
    public static final String COLUMN_DAY = "_day";
    public static final String COLUMN_PERIOD = "_period";
    public static final String COLUMN_LESSON = "_lesson";
    public static final String COLUMN_ROOM = "_room";
    public static final String COLUMN_HWKSET = "_hwkset";

    //Variables for tables groups
    public static final String TABLE_GROUPS = "tt_groups";
    public static final String COLUMN_GROUP = "_group";
    public static final String COLUMN_COLOUR = "_colour";

    //variables for Rooms
    public static final String TABLE_ROOMS = "tt_rooms";
    public static final String COLUMN_ROOM2 = "_room";

    //variables for term dates
    public static final String TABLE_TERM = "tt_terms";
    public static final String COLUMN_WEEKBEG = "_week_beg";
    public static final String COLUMN_MON = "_mon";
    public static final String COLUMN_TUE = "_tue";
    public static final String COLUMN_WED = "_wed";
    public static final String COLUMN_THUR = "_thur";
    public static final String COLUMN_FRI = "_fri";
    public static final String COLUMN_AORB = "_a_or_b";

    public static final String TABLE_REMINDER = "tt_reminders";
    public static final String COLUMN_REMINDER_ITEM = "_reminderItem";
    public static final String COLUMN_REMINDER_DATE = "_reminderDate";

    public static final String TABLE_SETTINGS = "tt_settings";
    public static final String COLUMN_YEAR_START_DATE = "_yearStartDate";
    public static final String COLUMN_YEAR_END_DATE = "_yearEndDate";
    public static final String COLUMN_PERIODS_IN_DAY = "_periodsInDay";
    public static final String COLUMN_DB_VERSION_NUM = "_dbVersionNumber";
    public static final String COLUMN_APP_NUM = "_appNumber";

    public termWeek termStart;
    public termWeek termEnd;
    private SimpleDateFormat reminderSDF;

    //**************************************************************************
    //Construction and Update functions
    //**************************************************************************
    public myDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        reminderSDF = new SimpleDateFormat("yyyy/MM/dd");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        dropAllTables(db);
        createNewTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropAllTables(db);
    }

    public void dropAllTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROOMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TERM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        //onCreate(db);
    }

    public void dropAllTables() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROOMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TERM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
    }

    public void createNewTables(SQLiteDatabase db) {
        createTTDB(db);
        createGroupTable(db);
        createRoomTable(db);
        createTermTable(db);
        createReminderTable(db);
        createSettingsTable(db);
    }

    public void createNewTables() {
        createTTDB();
        createGroupTable();
        createRoomTable();
        createTermTable();
        createReminderTable();
        createSettingsTable();
    }

    //**************************************************************************
    //FOR MANIPULATION OF THE TIMETABLE TABLE
    //**************************************************************************

    public void createTTDB(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_TT + "(" +
                //COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_WEEK + " TEXT, " +
                COLUMN_DAY + " TEXT, " +
                COLUMN_PERIOD + " TEXT, " +
                COLUMN_LESSON + " TEXT, " +
                COLUMN_ROOM + " TEXT, " +
                COLUMN_HWKSET + ");";

        db.execSQL(query);
    }

    public void createTTDB() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_TT + "(" +
                //COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_WEEK + " TEXT, " +
                COLUMN_DAY + " TEXT, " +
                COLUMN_PERIOD + " TEXT, " +
                COLUMN_LESSON + " TEXT, " +
                COLUMN_ROOM + " TEXT, " +
                COLUMN_HWKSET + ");";

        db.execSQL(query);
    }

    public void clearTimetableFromDB () {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TT);
    }

    public void addLesson(ttDB ttdb) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_WEEK, ttdb.get_week());
        values.put(COLUMN_DAY, ttdb.get_day());
        values.put(COLUMN_PERIOD, ttdb.get_period());
        values.put(COLUMN_LESSON, ttdb.get_lesson());
        values.put(COLUMN_ROOM, ttdb.get_room());
        values.put(COLUMN_HWKSET, ttdb.get_hwkset());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_TT, null, values);
        db.close();
    }

    public void deleteLesson(String week, String day, String period) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TT + " WHERE " + COLUMN_WEEK + "=\"" + week + "\" AND " +
                COLUMN_DAY + "=\"" + day + "\" AND " + COLUMN_PERIOD + "=\"" + period + "\";");
    }

    //Can probably be deleted now (both functions)
    public String databaseToString2() {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TT + " WHERE 1";
        //String query = "SELECT * FROM tt_table;";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("week")) != null){
                dbString += c.getString(c.getColumnIndex("week"));
                dbString += "\n";
            }
        }

        db.close();
        return dbString;
    }
    public String databaseToString() {
        return "Woohoo";
    }

    public ttDB[] getLesson(String week, String day){

        ttDB[] results = new ttDB[6];

        String query = "SELECT * FROM " + TABLE_TT + " WHERE " +
                COLUMN_WEEK + " = \"" + week + "\" AND " +
                COLUMN_DAY + " = \"" + day + "\";";

        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.rawQuery(query, null);

        if (c.getCount() == 0){
            for (int i = 0; i < 6; i++) {
                int p = i + 1;
                results[i] = new ttDB(week, day, Integer.toString(p), "FREE", "N/A", "NO HWK SET");
            }
        }
        else {
            c.moveToFirst();
            int counter = 0;
            while (!c.isAfterLast()) {

                if (c.getString(0) != null) {
                    results[counter] = new ttDB(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5));
                }
                c.moveToNext();
                counter += 1;
            }
            db.close();
        }
        return results;
    }

    public void updateLesson(ttDB a){

        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE " + TABLE_TT +
                " SET " + COLUMN_LESSON + " = \"" + a.get_lesson() + "\", " + COLUMN_ROOM + " = \"" + a.get_room() + "\", " + COLUMN_HWKSET + " = \"" + a.get_hwkset() + "\"" +
                " WHERE " + COLUMN_WEEK + " = \"" + a.get_week() + "\" AND " + COLUMN_DAY + " = \"" + a.get_day() + "\" AND " + COLUMN_PERIOD + " = \"" + a.get_period() + "\";";

        db.execSQL(query);
        db.close();
    }

    public void resetLesson(String oldLesson) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE " + TABLE_TT + " SET " + COLUMN_LESSON + " = \"FREE\", " + COLUMN_ROOM + " = \"N/a\", " + COLUMN_HWKSET + " = \"NO HWK SET\"" + " WHERE " + COLUMN_LESSON + " = \"" + oldLesson + "\";";
        db.execSQL(query);
        db.close();
    }

    public String getColourForLesson(String lesson) {
        SQLiteDatabase db = getWritableDatabase();
        String returnString = "";
        String query = "SELECT " + COLUMN_COLOUR + " FROM " + TABLE_GROUPS + " WHERE " + COLUMN_GROUP + " = \"" + lesson + "\";";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        if (c.getString(0) != null) {
            returnString = c.getString(0);
        }
        return returnString;
    }

    //**************************************************************************
    //FOR MANIPULATION OF THE GROUPS AND ROOMS TABLE
    //**************************************************************************

    public void resetGroup(String oldRoom) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE " + TABLE_TT + " SET " + COLUMN_ROOM + " = \"N/a\"" + " WHERE " + COLUMN_ROOM + " = \"" + oldRoom + "\";";
        db.execSQL(query);
        db.close();
    }

    public void addGroup(String data, String colour) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        values.put(COLUMN_GROUP, data);
        values.put(COLUMN_COLOUR, colour);
        db.insert(TABLE_GROUPS, null, values);


        db.close();
    }

    public void createGroupTable(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_GROUPS + "(" +
                COLUMN_GROUP + " TEXT, " +
                COLUMN_COLOUR + " TEXT " + ");";
        db.execSQL(query);
    }

    public void createGroupTable() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_GROUPS + "(" +
                COLUMN_GROUP + " TEXT, " +
                COLUMN_COLOUR + " TEXT " + ");";
        db.execSQL(query);
    }



    public void clearGroupsDB () {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_GROUPS);
    }

    public void deleteGroup(String group) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_GROUPS + " WHERE " + COLUMN_GROUP + "=\"" + group + "\";");
    }

    public void editGroup(String group, String newColour) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_GROUPS + " SET " + COLUMN_COLOUR + "=\"" + newColour + "\" WHERE " + COLUMN_GROUP  + "=\"" + group + "\";");
    }

    public int checkGroupTableLength() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_GROUPS + ";";
        Cursor c = db.rawQuery(query, null);
        return c.getCount();
    }

    public String[][] getGroupData() {

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_GROUPS + ";";
        String[][] listOfData;
        int numResults = 0;

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        numResults = c.getCount();
        listOfData = new String[numResults][2];

        int counter = 0;
        if (!c.isNull(0)) {
            while (!c.isAfterLast()) {

                if (c.getString(0) != null) {
                    listOfData[counter][0] = c.getString(0);
                    listOfData[counter][1] = c.getString(1);
                }

                c.moveToNext();
                counter += 1;
            }
        }
        return listOfData;
    }

    //does both group and room now. May want to rename the function
    //do we still need this function
    public String groupDatabaseToString(String choice) {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query;

        switch (choice){
            case "groups":
                query = "SELECT * FROM " + TABLE_GROUPS + ";";

                Cursor c = db.rawQuery(query, null);
                c.moveToFirst();

                while(!c.isAfterLast()){
                    //if(c.getString(c.getColumnIndex("group")) != null){
                    if (c.getString(0) != null) {
                        //dbString += c.getString(c.getColumnIndex("group"));
                        dbString += c.getString(0) + " (" + c.getString(1) + ")";
                        dbString += "\n";
                    }
                    c.moveToNext();
                }

                db.close();

                break;


            case "rooms":
                query = "SELECT * FROM " + TABLE_ROOMS + ";";

                Cursor c2 = db.rawQuery(query, null);
                c2.moveToFirst();

                while(!c2.isAfterLast()){
                    //if(c.getString(c.getColumnIndex("group")) != null){
                    if (c2.getString(0) != null) {
                        //dbString += c.getString(c.getColumnIndex("group"));
                        dbString += c2.getString(0);
                        dbString += "\n";
                    }
                    c2.moveToNext();
                }

                db.close();
                break;
            default:
                return "Error - No Table Found";
        }
        return dbString;
    }

    // Functions for Rooms Table are all below
    public void createRoomTable(SQLiteDatabase db){
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_ROOMS + "(" +
                COLUMN_ROOM2 + " TEXT " + ");";
        db.execSQL(query);
    }

    public void createRoomTable(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_ROOMS + "(" +
                COLUMN_ROOM2 + " TEXT " + ");";
        db.execSQL(query);
    }

    public void addRoom(String room) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ROOM2, room);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_ROOMS, null, values);
        db.close();
    }

    public void clearRoomsDB () {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ROOMS);
        db.close();
    }

    public void deleteRoom(String room) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ROOMS + " WHERE " + COLUMN_ROOM2 + "=\"" + room + "\";");
        db.close();
    }

    public String[] getRoomData() {

        SQLiteDatabase db = getWritableDatabase();
        String query = "";
        String[] listOfData;
        int numResults = 0;


        query = "SELECT " + COLUMN_ROOM2 + " FROM " + TABLE_ROOMS + ";";
        Cursor c = db.rawQuery(query, null);

        c.moveToFirst();
        numResults = c.getCount();
        listOfData = new String[numResults];

        int counter = 0;
        if (numResults > 0) {
            while (!c.isAfterLast()) {
                if (c.getString(0) != null) {
                    listOfData[counter] = c.getString(0);
                }
                c.moveToNext();
                counter += 1;
            }
        }

        return listOfData;
    }

    public int checkRoomTableLength() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_ROOMS + ";";
        Cursor c = db.rawQuery(query, null);
        return c.getCount();
    }

    //**************************************************************************
    //FOR MANIPULATION OF THE TERM TABLE TABLE
    //**************************************************************************

    public void createTermTable(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_TERM + "(" +
                COLUMN_WEEKBEG + " TEXT PRIMARY KEY, " + COLUMN_MON + " TEXT, " +
                COLUMN_TUE + " TEXT, " + COLUMN_WED + " TEXT, " +
                COLUMN_THUR + " TEXT, " + COLUMN_FRI + " TEXT, " +
                COLUMN_AORB + " TEXT);";
        db.execSQL(query);
    }

    public void createTermTable() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_TERM + "(" +
                COLUMN_WEEKBEG + " TEXT PRIMARY KEY, " + COLUMN_MON + " TEXT, " +
                COLUMN_TUE + " TEXT, " + COLUMN_WED + " TEXT, " +
                COLUMN_THUR + " TEXT, " + COLUMN_FRI + " TEXT, " +
                COLUMN_AORB + " TEXT);";
        db.execSQL(query);
    }

    public termWeek getTermString(String startWeek) {
        SQLiteDatabase db = getWritableDatabase();
        termWeek termResults = new termWeek();

        String query = "SELECT * FROM " + TABLE_TERM + " WHERE " + COLUMN_WEEKBEG + " = \"" + startWeek + "\";";
        Cursor c = db.rawQuery(query, null);
        Date w = stringToDate(startWeek, "yyyy/MM/dd");

        if (c.getCount() == 0) {
            termResults = noAvailableDate(w);
        }
        else {

            c.moveToFirst();

            if (c.getString(0) != null) {

                termResults.setWeek_beg(w);
                termResults.setMon(c.getString(1));
                termResults.setTue(c.getString(2));
                termResults.setWed(c.getString(3));
                termResults.setThur(c.getString(4));
                termResults.setFri(c.getString(5));
                termResults.setWeekAorB(c.getString(6));
            }
        }

        return termResults;
    }

    public termWeek[] getAllTermStrings() {
        termWeek[] termResults;
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_TERM + ";";
        Cursor c = db.rawQuery(query, null);

        if (c.getCount() == 0) {
            termResults = new termWeek[1];
            termResults[0] = new termWeek();
            //return termResults;
        }
        else {
            int numWeeks = c.getCount();
            int counter = 0;
            termResults = new termWeek[numWeeks];

            c.moveToFirst();
            while (c.isAfterLast() != true) {
                termResults[counter] = new termWeek();

                Date w = stringToDate(c.getString(0), "yyyy/MM/dd");
                termResults[counter].setWeek_beg(w);
                termResults[counter].setMon(c.getString(1));
                termResults[counter].setTue(c.getString(2));
                termResults[counter].setWed(c.getString(3));
                termResults[counter].setThur(c.getString(4));
                termResults[counter].setFri(c.getString(5));
                termResults[counter].setWeekAorB(c.getString(6));

                counter += 1;
                c.moveToNext();
            }
        }

        return termResults;
    }

    public void addTermData(termWeek t){
        ContentValues values = new ContentValues();
        values.put(COLUMN_WEEKBEG, reminderSDF.format(t.getWeek_beg()));
        values.put(COLUMN_MON, t.getMon());
        values.put(COLUMN_TUE, t.getTue());
        values.put(COLUMN_WED, t.getWed());
        values.put(COLUMN_THUR, t.getThur());
        values.put(COLUMN_FRI, t.getFri());
        values.put(COLUMN_AORB, t.getWeekAorB());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_TERM, null, values);
        db.close();
    }

    public void updateTerm(String start, String end){

        termWeek[] termResults = getAllTermStrings();
        int lengthOfTermArray = termResults.length;

        Date startDate = stringToDate(start, "dd/MM/yyyy");
        Date endDate = stringToDate(end, "dd/MM/yyyy");

        Calendar pointerCal = Calendar.getInstance();
        Calendar weekEndCal = Calendar.getInstance();

        for (int counter = 0; counter < lengthOfTermArray; counter++) {

            Date ws = termResults[counter].getWeek_beg();
            weekEndCal.setTime(ws);
            weekEndCal.add(Calendar.DATE, 4);
            pointerCal.setTime(ws);

            int daysOffCounter = 0;
            for (int counter2 = 2; counter2 <= 6; counter2++) {
                if ((pointerCal.getTime().after(startDate) && pointerCal.getTime().before(endDate)) || pointerCal.getTime().equals(startDate) || pointerCal.getTime().equals(endDate)) {
                    //only changes the day of week beginning
                    if (pointerCal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                        termResults[counter].setMon("H");
                        daysOffCounter += 1;
                    } else if (pointerCal.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
                        termResults[counter].setTue("H");
                        daysOffCounter += 1;
                    } else if (pointerCal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
                        termResults[counter].setWed("H");
                        daysOffCounter += 1;
                    } else if (pointerCal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
                        termResults[counter].setThur("H");
                        daysOffCounter += 1;
                    } else if (pointerCal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                        termResults[counter].setFri("H");
                        daysOffCounter += 1;
                    }
                }
                pointerCal.add(Calendar.DATE, 1);
            }

            if (daysOffCounter == 5){
                termResults[counter].setWeekAorB("H");
            }
        }

        String query = "";
        int week_counter = 0;
        String[] week = new String[2];
        week[0] = "A";
        week[1] = "B";

        for (int x = 0; x < lengthOfTermArray; x++) {
            if (!termResults[x].getWeekAorB().equalsIgnoreCase("H")) {
                termResults[x].setWeekAorB(week[week_counter]);
                if (week_counter == 0) {
                    week_counter += 1;
                }
                else {
                    week_counter = 0;
                }
            }

            SQLiteDatabase db = getWritableDatabase();
            query = "UPDATE " + TABLE_TERM +
                    " SET " + COLUMN_WEEKBEG + " = \"" + reminderSDF.format(termResults[x].getWeek_beg()) + "\", " + COLUMN_MON + " = \"" + termResults[x].getMon() + "\", " + COLUMN_TUE + " = \"" + termResults[x].getTue()
                    + "\", " + COLUMN_WED + " = \"" + termResults[x].getWed() + "\", " + COLUMN_THUR + " = \"" + termResults[x].getThur() + "\", " + COLUMN_FRI + " = \"" + termResults[x].getFri() + "\", " + COLUMN_AORB + " = \"" + termResults[x].getWeekAorB() + "\"" +
                    " WHERE " + COLUMN_WEEKBEG + " = \"" + reminderSDF.format(termResults[x].getWeek_beg()) + "\";";
            db.execSQL(query);
            db.close();
        }
    }

    public void deleteTerm(String start, String end){

        termWeek[] termResults = getAllTermStrings();
        int lengthOfTermArray = termResults.length;

        Date startDate = stringToDate(start, "dd/MM/yyyy");
        Date endDate = stringToDate(end, "dd/MM/yyyy");

        Calendar pointerCal = Calendar.getInstance();
        Calendar weekEndCal = Calendar.getInstance();

        for (int counter = 0; counter < lengthOfTermArray; counter++) {

            Date ws = termResults[counter].getWeek_beg();
            weekEndCal.setTime(ws);
            weekEndCal.add(Calendar.DATE, 4);
            pointerCal.setTime(ws);

            int daysOffCounter = 0;
            for (int counter2 = 2; counter2 <= 6; counter2++) {
                if ((pointerCal.getTime().after(startDate) && pointerCal.getTime().before(endDate)) || pointerCal.getTime().equals(startDate) || pointerCal.getTime().equals(endDate)) {
                    //only changes the day of week beginning
                    if (pointerCal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                        termResults[counter].setMon("Y");
                        daysOffCounter += 1;
                    } else if (pointerCal.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
                        termResults[counter].setTue("Y");
                        daysOffCounter += 1;
                    } else if (pointerCal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
                        termResults[counter].setWed("Y");
                        daysOffCounter += 1;
                    } else if (pointerCal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
                        termResults[counter].setThur("Y");
                        daysOffCounter += 1;
                    } else if (pointerCal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                        termResults[counter].setFri("Y");
                        daysOffCounter += 1;
                    }
                }
                pointerCal.add(Calendar.DATE, 1);
            }

            if (daysOffCounter == 5){
                termResults[counter].setWeekAorB("X");
            }
        }

        String query = "";
        int week_counter = 0;
        String[] week = new String[2];
        week[0] = "A";
        week[1] = "B";

        for (int x = 0; x < lengthOfTermArray; x++) {
            if (!termResults[x].getWeekAorB().equalsIgnoreCase("H")) {
                termResults[x].setWeekAorB(week[week_counter]);
                if (week_counter == 0) {
                    week_counter += 1;
                }
                else {
                    week_counter = 0;
                }
            }

            SQLiteDatabase db = getWritableDatabase();
            query = "UPDATE " + TABLE_TERM +
                    " SET " + COLUMN_WEEKBEG + " = \"" + reminderSDF.format(termResults[x].getWeek_beg()) + "\", " + COLUMN_MON + " = \"" + termResults[x].getMon() + "\", " + COLUMN_TUE + " = \"" + termResults[x].getTue()
                    + "\", " + COLUMN_WED + " = \"" + termResults[x].getWed() + "\", " + COLUMN_THUR + " = \"" + termResults[x].getThur() + "\", " + COLUMN_FRI + " = \"" + termResults[x].getFri() + "\", " + COLUMN_AORB + " = \"" + termResults[x].getWeekAorB() + "\"" +
                    " WHERE " + COLUMN_WEEKBEG + " = \"" + reminderSDF.format(termResults[x].getWeek_beg()) + "\";";
            db.execSQL(query);
            db.close();
        }
    }

    public Date findStartWeekDate(Date d) {
        Calendar startofWeek = Calendar.getInstance();
        startofWeek.setTime(d);
        int tempDay = startofWeek.get(Calendar.DAY_OF_WEEK);

        if (tempDay < 2) {
            startofWeek.add(Calendar.DATE, 1);
            //If its sunday move to the next Monday date
        }

        while (tempDay > 2) {
            startofWeek.add(Calendar.DATE, -1);
            tempDay -= 1;
        }

        return startofWeek.getTime();
    }

    public termWeek findStartWeek(Date startOfWeekDate) {

        Date newDate = findStartWeekDate(startOfWeekDate);
        termWeek weekData = getTermString(reminderSDF.format(newDate));

        return weekData;
    }

    public Date stringToDate(String aDate, String aFormat) {
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat sdf2 = new SimpleDateFormat(aFormat);
        Date stringDate = sdf2.parse(aDate, pos);

        return stringDate;
    }

    //Can probably delete the first and last date functions now.
    public Date getLastDate() {
        SQLiteDatabase db = getWritableDatabase();
        termEnd = new termWeek();

        String query = "SELECT * FROM " + TABLE_TERM + ";";

        Cursor c = db.rawQuery(query, null);
        c.moveToLast();

        if (c.getString(0) != null) {

            Date w2 = stringToDate(c.getString(0), "yyyy/MM/dd");
            termEnd.setWeek_beg(w2);

            termEnd.setMon(c.getString(1));
            termEnd.setTue(c.getString(2));
            termEnd.setWed(c.getString(3));
            termEnd.setThur(c.getString(4));
            termEnd.setFri(c.getString(5));
            termEnd.setWeekAorB(c.getString(6));
        }

        int counter = 0;
        if (termEnd.getFri().equalsIgnoreCase("y")) {
            counter += 1;
        }
        if (termEnd.getThur().equalsIgnoreCase("y")) {
            counter += 1;
        }
        if (termEnd.getWed().equalsIgnoreCase("y")) {
            counter += 1;
        }
        if (termEnd.getTue().equalsIgnoreCase("y")) {
            counter += 1;
        }

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(termEnd.getWeek_beg());
        if (counter >= 1) {
            endCal.add(Calendar.DAY_OF_MONTH, counter);
        }
        Date actualYearEnd = endCal.getTime();
        return actualYearEnd;
    }

    public Date getFirstDate() {
            //Gets the first and last entry from the term table
            SQLiteDatabase db = getWritableDatabase();
            termStart = new termWeek();

            String query = "SELECT * FROM " + TABLE_TERM + ";";

            Cursor c = db.rawQuery(query, null);
            c.moveToFirst();

            if (c.getString(0) != null) {

                Date w = stringToDate(c.getString(0), "yyyy/MM/dd");
                termStart.setWeek_beg(w);
                termStart.setMon(c.getString(1));
                termStart.setTue(c.getString(2));
                termStart.setWed(c.getString(3));
                termStart.setThur(c.getString(4));
                termStart.setFri(c.getString(5));
                termStart.setWeekAorB(c.getString(6));
            }



        int counter = 0;
        if (termStart.getMon().equalsIgnoreCase("n")) {
            counter += 1;
        }
        if (termStart.getTue().equalsIgnoreCase("n")) {
            counter += 1;
        }
        if (termStart.getWed().equalsIgnoreCase("n")) {
            counter += 1;
        }
        if (termStart.getThur().equalsIgnoreCase("n")) {
            counter += 1;
        }

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(termStart.getWeek_beg());
        if (counter >= 1) {
            startCal.add(Calendar.DAY_OF_MONTH, counter);
        }

        Date actualYearStart = startCal.getTime();

        return actualYearStart;
    }

    public termWeek noAvailableDate(Date d) {

        termWeek t = new termWeek(d, "N", "N", "N", "N", "N", "N");
        return t;
    }

    //**************************************************************************
    //FOR MANIPULATION OF THE REMINDERS TABLE
    //**************************************************************************

    public void createReminderTable(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_REMINDER + "(" +
                COLUMN_REMINDER_ITEM + " TEXT, " + COLUMN_REMINDER_DATE + " DATE);";
        db.execSQL(query);
    }

    public void createReminderTable() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_REMINDER + "(" +
                COLUMN_REMINDER_ITEM + " TEXT, " + COLUMN_REMINDER_DATE + " DATE);";
        db.execSQL(query);
    }

    public void addReminder(String reminder, String date) {
        Date newDateForDB = stringToDate(date, "dd/MM/yyyy");
        String newDateString = reminderSDF.format(newDateForDB);

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_REMINDER_ITEM, reminder);
        contentValues.put(COLUMN_REMINDER_DATE, newDateString);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_REMINDER, null, contentValues);
        db.close();
    }

    public void deleteReminder(String reminder, String date) {
        Date newDateForDB = stringToDate(date, "dd/MM/yyyy");
        String newDateString = reminderSDF.format(newDateForDB);

        String query = "DELETE FROM " + TABLE_REMINDER + " WHERE " + COLUMN_REMINDER_ITEM + " = \"" + reminder + "\" AND " +
                COLUMN_REMINDER_DATE + " = \"" +  newDateString + "\";";

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    public String[][] getReminders(String Source, String startDateString) {
        String[][] results;

        String query = "SELECT * FROM " + TABLE_REMINDER + " ORDER BY " + COLUMN_REMINDER_DATE + " ASC;";

        if (Source.equalsIgnoreCase("Dashboard")) {
            Date startDate = stringToDate(startDateString, "dd/MM/yyyy");
            Calendar reminderCal = Calendar.getInstance();
            reminderCal.setTime(startDate);
            reminderCal.add(Calendar.DATE, 28);
            Date endDate = reminderCal.getTime();



            String startDateForQuery = reminderSDF.format(startDate);
            String endDateForQuery = reminderSDF.format(endDate);


            query = "SELECT * FROM " + TABLE_REMINDER + " WHERE " +
                    COLUMN_REMINDER_DATE + " >= \"" + startDateForQuery + "\" and " +
                    COLUMN_REMINDER_DATE + " <= \"" + endDateForQuery + "\";";
        }

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        //db.close();

        int numResults = c.getCount();

        if (numResults > 0) {
            results = new String[numResults][2];
            c.moveToFirst();
            for (int i = 0; i < numResults; i++) {
                results[i][0] = c.getString(0);
                String dateString = c.getString(1);
                Date reminderDate = stringToDate(dateString, "yyyy/MM/dd");
                String newDateString = sdf.format(reminderDate);
                results[i][1] = newDateString;
                c.moveToNext();
            }
        }
        else {
            results = new String[1][2];
            results[0][0] = "No Results";
            results[0][1] = "";
        }

        return results;
    }

    //**************************************************************************
    //FOR SETTINGS TABLE
    //**************************************************************************

    public void createSettingsTable(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_SETTINGS + "(" +
                COLUMN_DB_VERSION_NUM + " TEXT, " + COLUMN_APP_NUM + " TEXT, " + COLUMN_PERIODS_IN_DAY + " TEXT, " +
                COLUMN_YEAR_START_DATE + " DATE, " + COLUMN_YEAR_END_DATE + " DATE);";
        db.execSQL(query);
        setupSettingsTable(db);

    }

    public void createSettingsTable() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_SETTINGS + "(" +
                COLUMN_DB_VERSION_NUM + " TEXT, " + COLUMN_APP_NUM + " TEXT, " + COLUMN_PERIODS_IN_DAY + " TEXT, " +
                COLUMN_YEAR_START_DATE + " DATE, " + COLUMN_YEAR_END_DATE + " DATE);";
        db.execSQL(query);
        setupSettingsTable(db);
        db.close();

    }

    public void setupSettingsTable(SQLiteDatabase db) {

        String query = "SELECT * FROM " + TABLE_SETTINGS + ";";
        Cursor c = db.rawQuery(query, null);

        if (c.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_DB_VERSION_NUM, DATABASE_VERSION);
            values.put(COLUMN_APP_NUM, APP_VERSION);

            db.insert(TABLE_SETTINGS, null, values);
            //db.close();
        }
    }

    public void updateStartAndEndDate(String newYearStartDate, String newYearEndDate){
        SQLiteDatabase db = getWritableDatabase();

        Date tempStartDate = new java.util.Date();
        tempStartDate = stringToDate(newYearStartDate, "dd/MM/yyyy");
        Date tempEndDate = new java.util.Date();
        tempEndDate = stringToDate(newYearEndDate, "dd/MM/yyyy");

        String query = "UPDATE " + TABLE_SETTINGS +
                " SET " + COLUMN_YEAR_START_DATE+ " = \"" + reminderSDF.format(tempStartDate) + "\", " + COLUMN_YEAR_END_DATE + " = \"" + reminderSDF.format(tempEndDate) + "\"" +
                " WHERE " + COLUMN_DB_VERSION_NUM + " = \"" + DATABASE_VERSION + "\" AND " + COLUMN_APP_NUM + " = \"" + APP_VERSION + "\"";

        db.execSQL(query);
        db.close();
    }

    public String getStartOrEndDate(String choice) {
        String yearDate;
        String query;

        SQLiteDatabase db = getWritableDatabase();
        if (choice.equalsIgnoreCase("start")) {
            query = "SELECT " + COLUMN_YEAR_START_DATE + " FROM " + TABLE_SETTINGS + " WHERE " + COLUMN_APP_NUM + " = \"" + APP_VERSION + "\" AND " + COLUMN_DB_VERSION_NUM + " = \"" + DATABASE_VERSION + "\";";
        }
        else if (choice.equalsIgnoreCase("end")) {
            query = "SELECT " + COLUMN_YEAR_END_DATE + " FROM " + TABLE_SETTINGS + " WHERE " + COLUMN_APP_NUM + " = \"" + APP_VERSION + "\" AND " + COLUMN_DB_VERSION_NUM + " = \"" + DATABASE_VERSION + "\";";
        }
        else {
            query = "SELECT "  + COLUMN_YEAR_END_DATE + " FROM " + TABLE_SETTINGS + ";";
        }

        Cursor c = db.rawQuery(query, null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            if (c.isNull(0)) {
                yearDate = "No date in database";
            }
            else {
                yearDate = c.getString(0);
                Date tempDate = stringToDate(yearDate, "yyyy/MM/dd");
                yearDate = sdf.format(tempDate);
            }

        }
        else {
            yearDate = "No date in database";
        }

        return yearDate;
    }

    public boolean checkStartIsBeforeEndDate(String start, String end, String format) {
        Date startDate = stringToDate(start, format);
        Date endDate = stringToDate(end, format);

        if (startDate.before(endDate)) { return true; }
        else { return false; }
    }

    public void updateSchoolYearWeeks(String start, String end, String format){

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TERM);
        db.close();
        createTermTable();

        Date startDate = stringToDate(start, format);
        Date endDate = stringToDate(end, format);

        int counter = 0;
        Date newDate = firstDayOfWeek(startDate);

        while (endDate.after(newDate)){
            counter += 1;
            newDate = minusOrAddDayFromDate(newDate, 7);
        }

        termWeek[] tempTermWeek = new termWeek[counter];

        counter = 0;
        newDate = firstDayOfWeek(startDate);

        while (endDate.after(newDate)){

            //Works out whether its week A or B
            String weekAorB = "";
            if (counter % 2 == 0){ weekAorB = "A"; }
            else { weekAorB = "B"; }

            String[] weekData = checkWeek(newDate);
            tempTermWeek[counter] = new termWeek(newDate, weekData, weekAorB);
            addTermData(tempTermWeek[counter]); //Add term data to the database

            //Move on 7 days
            newDate = minusOrAddDayFromDate(newDate, 7);

            //update counter to get array number
            counter += 1;

        }
        /*String results = "";
        for (int x = 0; x < tempTermWeek.length; x++) {

            results +=  tempTermWeek[x].toString();
        }*/
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

    public Date minusOrAddDayFromDate(Date d, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DAY_OF_WEEK, amount);

        Date newDate = c.getTime();
        return newDate;
    }

    public String[] checkWeek(Date WB){
        String[] data = new String[5];
        int new_counter = 0;
        String startDateString = getStartOrEndDate("start");
        Date startDate = stringToDate(startDateString, "dd/MM/yyyy");

        String endDateString = getStartOrEndDate("end");
        Date endDate = stringToDate(endDateString, "dd/MM/yyyy");


        Date tempDate = WB;

        Date new_start_date = minusOrAddDayFromDate(startDate, -1);
        Date new_end_date = minusOrAddDayFromDate(endDate, 1);

        while (new_counter < 5){
            if(new_start_date.before(tempDate) && new_end_date.after(tempDate)){ data[new_counter] = "Y"; }
            else { data[new_counter] = "N"; }
            tempDate = minusOrAddDayFromDate(tempDate, 1);
            new_counter += 1;
        }
        return data;
    }

    public String getAppAndDBVersion(String choice) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_SETTINGS + ";";
        String result = "";
        Cursor c = db.rawQuery(query, null);
        //db.close();

        if (c.getCount() > 0){
            c.moveToFirst();
            if (choice.equalsIgnoreCase("app")) {
                result = c.getString(1);
            }
            else if (choice.equalsIgnoreCase("db")) {
                result = c.getString(0);
            }
            else {
                result = "error";
            }
        }

        return result;
    }
}


