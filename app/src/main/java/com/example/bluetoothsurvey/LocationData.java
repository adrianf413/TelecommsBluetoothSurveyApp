package com.example.bluetoothsurvey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LocationData {

    public double latitude;
    public double longitude;
    public long timestamp;
    public Date date;
    public String datetime;
    public List devices_found;
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

    public List getDevicesList(){
        return devices_found;
    }
}