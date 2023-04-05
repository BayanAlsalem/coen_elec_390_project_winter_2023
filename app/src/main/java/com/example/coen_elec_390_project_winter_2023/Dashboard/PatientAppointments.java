package com.example.coen_elec_390_project_winter_2023.Dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.coen_elec_390_project_winter_2023.Controller.FirebaseHelper;
import com.example.coen_elec_390_project_winter_2023.Models.Appointment;
import com.example.coen_elec_390_project_winter_2023.Models.Reading;
import com.example.coen_elec_390_project_winter_2023.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class PatientAppointments extends AppCompatActivity {
    FirebaseHelper firebaseHelper;
    List<Appointment> listTest;

    ListView spinner;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointments);
        firebaseHelper= new FirebaseHelper();
        spinner = findViewById(R.id.appointmentList);
        context = this;

        updateSpinner();


    }
    public void updateSpinner(){
       // String userID = getIntent().getStringExtra("patientId");
      //  System.out.println(userID);

        listTest= firebaseHelper.getAppointments(new FirebaseHelper.getAppointmentsListCallbackInterface() {
            @Override
            public void onSuccess(List<Appointment> readingsList) {
                listTest = readingsList;
                Collections.sort(listTest, new Comparator<Appointment>() {
                    public int compare(Appointment o1, Appointment o2) {
                        if (o1.getRequestDate() == null || o2.getRequestDate() == null)
                            return 0;
                        return o2.getRequestDate().compareTo(o1.getRequestDate());
                    }
                });

                // Change the date format to December 02, 2020 00:00 PM from Wed Dec 02 00:00:00 EDT 2020
//                for (int i = 0; i < listTest.size(); i++) {
//                    String date = listTest.get(i).getReadingDate().toString();
//                    String[] dateArray = date.split(" ");
//                    String month = dateArray[1];
//                    String day = dateArray[2];
//                    String year = dateArray[5];
//                    String time = dateArray[3];
//                    String[] timeArray = time.split(":");
//                    String hour = timeArray[0];
//                    String minute = timeArray[1];
//                    String ampm = "AM";
//                    if (Integer.parseInt(hour) > 12) {
//                        hour = String.valueOf(Integer.parseInt(hour) - 12);
//                        ampm = "PM";
//                    }
//                    String newDate = month + " " + day + ", " + year + " " + hour + ":" + minute + " " + ampm;
//                    spinnerArray.add(newDate);
//
//                }

                // add the spinnerArray to the spinner and show it has a dropdown with arrow
                List<String> spinnerArray = new ArrayList<>();
                for (int i = 0; i < listTest.size(); i++) {
                    // add the date to the spinnerArray
                    spinnerArray.add(listTest.get(i).getDoctorName());
                }

                ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_2, android.R.id.text1, listTest) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                        TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                        text1.setText(listTest.get(position).getDoctorName());
                        text2.setText(listTest.get(position).getRequestDate()+": \n"+listTest.get(position).getReason());

                        return view;
                    }
                };

                spinner.setAdapter(adapter);


            }

            @Override
            public void onFail(Exception e) {
                Log.d("Readings", "Error: " + e.getMessage());


            }
        });
    }
}