package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.Cartesian;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.Pie;
import com.anychart.anychart.ValueDataEntry;
import com.example.coen_elec_390_project_winter_2023.Controller.FirebaseHelper;
import com.example.coen_elec_390_project_winter_2023.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class PatientDashboardActivity extends AppCompatActivity {

    Button Start_Reading;
    FirebaseHelper firebaseHelper = new FirebaseHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_dashboard_layout);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }


        Start_Reading = findViewById(R.id.btn_StartReading);
        Start_Reading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientDashboardActivity.this, PatientInstructionsActivity.class);
                startActivity(intent);
            }
        });
        //To Do: Handle logged in user...

        //To Do: Dynamically populate chart...
        Cartesian line = AnyChart.line();

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("0", 10));
        data.add(new ValueDataEntry("50", 50));
        data.add(new ValueDataEntry("100", 5));
        data.add(new ValueDataEntry("150", 55));
        data.add(new ValueDataEntry("200", 0));
        data.add(new ValueDataEntry("250", 60));
        data.add(new ValueDataEntry("300", 10));
        data.add(new ValueDataEntry("350", 50));
        data.add(new ValueDataEntry("400", 5));
        data.add(new ValueDataEntry("450", 55));
        data.add(new ValueDataEntry("500", 0));
        data.add(new ValueDataEntry("550", 60));

        line.setData(data);

        AnyChartView anyChartView = (AnyChartView) findViewById(R.id.any_chart_view);
        anyChartView.setChart(line);

    }//end of onCreate() function

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_patient_dashboard, menu);
        return true;
    }
    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.patient_logout:
                firebaseHelper.logout();
                startActivity(new Intent(PatientDashboardActivity.this, SplashActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}//end of PatientDashboardActivity{} class


