package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coen_elec_390_project_winter_2023.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ListOfPatientsActivity extends AppCompatActivity{
    private ListView patientListView;
    private DatabaseReference mDatabase;
    private ArrayList<String> patientList = new ArrayList<>();
    private ArrayAdapter<String> patientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_patients_layout);

        patientListView = findViewById(R.id.patientListViewID);

        // For every patient in the database, add their name to the list

        // For each row, display the patient's name
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child("Patients").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    // for item in collection, add name to patientList
                    for (DataSnapshot patient : task.getResult().getChildren()) {
                        patientList.add(patient.child("name").getValue().toString());
                    }

                    System.out.println(patientList);
                    patientAdapter = new ArrayAdapter<String>(ListOfPatientsActivity.this, android.R.layout.simple_list_item_1, patientList);
                    patientListView.setAdapter(patientAdapter);
                }
            }
        });


        // create adapter
        // set adapter to listview
        // print to console the arraylist


        }

}//end of ListOfPatientsActivity{} class


