package com.example.coen_elec_390_project_winter_2023.SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coen_elec_390_project_winter_2023.Controller.FirebaseHelper;
import com.example.coen_elec_390_project_winter_2023.Dashboard.PatientDashboard;
import com.example.coen_elec_390_project_winter_2023.Login.LoginActivity;
import com.example.coen_elec_390_project_winter_2023.Models.Patient;
import com.example.coen_elec_390_project_winter_2023.R;

public class PatientSignUpActivity extends AppCompatActivity {
    //This class integrates Firebase Authentication service and signs up a user
    FirebaseHelper firebaseHelper = new FirebaseHelper();

    private EditText patientEmailSignUp, patientPasswordSignup, patientFullName, patientAge;
    private Button signupBtnFromPatientSignupLayout;
    private TextView patientLoginRedirectText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_signup_layout);

        patientAge = findViewById(R.id.patientAgeID);
        patientFullName = findViewById(R.id.patientFullNameID);
        patientEmailSignUp = findViewById(R.id.patientEmailSignUpID);
        patientPasswordSignup = findViewById(R.id.patientPasswordSignupID);

        signupBtnFromPatientSignupLayout = findViewById(R.id.signupBtnFromPatientSignupLayout);
        patientLoginRedirectText = findViewById(R.id.loginRedirectTextFromPatientSignupLayout);


        signupBtnFromPatientSignupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = patientEmailSignUp.getText().toString().trim();
                String pass = patientPasswordSignup.getText().toString().trim();
                String name = patientFullName.getText().toString().trim();

                if (user.isEmpty()){
                    patientEmailSignUp.setError("Please provide an email address to continue registration!");
                }

                if (pass.isEmpty()){
                    patientPasswordSignup.setError("Please provide a password to continue registration! ");
                } else {
                    //Firebase integration to create a user
                    Patient patient = new Patient(name, user, pass, null);
                    patient.setAge(patientAge.getText().toString().trim());
                    firebaseHelper.createUser(patient, new FirebaseHelper.voidCallbackInterface() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(PatientSignUpActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PatientSignUpActivity.this, PatientDashboard.class));
                            PatientSignUpActivity.this.finish();
                        }
                        @Override
                        public void onFail(Exception e) {
                            Toast.makeText(PatientSignUpActivity.this, "SignUp Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });//end of setOnClickListener() function for the signup button

        patientLoginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(new Intent(PatientSignUpActivity.this, LoginActivity.class));
            }
        });//end of setOnClickListener() function for the login redirect button

    }//end of onCreate() function

}//end of PatientSignupActivity{} class