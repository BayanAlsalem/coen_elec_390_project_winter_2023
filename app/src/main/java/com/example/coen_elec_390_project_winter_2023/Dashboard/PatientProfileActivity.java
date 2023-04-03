package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coen_elec_390_project_winter_2023.Controller.FirebaseHelper;
import com.example.coen_elec_390_project_winter_2023.Models.Doctor;
import com.example.coen_elec_390_project_winter_2023.Models.Patient;
import com.example.coen_elec_390_project_winter_2023.Models.User;
import com.example.coen_elec_390_project_winter_2023.R;



public class PatientProfileActivity extends AppCompatActivity {
    FirebaseHelper firebaseHelper = new FirebaseHelper();
    private TextView patientFullName;
    private EditText fullNameEditText;
    private EditText patientAgeEditText;
    private EditText cityEditText;
    private EditText countryEditText;
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
        cityEditText = findViewById(R.id.cityPatientProfileID);
        countryEditText = findViewById(R.id.countryPatientProfileID);
        doctorNameEditText = findViewById(R.id.doctorNameID);
        editButton = findViewById(R.id.editBtnPatientID);
        saveButton = findViewById(R.id.saveBtnPatientID);

        // Disable the EditText fields by default
        fullNameEditText.setEnabled(false);
        patientAgeEditText.setEnabled(false);
        cityEditText.setEnabled(false);
        countryEditText.setEnabled(false);
        doctorNameEditText.setEnabled(false);

        firebaseHelper.getCurrentUser(new FirebaseHelper.getUserCallbackInterface() {
            @Override
            public void onSuccess(User user) {
                Patient patient = (Patient) user;
                patientFullName.setText(patient.getName());
                fullNameEditText.setText(patient.getName());
                patientAgeEditText.setText(patient.getAge());
                cityEditText.setText(patient.getCity());
                countryEditText.setText(patient.getCountry());
                doctorNameEditText.setText(patient.getDoctorName());

            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(PatientProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Set an onClickListener on the Edit button
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Enable the EditText fields for editing
                fullNameEditText.setEnabled(true);
                patientAgeEditText.setEnabled(true);
                cityEditText.setEnabled(true);
                countryEditText.setEnabled(true);
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
                String city = cityEditText.getText().toString();
                String country = countryEditText.getText().toString();
                String doctorName = doctorNameEditText.getText().toString();

                if(fullName.isEmpty() || age.isEmpty() || city.isEmpty() || country.isEmpty() || doctorName.isEmpty()) {
                    Toast.makeText(PatientProfileActivity.this, "Please fill out the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Disable the EditText fields again
                fullNameEditText.setEnabled(false);
                patientAgeEditText.setEnabled(false);
                cityEditText.setEnabled(false);
                countryEditText.setEnabled(false);
                doctorNameEditText.setEnabled(false);

                // Show the Edit button and hide the Save button
                saveButton.setVisibility(View.GONE);
                editButton.setVisibility(View.VISIBLE);

                firebaseHelper.updatePatientInfo(fullName, age, city, country, doctorName, new FirebaseHelper.voidCallbackInterface() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(PatientProfileActivity.this, "Info saved successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(Exception e) {
                        Toast.makeText(PatientProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    } // end of onCreate() method
} // end of PatientProfileActivity {} class
