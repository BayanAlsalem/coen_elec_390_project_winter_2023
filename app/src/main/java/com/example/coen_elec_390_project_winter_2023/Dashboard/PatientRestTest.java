package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

    List<Integer> restReadings;

    private SemiCircleProgressBar semiCircleProgressBar;
    Button restartButton;
    BluetoothAsyncTask bluetoothAsyncTask;

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
        semiCircleProgressBar = findViewById(R.id.semiCircleProgressBar);
        Start_test = findViewById(R.id.btn_StartRest);
        Redo = findViewById(R.id.btn_redo);
        next_test = findViewById(R.id.btn_Contracting);
        restartButton = findViewById(R.id.restartButton);

        progressBar.setProgress(0);
        semiCircleProgressBar.setProgress(0);

        restartButton.setOnClickListener(view -> {
            restartButton.setVisibility(View.GONE);
            startBluetoothTask();
        });

        Start_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Start_test.setVisibility(View.INVISIBLE);
                if (ActivityCompat.checkSelfPermission(PatientRestTest.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                try {
                    btSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if(attemptToConnect()) {
                    System.out.println("CONNECTED FUCKKERS");
                    try {
                        btSocket.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    startBluetoothTask();

                }

                System.out.println("Reading Button Pressed");


            }

        });

        next_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientRestTest.this, PatientContractTest.class);
                intent.putIntegerArrayListExtra("restReadings", (ArrayList<Integer>) restReadings);
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

    @Override
    protected void onStop() {
        super.onStop();
        // Cancel the Bluetooth task and the timeout, and show the restart button here
        if(bluetoothAsyncTask!=null && restReadings==null) {
            bluetoothAsyncTask.cancel(true);
            bluetoothAsyncTask.cancelTimeout();
            restartButton.setVisibility(View.VISIBLE);
        }
    }

    private void startBluetoothTask() {
        // Assuming you have the Bluetooth device address
        String deviceAddress = selectedDevice.getAddress(); // Replace with the actual Bluetooth device address

        bluetoothAsyncTask = new BluetoothAsyncTask(deviceAddress, new Handler(Looper.getMainLooper()), progressBar,semiCircleProgressBar, new BluetoothAsyncTask.OnReadingsReceivedListener() {

            @Override
            public void onReadingsReceived(List<Integer> readings) {
                // Process the readings received from the Bluetooth device
                if(readings!=null) {
                    Log.d("MainActivity", "Readings: " + readings.toString());
                    restReadings=readings;
                    if(readings.size()!=0) {
                        next_test.setVisibility(View.VISIBLE);
                    }else{
                        Start_test.setVisibility(View.INVISIBLE);
                    }
                }
            }
        },this);
        bluetoothAsyncTask.execute();
    }



}
