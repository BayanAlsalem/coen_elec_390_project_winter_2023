package com.example.coen_elec_390_project_winter_2023.Models;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class Reading {
    String userID;
    List<Integer> flexedValues;
    List<Integer> restedValues;
    Date readingDate;

    @Override
    public String toString() {
        return "Reading{" +
                "userID='" + userID + '\'' +
                ", flexedValues=" + flexedValues +
                ", restedValues=" + restedValues +
                ", readingDate=" + readingDate +
                '}';
    }
    public Reading(){

    }
    public Reading(String userID, List<Integer> flexedValues, List<Integer> restedValues, Timestamp readingDate) {
        this.userID = userID;
        this.flexedValues = flexedValues;
        this.restedValues = restedValues;
        this.readingDate = readingDate;
    }

    public List<Integer> getFlexedValues() {
        return flexedValues;
    }

    public void setFlexedValues(List<Integer> flexedValues) {
        this.flexedValues = flexedValues;
    }

    public List<Integer> getRestedValues() {
        return restedValues;
    }

    public void setRestedValues(List<Integer> restedValues) {
        this.restedValues = restedValues;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


    public Date getReadingDate() {
        return readingDate;
    }

    public void setReadingDate(Date readingDate) {
        this.readingDate = readingDate;
    }
}
