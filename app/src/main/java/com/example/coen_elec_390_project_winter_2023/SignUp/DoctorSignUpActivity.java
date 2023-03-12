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

import com.example.coen_elec_390_project_winter_2023.Dashboard.DoctorDashboardActivity;
import com.example.coen_elec_390_project_winter_2023.Login.LoginOptionsActivity;
import com.example.coen_elec_390_project_winter_2023.Models.Doctor;
import com.example.coen_elec_390_project_winter_2023.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DoctorSignUpActivity extends AppCompatActivity {
    //This class integrates Firebase Authentication service and signs up a user

    private FirebaseAuth mAuth;
    private EditText doctorEmailSignUp, doctorPasswordSignup, doctorFullName;
    private Button signupBtnFromDoctorSignupLayout;
    private TextView doctorLoginRedirectText;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_signup_layout);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
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

                    mAuth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(DoctorSignUpActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                // Create a new Doctor object and store it in the database
                                Doctor doctor = new Doctor(name, user, pass, mAuth.getUid());
                                // Save user to database under Users/Doctors
                                mDatabase.child("Users").child("Doctors").child(mAuth.getUid()).setValue(doctor);
                                startActivity(new Intent(DoctorSignUpActivity.this, DoctorDashboardActivity.class));

                            } else {
                                Toast.makeText(DoctorSignUpActivity.this, "SignUp Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });//end of setOnClickListener() function for the signup button

        doctorLoginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorSignUpActivity.this, LoginOptionsActivity.class));
            }
        });//end of setOnClickListener() function for the login redirect button

    }//end of onCreate() function

}//end of DoctorSignupActivity{} class
