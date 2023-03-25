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
import com.example.coen_elec_390_project_winter_2023.R;

public class PatientDashboard extends AppCompatActivity {
    FirebaseHelper firebaseHelper = new FirebaseHelper();

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
//
//        // Set click listeners for takeReadings
//        TextView takeReadingsText = findViewById(R.id.takeReadingsTextID);
//        ImageView takeReadingsImage = findViewById(R.id.takeReadingsImageID);
//        takeReadingsText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(PatientDashboard.this, takeReadings.class);
//                startActivity(intent);
//            }
//        });
//        takeReadingsImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(PatientDashboard.this, takeReadings.class);
//                startActivity(intent);
//            }
//        });

        // Set click listeners for instructions
        TextView instructionsText = findViewById(R.id.instructionsTextID);
        ImageView instructionsImage = findViewById(R.id.instructionsImageID);
        instructionsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientDashboard.this, PatientInstructionsActivity.class);
                startActivity(intent);
            }
        });
        instructionsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientDashboard.this, PatientInstructionsActivity.class);
                startActivity(intent);
            }
        });
//
//        // Set click listeners for myData
//        TextView myDataText = findViewById(R.id.myDataTextID);
//        ImageView myDataImage = findViewById(R.id.myDataImageID);
//        myDataText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(PatientDashboard.this, listOfGraphs.class);
//                startActivity(intent);
//            }
//        });
//        myDataImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(PatientDashboard.this, listOfGraphs.class);
//                startActivity(intent);
//            }
//        });




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
        }
        return super.onOptionsItemSelected(item);
    }
}

