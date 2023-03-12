package com.example.coen_elec_390_project_winter_2023.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coen_elec_390_project_winter_2023.Dashboard.DoctorDashboardActivity;
import com.example.coen_elec_390_project_winter_2023.Dashboard.PatientDashboardActivity;
import com.example.coen_elec_390_project_winter_2023.R;
import com.example.coen_elec_390_project_winter_2023.SignUp.PatientSignUpActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PatientLoginActivity extends AppCompatActivity {

    //This class will be here until we figure out how to direct
    // the user to its assigned dashboard from one LoginActivity

    private FirebaseAuth mAuth;
    private EditText patientLoginEmail, patientLoginPassword;
    private TextView patientSignupRedirectText;
    private Button patientLoginButton;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_login_layout);

        patientLoginEmail = findViewById(R.id.patient_login_email);
        patientLoginPassword = findViewById(R.id.patient_login_password);
        patientLoginButton = findViewById(R.id.patient_login_button);
        patientSignupRedirectText = findViewById(R.id.patientSignUpRedirectText);
        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child("Patients");


        mAuth = FirebaseAuth.getInstance();

        patientLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = patientLoginEmail.getText().toString();
                String pass = patientLoginPassword.getText().toString();

                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!pass.isEmpty()) {
                        mAuth.signInWithEmailAndPassword(email, pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Query query = mDatabase.orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid());

                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    Toast.makeText(PatientLoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(PatientLoginActivity.this, PatientDashboardActivity.class));
                                                    finish();
                                                } else {
                                                    Toast.makeText(PatientLoginActivity.this, "You are not a patient", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                // Handle errors here
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(PatientLoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        patientLoginPassword.setError("Please provide your password");
                    }
                } else if (email.isEmpty()) {
                    patientLoginEmail.setError("Please provide your email address");
                } else {
                    patientLoginEmail.setError("Please enter a valid email");
                }
            }
        });

        patientSignupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientLoginActivity.this, PatientSignUpActivity.class));
            }
        });


    }//end of onCreate() function
}//end of PatientLoginActivity{} class
