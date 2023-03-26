package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coen_elec_390_project_winter_2023.Controller.FirebaseHelper;
import com.example.coen_elec_390_project_winter_2023.Login.LoginActivity;
import com.example.coen_elec_390_project_winter_2023.R;
import com.example.coen_elec_390_project_winter_2023.SignUp.SignUpOptionsActivity;

public class SplashActivity extends AppCompatActivity{
    //This class directs the user to either the signup or the login pages
    FirebaseHelper firebaseHelper = new FirebaseHelper();
    private Button loginBtn;
    private Button signupBtn;

    @Override
    protected void onResume() {
        super.onResume();

        if(firebaseHelper.isLoggedIn()){
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);


        // initialize views
        loginBtn = findViewById(R.id.loginBtn);
        signupBtn = findViewById(R.id.signupBtn);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            //When click on the Signup button, the user will be directed to the Signup Options Activity screen
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashActivity.this, SignUpOptionsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            //When click on the login button, the user will be directed to the loginActivity screen
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }//end of onCreate() function

    public void openPrivacyActivity(View view) {
        Intent intent = new Intent(SplashActivity.this, PrivacyActivity.class);
        startActivity(intent);
    }
}//end of SplashActivity{} class



