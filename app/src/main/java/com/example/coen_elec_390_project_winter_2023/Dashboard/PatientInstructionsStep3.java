package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coen_elec_390_project_winter_2023.R;

public class PatientInstructionsStep3 extends AppCompatActivity {
    Button nextButton3, backButton3;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_instructions_step3);

        if (getIntent().getExtras() != null) {
            userID=getIntent().getStringExtra("userID");
        }else{
            System.out.println("Intent Failed");
            return;
        }

        nextButton3 = findViewById(R.id.btn_next3);
        backButton3 = findViewById(R.id.btn_back3);

        nextButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientInstructionsStep3.this, PatientInstructionsStep4.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });

        backButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientInstructionsStep3.this, PatientInstructionsStep2.class);
                startActivity(intent);
            }
        });
    }
}

