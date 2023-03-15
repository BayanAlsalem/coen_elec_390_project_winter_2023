package com.example.coen_elec_390_project_winter_2023.SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coen_elec_390_project_winter_2023.R;

public class SignUpOptionsActivity extends AppCompatActivity {
    //This class directs the user to the login page based on the type of user
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
            //When click on the Doctor button, the user will be directed to the LoginActivity screen
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpOptionsActivity.this, DoctorSignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        patientSignUpBtn.setOnClickListener(new View.OnClickListener() {
            //When click on the Patient button, the user will be directed to the LoginActivity screen
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpOptionsActivity.this, PatientSignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }//end of onCreate() function
}//end of signUpOptionsActivity{} class