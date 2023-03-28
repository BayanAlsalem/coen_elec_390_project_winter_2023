package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.example.coen_elec_390_project_winter_2023.Controller.FirebaseHelper;
import com.example.coen_elec_390_project_winter_2023.Models.Reading;
import com.example.coen_elec_390_project_winter_2023.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {
    //TODO change the name of this class to ReadingResults or Results or GraphResults

    List<DataEntry> data = new ArrayList<>();
    FirebaseHelper firebaseHelper = new FirebaseHelper();
    List<Reading> listTest = new ArrayList<Reading>();

    String userID;

    ArrayList<Integer> flexedReading;
    ArrayList<Integer> restReading;

    Cartesian cartesian;
    AnyChartView anyChartView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_dashboard_layout);
        anyChartView = findViewById(R.id.any_chart_view);


        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
// get data via the key
        String userEmail = extras.getString("userEmail");
        userID = extras.getString("userID");
        if (userID != null) {
            System.out.println(userID);
            System.out.println("USER ID IS NOT NULLL");
        }
        flexedReading = extras.getIntegerArrayList("flexedReadings");

        restReading = extras.getIntegerArrayList("restReadings");

        if(!(flexedReading!=null) || !(restReading!=null)){
            System.out.println("Readings are null");
            return;
        }

        // GRAPHHH
        data = listToEntry(restReading,flexedReading);

        Set set = Set.instantiate();
        set.data(data);
        cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Preview of Readings");

       // cartesian.yAxis(0).title("Millivolts");
        cartesian.xAxis(0).title("Milliseconds");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name("Rested");
        series1.color("#D6D1E7");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series2 = cartesian.line(series2Mapping);
        series2.name("Contracted");
        series2.color("#8C89C2");
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartesian);

        // END OF GRAPH



        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        Button readingsButton = (Button)findViewById(R.id.take_reading_button);

        readingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ResultsActivity.this, PatientInstructionsActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });

      //  updateGraph();

    }//end of onCreate() function

    protected void onPause() {
        super.onPause();
        //updateGraph();
    }


    protected void onResume() {
        super.onResume();

        System.out.println("OnResume...");
        userID = firebaseHelper.getCurrentUserId();
       // updateGraph();
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
                startActivity(new Intent(ResultsActivity.this, SplashActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public List<DataEntry> listToEntry(List<Integer> reading1, List<Integer> reading2){
        List<DataEntry> data = new ArrayList<>();
        for(int i=0;i<reading1.size();i++){
            data.add(new CustomDataEntry(i*150, reading1.get(i), reading2.get(i)));
        }

        return data;
    }
    public void updateGraph(){


        System.out.println("Update Graph..");
        System.out.println(userID);

        /*listTest= firebaseHelper.getReadings(userID, new FirebaseHelper.getReadingsListCallbackInterface() {
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

                Log.d("Readings","ReadingsList: "+readingsList.get(0).toString());

                if(listTest.size()!=0) {
                    data=listToEntry(listTest.get(0).getFlexedValues());
                }

                line = AnyChart.line();
                line.setData(data);
                anyChartView.setChart(line);
            }

            @Override
            public void onFail(Exception e) {

            }
        });*/

       data = listToEntry(restReading,flexedReading);

        Set set = Set.instantiate();
        set.data(data);
        cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Preview of Readings");

        cartesian.yAxis(0).title("Millivolts");
        cartesian.xAxis(0).title("Milliseconds");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name("Rested");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series2 = cartesian.line(series2Mapping);
        series2.name("Contracted");
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartesian);
    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(Number x, Number value, Number value2) {
            super(x,value);
            setValue("value2", value2);
        }

    }
}//end of PatientDashboardActivity{} class


