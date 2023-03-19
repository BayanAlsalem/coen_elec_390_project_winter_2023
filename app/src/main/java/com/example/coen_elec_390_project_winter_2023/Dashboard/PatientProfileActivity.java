package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.coen_elec_390_project_winter_2023.R;



public class PatientProfileActivity extends AppCompatActivity {
    public TextView patientName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_profile_layout);

        patientName = findViewById(R.id.patientNameID);

        // get the patient name from the intent
        String patientNameString = getIntent().getStringExtra("patientName");
        patientName.setText("Name of patient: " + patientNameString);


    } // end of onCreate() method
} // end of PatientProfileActivity {} class
