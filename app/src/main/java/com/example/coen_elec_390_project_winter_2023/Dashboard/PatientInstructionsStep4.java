package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coen_elec_390_project_winter_2023.R;

public class PatientInstructionsStep4 extends AppCompatActivity {

    Button nextButton4, backButton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_instructions_step4);

        nextButton4 = findViewById(R.id.btn_next4);
        backButton4 = findViewById(R.id.btn_back4);

        nextButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientInstructionsStep4.this, PatientInstructionsStep5.class);
                startActivity(intent);
            }
        });

        backButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientInstructionsStep4.this, PatientInstructionsStep3.class);
                startActivity(intent);
            }
        });
    }
}
