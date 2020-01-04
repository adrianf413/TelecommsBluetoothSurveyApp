package com.example.bluetoothsurvey;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class CurrentLocationProvider implements LocationListener {

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    String lat;
    String provider;
    protected String latitude,longitude;
    protected boolean gps_enabled,network_enabled;
    double nlatitude, nlongitude;


    @Override
    public void onLocationChanged(Location location) {
        //txtLat = (TextView) findViewById(R.id.textview1);
        //txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        this.nlatitude = location.getLatitude();
        this.nlongitude = location.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
        //Log.d("Latitude","disable");
    }

    public double getNlatitude(){
        return this.nlatitude;
    }

    public double getNlongitude(){
        return this.nlongitude;
    }

    @Override
    public void onProviderEnabled(String provider) {
        //Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //Log.d("Latitude","status");
    }

}
