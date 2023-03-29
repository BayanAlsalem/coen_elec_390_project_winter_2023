package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.coen_elec_390_project_winter_2023.R;



public class PatientProfileActivity extends AppCompatActivity {
//TODO Patient is only allowed to edit when click on EDIT and then SAVE to store in the database
    //TODO the name of the patient is always NULL!!


    private TextView patientFullName;
    private EditText fullNameEditText;
    private EditText patientAgeEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText doctorNameEditText;
    private Button editButton;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_profile_layout);

        // Find views by their respective IDs
        patientFullName = findViewById(R.id.fullname_field);
        fullNameEditText = findViewById(R.id.fullNameID);
        patientAgeEditText = findViewById(R.id.patientAgeID);
        emailEditText = findViewById(R.id.emailSignUpID);
        passwordEditText = findViewById(R.id.passwordSignupID);
        doctorNameEditText = findViewById(R.id.doctorNameID);
        editButton = findViewById(R.id.editBtnPatientID);
        saveButton = findViewById(R.id.saveBtnPatientID);

        // Get the patient name from the intent
        String patientNameString = getIntent().getStringExtra("patientName");
        patientFullName.setText("Name of patient: " + patientNameString);

        // Disable the EditText fields by default
        fullNameEditText.setEnabled(false);
        patientAgeEditText.setEnabled(false);
        emailEditText.setEnabled(false);
        passwordEditText.setEnabled(false);
        doctorNameEditText.setEnabled(false);

        // Set an onClickListener on the Edit button
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Enable the EditText fields for editing
                fullNameEditText.setEnabled(true);
                patientAgeEditText.setEnabled(true);
                emailEditText.setEnabled(true);
                passwordEditText.setEnabled(true);
                doctorNameEditText.setEnabled(true);

                // Show the Save button and hide the Edit button
                saveButton.setVisibility(View.VISIBLE);
                editButton.setVisibility(View.GONE);
            }
        });

        // Set an onClickListener on the Save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the values from the EditText fields
                String fullName = fullNameEditText.getText().toString();
                String age = patientAgeEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String doctorName = doctorNameEditText.getText().toString();

                // Save these values to the firestore

                // Disable the EditText fields again
                fullNameEditText.setEnabled(false);
                patientAgeEditText.setEnabled(false);
                emailEditText.setEnabled(false);
                passwordEditText.setEnabled(false);
                doctorNameEditText.setEnabled(false);

                // Show the Edit button and hide the Save button
                saveButton.setVisibility(View.GONE);
                editButton.setVisibility(View.VISIBLE);
            }
        });
    } // end of onCreate() method
} // end of PatientProfileActivity {} class
