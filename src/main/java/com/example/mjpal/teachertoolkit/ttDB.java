package com.example.mjpal.teachertoolkit;

public class ttDB {

    private int _id;
    private String _week;
    private String _day;
    private String _period;
    private String _lesson;
    private String _room;
    private String _hwkset;

    public ttDB() {

    }

    public ttDB(String _week, String _day, String _period, String _lesson, String _room, String _hwkset){
        //this._id = _id;
        this._week = _week;
        this._day = _day;
        this._period = _period;
        this._lesson = _lesson;
        this._room = _room;
        this._hwkset = _hwkset;
    }

    public void set_id(int _id){
        this._id = _id;
    }

    public void set_week(String _week){
        this._week = _week;
    }

    public void set_day(String _day){
        this._day = _day;
    }

    public void set_period(String _period){
        this._period = _period;
    }

    public void set_lesson(String _lesson){
        this._lesson = _lesson;
    }

    public void set_room(String _room){
        this._room = _room;
    }

    public void set_hwkset(String _hwkset) {this._hwkset = _hwkset;}

    public int get_id() {
        return _id;
    }

    public String get_week() {
        return _week;
    }

    public String get_day() {
        return _day;
    }

    public String get_period() {
        return _period;
    }

    public String get_lesson() {
        return _lesson;
    }

    public String get_room() {
        return _room;
    }

    public String get_hwkset() { return _hwkset; }
}
