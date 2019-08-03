package com.example.mjpal.teachertoolkit;

import java.util.Date;

public class Holidays {
    private Date startDate;
    private Date endDate;
    private int holidayID;

    public Holidays() {
    }

    public Holidays(Date startDate, Date endDate, int newID) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.holidayID = newID;
    }

    public Holidays(int holidayCounter) {
        this.holidayID = holidayCounter;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getHolidayID() { return holidayID; }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setHolidayID(int newHolidayID) { this.holidayID = newHolidayID; }
}
