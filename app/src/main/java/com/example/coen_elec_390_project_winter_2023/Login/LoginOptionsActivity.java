package com.example.coen_elec_390_project_winter_2023.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coen_elec_390_project_winter_2023.R;


public class LoginOptionsActivity extends AppCompatActivity {
    //This class directs the user to the login page based on the type of user
    private Button doctorLoginBtn;
    private Button patientLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_options_layout);

        // initialize views
        doctorLoginBtn = findViewById(R.id.doctorLoginBtnID);
        patientLoginBtn = findViewById(R.id.patientLoginBtnID);

        doctorLoginBtn.setOnClickListener(new View.OnClickListener() {
            //When click on the Doctor button, the user will be directed to the LoginActivity screen
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginOptionsActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        patientLoginBtn.setOnClickListener(new View.OnClickListener() {
            //When click on the Patient button, the user will be directed to the LoginActivity screen
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginOptionsActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }//end of onCreate() function
}//end of LoginOptionsActivity{} class
