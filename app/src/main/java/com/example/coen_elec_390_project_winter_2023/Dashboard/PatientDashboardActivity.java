//package com.example.coen_elec_390_project_winter_2023.Dashboard;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.anychart.anychart.AnyChart;
//import com.anychart.anychart.AnyChartView;
//import com.anychart.anychart.Cartesian;
//import com.anychart.anychart.DataEntry;
//import com.anychart.anychart.Pie;
//import com.anychart.anychart.ValueDataEntry;
//import com.example.coen_elec_390_project_winter_2023.Controller.FirebaseHelper;
//import com.example.coen_elec_390_project_winter_2023.Login.LoginActivity;
//import com.example.coen_elec_390_project_winter_2023.Models.Reading;
//import com.example.coen_elec_390_project_winter_2023.R;
//import com.example.coen_elec_390_project_winter_2023.SignUp.PatientSignUpActivity;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
//public class PatientDashboardActivity extends AppCompatActivity {
//    //TODO change the name of this class to ReadingResults or Results or GraphResults
//
//    List<DataEntry> data = new ArrayList<>();
//    FirebaseHelper firebaseHelper = new FirebaseHelper();
//    List<Reading> listTest = new ArrayList<Reading>();
//
//    String userID;
//    String userEmail;
//
//    Cartesian line = AnyChart.line();
//    AnyChartView anyChartView;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.patient_dashboard_layout);
//        anyChartView = findViewById(R.id.any_chart_view);
//
//        /*Bundle extras = getIntent().getExtras();
//        if (extras == null) {
//            return;
//        }
//// get data via the key
//        String userEmail = extras.getString("userEmail");
//        if (userEmail != null) {
//        System.out.println(userEmail);
//        }else{
//            System.out.println("USER EMAIL IS NULLL");
//        }
//        userID = extras.getString("userID");
//        if (userID != null) {
//            System.out.println(userID);
//            System.out.println("USER ID IS NOT NULLL");
//        }*/
//
//        if (savedInstanceState == null) {
//            Bundle extras = getIntent().getExtras();
//            if (extras != null) {
//                userEmail = extras.getString("userEmail");
//                userID = extras.getString("userID");
//            }
//        } else {
//            userEmail = savedInstanceState.getString("userEmail");
//            userID = savedInstanceState.getString("userID");
//        }
//
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(false);
//        }
//
//        Button readingsButton = (Button)findViewById(R.id.take_reading_button);
//
//        readingsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent= new Intent(PatientDashboardActivity.this, PatientInstructionsActivity.class);
//                intent.putExtra("userID",userID);
//                startActivity(intent);
//            }
//        });
//
//        updateGraph();
//
//    }//end of onCreate() function
//
//    protected void onPause() {
//        super.onPause();
//        //updateGraph();
//    }
//
//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putString("userEmail", userEmail);
//        outState.putString("userID", userID);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        userEmail = savedInstanceState.getString("userEmail");
//        userID = savedInstanceState.getString("userID");
//    }
//
//    protected void onResume() {
//        super.onResume();
//
//        System.out.println("OnResume...");
//        userID = firebaseHelper.getCurrentUserId();
//        updateGraph();
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_patient_dashboard, menu);
//        return true;
//    }
//    // this event will enable the back
//    // function to the button on press
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.patient_logout:
//                firebaseHelper.logout();
//                startActivity(new Intent(PatientDashboardActivity.this, SplashActivity.class));
//                finish();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    public List<DataEntry> listToEntry(List<Integer> reading){
//        List<DataEntry> data = new ArrayList<>();
//        for(int i=0;i<reading.size();i++){
//            data.add(new ValueDataEntry(i*150, reading.get(i)));
//        }
//
//        return data;
//    }
//    public void updateGraph(){
//
//
//        anyChartView.setChart(line);
//
//        System.out.println("Update Graph..");
//        System.out.println(userID);
//
//        listTest= firebaseHelper.getReadings(userID, new FirebaseHelper.getReadingsListCallbackInterface() {
//            @Override
//            public void onSuccess(List<Reading> readingsList) {
//                listTest = readingsList;
//                Collections.sort(listTest, new Comparator<Reading>() {
//                    public int compare(Reading o1, Reading o2) {
//                        if (o1.getReadingDate() == null || o2.getReadingDate() == null)
//                            return 0;
//                        return o2.getReadingDate().compareTo(o1.getReadingDate());
//                    }
//                });
//
//                Log.d("Readings","ReadingsList: "+readingsList.get(0).toString());
//
//                if(listTest.size()!=0) {
//                    data=listToEntry(listTest.get(0).getFlexedValues());
//                }
//
//                line = AnyChart.line();
//                line.setData(data);
//                anyChartView.setChart(line);
//            }
//
//            @Override
//            public void onFail(Exception e) {
//
//            }
//        });
//    }
//}//end of PatientDashboardActivity{} class
//
//
