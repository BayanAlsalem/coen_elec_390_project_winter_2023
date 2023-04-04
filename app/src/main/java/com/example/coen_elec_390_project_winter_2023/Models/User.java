package com.example.coen_elec_390_project_winter_2023.Models;

public abstract class User {
    public String uid;
    public String hospital;
    public String name;
    public String age;
    public String experience;
    public String city;
    public String country;
    public String doctorName;
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
    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public String getExperience() {
        return experience;
    }
    public void setExperience(String experience) {
        this.experience = experience;
    }
    public void setCity(){
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public userOptions.userType getUserType() {
        return type;
    }

    @Override
    public String toString() {
        return name;
    }
}

