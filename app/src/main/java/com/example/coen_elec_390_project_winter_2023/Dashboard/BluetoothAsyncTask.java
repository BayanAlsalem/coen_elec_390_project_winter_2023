package com.example.coen_elec_390_project_winter_2023.Dashboard;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.coen_elec_390_project_winter_2023.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BluetoothAsyncTask extends AsyncTask<Void, Integer, List<Integer>> {
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // Replace with the UUID of your Bluetooth module


    private final String deviceAddress;
    private final Handler handler;
    private final ProgressBar progressBar;
    private final OnReadingsReceivedListener onReadingsReceivedListener;

    private final SemiCircleProgressBar semiCircleProgressBar;

    InputStream inStream;

    OutputStream outStream;

    BluetoothSocket btsocket;

    private List<Integer> readings;

    private static final long TIMEOUT_MS = 5000; // 5 seconds time slice
    private long timeout;
    private Handler timeoutHandler;

    private final AppCompatActivity activity;

    public BluetoothAsyncTask(String deviceAddress, Handler handler, ProgressBar progressBar,SemiCircleProgressBar semiCircleProgressBar, OnReadingsReceivedListener onReadingsReceivedListener, AppCompatActivity activity) {
        this.deviceAddress = deviceAddress;
        this.handler = handler;
        this.progressBar = progressBar;
        this.onReadingsReceivedListener = onReadingsReceivedListener;
        this.semiCircleProgressBar = semiCircleProgressBar;
        this.activity = activity;
        readings = new ArrayList<>();
    }

    @Override
    protected List<Integer> doInBackground(Void... voids) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
        btsocket = null;
        if (attemptToConnect()) {

            try {
                //btsocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                /*btsocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                btsocket.connect();*/
                inStream = btsocket.getInputStream();
                outStream = btsocket.getOutputStream();

            }catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

                // Send "HELLO" message
                try {
                    outStream.write("HELLO\n".getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

                // Read the "READY" response
                try {
                    StringBuilder helloMessageBuilder = new StringBuilder();
                    int helloChar;
                    while ((helloChar = inStream.read()) != -1 && helloChar != '\n' ) {
                        if (helloChar !='\n') { // Check if the character is not a space
                            helloMessageBuilder.append((char) helloChar);
                        }
                    }
                    String helloMessage = helloMessageBuilder.toString().trim();

                    System.out.println(helloMessage);

                    if (!(helloMessage.equals("READY"))) {
                        String[] array = helloMessage.split("");
                        for(int i=0;i< array.length;i++){
                            System.out.println(array[i]);
                        }
                        System.out.println("HELLO MESSAGE ISNT RIGHT");
                        return null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

                // Request readings
                try {
                    System.out.println("Sending Get Readings");
                    outStream.write("READINGS\n".getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

                // Read readings
                List<Integer> readings = new ArrayList<>();
                int reading;
                String fullReading="";
                try {
                    while (!isCancelled()) {
                            startTimeout();
                            String readingStr = "";
                            int incomingChar;
                            while ((incomingChar = inStream.read()) != -1 && incomingChar != '\n') {
                                readingStr += (char) incomingChar;
                            }
                            readingStr = readingStr.trim();
                            System.out.println(readingStr);
                            if ("DONE".equals(readingStr)) {
                                System.out.println("GOT DONE");
                                break;
                            }
                            /*String[] array = readingStr.split("");
                            fullReading+= readingStr;
                            fullReading+=',';*/
                            /*for(int i=0; i< array.length;i++){
                                System.out.println(array[i]);
                            }*/
                            reading = Integer.parseInt(readingStr);
                            readings.add(reading);


                            // Update progress bar
                            handler.post(() -> progressBar.setProgress(0));
                            handler.post(() -> progressBar.setMax(100));
                            handler.post(() -> progressBar.setProgress((readings.size() / 3)));

                            // handler.post(() -> semiCircleProgressBar.setProgress(0));
                            // handler.post(() -> semiCircleProgressBar.setMax(100));
                            int finalReading = reading;
                            handler.post(() -> semiCircleProgressBar.setProgress(finalReading));

                    }
                    System.out.println(fullReading);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } finally {
                    try {
                        btsocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return readings;
            }else{
            Button restartButton = activity.findViewById(R.id.restartButton);
            restartButton.setVisibility(View.VISIBLE);
            cancel(true);
        }
        return readings;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (values.length > 0) {
            int lastReading = values[0];
            int progress = readings.size()/3; // Calculate the progress percentage

            if (progress > 100) {
                progress = 100;
            }
            progressBar.setProgress(0);
            progressBar.setMax(100);
            progressBar.setProgress(progress);// Update the ProgressBar

          //  semiCircleProgressBar.setProgress(0);
           // semiCircleProgressBar.setMax(100);
            semiCircleProgressBar.setProgress(lastReading);// Update the ProgressBar

            // You can also display the progress percentage using a TextView or other UI element, if needed
        }
    }

    @Override
    protected void onPostExecute(List<Integer> readings) {
        super.onPostExecute(readings);
        onReadingsReceivedListener.onReadingsReceived(readings);
        cancelTimeout();
        cancel(true);
    }

    public interface OnReadingsReceivedListener {
        void onReadingsReceived(List<Integer> readings);
    }
    public boolean attemptToConnect(){
        int counter = 0;
        do {
            try {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
                System.out.println(device.getName());
                btsocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                System.out.println(btsocket);
                btsocket.connect();
                System.out.println(btsocket.isConnected());
            } catch (IOException e) {
                e.printStackTrace();
            }
            counter++;
        } while (!btsocket.isConnected() && counter<3);
        if(btsocket.isConnected())
            return true;
        else return false;
    }
    private final Runnable timeoutRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isCancelled()) {
                // Timeout occurred, handle it here
                System.out.println("TIMEOUT RANNNN!!!!");
                Button restartButton = activity.findViewById(R.id.restartButton);
                restartButton.setVisibility(View.VISIBLE);
                cancel(true);
            }
        }
    };
    private void startTimeout() {
        if (progressBar.getProgress() != 100) {
            if (timeoutHandler == null) {
                timeoutHandler = new Handler(Looper.getMainLooper());
            }
            timeoutHandler.removeCallbacks(timeoutRunnable);
            timeout = System.currentTimeMillis() + TIMEOUT_MS;
            timeoutHandler.postDelayed(timeoutRunnable, TIMEOUT_MS);
        }
    }

    public void cancelTimeout(){
        if(timeoutHandler!=null) {
            timeoutHandler.removeCallbacks(timeoutRunnable);
        }
    }
}
