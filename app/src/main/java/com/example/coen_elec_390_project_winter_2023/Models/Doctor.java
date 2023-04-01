package com.example.coen_elec_390_project_winter_2023.Models;

import java.util.List;

public class Doctor extends User {
    private List<String> patientsList;

    public Doctor() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Doctor(String name,String hospital, String email, String password, String uid, List<String> patientsList) {
        this.name = name;
        this.hospital = hospital;
        this.email = email;
        this.password = password;
        this.uid = uid;
        this.type = userOptions.userType.DOCTOR;
        this.patientsList = patientsList;
    }

    public List<String> getPatientsList() {
        return patientsList;
    }
}
