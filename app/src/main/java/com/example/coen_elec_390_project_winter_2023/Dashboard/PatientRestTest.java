package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coen_elec_390_project_winter_2023.Controller.FirebaseHelper;
import com.example.coen_elec_390_project_winter_2023.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PatientRestTest extends BluetoothReadingsActivity {

    private static final long START_TIME_IN_SECONDS = 30000;
    private TextView Countdown;
    private Button Start_test, Redo, next_test;
    private CountDownTimer countDownTimer;
    private boolean mTimerRunning;
    private long timeLeft = START_TIME_IN_SECONDS;
    String userID;


    boolean ready= false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_instruction_resting_data);
        /*
        firebaseHelper.createReading(readings,readings,  new FirebaseHelper.voidCallbackInterface() {
                                            @Override
                                            public void onSuccess() {
                                                Toast.makeText(BluetoothReadingsActivity.this, "Readings Created Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                            @Override
                                            public void onFail(Exception e) {
                                                Toast.makeText(BluetoothReadingsActivity.this, "Reading Creation Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }, userID);
         */
        if (getIntent().getExtras() != null) {
            userID=getIntent().getStringExtra("userID");
            selectedDevice = getIntent().getExtras().getParcelable("BTdevice");
        }else{
            System.out.println("Intent Failed");
            return;
        }
        btSocket=null;
        if(attemptToConnect()){

        }else{
            Intent intent = new Intent (PatientRestTest.this,BluetoothConnectionActivity.class);
            startActivity(intent);
        }
        progressBar=findViewById(R.id.determinateBarRest);
        Start_test = findViewById(R.id.btn_StartRest);
        Redo = findViewById(R.id.btn_redo);
        next_test = findViewById(R.id.btn_Contracting);

        progressBar.setProgress(0);

        Start_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*countDownTimer = new CountDownTimer(timeLeft, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        timeLeft = millisUntilFinished;
                        updateCountDownText();
                    }

                    @Override
                    public void onFinish() {
                        mTimerRunning = false;


                    }
                }.start();
                mTimerRunning = true;*/

                Start_test.setVisibility(View.INVISIBLE);

                System.out.println("Reading Button Pressed");
                try {

                    if(btSocket.isConnected()) {
                        if (startSignal()) {
                            readings = startReading( new FirebaseHelper.getReadingsCallbackInterface() {
                                @Override
                                public void onSuccess(List<Integer> readingsResult) {
                                    Toast.makeText(PatientRestTest.this, readingsResult.toString(), Toast.LENGTH_SHORT).show();
                                    readings=readingsResult;
                                    if(readings.size()!=0){
                                        Log.d("ReadingsFinal",readings.toString());
                                        Log.d("ReadingsFinal",userID);
                                        ready=true;
                                        Redo.setVisibility(View.VISIBLE);
                                        next_test.setVisibility(View.VISIBLE);
                                    }
                                }
                                @Override
                                public void onFail(Exception e) {
                                }
                            },progressBar);

                        }
                    }else{
                        System.out.println("Socket is Not Connected");
                    }


                } catch (
                        IOException e) {
                    throw new RuntimeException(e);
                } catch (
                        InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        });

        next_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientRestTest.this, PatientContractTest.class);
                intent.putIntegerArrayListExtra("restReadings", (ArrayList<Integer>) readings);
                intent.putExtra("userID", userID);
                intent.putExtra("BTdevice", selectedDevice);
                startActivity(intent);
            }
        });

        Redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeLeft = START_TIME_IN_SECONDS;
               // updateCountDownText();
                Redo.setVisibility(View.INVISIBLE);
                Start_test.setVisibility(View.VISIBLE);
                next_test.setVisibility(View.INVISIBLE);
            }
        });
        //updateCountDownText();
    }



}
