package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coen_elec_390_project_winter_2023.Controller.FirebaseHelper;
import com.example.coen_elec_390_project_winter_2023.Models.Doctor;
import com.example.coen_elec_390_project_winter_2023.Models.Patient;
import com.example.coen_elec_390_project_winter_2023.Models.User;
import com.example.coen_elec_390_project_winter_2023.Models.userOptions;
import com.example.coen_elec_390_project_winter_2023.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListOfPatientsActivity extends AppCompatActivity {
    private ListView patientListView;
    private ArrayList<User> patientList = new ArrayList<>();
    private ArrayAdapter<User> patientAdapter;
    FirebaseHelper firebaseHelper = new FirebaseHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_patients_layout);

        patientListView = findViewById(R.id.patientListViewID);

        //Load all patients
        Toast.makeText(ListOfPatientsActivity.this, "Please wait, while we load your patients...", Toast.LENGTH_SHORT).show();

        firebaseHelper.getCurrentUser(new FirebaseHelper.getUserCallbackInterface() {
            @Override
            public void onSuccess(User user) {
                if (user.getUserType() == userOptions.userType.DOCTOR){

                    //Get all patients
                    firebaseHelper.loadAllPatients(new FirebaseHelper.getUsersCallbackInterface() {
                        @Override
                        public void onSuccess(List<User> users) {
                            //Load doctor patients
                            ArrayList<String> doctorPatientsList = (ArrayList<String>) ((Doctor) user).getPatientsList();

                            //Load the doctor's patients data
                            for(int i = 0; i < users.size(); i++){
                                String patientUid = users.get(i).getUid();
                                if(doctorPatientsList.contains(patientUid)){
                                    patientList.add(users.get(i));
                                }
                            }

                            //Set Adapter
                            patientAdapter = new ArrayAdapter<User>(ListOfPatientsActivity.this, R.layout.patient_list_item, R.id.patientNameTextView, patientList) {
                                @NonNull
                                @Override
                                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                    View view = super.getView(position, convertView, parent);

                                    ImageView patientImageView = view.findViewById(R.id.patientImageView);
                                    // Set the image resource for the ImageView
                                    patientImageView.setImageResource(R.drawable.user);

                                    TextView patientNameTextView = view.findViewById(R.id.patientNameTextView);

                                    // Set the text for the TextView
                                    patientNameTextView.setText(patientList.get(position).getName());

                                    return view;
                                }
                            };
                            patientListView.setAdapter(patientAdapter);
                        }

                        @Override
                        public void onFail(Exception e) {
                            Toast.makeText(ListOfPatientsActivity.this, "Failed to load all patients", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                } else {
                    onFail(new Exception());
                }
            }
            @Override
            public void onFail(Exception e) {
                Toast.makeText(ListOfPatientsActivity.this, "Failed to load your data", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // onclick listener for each patient in the list
        // when clicked, it will open the patient's profile
        patientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ListOfPatientsActivity.this, "You clicked on " + patientList.get(i).getName(), Toast.LENGTH_SHORT).show();
                // intent to open patient's profile and send the patient's name through intent
                Intent intent = new Intent(ListOfPatientsActivity.this, MyDataActivity.class);
                intent.putExtra("patientName", patientList.get(i).getName());
                intent.putExtra("patientUserType", patientList.get(i).getUserType());
                intent.putExtra("patientId", patientList.get(i).getUid());
                startActivity(intent);
            }
        });
    }
}
