package com.example.mjpal.teachertoolkit;

import java.util.Date;

public class termWeek {

    private Date week_beg;
    private String Mon;
    private String Tue;
    private String Wed;
    private String Thur;
    private String Fri;
    private String WeekAorB;

    public termWeek(Date week_beg, String mon, String tue, String wed, String thur, String fri, String weekAorB) {
        this.week_beg = week_beg;
        Mon = mon;
        Tue = tue;
        Wed = wed;
        Thur = thur;
        Fri = fri;
        WeekAorB = weekAorB;

    }

    public termWeek() {
    }

    public termWeek(Date week_beg, String[] days, String weekAorB) {
        this.week_beg = week_beg;
        Mon = days[0];
        Tue = days[1];
        Wed = days[2];
        Thur = days[3];
        Fri = days[4];
        WeekAorB = weekAorB;
    }

    public Date getWeek_beg() {
        return week_beg;
    }

    public String getMon() {
        return Mon;
    }

    public String getTue() {
        return Tue;
    }

    public String getWed() {
        return Wed;
    }

    public String getThur() {
        return Thur;
    }

    public String getFri() {
        return Fri;
    }

    public String getWeekAorB() {
        return WeekAorB;
    }

    public void setWeek_beg(Date week_beg) {
        this.week_beg = week_beg;
    }

    public void setMon(String mon) {
        Mon = mon;
    }

    public void setTue(String tue) {
        Tue = tue;
    }

    public void setWed(String wed) {
        Wed = wed;
    }

    public void setThur(String thur) {
        Thur = thur;
    }

    public void setFri(String fri) {
        Fri = fri;
    }

    public void setWeekAorB(String weekAorB) {
        WeekAorB = weekAorB;
    }

    public String toString() {
        String x = this.week_beg.toString() + ", " + this.Mon + ", " + this.Tue + " " + this.Wed + ", " + this.Thur + " " + this.Fri + " " + this.WeekAorB + "\n";
        return x;
    }

}
