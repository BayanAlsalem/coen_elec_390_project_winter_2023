package com.example.coen_elec_390_project_winter_2023.Models;

public class Doctor extends User {
    public Doctor() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Doctor(String name,String hospital, String email, String password, String uid) {
        this.name = name;
        this.hospital = hospital;
        this.age = email;
        this.experience = password;
        this.uid = uid;
        this.type = userOptions.userType.DOCTOR;
    }


}
