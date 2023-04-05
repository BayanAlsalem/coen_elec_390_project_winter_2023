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
import androidx.viewpager.widget.ViewPager;

import com.example.coen_elec_390_project_winter_2023.Controller.FirebaseHelper;
import com.example.coen_elec_390_project_winter_2023.R;

public class PatientDashboard extends AppCompatActivity {
    FirebaseHelper firebaseHelper = new FirebaseHelper();

    int TakeReadings, Instruction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_test_layout);

        // Set click listeners for editMyProfile
        TextView editMyProfileText = findViewById(R.id.editMyProfileTextID);
        ImageView editMyProfileImage = findViewById(R.id.editMyProfileImageID);

        editMyProfileText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientDashboard.this, PatientProfileActivity.class);
                startActivity(intent);
            }
        });
        editMyProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientDashboard.this, PatientProfileActivity.class);
                startActivity(intent);
            }
        });

        // Set click listeners for takeReadings
        TextView takeReadingsText = findViewById(R.id.takeReadingsTextID);
        ImageView takeReadingsImage = findViewById(R.id.takeReadingsImageID);
        takeReadingsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TakeReadings = 1;
                Instruction = 0;
                Intent intent = new Intent(PatientDashboard.this, PatientInstructionsActivity.class);
                intent.putExtra("TakeReadings", TakeReadings);
                intent.putExtra("Instruction", Instruction);
                startActivity(intent);
            }
        });
        takeReadingsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TakeReadings = 1;
                Instruction = 0;
                Intent intent = new Intent(PatientDashboard.this, PatientInstructionsActivity.class);
                intent.putExtra("TakeReadings", TakeReadings);
                intent.putExtra("Instruction", Instruction);
                startActivity(intent);
            }
        });

        // Set click listeners for instructions
        TextView instructionsText = findViewById(R.id.instructionsTextID);
        ImageView instructionsImage = findViewById(R.id.instructionsImageID);
        instructionsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Instruction = 1;
                TakeReadings = 0;
                Intent intent = new Intent(PatientDashboard.this, PatientInstructionsActivity.class);
                intent.putExtra("TakeReadings", TakeReadings);
                intent.putExtra("Instruction", Instruction);
                startActivity(intent);

            }
        });
        instructionsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Instruction = 1;
                TakeReadings = 0;
                Intent intent = new Intent(PatientDashboard.this, PatientInstructionsActivity.class);
                intent.putExtra("TakeReadings", TakeReadings);
                intent.putExtra("Instruction", Instruction);
                startActivity(intent);

            }
        });
//
        // Set click listeners for myData
        TextView myDataText = findViewById(R.id.myDataTextID);
        ImageView myDataImage = findViewById(R.id.myDataImageID);
        myDataText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send patient id to myDataActivity using intent extras
                String patientID = firebaseHelper.getCurrentUserId();
                System.out.println("Patient ID: " + patientID);
                Intent intent = new Intent(PatientDashboard.this, MyDataActivity.class);
                intent.putExtra("patientId", patientID);
                startActivity(intent);
            }
        });
        myDataImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String patientID = firebaseHelper.getCurrentUserId();
                System.out.println("Patient ID: " + patientID);
                Intent intent = new Intent(PatientDashboard.this, MyDataActivity.class);
                intent.putExtra("patientId", patientID);
                startActivity(intent);
            }
        });




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_patient_dashboard, menu);
        return true;
    }
    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.patient_logout:
                firebaseHelper.logout();
                startActivity(new Intent(PatientDashboard.this, HomeActivity.class));
                finish();
                return true;
            case R.id.appointments:
                startActivity(new Intent(PatientDashboard.this, PatientAppointments.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

