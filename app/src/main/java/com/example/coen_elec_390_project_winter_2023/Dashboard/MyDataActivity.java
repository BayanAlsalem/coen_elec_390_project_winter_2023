package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.example.coen_elec_390_project_winter_2023.Controller.FirebaseHelper;
import com.example.coen_elec_390_project_winter_2023.Login.LoginActivity;
import com.example.coen_elec_390_project_winter_2023.Models.Reading;
import com.example.coen_elec_390_project_winter_2023.Models.User;
import com.example.coen_elec_390_project_winter_2023.Models.userOptions;
import com.example.coen_elec_390_project_winter_2023.R;




import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyDataActivity extends AppCompatActivity {
    // firebase variables
    FirebaseHelper firebaseHelper = new FirebaseHelper();

    // lists
    List<DataEntry> data = new ArrayList<>();
    List<Reading> listTest = new ArrayList<Reading>();

    // graph variables
    static Cartesian line;
    AnyChartView anyChartView;
    static Line series;


    //spinner variables
    Spinner spinner;
    ArrayAdapter<String> adapter;
    List<String> spinnerArray = new ArrayList<String>();

    //button variables
    Button button;


    public static boolean hasData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_data_activity);
        anyChartView = findViewById(R.id.my_data_chart1);
        spinner = findViewById(R.id.spinnerId);
        button = findViewById(R.id.request_appointmentId);

        line = AnyChart.line();
        anyChartView.setChart(line);

        //get intent from previous activity
        String userID = getIntent().getStringExtra("patientId");
        System.out.println(userID);

        // if user is a patient, hide the request appointment button
        firebaseHelper.getCurrentUser(new FirebaseHelper.getUserCallbackInterface() {
            @Override
            public void onSuccess(User user) {
                if (user.getUserType() == userOptions.userType.PATIENT) {
                    button.setVisibility(View.INVISIBLE);

                } else if (user.getUserType() == userOptions.userType.DOCTOR) {
                    button.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFail(Exception e) {
                Log.d("Error", "Error getting user");
            }
        });

        checkData();
        System.out.println("after check "+hasData);

        if(hasData) {
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
                    updateGraph();

                }
            });
        }else {
            Toast.makeText(MyDataActivity.this, "No data to display", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MyDataActivity.this, SplashActivity.class));
            finish();
        }

    }//end of onCreate() function

    // function to check if user has data in readings
    public void checkData(){
        String userID = getIntent().getStringExtra("patientId");
        System.out.println(userID);

        firebaseHelper.getReadingsNotCurrentUser(userID, new FirebaseHelper.getReadingsListCallbackInterface() {
            @Override
            public void onSuccess(List<Reading> readingsList) {
                if(readingsList.size() > 0){
                    System.out.println("has data");
                    hasData = true;
                }else{
                    System.out.println("no data");
                    hasData = false;
                }
            }

            @Override
            public void onFail(Exception e) {
                Log.d("Error", "Error getting readings");
                hasData = false;
            }
        });

    }

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

        listTest= firebaseHelper.getReadingsNotCurrentUser(userID, new FirebaseHelper.getReadingsListCallbackInterface() {
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
                // add the spinnerArray to the spinner and show it has a dropdown with arrow
                adapter = new ArrayAdapter<String>(MyDataActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
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

        firebaseHelper.getReadingsNotCurrentUser(userID, new FirebaseHelper.getReadingsListCallbackInterface() {
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
                System.out.println("NEW VALUES:" + flexedValues);

                System.out.println("data after being cleared: " + data);

                // Convert the flexed values to data entries
                data = listToEntry(flexedValues);

                System.out.println("data after being converted: " + data);


                // chart config
                line.xAxis(0)
                        .title("Time [s]")
                        .stroke("#000000 3")
                        .labels().fontSize(16).fontColor("#8C89C2");

                line.xAxis(0).ticks().stroke("#000000 3");
                line.xAxis(0).minorTicks().stroke("#CCCCCC 3");
                line.xAxis(0).title().fontSize(16);


                line.yAxis(0)
                        .title("Amplitude [mv]")
                        .stroke("#000000 3")
                        .labels().fontSize(16).fontColor("#8C89C2");

                line.yAxis(0).ticks().stroke("#000000 3");
                line.yAxis(0).minorTicks().stroke("#CCCCCC 3");
                line.yAxis(0).title().fontSize(16);


                line.title("Flexed Values");
                line.title().fontSize(20).fontColor("#8C89C2");


                //remove data from the chart
                line.removeAllSeries();


                // Add the data to the chart
                APIlib.getInstance().setActiveAnyChartView(anyChartView);
                line.line(data).stroke("3 #8C89C2");

            }

            @Override
            public void onFail(Exception e) {
                Log.d("Readings", "Error: " + e.getMessage());
            }
        });
    }


}//end of MyDataActivity class


