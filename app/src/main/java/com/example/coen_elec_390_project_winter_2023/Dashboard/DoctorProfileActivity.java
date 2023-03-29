package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coen_elec_390_project_winter_2023.Controller.FirebaseHelper;
import com.example.coen_elec_390_project_winter_2023.Models.Doctor;
import com.example.coen_elec_390_project_winter_2023.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DoctorProfileActivity extends AppCompatActivity {
    //TODO the name of the doctor is always NULL!!

    FirebaseHelper firebaseHelper = new FirebaseHelper();
    Doctor doc_obj = new Doctor();
    private TextView doctorFullName;
    private EditText fullNameEditText;
    private EditText hospitalNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button editButton, saveButton;



    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference usersRef = db.collection("users");



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

        // Disable the EditText fields initially
        fullNameEditText.setEnabled(false);
        hospitalNameEditText.setEnabled(false);
        emailEditText.setEnabled(false);
        passwordEditText.setEnabled(false);


        // Get the doctor name from the intent
      //  String doctorNameString = getIntent().getStringExtra("name");
       // doctorFullName.setText("Name of doctor: " + doctorNameString);

        usersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
         @Override
         public void onComplete(@NonNull Task<QuerySnapshot> task) {
             if (task.isSuccessful()) {
                 for (QueryDocumentSnapshot document : task.getResult()) {
                     Doctor doctor = document.toObject(Doctor.class);
                     doctorFullName.setText(doctor.getName());
                 }
             } else {
                 System.out.println("Error getting documents: " + task.getException());
             }
         }
     });

        // Set an onClickListener on the Edit button
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Enable the EditText fields for editing
                fullNameEditText.setEnabled(true);
                hospitalNameEditText.setEnabled(true);
                emailEditText.setEnabled(true);
                passwordEditText.setEnabled(true);

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
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Disable the EditText fields after saving
                fullNameEditText.setEnabled(false);
                hospitalNameEditText.setEnabled(false);
                emailEditText.setEnabled(false);
                passwordEditText.setEnabled(false);

                // Show the Edit button and hide the Save button
                saveButton.setVisibility(View.GONE);
                editButton.setVisibility(View.VISIBLE);
            }
        });
    } // end of onCreate() method
} // end of DoctorProfileActivity {} class
