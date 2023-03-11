package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coen_elec_390_project_winter_2023.R;

public class DoctorDashboardActivity extends AppCompatActivity {
    private Button patientListBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_dashboard_layout);

        // initialize views
        patientListBtn = findViewById(R.id.patientListBtnID);

        patientListBtn.setOnClickListener(new View.OnClickListener() {
            //When click on the Doctor button, the user will be directed to the LoginActivity screen
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorDashboardActivity.this, ListOfPatientsActivity.class);
                startActivity(intent);
            }
        });
    }//end of onCreate() function



}//end of DoctorDashboardActivity{} class
