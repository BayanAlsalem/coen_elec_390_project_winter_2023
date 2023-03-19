package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coen_elec_390_project_winter_2023.R;

public class PatientInstructionsStep1 extends AppCompatActivity {

    Button nextButton1, backButton1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_instructions_step1);

        nextButton1 = findViewById(R.id.btn_next1);
        backButton1 = findViewById(R.id.btn_back1);

        nextButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientInstructionsStep1.this, PatientInstructionsStep2.class);
                startActivity(intent);
            }
        });

        backButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientInstructionsStep1.this, PatientInstructionsActivity.class);
                startActivity(intent);
            }
        });
    }
}
