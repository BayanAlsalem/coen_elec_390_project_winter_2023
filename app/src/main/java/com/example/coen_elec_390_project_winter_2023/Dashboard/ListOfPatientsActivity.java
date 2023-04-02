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
import com.example.coen_elec_390_project_winter_2023.Models.Patient;
import com.example.coen_elec_390_project_winter_2023.Models.User;
import com.example.coen_elec_390_project_winter_2023.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListOfPatientsActivity extends AppCompatActivity {
    private ListView patientListView;
    private ArrayList<User> patientList = new ArrayList<>();
    private ArrayList<String> patientListNames = new ArrayList<>();
    private ArrayAdapter<String> patientAdapter;
    FirebaseHelper firebaseHelper = new FirebaseHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_patients_layout);

        patientListView = findViewById(R.id.patientListViewID);

        CollectionReference usersRef = (FirebaseHelper.db()).collection("users"); // get a reference to the "users" collection

        usersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String userId = document.getId(); // get the user ID
                        String userType = document.getString("userType"); // get the value of the "userType" key
                        Log.d("firebase", "The user type for user " + userId + " is: " + userType);
                        // if user is a patient, add their name to the patientList
                        if (userType.equals("PATIENT")) {
                            patientListNames.add(document.getString("name").toString());
                            //create a new user object and add it to the patientList
                            User user = new Patient(document.getString("name").toString(), document.getString("email").toString(), document.getString("password").toString(), document.getString("uid").toString());
                            patientList.add(user);

                        }
                    }

                    System.out.println(patientList);
                    patientAdapter = new ArrayAdapter<String>(ListOfPatientsActivity.this, R.layout.patient_list_item, R.id.patientNameTextView, patientListNames) {
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);

                            ImageView patientImageView = view.findViewById(R.id.patientImageView);
                            // Set the image resource for the ImageView
                            patientImageView.setImageResource(R.drawable.user);

                            TextView patientNameTextView = view.findViewById(R.id.patientNameTextView);
                            // Set the text for the TextView
                            patientNameTextView.setText(patientListNames.get(position));

                            return view;
                        }
                    };
                    patientListView.setAdapter(patientAdapter);

                } else {
                    Log.d("firebase", "Error getting user documents: " + task.getException());
                }
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
                intent.putExtra("patientEmail", patientList.get(i).getEmail());
                intent.putExtra("patientPassword", patientList.get(i).getPassword());
                intent.putExtra("patientUserType", patientList.get(i).getUserType());
                intent.putExtra("patientId", patientList.get(i).getUid());
                startActivity(intent);
            }
        });
    }
}
