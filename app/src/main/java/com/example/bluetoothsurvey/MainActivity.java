package com.example.bluetoothsurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String device1 = getbtdevices();

        //Write to database for test on startup
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("App-Start-up");
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        String startValue = "Started at: " + formatter.format(date);
        myRef.child("StartTimes").push().setValue(startValue);
        myRef.child("devices").push().setValue(device1);

        DatabaseReference mysecondRef = database.getReference("Second-up");
        mysecondRef.setValue("Hello world");

        //Start the background service
        startService(new Intent(this, BTScanService.class));

        //Add Two buttons in here
        //One button to open mna activity where detected devices were displayed on a map
        Button locationsButton = (Button) findViewById(R.id.LocationsButton);

        locationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LocationHistory.class));
            }
        });

        //Button for opening up list of devices found
        Button devicesButton = (Button) findViewById(R.id.Devicesbutton);

        devicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DeviceList.class));
            }
        });
    }

    public String getbtdevices(){
        int REQUEST_BLUETOOTH = 1;
        System.out.println("Getting bluetooth devices");
        BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!BTAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, REQUEST_BLUETOOTH);
        }

        //ArrayList<> deviceItemList = new ArrayList<DeviceItem>();

        Set<BluetoothDevice> pairedDevices = BTAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                System.out.println("\n\n\n");
                System.out.println(device.getName());
                System.out.println("\n\n\n");
                return device.getName();
                //DeviceItem newDevice= new DeviceItem(device.getName(),device.getAddress(),"false");
                //deviceItemList.add(newDevice);
            }
        }
        return null;
    }
}
