package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.coen_elec_390_project_winter_2023.R;
import android.view.View;

public class PatientInstructionsActivity extends AppCompatActivity{
    private Button nextButton0, nextButton1, nextButton2, nextButton3, nextButton4;
    private Button backButton0, backButton1, backButton2, backButton3, backButton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_instructions_list);

           nextButton0 = findViewById(R.id.btn_next0);
           backButton0 = findViewById(R.id.btn_back0);

           nextButton0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientInstructionsActivity.this, PatientInstructionsStep1.class);
                startActivity(intent);
            }
           });

          backButton0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientInstructionsActivity.this, PatientDashboardActivity.class);
                startActivity(intent);
            }
        });

    }


}
