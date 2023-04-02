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
import com.example.coen_elec_390_project_winter_2023.Models.User;
import com.example.coen_elec_390_project_winter_2023.R;

public class DoctorProfileActivity extends AppCompatActivity {
    FirebaseHelper firebaseHelper = new FirebaseHelper();
    private TextView doctorFullName;
    private EditText fullNameEditText;
    private EditText hospitalNameEditText;
    private EditText ageEditText;
    private EditText experienceEditText;
    private Button editButton, saveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_profile_layout);

        // Find views by their respective IDs
        doctorFullName = findViewById(R.id.fullname_field);
        fullNameEditText = findViewById(R.id.docFullNameID);
        hospitalNameEditText = findViewById(R.id.hospitalNameID);
        ageEditText = findViewById(R.id.ageDoctorProfileID);
        experienceEditText = findViewById(R.id.experienceDoctorProfileID);
        editButton = findViewById(R.id.editBtnDoctorID);
        saveButton = findViewById(R.id.saveBtnDoctorID);

        // Disable the EditText fields initially
        fullNameEditText.setEnabled(false);
        hospitalNameEditText.setEnabled(false);
        ageEditText.setEnabled(false);
        experienceEditText.setEnabled(false);


        firebaseHelper.getCurrentUser(new FirebaseHelper.getUserCallbackInterface() {
            @Override
            public void onSuccess(User user) {
                Doctor doctor = (Doctor) user;
                doctorFullName.setText(doctor.getName());
                fullNameEditText.setText(doctor.getName());
                hospitalNameEditText.setText(doctor.getHospital());
                ageEditText.setText(doctor.getAge());
                experienceEditText.setText(doctor.getExperience());

            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(DoctorProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        // Set an onClickListener on the Edit button
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Enable the EditText fields for editing
                fullNameEditText.setEnabled(true);
                hospitalNameEditText.setEnabled(true);
                ageEditText.setEnabled(true);
                experienceEditText.setEnabled(true);

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
                String hospitalName = hospitalNameEditText.getText().toString();
                String age = ageEditText.getText().toString();
                String experience = experienceEditText.getText().toString();

                if(fullName.isEmpty() || hospitalName.isEmpty() || age.isEmpty() || experience.isEmpty()) {
                    Toast.makeText(DoctorProfileActivity.this, "Please fill out the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Disable the EditText fields after saving
                fullNameEditText.setEnabled(false);
                hospitalNameEditText.setEnabled(false);
                ageEditText.setEnabled(false);
                experienceEditText.setEnabled(false);

                // Show the Edit button and hide the Save button
                saveButton.setVisibility(View.GONE);
                editButton.setVisibility(View.VISIBLE);

                firebaseHelper.updateDoctorInfo(fullName, hospitalName, age, experience, new FirebaseHelper.voidCallbackInterface() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(DoctorProfileActivity.this, "Info saved successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(Exception e) {
                        Toast.makeText(DoctorProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    } // end of onCreate() method
} // end of DoctorProfileActivity {} class
