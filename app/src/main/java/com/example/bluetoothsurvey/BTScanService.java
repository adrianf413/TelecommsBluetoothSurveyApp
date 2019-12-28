package com.example.bluetoothsurvey;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class BTScanService extends Service {
    ArrayList<BluetoothDeviceObject> devices;
    @Override
    public void onCreate() {
        devices = getListOfFoundDevices();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("App-Start-up");
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        String startValue = "Started at: " + formatter.format(date);
        myRef.child("StartTimes").push().setValue(startValue);
        myRef.child("ScannedDevices").push().setValue(devices);
        //scan_and_push();
    }

    public void scan_and_push(){
        int delay = 15; // The delay in minutes
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() { // Function runs every MINUTES minutes.

            }
        }, 0, 1000 * 60 * delay);
        // 1000 milliseconds in a second * 60 per minute * the MINUTES variable.
    }

    public ArrayList getListOfFoundDevices()
    {
        final ArrayList<BluetoothDeviceObject> arrayOfFoundBTDevices = new ArrayList<>();

        // start looking for bluetooth devices
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery();

        // Discover new devices
        // Create a BroadcastReceiver for ACTION_FOUND
        final BroadcastReceiver mReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action))
                {
                    // Get the bluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    // Get the "RSSI" to get the signal strength as integer,
                    // but should be displayed in "dBm" units
                    int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);

                    // Create the device object and add it to the arrayList of devices
                    BluetoothDeviceObject bluetoothObject = new BluetoothDeviceObject();
                    bluetoothObject.setName(device.getName());
                    bluetoothObject.setAddress(device.getAddress());
                    /*
                    bluetoothObject.setBluetooth_state(device.getBondState());
                    bluetoothObject.setBluetooth_type(device.getType());    // requires API 18 or higher
                    bluetoothObject.setBluetooth_uuids(device.getUuids());
                    bluetoothObject.setBluetooth_rssi(rssi);
                    */
                    arrayOfFoundBTDevices.add(bluetoothObject);
                    /*
                    // 1. Pass context and data to the custom adapter
                    FoundBTDevicesAdapter adapter = new FoundBTDevicesAdapter(getApplicationContext(), arrayOfFoundBTDevices);

                    // 2. setListAdapter
                    setListAdapter(adapter);
                    */
                }
            }
        };
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        return arrayOfFoundBTDevices;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        //do something
    }

    /*
    @Override
    protected void onPause()
    {
        super.onPause();

        mBluetoothAdapter.cancelDiscovery();
    }*/

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
