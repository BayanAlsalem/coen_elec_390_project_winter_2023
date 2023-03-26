package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coen_elec_390_project_winter_2023.R;

public class PatientInstructionsStep2 extends AppCompatActivity {
    Button nextButton2, backButton2;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_instructions_step2);

        nextButton2 = findViewById(R.id.btn_next2);
        backButton2 = findViewById(R.id.btn_back2);

        if (getIntent().getExtras() != null) {
            userID=getIntent().getStringExtra("userID");
        }else{
            System.out.println("Intent Failed");
            return;
        }

        nextButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientInstructionsStep2.this, PatientInstructionsStep3.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });

        backButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientInstructionsStep2.this, PatientInstructionsStep1.class);
                startActivity(intent);
            }
        });
    }
}

