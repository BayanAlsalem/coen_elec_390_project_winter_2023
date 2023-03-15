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
import com.example.coen_elec_390_project_winter_2023.Login.LoginActivity;
import com.example.coen_elec_390_project_winter_2023.Models.Doctor;
import com.example.coen_elec_390_project_winter_2023.R;

public class DoctorSignUpActivity extends AppCompatActivity {
    //This class integrates Firebase Authentication service and signs up a user
    FirebaseHelper firebaseHelper = new FirebaseHelper();
    private EditText doctorEmailSignUp, doctorPasswordSignup, doctorFullName;
    private Button signupBtnFromDoctorSignupLayout;
    private TextView doctorLoginRedirectText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_signup_layout);

        doctorEmailSignUp = findViewById(R.id.doctorEmailSignUpID);
        doctorPasswordSignup = findViewById(R.id.doctorPasswordSignupID);
        signupBtnFromDoctorSignupLayout = findViewById(R.id.signupBtnFromDoctorSignupLayout);
        doctorLoginRedirectText = findViewById(R.id.loginRedirectTextFromDoctorSignupLayout);
        doctorFullName = findViewById(R.id.doctorFullNameID);

        signupBtnFromDoctorSignupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = doctorEmailSignUp.getText().toString().trim();
                String pass = doctorPasswordSignup.getText().toString().trim();
                String name = doctorFullName.getText().toString().trim();

                if (user.isEmpty()){
                    doctorEmailSignUp.setError("Please provide an email address to continue registration!");
                }

                if (pass.isEmpty()){
                    doctorPasswordSignup.setError("Please provide a password to continue registration! ");
                } else{
                    //Firebase integration to create a user
                    Doctor doctor = new Doctor(name, user, pass, null);
                    firebaseHelper.createUser(doctor, new FirebaseHelper.voidCallbackInterface() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(DoctorSignUpActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(DoctorSignUpActivity.this, LoginActivity.class));
                            DoctorSignUpActivity.this.finish();
                        }
                        @Override
                        public void onFail(Exception e) {
                            Toast.makeText(DoctorSignUpActivity.this, "SignUp Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });//end of setOnClickListener() function for the signup button

        doctorLoginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorSignUpActivity.this, LoginActivity.class));
            }
        });//end of setOnClickListener() function for the login redirect button

    }//end of onCreate() function

}//end of DoctorSignupActivity{} class
