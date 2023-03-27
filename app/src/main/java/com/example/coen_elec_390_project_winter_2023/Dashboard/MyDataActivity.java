package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.Cartesian;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.ValueDataEntry;
import com.example.coen_elec_390_project_winter_2023.Controller.FirebaseHelper;
import com.example.coen_elec_390_project_winter_2023.Models.Reading;
import com.example.coen_elec_390_project_winter_2023.R;




import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyDataActivity extends AppCompatActivity {
    List<DataEntry> data = new ArrayList<>();
    FirebaseHelper firebaseHelper = new FirebaseHelper();
    List<Reading> listTest = new ArrayList<Reading>();
    static Cartesian line;
    AnyChartView anyChartView;
    Spinner spinner;
    ArrayAdapter<String> adapter;
    List<String> spinnerArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_data_activity);
        anyChartView = findViewById(R.id.my_data_chart1);
        spinner = findViewById(R.id.spinnerId);

        // chart config
        line = AnyChart.line();
        line.getXAxis().setTitle("Time [s]");
        line.getYAxis().setTitle("Amplitude [mv]");
        line.setPalette(new String[]{"#8C89C2"});
        line.setTitle("Flexed Values");
        line.setAnimation(true);
        line.getXAxis().getLabels().setFontColor("#8C89C2");
        line.getYAxis().getLabels().setFontColor("#8C89C2");

        updateSpinner();
        updateGraph();

        // when a different date in the spinner is chosen, update the graph to show the new data on that date
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateGraph();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }//end of onCreate() function

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_patient_dashboard, menu);
        return true;
    }

    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.patient_logout:
                firebaseHelper.logout();
                startActivity(new Intent(MyDataActivity.this, SplashActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public List<DataEntry> listToEntry(List<Integer> reading){
        List<DataEntry> data = new ArrayList<>();
        for(int i=0;i<reading.size();i++){
            data.add(new ValueDataEntry((i*150), reading.get(i)));
        }

        return data;
    }

// function to get dates from firebase and add them to the spinner
    public void updateSpinner(){
        String userID = getIntent().getStringExtra("patientId");
        System.out.println(userID);

        listTest= firebaseHelper.getReadingsDoctor(userID, new FirebaseHelper.getReadingsListCallbackInterface() {
            @Override
            public void onSuccess(List<Reading> readingsList) {
                listTest = readingsList;
                Collections.sort(listTest, new Comparator<Reading>() {
                    public int compare(Reading o1, Reading o2) {
                        if (o1.getReadingDate() == null || o2.getReadingDate() == null)
                            return 0;
                        return o2.getReadingDate().compareTo(o1.getReadingDate());
                    }
                });

                for (int i = 0; i < listTest.size(); i++) {
                    // add the date to the spinnerArray
                    spinnerArray.add(listTest.get(i).getReadingDate().toString());
                }
                // add the spinnerArray to the spinner
                adapter = new ArrayAdapter<String>(MyDataActivity.this, android.R.layout.simple_spinner_item, spinnerArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onFail(Exception e) {
                Log.d("Readings", "Error: " + e.getMessage());

            }
        });
    }




    public void updateGraph(){

        //get the patientId from intent extras
        String userID = getIntent().getStringExtra("patientId");

        firebaseHelper.getReadingsDoctor(userID, new FirebaseHelper.getReadingsListCallbackInterface() {
            @Override
            public void onSuccess(List<Reading> readingsList) {
                // Sort the readings by date
                Collections.sort(readingsList, new Comparator<Reading>() {
                    public int compare(Reading o1, Reading o2) {
                        if (o1.getReadingDate() == null || o2.getReadingDate() == null)
                            return 0;
                        return o2.getReadingDate().compareTo(o1.getReadingDate());
                    }
                });

                // Get the selected date from the spinner
                String selectedDate = spinner.getSelectedItem().toString();
                Log.d("Readings","Date: "+ selectedDate);

                // Clear the existing data
                data.clear();

                // Loop through the readings and add the data for the selected date to the chart
                List<Integer> flexedValues = new ArrayList<Integer>();
                for(int i=0;i<listTest.size();i++){
                    if(listTest.get(i).getReadingDate().toString().equals(selectedDate)){
                        flexedValues = listTest.get(i).getFlexedValues();
                    }
                }

                // Convert the flexed values to data entries
                data = listToEntry(flexedValues);

                // Add the data to the chart
                line.data(data);
                anyChartView.setChart(line);
            }

            @Override
            public void onFail(Exception e) {
                Log.d("Readings", "Error: " + e.getMessage());
            }
        });
    }


}//end of MyDataActivity class


