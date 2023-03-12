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
import com.example.coen_elec_390_project_winter_2023.Login.LoginOptionsActivity;
import com.example.coen_elec_390_project_winter_2023.Models.Patient;
import com.example.coen_elec_390_project_winter_2023.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PatientSignUpActivity extends AppCompatActivity {
    //This class integrates Firebase Authentication service and signs up a user

    private FirebaseAuth mAuth;
    private EditText patientEmailSignUp, patientPasswordSignup, patientFullName;
    private Button signupBtnFromPatientSignupLayout;
    private TextView patientLoginRedirectText;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_signup_layout);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        patientEmailSignUp = findViewById(R.id.patientEmailSignUpID);
        patientPasswordSignup = findViewById(R.id.patientPasswordSignupID);
        signupBtnFromPatientSignupLayout = findViewById(R.id.signupBtnFromPatientSignupLayout);
        patientLoginRedirectText = findViewById(R.id.loginRedirectTextFromPatientSignupLayout);
        patientFullName = findViewById(R.id.patientFullNameID);

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
                } else{
                    //Firebase integration to create a user

                    mAuth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(PatientSignUpActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                // Create a new patient object and store it in the database
                                Patient patient = new Patient(name, user, pass, mAuth.getUid());
                                // Save user to database under Users/Patients
                                mDatabase.child("Users").child("Patients").child(mAuth.getUid()).setValue(patient);
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
                startActivity(new Intent(PatientSignUpActivity.this, LoginOptionsActivity.class));
            }
        });//end of setOnClickListener() function for the login redirect button

    }//end of onCreate() function

}//end of PatientSignupActivity{} class