package com.example.coen_elec_390_project_winter_2023.Models;

import java.util.Date;

public class Appointment {
    String doctorName;

    String reason;

    Date requestDate;

    public Appointment(){

    }

    public Appointment(String doctorName, String reason, Date requestDate) {
        this.doctorName = doctorName;
        this.reason = reason;
        this.requestDate= requestDate;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


}
