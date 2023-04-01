package com.example.coen_elec_390_project_winter_2023.Models;

public abstract class User {
    public String uid;
    public String hospital;
    public String name;
    public String email;
    public String password;
    public userOptions.userType type;

    public String getUid() {
        return uid;
    }
    public void setUID(String uid) {
        this.uid = uid;
    }
    public String getHospital(){return hospital;}
    public void setHospital(String hospital){this.hospital = hospital;}
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public userOptions.userType getUserType() {
        return type;
    }

    @Override
    public String toString() {
        return name;
    }
}

