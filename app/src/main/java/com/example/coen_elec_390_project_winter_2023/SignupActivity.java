package com.example.coen_elec_390_project_winter_2023;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {
    //This class integrates Firebase Authentication service and signs up a user

    private FirebaseAuth mAuth;
    private EditText emailSignUpID, passwordSignupID;
    private Button signupBtnFromSignupLayout;
    private TextView loginRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

        mAuth = FirebaseAuth.getInstance();
        emailSignUpID = findViewById(R.id.emailSignUpID);
        passwordSignupID = findViewById(R.id.passwordSignupID);
        signupBtnFromSignupLayout = findViewById(R.id.signupBtnFromSignupLayout);
        loginRedirectText = findViewById(R.id.loginRedirectTextFromSignupLayout);

        signupBtnFromSignupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = emailSignUpID.getText().toString().trim();
                String pass = passwordSignupID.getText().toString().trim();

                if (user.isEmpty()){
                    emailSignUpID.setError("Please provide an email address to continue registration!");
                }

                if (pass.isEmpty()){
                    passwordSignupID.setError("Please provide a password to continue registration! ");
                } else{
                    //Firebase integration to create a user

                    mAuth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignupActivity.this, SplashActivity.class));

                            } else {
                                Toast.makeText(SignupActivity.this, "SignUp Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });//end of setOnClickListener() function for the signup button

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });//end of setOnClickListener() function for the login redirect button

    }//end of onCreate() function

}//end of SignupActivity{} class