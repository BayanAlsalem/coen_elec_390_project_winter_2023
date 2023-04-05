package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.coen_elec_390_project_winter_2023.Controller.FirebaseHelper;
import com.example.coen_elec_390_project_winter_2023.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public class PatientContractTest extends BluetoothReadingsActivity{

    private static final long START_TIME_IN_SECONDS = 30000;

    private TextView CountdownContract;
    private Button Start_Flex, Redo2, end_test;
    private CountDownTimer countDownTimer;
    private boolean mTimerRunning;
    private long timeLeft = START_TIME_IN_SECONDS;
    String userID;

    List<Integer> flexedReading;
    private SemiCircleProgressBar semiCircleProgressBar;
    BluetoothAsyncTask bluetoothAsyncTask;

    Button restartButton;
    boolean ready= false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_instruction_flexing_data);
        if (getIntent().getExtras() != null) {
            userID=getIntent().getStringExtra("userID");
            selectedDevice = getIntent().getExtras().getParcelable("BTdevice");
            restReadings=getIntent().getIntegerArrayListExtra("restReadings");
        }else{
            System.out.println("Intent Failed");
            return;
        }
        btSocket=null;
        if(attemptToConnect()){

        }else{
            Intent intent = new Intent (PatientContractTest.this,BluetoothConnectionActivity.class);
            startActivity(intent);
        }
        progressBar=findViewById(R.id.determinateBarFlexed);
        Start_Flex = findViewById(R.id.btn_StartFlex);
        Redo2 = findViewById(R.id.btn_redo2);
        end_test = findViewById(R.id.btn_end);
        semiCircleProgressBar = findViewById(R.id.semiCircleProgressBarFlexed);

        restartButton = findViewById(R.id.restartButton);

        restartButton.setOnClickListener(view -> {
            restartButton.setVisibility(View.GONE);
            startBluetoothTask();
        });


        //CountdownContract = findViewById(R.id.text_view_countdown2);
        semiCircleProgressBar.setProgress(0);
        progressBar.setProgress(0);
        Start_Flex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*countDownTimer = new CountDownTimer(timeLeft, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        timeLeft = millisUntilFinished;
                    }

                    @Override
                    public void onFinish() {
                        mTimerRunning = false;


                    }
                }.start();
                mTimerRunning = true;*/
                Start_Flex.setVisibility(View.INVISIBLE);
                if (ActivityCompat.checkSelfPermission(PatientContractTest.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
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

        end_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ready) {
                    Intent intent = new Intent(PatientContractTest.this, ResultsActivity.class);
                    intent.putExtra("userID", userID);
                    intent.putIntegerArrayListExtra("flexedReadings",(ArrayList<Integer>) flexedReading);
                    intent.putIntegerArrayListExtra("restReadings",(ArrayList<Integer>) restReadings );
                    startActivity(intent);
                }
                else{
                    Toast.makeText(PatientContractTest.this, "Not Ready, Please wait to process.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Redo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeLeft = START_TIME_IN_SECONDS;
               // updateCountDownText();
                Redo2.setVisibility(View.INVISIBLE);
                Start_Flex.setVisibility(View.VISIBLE);
                end_test.setVisibility(View.INVISIBLE);
            }
        });
        //updateCountDownText();
    }

    @Override
    protected void onStop() {
        super.onStop();

        System.out.println("TRASHHHHHH TESTTT");
        // Cancel the Bluetooth task and the timeout, and show the restart button here
        if(bluetoothAsyncTask!=null) {
            System.out.println("ONSTOPPPPPP RANNNNN IFFF2222111414141222222");

                System.out.println("ONSTOPPPPPP RANNNNN IFFF");
                bluetoothAsyncTask.cancel(true);
                bluetoothAsyncTask.cancelTimeout();
                restartButton.setVisibility(View.VISIBLE);
        }
    }

/*    private void updateCountDownText() {
        int minutes = (int) (timeLeft / 1000) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d", seconds);

        CountdownContract.setText(timeLeftFormatted);
    }*/
