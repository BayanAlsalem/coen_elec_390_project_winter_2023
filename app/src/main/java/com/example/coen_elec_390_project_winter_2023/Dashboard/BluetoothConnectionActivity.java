package com.example.coen_elec_390_project_winter_2023.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coen_elec_390_project_winter_2023.R;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class BluetoothConnectionActivity extends AppCompatActivity {
    private ListView deviceList;
    private ArrayAdapter deviceAdapter;
    private Button pairedButton;
    private BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

    TextView messageView;

    String userID;

    private BluetoothDevice selectedDevice;

    BluetoothSocket btSocket;
    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_connection_layout);
        System.out.println("TESSSSSTTTT");
        if (getIntent().getExtras() != null) {
            userID=getIntent().getStringExtra("userID");
        }else{
            System.out.println("Intent Failed");
            return;
        }
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        deviceList = (ListView) findViewById(R.id.deviceList);
        pairedButton = (Button) findViewById(R.id.pairedButton);
        messageView = (TextView) findViewById(R.id.messageView);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                System.out.println("GOT PERMISSION LESS GOOOO");
                ActivityCompat.requestPermissions(BluetoothConnectionActivity.this, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, 2);
            } else {
                System.out.println("BlueTooth Permission Denied");
                return;
            }
        }
        ArrayList<BluetoothDevice> pairedList = new ArrayList<>();
        ArrayList<String> viewList = new ArrayList<>();
        pairedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewList.clear();
                pairedList.clear();
                System.out.println("BUTTON PRESSED");
                if (!(ActivityCompat.checkSelfPermission(BluetoothConnectionActivity.this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)) {
                    Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
                    if (pairedDevices.size() > 0) {
                        for (BluetoothDevice device : pairedDevices) {
                            String devicename = device.getName();
                            String macAddress = device.getAddress();
                            viewList.add("Name: " + devicename + " MAC Address: " + macAddress);
                            pairedList.add(device);
                        }
                        deviceList = (ListView) findViewById(R.id.deviceList);
                        deviceAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, viewList);
                        deviceList.setAdapter(deviceAdapter);
                    }
                }
            }
        });
        deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                if (!(ActivityCompat.checkSelfPermission(BluetoothConnectionActivity.this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)) {
                    selectedDevice = btAdapter.getRemoteDevice(pairedList.get(position).getAddress());
                    Toast.makeText(BluetoothConnectionActivity.this, "Selected Device: " + selectedDevice.getName(), Toast.LENGTH_LONG).show();

                    if(attemptToConnect()) {
                        Intent intent = new Intent(BluetoothConnectionActivity.this, BluetoothReadingsActivity.class);
                        intent.putExtra("userID",userID);
                        intent.putExtra("BTdevice", selectedDevice);
                        startActivity(intent);
                    }else{
                        Toast.makeText(BluetoothConnectionActivity.this, "Device is unavailable, try again" , Toast.LENGTH_LONG).show();
                    }

                }
            }
        });




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
                    } else {

                    }

                }
                System.out.println(selectedDevice.getName());
                btSocket = selectedDevice.createRfcommSocketToServiceRecord(mUUID);
                System.out.println(btSocket);
                btSocket.connect();
                System.out.println(btSocket.isConnected());
            } catch (IOException e) {
                return false;
            }
            counter++;
        } while (!btSocket.isConnected() && counter<3);
        if(btSocket.isConnected())
            return true;
        else return false;
    }
}

