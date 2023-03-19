package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coen_elec_390_project_winter_2023.Controller.FirebaseHelper;
import com.example.coen_elec_390_project_winter_2023.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListOfPatientsActivity extends AppCompatActivity{
    private ListView patientListView;
    private ArrayList<String> patientList = new ArrayList<>();
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
                            patientList.add(document.getString("name").toString());
                        }
                    }

                    System.out.println(patientList);
                    patientAdapter = new ArrayAdapter<String>(ListOfPatientsActivity.this, android.R.layout.simple_list_item_1, patientList);
                    patientListView.setAdapter(patientAdapter);

                } else {
                    Log.d("firebase", "Error getting user documents: " + task.getException());
                }
            }
        });



        }

}//end of ListOfPatientsActivity{} class


