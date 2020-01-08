package com.example.bluetoothsurvey;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocationData {

    public double latitude;
    public double longitude;
    public long timestamp;
    public Date date;
    public String datetime;
    public ArrayList<BluetoothDeviceObject> devices_found;
    public int num_devices;

    public LocationData(){


    }

    public LocationData(double latitude, double longitude, int num_devices){
        this.latitude = latitude;
        this.longitude = longitude;
        this.devices_found = devices_found;
        this.num_devices = num_devices;
        this.date = new Date();
        this.timestamp = this.date.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        this.datetime = (formatter.format(date));
    }

    public void setDevices_found(ArrayList<BluetoothDeviceObject> devices_found){
        this.devices_found = devices_found;
    }
    public ArrayList<BluetoothDeviceObject> getDevicesList(){

        return devices_found;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public int getNum_devices(){
        return this.num_devices;
    }
}