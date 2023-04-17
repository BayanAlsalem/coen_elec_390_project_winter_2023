package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coen_elec_390_project_winter_2023.Controller.FirebaseHelper;
import com.example.coen_elec_390_project_winter_2023.R;

public class RequestAppointmentActivity extends AppCompatActivity {
    private EditText patientNameEditText;
    private EditText appointmentReasonEditText;
    private Button requestAppointmentButton;

    FirebaseHelper firebaseHelper;

    String doctorName;
    String userID;

    String patientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_appointment_layout);
        firebaseHelper=new FirebaseHelper();
        // Get references to UI elements
        patientNameEditText = findViewById(R.id.patient_name_edittext);
        appointmentReasonEditText = findViewById(R.id.appointment_reason_edittext);
        requestAppointmentButton = findViewById(R.id.request_appointment_button);

        if(getIntent().getExtras()!=null){
            doctorName = getIntent().getStringExtra("doctorName");
            userID= getIntent().getStringExtra("patientID");
            patientName= getIntent().getStringExtra("patientName");
        }

        patientNameEditText.setText(patientName);
        patientNameEditText.setEnabled(false);

        // Set up click listener for request appointment button
        requestAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values from UI element
                String appointmentReason = appointmentReasonEditText.getText().toString();

                firebaseHelper.createAppointment(doctorName,appointmentReason,userID,new FirebaseHelper.voidCallbackInterface() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(RequestAppointmentActivity.this, "Appointment Created Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(RequestAppointmentActivity.this, SplashActivity.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onFail(Exception e) {
                        Toast.makeText(RequestAppointmentActivity.this, "Appointment Creation Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                // TODO: send and appointment request to the database
            }
        });
    }
}

