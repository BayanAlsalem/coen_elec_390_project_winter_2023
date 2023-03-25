package com.example.coen_elec_390_project_winter_2023.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coen_elec_390_project_winter_2023.Controller.FirebaseHelper;
import com.example.coen_elec_390_project_winter_2023.Dashboard.DoctorDashboard;
import com.example.coen_elec_390_project_winter_2023.Dashboard.DoctorDashboardActivity;
import com.example.coen_elec_390_project_winter_2023.Dashboard.PatientDashboard;
import com.example.coen_elec_390_project_winter_2023.Dashboard.PatientDashboardActivity;
import com.example.coen_elec_390_project_winter_2023.Models.User;
import com.example.coen_elec_390_project_winter_2023.Models.userOptions;
import com.example.coen_elec_390_project_winter_2023.R;
import com.example.coen_elec_390_project_winter_2023.SignUp.PatientSignUpActivity;

public class LoginActivity extends AppCompatActivity {
    FirebaseHelper firebaseHelper = new FirebaseHelper();

    private EditText loginEmail, loginPassword;
    private TextView signupRedirectText;
    private Button loginButton;


    @Override
    protected void onResume() {
        super.onResume();

        //Check if user is logged in!
        if(firebaseHelper.isLoggedIn()){
            Toast.makeText(LoginActivity.this, "User detected, verifying data..", Toast.LENGTH_SHORT).show();
            firebaseHelper.getCurrentUser(new FirebaseHelper.getUserCallbackInterface() {
                @Override

                //I have to change the class to test the PatientDashboard
                public void onSuccess(User user) {
                    if (user.getUserType() == userOptions.userType.PATIENT){
                        //To Do: we need to pass user object to either dashboards (eg: putExtra)
                        Intent intent = new Intent(LoginActivity.this, PatientDashboardActivity.class);
                        intent.putExtra("userID",user.getUid());
                        intent.putExtra("userName",user.getName());
                        intent.putExtra("userEmail",user.getEmail());
                        startActivity(intent);
                    } else if (user.getUserType() == userOptions.userType.DOCTOR){
                        //To Do: we need to pass user object to either dashboards (eg: putExtra)
                        startActivity(new Intent(LoginActivity.this, DoctorDashboard.class));
                    }
                    finish();
                }
                @Override
                public void onFail(Exception e) {
                    Toast.makeText(LoginActivity.this, "login failed, please log in", Toast.LENGTH_SHORT).show();
                    firebaseHelper.logout();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signUpRedirectText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = loginEmail.getText().toString();
                String pass = loginPassword.getText().toString();

                if (email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    loginEmail.setError("Please provide a valid email address");
                    return;
                }

                if(pass.isEmpty()){
                    loginPassword.setError("Please provide your password");
                    return;
                }

                Toast.makeText(LoginActivity.this, "Logging in, please wait..", Toast.LENGTH_SHORT).show();
                firebaseHelper.login(email, pass, new FirebaseHelper.voidCallbackInterface() {
                    @Override
                    public void onSuccess() {
                        onResume(); //Login happens here.
                    }

                    @Override
                    public void onFail(Exception e) {
                        Toast.makeText(LoginActivity.this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, PatientSignUpActivity.class));
            }
        });
    }//end of onCreate() function
}//end of PatientLoginActivity{} class
