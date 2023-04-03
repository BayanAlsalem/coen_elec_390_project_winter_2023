package com.example.coen_elec_390_project_winter_2023.Models;

public class Patient extends User {
    public Patient() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Patient(String name, String email, String password, String uid) {
        this.name = name;
        this.age = email;
        this.experience = password;
        this.uid = uid;
        this.type = userOptions.userType.PATIENT;
    }
}

