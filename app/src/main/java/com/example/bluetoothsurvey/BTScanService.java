package com.example.bluetoothsurvey;

import android.Manifest;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class BTScanService extends Service {
    ArrayList<BluetoothDeviceObject> deviceList = new ArrayList<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference(this.getDeviceName());

    ArrayList<BluetoothDeviceObject> arrayOfFoundBTDevices = new ArrayList<>();


    LocationManager locationManager;
    LocationListener locationListener;

    int device_number;
    int iteration = 0;

    public double latitude;
    public double longitude;

    @Override
    public void onCreate() {

        //devices = arrayOfFoundBTDevices;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        String startValue = "Started at: " + formatter.format(date);
        myRef.child("Service Start Times").push().setValue(startValue);

        //create location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //Permission will be asked in the Location History activity
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);


        scan_and_push();
    }

    public void scan_and_push(){
        int delay = 15; // The delay in minutes

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() { // Function runs every MINUTES minutes.

                //start discovery
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                mBluetoothAdapter.startDiscovery();

                // Register for broadcasts when a device is discovered.
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(receiver, filter);

                try {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
                }catch(SecurityException e){}

                if(deviceList.isEmpty()){
                    //No devices detected in this iteration, push object with 0 devices.
                    LocationData datapoint = new LocationData(latitude, longitude, 0);
                    myRef.child("Locations").push().setValue(datapoint);
                }else{
                    //Push location data object to database with devices detected
                    LocationData datapoint = new LocationData(latitude, longitude, deviceList.size());
                    datapoint.setDevices_found(deviceList);
                    myRef.child("Locations").push().setValue(datapoint);

                    //Clearing the device list for the next iteration
                    deviceList.removeAll(deviceList);
                }

            }
        }, 0, 1000 * 60 * delay);
        // 1000 milliseconds in a second * 60 per minute * the MINUTES variable.
    }


    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // create a bluetooth object from the info
                BluetoothDeviceObject deviceFound = new BluetoothDeviceObject();
                deviceFound.setName(device.getName());
                deviceFound.setAddress(device.getAddress());

                //add object to the array of devices found
                //myRef.child("DevicesFound").push().setValue(deviceName);
                if(!deviceList.contains(deviceFound)) {
                    //checking for duplication
                    deviceList.add(deviceFound);
                }
            }
        }
    };

    @Override
    public void onStart(Intent intent, int startId) {
        //do something
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}
