package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coen_elec_390_project_winter_2023.R;

public class DoctorProfileActivity extends AppCompatActivity {
    //TODO Doctor is only allowed to edit when click on EDIT and then SAVE to store in the database
    //TODO the name of the doctor is always NULL!!

    private TextView doctorFullName;
    private EditText fullNameEditText;
    private EditText hospitalNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button editButton, saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_profile_layout);

        // Find views by their respective IDs
        doctorFullName = findViewById(R.id.fullname_field);
        fullNameEditText = findViewById(R.id.docFullNameID);
        hospitalNameEditText = findViewById(R.id.hospitalNameID);
        emailEditText = findViewById(R.id.emailSignUpID);
        passwordEditText = findViewById(R.id.passwordSignupID);
        editButton = findViewById(R.id.editBtnDoctorID);
        saveButton = findViewById(R.id.saveBtnDoctorID);

        // Get the doctor name from the intent
        String doctorNameString = getIntent().getStringExtra("doctorName");
        doctorFullName.setText("Name of doctor: " + doctorNameString);

        // Set an onClickListener on the Edit button
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the values from the EditText fields
                String fullName = fullNameEditText.getText().toString();
                String hospitalName = hospitalNameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Save these values to the firestore

            }
        });
    } // end of onCreate() method
} // end of DoctorProfileActivity {} class

