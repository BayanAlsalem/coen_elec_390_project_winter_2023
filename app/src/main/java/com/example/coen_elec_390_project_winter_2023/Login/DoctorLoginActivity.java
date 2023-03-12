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
import com.example.coen_elec_390_project_winter_2023.SignUp.DoctorSignUpActivity;
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

public class DoctorLoginActivity extends AppCompatActivity {

    //This class will be here until we figure out how to direct
    // the user to its assigned dashboard from one LoginActivity

    private FirebaseAuth mAuth;
    private EditText doctorLoginEmail, doctorLoginPassword;
    private TextView doctorSignupRedirectText;
    private Button doctorLoginButton;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_login_layout);

        doctorLoginEmail = findViewById(R.id.doctor_login_email);
        doctorLoginPassword = findViewById(R.id.doctor_login_password);
        doctorLoginButton = findViewById(R.id.doctor_login_button);
        doctorSignupRedirectText = findViewById(R.id.doctorSignUpRedirectText);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child("Doctors");

        doctorLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = doctorLoginEmail.getText().toString();
                String pass = doctorLoginPassword.getText().toString();

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
                                                    Toast.makeText(DoctorLoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(DoctorLoginActivity.this, DoctorDashboardActivity.class));
                                                    finish();
                                                } else {
                                                    Toast.makeText(DoctorLoginActivity.this, "You are not a doctor", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(DoctorLoginActivity.this, "Account does not exist", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        doctorLoginPassword.setError("Please provide your password");
                    }
                } else if (email.isEmpty()) {
                    doctorLoginEmail.setError("Please provide your email address");
                } else {
                    doctorLoginEmail.setError("Please enter a valid email");
                }
            }
        });

        doctorSignupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoctorLoginActivity.this, DoctorSignUpActivity.class));
            }
        });


    }//end of onCreate() function
}//end of PatientLoginActivity{} class
