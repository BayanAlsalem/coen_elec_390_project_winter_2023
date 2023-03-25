package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coen_elec_390_project_winter_2023.R;

public class RequestAppointmentActivity extends AppCompatActivity {
    private EditText patientNameEditText;
    private EditText appointmentReasonEditText;
    private Button requestAppointmentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_appointment_layout);

        // Get references to UI elements
        patientNameEditText = findViewById(R.id.patient_name_edittext);
        appointmentReasonEditText = findViewById(R.id.appointment_reason_edittext);
        requestAppointmentButton = findViewById(R.id.request_appointment_button);

        // Set up click listener for request appointment button
        requestAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values from UI elements
                String patientName = patientNameEditText.getText().toString();
                String appointmentReason = appointmentReasonEditText.getText().toString();

                // TODO: send and appointment request to the database
            }
        });
    }
}

