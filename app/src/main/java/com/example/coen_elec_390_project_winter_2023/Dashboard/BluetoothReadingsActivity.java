package com.example.coen_elec_390_project_winter_2023.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.coen_elec_390_project_winter_2023.Controller.FirebaseHelper;
import com.example.coen_elec_390_project_winter_2023.Login.LoginActivity;
import com.example.coen_elec_390_project_winter_2023.R;
import com.example.coen_elec_390_project_winter_2023.SignUp.PatientSignUpActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BluetoothReadingsActivity extends AppCompatActivity {
    BluetoothDevice selectedDevice;

    InputStream inStream;
    OutputStream outStream;

    BluetoothSocket btSocket;

    ArrayList<Integer> restReadings = new ArrayList<>();

    ArrayList<Integer> flexedReadings = new ArrayList<>();

    FirebaseHelper firebaseHelper = new FirebaseHelper();
    String userID;
    Button readingButton;

    ProgressBar progressBar;

    List<Integer> readings = new ArrayList<>();

    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().getExtras() != null) {
            selectedDevice = getIntent().getExtras().getParcelable("BTdevice");
            userID=getIntent().getStringExtra("userID");
        }else{
            System.out.println("Intent Failed");
            return;
        }
        btSocket = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_readings);
        readingButton = (Button) findViewById(R.id.readingButton);
        progressBar = findViewById(R.id.determinateBar);
        if(attemptToConnect()){

        }else{
            Intent intent = new Intent (BluetoothReadingsActivity.this,BluetoothConnectionActivity.class);
            startActivity(intent);
        }
        // old code started here

        readingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Reading Button Pressed");
                try {

                    if(btSocket.isConnected()) {
                        if (startSignal()) {
                            readings = startReading( new FirebaseHelper.getReadingsCallbackInterface() {
                                @Override
                                public void onSuccess(List<Integer> readingsResult) {
                                    Toast.makeText(BluetoothReadingsActivity.this, readingsResult.toString(), Toast.LENGTH_SHORT).show();
                                    readings=readingsResult;
                                    if(readings.size()!=0){
                                        Log.d("ReadingsFinal",readings.toString());
                                        Log.d("ReadingsFinal",userID);
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
                                    }
                                }
                                @Override
                                public void onFail(Exception e) {
                                }
                            }, progressBar);

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

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            btSocket.close();
            System.out.println(btSocket.isConnected());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean attemptToConnect(){
        int counter = 0;
        do {
            try {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        System.out.println("GOT PERMISSION LESS GOOOO");
                        ActivityCompat.requestPermissions(BluetoothReadingsActivity.this, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, 2);
                    } else {
                        System.out.println("BlueTooth Permission Denied");
                        return false;
                    }

                }
                System.out.println(selectedDevice.getName());
                btSocket = selectedDevice.createRfcommSocketToServiceRecord(mUUID);
                System.out.println(btSocket);
                btSocket.connect();
                System.out.println(btSocket.isConnected());
            } catch (IOException e) {
                e.printStackTrace();
            }
            counter++;
        } while (!btSocket.isConnected() && counter<3);
        if(btSocket.isConnected())
            return true;
        else return false;
    }
    public void onResume() {
        super.onResume();
        int counter = 0;
        if(attemptToConnect()){

        }else{
            Intent intent = new Intent (BluetoothReadingsActivity.this,BluetoothConnectionActivity.class);
            startActivity(intent);
        }
    }
    public boolean startSignal() throws IOException, InterruptedException { //sends a signal and waits for a response to make sure everythings right
        int counter=0;
        int externalCounter=0;
        inStream = null;
        inStream = btSocket.getInputStream();
        while(externalCounter < 6) {
            Thread.sleep(100);
            //inputStream.skip(inputStream.available());
            btSocket.getOutputStream().write(235);
            Thread.sleep(10);
            while (counter < 6 && inStream.available() > 0) {
                int input;
                input = inStream.read();
                System.out.println(input);
                if (input == 25) {
                    System.out.println("Correct Device is Connected and Authorized");
                    return true;
                } else {
                    System.out.println("Unable to Authorize, Retrying");
                }
                Thread.sleep(10);
            }
            externalCounter++;
        }
        System.out.println("Authorization Failed");
        return false;
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
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }



}
// old code
                   /* try {
                        //now you can use out to send output via out.write
                        for (int i = 0; i < 150; i++) {
                            btSocket.getOutputStream().write(i);
                            //Thread.sleep(100);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }*/

                    /*InputStream inputStream = null;
                    byte[] buffer = new byte[256];  // buffer store for the stream
                    int bytes; // bytes returned from read()
                    try {
                        inputStream = btSocket.getInputStream();
                        inputStream.skip(inputStream.available());
                        for (int i = 0; i < 100; i++) {
                            if(btSocket.isConnected()) {
                                if(inputStream.available()>0){
                                    bytes = inputStream.read(buffer);
                                    String readMessage = new String(buffer, 0, bytes);
                                    System.out.println(readMessage);
                                }
                            }
                            Thread.sleep(100);
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }*/
//InputStream inputStream = null;
// bytes returned from read()
        /*boolean flag= false;
        while(!flag){
            try {
                btSocket.getOutputStream().write(234);
                Thread.sleep(10);

                InputStream inputStream = null;
                byte[] buffer = new byte[256];  // buffer store for the stream
                int bytes; // bytes returned from read()
                inputStream = btSocket.getInputStream();
                inputStream.skip(inputStream.available());
                for (int i = 0; i < 150; i++) {
                    if(btSocket.isConnected()) {
                        if(inputStream.available()>0){
                            //bytes = inputStream.read(buffer);
                            // String readMessage = new String(buffer, 0, bytes);
                            System.out.println(inputStream.read());
                        }
                    }
                    Thread.sleep(10);
                }
                flag = true;


                Thread.sleep(100);

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
*/