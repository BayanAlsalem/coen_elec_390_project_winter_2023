package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coen_elec_390_project_winter_2023.Controller.FirebaseHelper;
import com.example.coen_elec_390_project_winter_2023.R;

public class DoctorDashboardActivity extends AppCompatActivity {

    FirebaseHelper firebaseHelper = new FirebaseHelper();
    private Button patientListBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_dashboard_layout);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        //To Do: Handle logged in user...

        // initialize views
        patientListBtn = findViewById(R.id.patientListBtnID);

        patientListBtn.setOnClickListener(new View.OnClickListener() {
            //When click on the Doctor button, the user will be directed to the LoginActivity screen
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorDashboardActivity.this, ListOfPatientsActivity.class);
                startActivity(intent);
            }
        });
    }//end of onCreate() function

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_doctor_dashboard, menu);
        return true;
    }
    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.doctor_logout:
                firebaseHelper.logout();
                startActivity(new Intent(DoctorDashboardActivity.this, SplashActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}//end of DoctorDashboardActivity{} class
