package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coen_elec_390_project_winter_2023.Controller.FirebaseHelper;
import com.example.coen_elec_390_project_winter_2023.Login.LoginActivity;
import com.example.coen_elec_390_project_winter_2023.Models.Doctor;
import com.example.coen_elec_390_project_winter_2023.Models.User;
import com.example.coen_elec_390_project_winter_2023.R;

import java.util.ArrayList;

public class DoctorDashboard extends AppCompatActivity {
    FirebaseHelper firebaseHelper = new FirebaseHelper();
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


        // Set click listeners for addPatient
        View.OnClickListener addPatientClk = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorDashboard.this, DoctorAddPatient.class);
                startActivity(intent);
            }
        };

        TextView addPatientText = findViewById(R.id.addPatientTextID);
        ImageView addPatientImage = findViewById(R.id.addPatientImageID);

        addPatientText.setOnClickListener(addPatientClk);
        addPatientImage.setOnClickListener(addPatientClk);


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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_doctor_dashboard, menu);
        return true;
    }
    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.doctor_logout:
                firebaseHelper.logout();
                startActivity(new Intent(DoctorDashboard.this, HomeActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

