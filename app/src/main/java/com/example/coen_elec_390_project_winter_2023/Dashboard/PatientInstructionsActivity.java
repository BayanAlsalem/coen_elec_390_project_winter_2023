package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.coen_elec_390_project_winter_2023.R;
import android.view.View;

public class PatientInstructionsActivity extends AppCompatActivity{
     Button nextButton0;
     Button backButton0;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_instructions_list);

//        if (getIntent().getExtras() != null) {
//            userID=getIntent().getStringExtra("userID");
//        }else{
//            System.out.println("Intent Failed");
//            return;
//        }
           nextButton0 = findViewById(R.id.btn_next0);
           backButton0 = findViewById(R.id.btn_back0);

           nextButton0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientInstructionsActivity.this, PatientInstructionsStep1.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
           });

          backButton0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientInstructionsActivity.this, PatientDashboard.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });

    }


}
