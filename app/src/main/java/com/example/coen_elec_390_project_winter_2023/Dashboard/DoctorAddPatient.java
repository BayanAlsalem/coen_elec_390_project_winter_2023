package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coen_elec_390_project_winter_2023.Controller.FirebaseHelper;
import com.example.coen_elec_390_project_winter_2023.Models.Doctor;
import com.example.coen_elec_390_project_winter_2023.Models.User;
import com.example.coen_elec_390_project_winter_2023.Models.userOptions;
import com.example.coen_elec_390_project_winter_2023.R;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.List;

public class DoctorAddPatient extends AppCompatActivity{

    private EditText filterPatientField;
    private ListView patientListView;
    private Button savePatientsBtn;

    private ArrayList<User> patientList = new ArrayList<>();
    private ArrayAdapter<User> patientAdapter;
    FirebaseHelper firebaseHelper = new FirebaseHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_add_patient_layout);

        filterPatientField = findViewById(R.id.filter_patients_field);
        patientListView = findViewById(R.id.patientListViewID);
        savePatientsBtn = findViewById(R.id.savePatientsBtn);

        //Load all patients
        Toast.makeText(DoctorAddPatient.this, "Please wait, while we load your patients...", Toast.LENGTH_SHORT).show();

        //Get Doctor's data and get his patients
        firebaseHelper.getCurrentUser(new FirebaseHelper.getUserCallbackInterface() {
            @Override
            //I have to change the class to test the PatientDashboard
            public void onSuccess(User user) {
                if (user.getUserType() == userOptions.userType.DOCTOR){

                    //Get all patients
                    firebaseHelper.loadAllPatients(new FirebaseHelper.getUsersCallbackInterface() {
                        @Override
                        public void onSuccess(List<User> users) {
                            //Load all patients to patientList
                            patientList.addAll(users);

                            //Initialize ListView using all patients
                            patientAdapter = new ArrayAdapter<User>(DoctorAddPatient.this, android.R.layout.select_dialog_multichoice, patientList);
                            patientListView.setAdapter(patientAdapter);
                            patientListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                            ArrayList<String> doctorPatientsList = (ArrayList<String>) ((Doctor) user).getPatientsList();
                            //Go through all patients and mark checked if they exist in doctor's patientList
                            for(int i = 0; i < patientList.size(); i++) {
                                String patientUid = patientList.get(i).getUid();
                                if(doctorPatientsList.contains(patientUid)){
                                    patientListView.setItemChecked(i, true);
                                }
                            }
                        }

                        @Override
                        public void onFail(Exception e) {
                            Toast.makeText(DoctorAddPatient.this, "Failed to load all patients", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                } else {
                    onFail(new Exception());
                }
            }
            @Override
            public void onFail(Exception e) {
                Toast.makeText(DoctorAddPatient.this, "Failed to load your data", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //Save patients logic on click
        savePatientsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> patientsUidList = new ArrayList<>();

                //Retrieve all checked patients to be appointed to the doctor
                SparseBooleanArray checkedIndices = DoctorAddPatient.this.patientListView.getCheckedItemPositions();
                for(int i = 0; i < checkedIndices.size(); i++){
                    int patientIndexPos = checkedIndices.keyAt(i);
                    if(checkedIndices.get(patientIndexPos)){ //if patient is checked
                        patientsUidList.add(DoctorAddPatient.this.patientList.get(patientIndexPos).getUid());
                    }
                }

                //Show loading message
                Toast.makeText(DoctorAddPatient.this, "Saving patients, please wait..", Toast.LENGTH_SHORT).show();

                //Save doctor with the new patientsList
                firebaseHelper.updateDoctorPatientsList(patientsUidList, new FirebaseHelper.voidCallbackInterface() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(DoctorAddPatient.this, "Patients updated successfully.", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFail(Exception e) {
                        Toast.makeText(DoctorAddPatient.this, "Error while saving patients, please try again..", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        //Filter patients list by Name
        filterPatientField.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                DoctorAddPatient.this.patientAdapter.getFilter().filter(arg0);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

}//end of DoctorAddPatient{} class


