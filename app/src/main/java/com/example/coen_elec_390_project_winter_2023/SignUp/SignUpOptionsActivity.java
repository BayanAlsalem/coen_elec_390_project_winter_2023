package com.example.coen_elec_390_project_winter_2023.SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coen_elec_390_project_winter_2023.R;

public class SignUpOptionsActivity extends AppCompatActivity {

    private Button doctorSignUpBtn;
    private Button patientSignUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_options_layout);

        // initialize views
        doctorSignUpBtn = findViewById(R.id.doctorSignUpBtnID);
        patientSignUpBtn = findViewById(R.id.patientSignUpBtnID);

        doctorSignUpBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpOptionsActivity.this, DoctorSignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        patientSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpOptionsActivity.this, PatientSignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }//end of onCreate() function
}//end of signUpOptionsActivity{} class