package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coen_elec_390_project_winter_2023.R;

public class DoctorDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_test_layout);

        // Set click listeners for editMyProfileDoc
        TextView editMyProfileDocText = findViewById(R.id.editMyProfileDocTextID);
        ImageView editMyProfileDocImage = findViewById(R.id.editMyProfileDocImageID);
        editMyProfileDocText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorDashboard.this, DoctorProfileActivity.class);
                startActivity(intent);
            }
        });
        editMyProfileDocImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorDashboard.this, DoctorProfileActivity.class);
                startActivity(intent);
            }
        });

        // Set click listeners for myPatients
        TextView myPatientsText = findViewById(R.id.myPatientsTextID);
        ImageView myPatientsImage = findViewById(R.id.myPatientsImageID);
        myPatientsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorDashboard.this, ListOfPatientsActivity.class);
                startActivity(intent);
            }
        });
        myPatientsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorDashboard.this, ListOfPatientsActivity.class);
                startActivity(intent);
            }
        });

//        // Set click listeners for addPatient
//        TextView addPatientText = findViewById(R.id.addPatientTextID);
//        ImageView addPatientImage = findViewById(R.id.addPatientImageID);
//        addPatientText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(DoctorDashboard.this, AddPatient.class);
//                startActivity(intent);
//            }
//        });
//        addPatientImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(DoctorDashboard.this, AddPatient.class);
//                startActivity(intent);
//            }
//        });

        // Set click listeners for requestAppointment
        TextView requestAppointmentText = findViewById(R.id.requestAppointmentTextID);
        ImageView requestAppointmentImage = findViewById(R.id.requestAppointmentImageID);
        requestAppointmentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorDashboard.this, RequestAppointmentActivity.class);
                startActivity(intent);
            }
        });
        requestAppointmentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorDashboard.this, RequestAppointmentActivity.class);
                startActivity(intent);
            }
        });
    }
}