private void startBluetoothTask() {
    // Assuming you have the Bluetooth device address
    String deviceAddress = selectedDevice.getAddress(); // Replace with the actual Bluetooth device address

    bluetoothAsyncTask = new BluetoothAsyncTask(deviceAddress, new Handler(Looper.getMainLooper()), progressBar,semiCircleProgressBar, new BluetoothAsyncTask.OnReadingsReceivedListener() {

        @Override
        public void onReadingsReceived(List<Integer> readings) {
            // Process the readings received from the Bluetooth device
            if(readings!=null) {
                Log.d("MainActivity", "Readings: " + readings.toString());
                flexedReading=readings;
                if(readings.size()!=0) {
                    firebaseHelper.createReading(readings,restReadings,  new FirebaseHelper.voidCallbackInterface() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(PatientContractTest.this, "Readings Created Successfully", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onFail(Exception e) {
                            Toast.makeText(PatientContractTest.this, "Reading Creation Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }, userID);
                    ready=true;
                    end_test.setVisibility(View.VISIBLE);
                }else{
                    ready=false;
                    Start_Flex.setVisibility(View.VISIBLE);
                }
            }
        }
    }, this);
    bluetoothAsyncTask.execute();
}

    public List<Integer> startReading(FirebaseHelper.getReadingsCallbackInterface callback, ProgressBar progressBarF) throws IOException, InterruptedException {
        List<Integer> readings = new ArrayList<>();
        String resultString="";
        int externalCounter = 0;
        int counter = 0;
        inStream = null;
        inStream = btSocket.getInputStream();
        byte[] buffer = new byte[256];  // buffer store for the stream
        int bytes;
        while (externalCounter < 3) {
            inStream.skip(inStream.available());
            Thread.sleep(100);
            btSocket.getOutputStream().write(234);
            Thread.sleep(10);
            while (counter < 100 && inStream.available()>0) {
                Thread.sleep(50);
                boolean endMarker = false;
                String message="";
                if(inStream.available()>0){

                    bytes = inStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);
                    System.out.println(readMessage);
                    message=readMessage;
                }
                message=message.substring(1);
                System.out.println("message is: "+message);
                if (message.equals("start")) {
                    int readingsCounter = 0;
                    System.out.println("Correct Device is Connected and Starting Reading");
                    do {
                        if(btSocket.isConnected()) {
                            //Thread.sleep(50);
                            message = "";
                            bytes = inStream.read(buffer);
                            String readMessage = new String(buffer, 0, bytes);
                            System.out.println(readMessage);
                            message = readMessage;
                            int progress=readingsCounter/2;
                            // Put your UI update code here

                            progressBarF.setProgress(0);
                            progressBarF.setMax(progress);
                            progressBarF.setProgress(progress);


                            System.out.println("Message is: " + message);
                            if (message.equals("end")) {
                                endMarker = true;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    readings = Stream.of(resultString.split(","))
                                            .map(String::trim)
                                            .map(Integer::parseInt)
                                            .collect(Collectors.toList());
                                }
                                System.out.println("Result String is: " + resultString);
                                System.out.println("Readings: " + readings.toString());
                                callback.onSuccess(readings);
                                return readings;
                            } else {
                                resultString += message;
                                System.out.println("Value is: " + message);
                            }
                            readingsCounter++;
                            //Thread.sleep(10);
                        }else{
                            break;
                        }
                    } while (!endMarker && readingsCounter < 1000);
                    if (readingsCounter >= 1000) {
                        System.out.println("Readings Timeout");
                    }
                    break;
                }
                if(endMarker){
                    System.out.println("Readings: " + readings.toString());
                    return readings;
                }
                Thread.sleep(10);
                counter++;
            }
            if (readings.isEmpty()) {
                System.out.println("Readings is EMPTY");
            } else {
                System.out.println("Readings: " + readings.toString());
            }
            externalCounter++;
        }
        return readings;
    }

}