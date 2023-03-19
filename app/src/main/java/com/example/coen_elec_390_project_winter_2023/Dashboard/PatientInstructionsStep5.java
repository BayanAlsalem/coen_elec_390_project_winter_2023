package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coen_elec_390_project_winter_2023.R;

public class PatientInstructionsStep5 extends AppCompatActivity {

    Button nextButton5, backButton5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_instructions_step5);

        nextButton5 = findViewById(R.id.btn_next5);
        backButton5 = findViewById(R.id.btn_back5);

        nextButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientInstructionsStep5.this, PatientRestTest.class);
                startActivity(intent);
            }
        });

        backButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientInstructionsStep5.this, PatientInstructionsStep4.class);
                startActivity(intent);
            }
        });
    }
}
