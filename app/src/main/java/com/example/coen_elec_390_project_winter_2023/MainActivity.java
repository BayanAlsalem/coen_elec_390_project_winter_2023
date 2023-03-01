package com.example.coen_elec_390_project_winter_2023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    //This class directs the user to the signup page based on the type of user
    private Button doctorBtn;
    private Button patientBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize views
        doctorBtn = findViewById(R.id.doctorBtnID);
        patientBtn = findViewById(R.id.patientBtnID);

        doctorBtn.setOnClickListener(new View.OnClickListener() {
            //When click on the Doctor button, the user will be directed to the SignupActivity screen
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        patientBtn.setOnClickListener(new View.OnClickListener() {
            //When click on the Patient button, the user will be directed to the SignupActivity screen
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });


    }//end of onCreate() function
}//end of MainActivity{} class