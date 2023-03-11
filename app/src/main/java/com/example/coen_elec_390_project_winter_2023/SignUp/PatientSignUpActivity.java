package com.example.coen_elec_390_project_winter_2023.SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coen_elec_390_project_winter_2023.Dashboard.BluetoothConnectionActivity;
import com.example.coen_elec_390_project_winter_2023.Login.LoginActivity;
import com.example.coen_elec_390_project_winter_2023.MainActivity;
import com.example.coen_elec_390_project_winter_2023.R;
import com.example.coen_elec_390_project_winter_2023.SignupActivity;
import com.example.coen_elec_390_project_winter_2023.SplashActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class PatientSignUpActivity extends AppCompatActivity {
    //This class integrates Firebase Authentication service and signs up a user

    private FirebaseAuth mAuth;
    private EditText patientEmailSignUp, patientPasswordSignup;
    private Button signupBtnFromPatientSignupLayout;
    private TextView patientLoginRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_signup_layout);

        mAuth = FirebaseAuth.getInstance();
        patientEmailSignUp = findViewById(R.id.patientEmailSignUpID);
        patientPasswordSignup = findViewById(R.id.patientPasswordSignupID);
        signupBtnFromPatientSignupLayout = findViewById(R.id.signupBtnFromPatientSignupLayout);
        patientLoginRedirectText = findViewById(R.id.loginRedirectTextFromPatientSignupLayout);

        signupBtnFromPatientSignupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = patientEmailSignUp.getText().toString().trim();
                String pass = patientPasswordSignup.getText().toString().trim();

                if (user.isEmpty()){
                    patientEmailSignUp.setError("Please provide an email address to continue registration!");
                }

                if (pass.isEmpty()){
                    patientPasswordSignup.setError("Please provide a password to continue registration! ");
                } else{
                    //Firebase integration to create a user

                    mAuth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(PatientSignUpActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(PatientSignUpActivity.this, BluetoothConnectionActivity.class));

                            } else {
                                Toast.makeText(PatientSignUpActivity.this, "SignUp Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
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